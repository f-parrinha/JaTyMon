# JaTyMon: Attya Bar-Noy Dolev Majority Quorum

The following project implements a majority quorum for read/write operations by [Attya Bar-Noy Dolev](https://dl.acm.org/doi/10.1145/200836.200869). We
use this project to test the behaviour of JaTyMon, a monitor generation tool from typestate specifications
for distributed protocols designed with [Babel](https://asc.di.fct.unl.pt/~jleitao/ngstorage/pubs/fouto-srds22.pdf). The project comes with a client protocol that sends read/write
operationss, sequentially, to a random replica in the quorum system. Essentially, a client is just a replica that
also has the client protocol running.


### Scripts
The project contains two scripts. One for executing (and compiling) the whole project, and another for running
JaTyMon to generate monitors

__run.sh:__ The _run.sh_ script compiles the project and launches different replicas (the number is configurable in 
the script via _peers_count_). By default, the script starts __five__ replicas.

__run-JaTyMon.sh:__ This script requires jatymon-core-1.0.jar to be in the same directory as the script's. It executes
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

The project has two main execution models: sender/receiver (the quorum protocol is divided in two distinct roles), and 
simple (the quorum protocol has the two roles unified in a single class).

__Dispatcher:__ Dispatcher is the most important protocol. It is an interface between all other protocols and the messages that sent/received.
All message handlers are registered in the Dispatcher protocol, who then triggers notifications for the corresponding message
reception event. Other protocols subscribe, then, to these notifications. This way, one channel can be shared
by multiple protocols.

__Faulty Dispatcher:__ A subclass of the dispatcher protocol. Wraps the execution of certain event handlers to induce/simulate
faults such message drop, message tinkering or connection drop.

__ABDProtocol:__ The class ABDProtocol is the unified version of the protocol and contains both roles: sender and receiver

__ABDSenderProtocol:__ The class ABDSenderProtocol contains all methods required for the sender role of the quorum protocol,
such as sending read-tag, read, or write messages

__ABDReceiverProtocol:__ The class ABDReceiverProtocol contains all methods required for the receiver role of the quorum protocol,
such as sending read-tag or read acknowledgements