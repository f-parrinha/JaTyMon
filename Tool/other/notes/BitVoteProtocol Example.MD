# BitVoteProtocol Example
The following example uses _BitVoteProtocol_, whose implementation uses Babel, to illustrate how the monitoring system could be used.

## Introduction

Let's begin by assuming each transition in a _typestate_ is always triggered by its method invocation. Let's also consider messages trigger events, and a class's state can evolve with these external events, that still do not exist at compile time.

The BitVoteProtocol is a simple algorithm. It contains _n_ peers with one leader _m_. The leader
can request (with _VoteRequest_) votes every tick _t_. Every vote request contains its round ID, and the leader
will insist in the same vote with a configurable number _k_, or until all votes are collected. The peers can then reply with an acknowledgement (using _VoteAck_) 
with their vote for the given round ID, either with a new value, or with the last acknowledged value if a reply to that given
round ID has already been given. A round is considered to be successful if a quorum of votes is met. Its decided value is
calculated by choosing the voted bit with the largest population. Upon an ending of a round, the leader _m_ sends a write-back
message (using _VoteWriteBack_) to all peers _n_ with the decided bit and round status (successful or failed).

The Java implementation contains **five** important methods for its execution:
- uponOutConnectionUp(**OutConnectionUp** _event_, **int** _channel_)
- uponOutConnectionFailed(**OutConnectionFailed<ProtoMessage>** _event_, **int** _channel_)
- uponVoteRequest(**VoteRequest** _message_, **Host** _sender_, **short** _sourceProto_, **int** _channelId_)
- uponVoteAck(**VoteAck** _message_, **Host** _sender_, **short** _sourceProto_, **int** _channelId_)
- uponVoteWriteBack(**VoteWriteBack** _message_, **Host** _sender_, **short** _sourceProto_, **int** _channelId_)

The _typestate_ processor must allow methods defined in the astValidationContext to be called in the same class to whom it is associated to. 
An example astValidationContext is illustrated below.

````
astValidationContext BitVoteProtocol {
    Init = {
        void uponOutConnectionUp(OutConnectionUp, int): Init,
        void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage>, int): Init,
        
        /* 
         * Introduced to transition to next state. In the Java class, the protocol begins
         *  after "n" uponOutConnectionUp calls. A solution to support several calls before
         *  transitioning is to separate into different methods. uponOutConnectionUp will then call begin()
         */
        void begin(): Idle,
        drop:end
    }
    
    Idle = {
        void uponVoteRequest(VoteRequest, Host, short, int): Idle,
        void uponVoteAck(VoteAck, Host, short, int): Idle,
        void uponVoteWriteBack(VoteWriteBack, Host, short, int): Idle,
        drop:end
    }
}
````
<br>

## Integrating ratios

Let's consider the process of observing a method call is independent (or memoryless), and that the collected actionId of calls is a subset of a larger population, that only tends to grow. The concept of ratios revolves around collecting samples of method calls in a state, and computing the average of successes for each. It consists in a Bernoulli experiment, where for each method _m_ in a state _s_, a call can be successful (1) when _m_ is called and unsuccessful (0) when _m_ is not called, where X ~ Binomial(_n_, **P**_m,s_) where _n_ is the total number of calls in _s_ and _p_ is the probability for each experiment for _m_ in _s_.

Specified ratios express the expected frequency of calls (or the true average of successes) to a given method within the total number of calls in the state it is at.
A state can be considered as a moment of choice, and it can choose transitions (methods) defined in its scope. Thus, every
state requires the total sum of its ratios to be equal to 1. The monitorData must keep track of the total number of received messages
regarding the scope of each state. A method _m_ may be presented in different states, meaning the monitorData must keep track
of its calls depending on the states where it was called.

A frequency _p^m,s_, or actionId, is calculated by dividing the number of calls **C** of a method _m_ in a state _s_ by the total method calls in _s_: **C***m,s*/**C***s*.

However, this number is too volatile, since its precision is dependent on how big **C***s* is. An error must be taken into account. The intuition is to build an interval around the expected frequency, and check whether the observed frequencies match the created interval.

Since the process is memoryless, the variance of a single experiment is Var(**X**_m,s_) = **P**_m,s_(1-**P**_m,s_), where _pm,s_ is the expected frequency, thus the variance of _p^m,s_, the observed frequency, is Var(_p^m,s_) = Var(**X**_m,s_) / **C***s* =  _pm,s_(1-**P**_m,s_) / **C**_s_. This is possible because _p^m,s_ is a mean, and the greater the actionId of means, the closer the next samples get to the true mean.

The next step is to calculate how much it is expected for the observed samples to deviate from the expected frequency, in other words the standard error. Since our variable is an average, **SE**_m,s_ = **SD**_m,s_ = sqrt(Var(_p^m,s_)) = sqrt((**P**_m,s_ * (1 − **P***m,s*))/ **C**_s_)
 
To finally create the interval around the expected frequency, one must create the _maximum acceptable error_ **E**. The final interval can be computed as such: **P**_m,s_ +- **E**.

According to the Central Limit Theorem, the sampling of calculated frequencies (or observed frequencies) approximate to a normal distribution when **C**_s_ is big enough, regardless of the previous distribution. Using a frequentist approach, we can build a confidence interval with a user provided confidence level _l_ where P(|p^- **P**_m,s_| <= **Z**(_l_)***SE**_m,s_) = l, with **Z**(_l_) being the z-score of _l_. The z-score can be found using the inverse of the cumulative distribution function of the normal distribution, F⁻¹((1 + _l_)/2) .

To calculate **E**_m,s_ we must multiply **Z**(_l_), that corresponds to the number of standard deviations in a normal distribution
representing the particular state, by **SE***m,s*: **E**_m,s_ = **Z**(_l_) * **SE**_m,s_

The interval defined as [_**P**_m,s_ - **E**_m,s_; **P**_m,s_ + **E**_m,s_]. When the computed frequencies fall out of this interval, the monitorData can show logs regarding the current anomalous behaviour. 
For the system to work correctly, a session can only be considered to be completed when the program terminates, meaning, 
any prior logs after the system stabilizes need to be retracted.

In summary: **E**_m,s_ = **Z**(_l_) * **SE***m,s*, where the final interval is [**P***m,s* - **E***m,s*; **P***m,s* + **E***m,s*]

Ratios can be integrated as illustrated below:
````
// For this example, let's assume there are 5 nodes in the P2P network
astValidationContext BitVoteProtocol {
    // For every 5 connection up events, 1 error and one to begin
    Init = {
        void uponOutConnectionUp(OutConnectionUp, int): Init [5/7],
        void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage>, int): Init [1/7],
        void begin() [1/7]: Idle,
        drop:end
    }
    
    // Here we receive 5 acks before sending a write-back and tolerate resending a request once
    Idle = {
        void uponVoteRequest(VoteRequest, Host, short, int): Idle [10/16],
        void uponVoteAck(VoteAck, Host, short, int): Idle [5/8],
        void uponVoteWriteBack(VoteWriteBack, Host, short, int): Idle [1/16],
        drop:end
    }
}
````

**NOTE**: the current implementation does not allow fractions, these values would need to be computed before writing.

However, the protocol is still not complete. Once begin is called, the astValidationContext advances to the Idle state. Messages from
states other than the current one are blocked. This means after begin is called, there is no way for the BitVoteProtocol
to keep track of a consistent network topology. The astValidationContext needs to be modified to include _uponOutConnectionUp_ and _uponOutConnectionFailed_
in the Idle state, like so:
````
astValidationContext BitVoteProtocol {
    Init = {
        void uponOutConnectionUp(OutConnectionUp, int): Init [5/7],
        void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage>, int): Init [1/7],
        void begin() [1/7]: Idle,
        drop:end
    }
    
    // For each conn up or failed, 20 requests were sent, and 10 acks were received, for 2 writebacks.
    // So for every write-back, 5 acks were received, and 10 requests were sent, allowing the possibility
    //  a second broadcast in the request phase, for very little change in the network topology
    Idle = {
        void uponOutConnectionUp(OutConnectionUp, int): Idle [1/32],
        void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage>, int): Idle [1/32],
        
        void uponVoteRequest(VoteRequest, Host, short, int): Idle [20/32],
        void uponVoteAck(VoteAck, Host, short, int): Idle [10/32],
        void uponVoteWriteBack(VoteWriteBack, Host, short, int): Idle [2/32],
        drop:end
    }
}
````

Now the protocol can withstand changes in the topology. One pattern that can be noticed is that as new states are created,
many of the functions from previously declared functions may need to be integrated in the new state. Theoretically, a astValidationContext
could be defined with only one state for all its methods with the correct expected ratios, but we would
lose the expressiveness of denying the execution of the protocol before all required connections are properly set up. In 
last provided astValidationContext (the one above) we know nodes can never contribute to voting rounds before calling _begin_.

With the provided syntax, it is also possible to allow protocols to evolve into more sophisticated states. As an example,
the current protocol could transition to a _RecoveryInspection_ state after a connection is lost:

````
astValidationContext BitVoteProtocol {
    Init = {
        ...
    }
    Idle = {
        void uponOutConnectionUp(OutConnectionUp, int): Idle [1/32],
        void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage>, int): RecoveryInspection [1/32],
        
        void uponVoteRequest(VoteRequest, Host, short, int): Idle [20/32],
        void uponVoteAck(VoteAck, Host, short, int): Idle [10/32],
        void uponVoteWriteBack(VoteWriteBack, Host, short, int): Idle [2/32],
        drop:end
    }
    
    // Here we lock the advance in voting rounds (to allow it, just add message callbacks such as uponVoteRequest)
    //  It is clearly expected to have more connections up than failed.
    RecoveryInspection = {
        void uponOutConnectionUp(OutConnectionUp, int): Idle [4/5],
        void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage>, int): RecoveryInspection [1/5],
        void proceed(): Idle,
        drop:end
    }
}
````

### AutoRatios
An auto-ratio is an extended feature that allows the processor to compute the ratio value automatically, by assigning the average
of the remainder probability value to methods that have the auto-ratio token **[*]**. This means, if no ratio has been specified
and all methods with count _c_ have auto-ratio, each method would have a ratio equal to 1/_c_. If there are specified ratios,
then, each ratio has a value of (1 - _s_)/_c_, where _s_ is the sum of specified ratios, and _c_ is the count of methods 
with auto-ratios.

````
astValidationContext BitVoteProtocol {
    Init = {
        ...
    }
    Idle = {
        ...
    }

    RecoveryInspection = {
        void uponOutConnectionUp(OutConnectionUp, int): RecoveryInspection [*],
        void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage>, int): RecoveryInspection [*],
        void proceed(): Idle [1/6],
        drop:end
    }
}
````

### NullRatios
A null-ratio is literally the inexistence of ratio in a transition/method. These are useful to specify that some methods
should not be considered for ratio evaluation.
````
astValidationContext BitVoteProtocol {
    Init = {
        ...
    }
    Idle = {
        ...
    }

    RecoveryInspection = {
        void uponOutConnectionUp(OutConnectionUp, int): RecoveryInspection [4/6],
        void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage>, int): RecoveryInspection [1/6],
        void proceed(): Idle [1/6],
        void logStatus(): RecoveryInspection,
        drop:end
    }
}
````
In the astValidationContext above, only _uponOutConnectionUp_, _uponOutConnectionFailed_ and _proceed_ are considered for ratio evaluation.
If the volume of calls in _logStatus_ is disproportionally bigger than the rest, no warning is issued. Null-ratios
provide this functionality. 

### Small correction
It is explained **C**_s_ represents the total count of methods calls for a state _s_, however this does not incur in correct results
due to the existence of null-ratios. **C***s* must then represent the total number of calls of methods that have ratios, such that
the calculated (or observed) frequencies are in the same scale as the expected ratios (either specified ones, or automatically assigned via the use of the token [*]).

## Summary
To conclude, the system assumes every method call invokes a transition to its destination - ratios are not thresholds for 
transitioning to the next state, they are only used for monitoring purposes.

Ratios are expected frequencies, and combined with a user-provided confidence level _l_, they can be used to verify
whether the calculated frequencies during the monitorData execution match the expected frequency (with some error).

Three different ratios are supported to allow more expressiveness (null-ratios), or to become more user-friendly (auto-ratios).

<br>

Here is the summary for the calculations, where _m_ is a method in a state _s_, **C** being the count of method calls for
methods with ratios in a state _s_, **P** being the expected frequency, **SE** the standard error, **E** the maximum acceptable 
error and **Z** the z-value for confidence level _l_:
- **SE**_m,s_ = sqrt((**P**_m,s_ * (1 − **P**_m,s_))/ **C**_s_) 
- **E**_m,s_ = **Z**(_l_) * **SE***m,s*,
- The confidence interval is: [**P***m,s* - **E***m,s*; **P***m,s* + **E***m,s*]
