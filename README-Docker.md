# JaTyMon

__JaTyMon__ is a Java tool for generating runtime monitors for [Babel](https://asc.di.fct.unl.pt/~jleitao/ngstorage/pubs/fouto-srds22.pdf) protocols associated with typestate definitions. It is directly associated with Babel, an event-driven Java framework for creating distributed systems. JaTyMon, implemented as Java annotation processor, collects __@Typestate__ annotated classes -- an annotation for specifying the location of a file with a typestate specification -- generating monitor classes for each. Monitors are subclasses of their original annotated ones. They implement and override methods that are also present in the typestate, wrapping the respective  __super__ call in conditional branches matching the corresponding transitions in the typestate. Therefore, a monitor represents the state-machine of the corresponding typestate.  
  
We do not use traditional typestates. We use _stateful mixed-choice typestates_, an extended version for describing distributed protocols. These include three new features:
- _Mixed sessions_ for describing concurrent __input__ and __output__ behaviour in a single state. We do so by adding mixed-choice (i.e., $$+$$) between __input__ (i.e., { $$\dots$$ }) and __output__ (i.e., $$\langle\dots\rangle$$) states in a single state (e.g., S = { $$\dots$$ } $$+$$ $$\langle\dots\rangle$$), supporting concurrent _send_ and _receive_ actions per state;
- _Expected ratios_ (i.e., $$(0 \leq r \leq 1) \land r = \epsilon$$, with $$\epsilon$$ denoting a null ratio) for describing expected distributions of action executions in states. These are added to actions (e.g., ``void m()[0.5]: S``) and represent the percentage of executions that that action will have in the total number of action executions that will occur in the state it is at;
- _Internal state_ for describing stateful operations (e.g., quorum operations require tracking the number of received acknowledgements). It supports variable (e.g., ``Val v = 0;``) and read-only (``Ext c;``) counters, assignments (e.g., ``Assign a: v := v + 1``) to mutate the values of variable counters, and predicates (e.g., ``Pred p: v = c``) which make transitions dependent on the values stored by the _internal state_. Both assignments and predicates are added to transitions. The former to mutate the _internal state_ in a reactive way, and the latter to give a finer control over how transitions are triggered, making these dependent on certain values of the _internal state_.  

A monitor always keeps track of the current state, and has the capability of verifying runtime compliance against expected distributions of action executions (i.e., _expected ratios_). As a result, our monitors can detect and log the following errors:
- __Illegal action log:__ an action that does not belong to the current state was executed;
- __Invalid high ratio log:__ an action was executed considerably more times than expected;
- __Invalid low ratio log:__ an action was executed considderably lesser times than expected.  

Due to using __mixed session__, both types of ratio logs contain in their text the confirmation whether it is an __input__ action, therefore, sending too many messages, or an __output__ action, implying the typestate is receiving too many messages.  

We published a paper regarding in PLACES 2026 introducing _stateful mixed-choice typestates_, which can be found [here](https://arxiv.org/abs/2604.06874).


## Methodology
We use a docker environment to ensure the reproducibility of our results. The image is built in the following steps. It begins by installing a pinned Debian distribution (snapshot 20261012T000000Z), Maven (version 3.9.9), and JDK 21 (Open JDK 21.0.12.1\_1). Every dependency is pinned. Then, it copies the content in the [project](https://github.com/f-parrinha/JaTyMon) folder. Now it proceeds in three stages:
- Execute build-tool.sh
- Execute gen-monitors.sh
- Execute build-usecases.sh

Firstly, it builds the tool using the [build-tool.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-tool.sh) script, which yields two ``.jar`` files -- ``lib.jar`` containing the library code (e.g., annotations), and ``core.jar`` which contains the actual monitor generator -- and two scripts,``install-JaTyMon.sh`` and ``run-JaTyMon.sh``. The former for installing __JaTyMon__ as a dependency in the Maven local repository, and the latter for executing __JaTyMon__ and generating monitors. The final stage of [build-tool.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-tool.sh) executes ``install-JaTyMon.sh``. Now it executes


## Verifying obtained results

## More verifications

## Autonomous Experiences 
