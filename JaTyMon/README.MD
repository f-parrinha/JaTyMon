# JaTyMon
JaTyMon is a monitor generation tool inspired by [JaTyC](github.com/jdmota/java-astValidationContext-checker) typestates, [mixed sessions](https://www.sciencedirect.com/science/article/pii/S0304397521004631), 
and [probabilistic session type monitoring](https://link.springer.com/chapter/10.1007/978-3-030-78142-2_7). The tool uses 
an augmented version of JaTyC's syntax. In reality, JaTyC's language can be considered to be a subset of 
JaTyMon's language. The created typestates are associated to Java classes using the @Typestate annotation. 
The processor then generates monitor classes for each associated Java class in the source code. Each monitor encodes a 
state-machine representing the corresponding protocol. They track the current state and actively block actions
(or method calls) that do not belong to the current state. 

The core idea is to have a runtime monitor generation system for the [Babel](https://nova-lincs.di.fct.unl.pt/publications/babel-a-framework-for-developing-performant-and-dependable-distributed-protocols/), an event-driven framework for creating 
distributed protocols. Our monitors are capable of blocking incorrect actions (those that violate the protocol) and 
issue warnings when the volume of action executions within a state does not follow a pre-defined probabilistic distribution.
Additionally, to support stateful operations (e.g., operations whose transitions depend on other values for their triggering),
we augment typestates with an explicit internal state containing counters, read-only counters, assignments to change the counters,
and predicates using those counters. An example where the internal state is useful is a quorum system, since these must
collect a certain number of votes before transitioning to the next state, where either the quorum is met or not.

With these features, the tool aims to capture the asynchronous and concurrent nature of distributed protocols, which is
more difficult with traditional typestates (and JaTyC). Furthermore, static verification alone cannot capture certain 
problems common in the distributed setting: message loss, random network partitioning and failures, channels that are closed
randomly, and more. Monitors using a statistical approach can withstand and detect failures when in these occasions.  

<br>

---
## New features to the syntax
### No terminal state is required
For our purposes, no terminal state is required, allowing the specification of never ending or long duration protocols.
However, if there is a terminal state, it is required that all states are productive.


### Internal State
Our language now also supports stateful operations: it contains fields to store counters, assignments 
to change the values of these counters and predicates to trigger transitions only when a certain condition 
over these counters is met. An example where this feature can is a quorum system that must collect a number of acknowledgements 
(a variable for acks) before transitioning to a next state. 

The internal state is composed by the following keywords:
- ```val```: a field to store variable counters
- ```ext```: a field to store read-only counters. These values are assigned and modified externally, not by the typestate itself
- ```Assign```: declares a new assignment
- ```Pred```: declares a new predicate

Assignments are used to change the values of variable counters. Predicates are conditions over variable and read-only 
counters. Both assignments and predicates can be associated to actions in a typestate. When an action is executed, the associated
assignment is firstly applied, then the associated predicated is evaluated. If the predicate yields true, the transition
is triggered. Since we may want to change the values of declared counters, we can also attach assignments after the destination
of an action. These assignments are only executed after the transition occurs. For this reason, assignments are divide in two
classes: pre-assignments and post-assignments, being the latter the ones that are only invoked after a transition occurs.

Example of a typestate:
```
typestate InternalStateExample {
    Ext n;
    Val conns = 0;
    
    Assign A1: conns := conns + 1;
    Assign A2: conns := 0;
    Pred P1: conns = n;
    
    State0 = {
        void m()[A1, P1]: State0[A2],
    }
}
```
In this example the internal state contains the value ```conns```, the read-only value ```n```, predicate ```P1``` and the
assignment ```A1```. The action ```m``` has the pre-assignment ```A1```, the predicate ```P1```, and the post-assignment
```A2``` associated to it.


### Ratios
Inspired by [Towards Probabilistic Session Type Monitoring](https://link.springer.com/chapter/10.1007/978-3-030-78142-2_7),
we introduce ratios to each action in a astValidationContext. Each ratio corresponds to the expected volume of executions the action
will take in the state it is declared. Monitors use a *frequentist* approach to detect runtime deviations from the pre-declared
ratios, meaning certain actions, or possibly messages, are not being triggered with the expected frequency: are there messages
being lost, why?

Example of a typestate:
```
typestate RatioExample {
    State0 = {
        void send()[0.5]: State0,
        void ack()[0.5]: State0,
    }
}
```

In the figure above, we declare that each time ```send``` is triggered, ```ack``` should also be triggered.


## Features to implement
- Support wildcards in generics (? extends Something)
- Support array types in generics (List<int[])
- Support top-unbounded and bot-unbounded intervals in ratios ([ _; _[, or ] _; _])
- Syntax for late messages
- Improved linear mode (?)
