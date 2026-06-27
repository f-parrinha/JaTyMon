# JaTyMon: Multi-Paxos Example

This is a prototype project that implements multi-paxos to replicate a value (an array of bytes) among a set of replicas.
A client can issue __read__ or __write__ requests to any replica and the system must always reply with the correct value.
The project is also used to test JaTyMon and its generated monitors.

### Scripts
The project contains two scripts. One for executing (and compiling) the whole project, and another for running
JaTyMon to generate monitors

___run.sh:___ The _run.sh_ script compiles the project and launches different replicas (the number is configurable in
the script via _peers_count_). By default, the script starts __five__ replicas.

___run-JaTyMon.sh:___ This script requires jatymon-core-1.0.jar to be in the same directory as the script's. It executes
JaTyMon to generate new monitors from the typestate specifications in the project.

### Configurations
The project is highly configurable via _config.properties_. These are the main configurations:
- __protocol_type__: There are two types _simple_, for a unified version (sender and receiver roles in a singular protocol) and _sender_receiver_
- __use_monitors__: Use JaTyMon monitor's or not
- __fault_test_on__: Include faults in the execution or not
- __fault_list__: List with fault types (MessageDrop, MessageTinker, ConnectionDrop)
- __clients_ports__: List with the ports that are meant to be clients. Replicas commonly start with port 9000, so the list should be "9000, 9001, 9002" etc...
- __client_ops_numb__: Total number of operations that the client should execute


### Protocols

The project contains a unified implementation of multi-paxos (both leader and non-leader roles in the class) and an intermediate,
dispatcher protocol, including its faulty version, to dispatch messages received from opened channels to other subscribing protocols. The
faulty version is used to test JaTyMon's generated monitors, to evaluate whether they warn and detect any protocol violations or
deviations.

__Dispatcher:__ Dispatcher is the most important protocol. It is an interface between all other protocols and the messages that sent/received.
All message handlers are registered in the Dispatcher protocol, who then triggers notifications for the corresponding message
reception event. Other protocols subscribe, then, to these notifications. This way, one channel can be shared
by multiple protocols.

__Faulty Dispatcher:__ A subclass of the dispatcher protocol. Wraps the execution of certain event handlers to induce/simulate
faults such message drop, message tinkering or connection drop.

__MultiPaxos:__ The class MultiPaxosProtocol contains all methods required for the unified version of the multi-paxos protocol (contains both leader and non-leader roles)