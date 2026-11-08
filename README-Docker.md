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

We benchmark our tool in two [use cases](https://github.com/f-parrinha/JaTyMon/tree/main/Use%20Cases). Both are a simple replicated storage systems with only _read_ and _write_ operations, though one uses a [read/write majority quorum](https://dl.acm.org/doi/10.1145/200836.200869) inspired by Attya et al. and the other uses a [Multi-Paxos](https://dl.acm.org/doi/abs/10.1145/279227.279229) protocol to achieve consensus on the order of operations. We also present empirical results showing that the well-formedness rules of our typestates hold. We present a project for each rule [here](https://github.com/f-parrinha/JaTyMon/tree/main/Failure%20Examples), containing one or more ill-formed typestates so __JaTyMon__ can show the appropriate error messages.  


## Methodology
We use a docker environment to ensure the reproducibility of our results. The image is built in the following steps. It begins by installing a pinned Debian distribution (snapshot 20261012T000000Z), Maven (version 3.9.9), and JDK 21 (Open JDK 21.0.12.1\_1). Every dependency is pinned. Then, it copies the content in the [project](https://github.com/f-parrinha/JaTyMon) folder. Now it proceeds in three stages:
- Execute [build-tool.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-tool.sh)
- Execute [gen-monitors.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/gen-monitors.sh)
- Execute [build-usecases.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-usecases.sh)

Firstly, it builds the tool using the [build-tool.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-tool.sh) script, which yields two ``.jar`` files -- ``lib.jar`` containing the library code (e.g., annotations), and ``core.jar`` which contains the actual monitor generator -- and two scripts,``install-JaTyMon.sh`` and ``run-JaTyMon.sh``. The former for installing __JaTyMon__ as a dependency in the Maven local repository, and the latter for executing __JaTyMon__ and generating monitors. The final stage of [build-tool.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-tool.sh) executes ``install-JaTyMon.sh``.  

Secondly, it runs [gen-monitors.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/gen-monitors.sh) which orchestrates the generation of monitors in our use cases with a predefined _confidence level_ (i.e., 0.9). Each use case already contains monitors. The script produces an hash on the content of every ``.java`` file in the source code of each use case before and after generating new monitors, and verifies whether these hashes (i.e., before and after synthesizing monitors) are the same, confirming that the code generation procedure is deterministic.  

Lastly, it builds both use cases with [build-usecases.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-usecases.sh) (i.e., replicated storage systems with read/write majority quorum and Multi-Paxos protocols, respectively). The image is now ready.  

We benchmark our tool on our use cases, collecting the average latencies in _read_ and _write_ operations in executions with and without monitors. These are collected with the [run-orchestrator.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/run-orchestrator.sh) script which manages the benchmark collection execution. Each execution creates one folder with two files: one with results with monitors and another containing results without monitors. These folders are created in the ``/Benchmarks`` folder. The image mounts a volume to this folder so can observe the results outside the container. Once every execution has finished, we execute [gen-benchmarks.sh](github.com/f-parrinha/JaTyMon/blob/main/Scripts/gen-benchmarks.sh) to generate a ``.csv`` file with the average of the collected latencies per use case, with and without monitors, including the standard deviation to verify the consistency of the collected results. 

To empirically demonstrate that the well-formedness rules of our typestates hold, we created one project for each rule containing one or more ill-formed typestates breaking the respective rule. We de so to observe that __JaTyMon__ catches the respective violations and presents the respective error messages.  


## Verifying obtained results
We offer [run-orchestrator.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/run-orchestrator.sh) script to verify our collected benchmarks. The script can be executed in the following way: ``bash run-orchestrator.sh <use_case> <runs> <client_ops>``, with ``<use_case>`` denoting which use case to be executed (i.e., ``ABD``, the read/write majority quorum; ``MultiPaxos``, or ``both``), ``runs`` the number of executions per use case, and ``client_ops`` the number of operations clients will produce per run. We collected our benchmarks using this script with 1 client, 5 replicas, 10 runs per use case with and without monitors, with the client producing 10000 operations. These values are chosen by default, therefore, considering we are inside the container (e.g., via ``-it``), there are two ways verify our benchmarks: 
- ``bash run-orchestrator.sh both 10 10000``;
- ``bash run-orchestrator.sh both``.

However, the __best__ way to verify our benchmarks is: ``docker run --rm -i -v "$PWD/Benchmarks:/Benchmarks" jatymon bash -c "bash Scripts/run-orchestrator.sh both && bash Scripts/gen-benchmarks.sh"``  

This command gives the most accurate results.

<br>

We also offer [run-failtests.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/run-failtests.sh) to empirically evaluate whether __JaTyMon__ ensures that the well-formedness rules of our typestates hold. We provide a [project](https://github.com/f-parrinha/JaTyMon/tree/main/Failure%20Examples) for each well-formedness rule with one or more ill-formed typestates breaking the corresponding rule. The script accepts a list of numbers representing the intended rules, separated by a space. If none is given, the script runs __JaTyMon__ on every project. The objective is to make sure __JaTyMon__ correctly catches each violation and presents the respective error message. Considering we are interacting with the container (e.g., via ``-it``), there are two ways to verify our results:
- ``bash run-failtests.sh 1 2 3 4 5 6 7 8 9``;
- ``bash run-failtests.sh`` (by default it executes every project).    

## More verifications
We also offer automatic random fault injection (i.e., connection loss, message loss, and message tinkering) in each use case to observe the generated monitors correctly reporting the respective runtime violations (i.e., illegal actions and invalid runtime ratios).  

TODO


<br>

## Autonomous Experiences
In this section, we introduce how to perform autonomous experiences. We assume the container is executed with following command: ``docker run --rm -it -v "$PWD/Benchmarks:/Benchmarks" jatymon``, considering the image was built with name ``jatymon``. We present per subsection how to use the corresponding script.

#### build-tool.sh
Usage: ``bash build-tool.sh``

First script to be executed. Simply builds the tool, generating the two jars and scripts. The content is by default created in a folder named _JaTyMon Build Results_.  
Created jars:
- ``lib-1.0-jar-with-dependencies.jar``: contains the library code, to be installed in the Maven local repository;
- ``core-1.0-jar-with-dependencies.jar``: contains the actual monitor generated.
  
Created scripts:
- ``install-JaTyMon-maven.sh``: installs our library in the Maven local repository; 
- ``run-JaTyMon-maven.sh``: executes the monitor generator on some given project path.

Note that both scripts assume the jar files reside in the same directory where the scripts are.

<br>

---

#### gen-monitors.sh
Usage: ``bash gen-monitors.sh``

The script [gen-monitors.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/gen-monitors.sh) generates the monitors for each use case with a predefined _confidence level_ (i.e., 0.9). It must be executed after [build-tool.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-tool.sh) and it is recommended to execute this script whenever the tool is rebuilt. The _confidence level_ can be configured with an environment variable:
- ``CONF_LEVEL``: desired _confidence level_ _l_ (0.9 by default) with $$0 \leq l < 1$$. 

For example, ``CONF_LEVEL=0.8 bash gen-monitors.sh`` generates monitors for each use case with _confidence level_ of 0.8.

<br>

---

#### build-usecases.sh
Usage: ``bash build-usecases.sh``

The script [build-usecases.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-usecases.sh) must be executed after [gen-monitors.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/gen-monitors.sh). If the tool is rebuilt, it is recommended to execute [gen-monitors.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/gen-monitors.sh) so [build-usecases.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/build-usecases.sh) may be executed again.

<br>

---

#### run-orchestrator.sh
Usage: ``bash run-orchestrator.sh <use_case> <runs> <client_ops>``  

It is possible to produce different kinds of experiments with the [run-failtests.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/run-failtests.sh) script. It is highly configurable via environment variables. The script offers the following variables changing the number clients, replicas and monitoring state:
- ``CLIENTS``: 1 by default. Changes the number of created clients;
- ``REPLICAS``: 5 by default. Mutates the number of replicas to launch;
- ``MONITORS``: ``on`` for with monitors only, ``off`` for otherwise, and ``both`` to execute with and without monitors per run.

For example, we may configure the script like so: ``CLIENTS=2 REPLICAS=3 MONITORS=on bash run-orchestrator.sh MultiPaxos 1 1000``, which executes the Multi-Paxos use case once, only with monitors, with 3 replicas and 2 clients producing 1000 operations.

Additionally, the script offers a way to configure the wait period between runs, whether to print the internal output of the executions of the given use cases, and whether to have automatic random fault injection or not:
- ``PAUSE_BETWEEN_RUNS``: wait interval in seconds between runs, 1 by default; 
- ``PRINT``: ``on`` for printing the output of given use cases, ``off`` for otherwise;
- ``FAULTS``: ``on`` or ``off``, with ``off`` by default.  

For example, ``PAUSE_BETWEEN_RUNS=4 PRINT=on FAULTS=on bash run-orchestrator.sh ABD 2 100`` executes the read/write majority quorum use case twice with the default number of replicas and clients, the client producing 100 operations. In this execution, the scripts waits 4 seconds between both runs, prints the logs from the execution of each use case and automatically injects random faults (i.e., connection loss, message drop, and message tinkering).

<br>

---

#### gen-benchmarks.sh
Usage: ``bash gen-benchmarks.sh``

The script [gen-benchmarks.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/gen-benchmarks.sh) generates a ``.csv`` file with the average latencies in both use cases comparing executions with and without monitors, including the standard deviation for verifying the consistency of collected results. This script must be executed after [run-failtests.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/run-failtests.sh) on __both__ use cases with and without monitors (i.e., ``MONITORS=both``)

<br>

---

#### run-failtests.sh
Usage: ``run-failtests.sh [-v (verbose)] [rule numbers...]``

The script [run-failtests.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/run-failtests.sh) executes failure example projects matching the given rule number. It executes every project if no rule number is given.
Additionally, it is possible to see the output of __JaTyMon__ on each project by giving the ``-v`` option to the script.

For example, ``bash run-failtests.sh -v 1 9 2 8`` executes projects matching rules numbered 1, 2,8,and 9, printing the output of each __JaTyMon__ execution

