# Use Cases
We provide two use cases which implement different consensus protocols for a simple replicated storage system which only accepts _read_ and _write_ requests. The ABD project implements a read/write majority quorum inspired by the work of [Attya, Bar-Noy, and Dolev](https://groups.csail.mit.edu/tds/papers/Attiya/PODC90.pdf), while the other implements a [Multi-Paxos](https://lamport.azurewebsites.net/pubs/lamport-paxos.pdf)  protocol.  

We use these projects for evaluating and benchmarking our tool. Our collected results are present int he [benchmarks](https://github.com/f-parrinha/JaTyMon/blob/main/Use%20Cases/Benchmarks.xlsx) file, here in this folder.  

Furthermore, each project contains an automatic fault injection system, which randomly selects a new fault from a given list of fault types. These can be ``ConnectionDrop``, ``MessageDrop``, and ``MessageTinker``. We add this feature for observing the monitors detecting protocol violations and deviations from expected distributions of action executions.  

We also provide scripts to build and execute these use cases. They can be found in the [Scripts] folder: ``build-usecases.sh`` for building the use cases and ``run-orchestrator.sh`` for executing and benchmarking the different use cases. The latter can configure the execution of use cases in many ways (see this [README](https://github.com/f-parrinha/JaTyMon/blob/main/Scripts/README.md) for more).

<br>

Each project contains in the resources folder a ``config.properties`` file which contains default options for executing the corresponding use case. For example, [this](https://github.com/f-parrinha/JaTyMon/blob/main/Use%20Cases/ABD/src/main/resources/config.properties) one. An example of a configuration goes as follows:
````
# 'simple' or 'sender_receiver'
protocol_type=simple

# fault test properties
fault_probability=0.5
fault_start_delay=5000
fault_min_delay=1000
fault_max_delay=4000
fault_conn_fail_min_time=2000
fault_conn_fail_max_time=4000

# client properties
client_start_delay=1000
client_ops_numb=10000
````

The ``protocol_type`` can either be simple, meaning, a unified version of the protocol (contains both _sender_ and _receiver_ roles), or ``sender_receiver``, which separates both roles in two different protocols.
Fault related variables control the probability of injecting a fault, the start delay, and the wait ranges (in milliseconds) between the next faults.
Client related configurations define the delay until the client can start its operations, and the number of operations it will produce, respectively.
