#### build-tool.sh
Usage: ``bash build-tool.sh``

First script to be executed. Simply builds the tool, generating the two jars and scripts. The content is by default created in ``/JaTyMon Build Results``.  
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

It is possible to produce different kinds of experiments with the [run-orchestrator.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/run-orchestrator.sh) script. It is highly configurable via environment variables. The script offers the following variables changing the number clients, replicas and monitoring state:
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

The script [gen-benchmarks.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/gen-benchmarks.sh) generates a ``.csv`` file with the average latencies in both use cases comparing executions with and without monitors, including the standard deviation for verifying the consistency of collected results. This script must be executed after [run-orchestrator.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/run-orchestrator.sh) on __both__ use cases with and without monitors (i.e., ``MONITORS=both``)

<br>

---

#### clear-benchmarks.sh
Usage: ``bash clear-benchmarks.sh``

The script [clear-benchmarks.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/clear-benchmarks.sh) removes the content inside ``/Benchmarks`` folder.

<br>

---

#### run-failtests.sh
Usage: ``run-failtests.sh [-v (verbose)] [rule numbers...]``

The script [run-failtests.sh](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/run-failtests.sh) executes failure example projects matching the given rule number. It executes every project if no rule number is given.
Additionally, it is possible to see the output of __JaTyMon__ on each project by giving the ``-v`` option to the script.

For example, ``bash run-failtests.sh -v 1 9 2 8`` executes projects matching rules numbered 1, 2,8,and 9, printing the output of each __JaTyMon__ execution

