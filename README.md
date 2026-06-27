# JaTyMon
JaTyMon is a tool for the Babel framework for generating monitor classes from stateful mixed-choice typestates for veryfing protocol compliance at runtime. Developers write an augmented version of typestate specifications tailored for the distributed environment, and annotate their Babel protocol classes using a provided library.

The paper for Stateful Mixed Choice Typestates was publish in PLACES 2026 and is available at: [https://arxiv.org/abs/2604.06874](https://arxiv.org/abs/2604.06874).

<br>
We test and evaluate our tool with two implementations of well-known consensus protocols: a read/write majority qurom inspired by the work of [Attya, Bar-Noy and Dolev](https://groups.csail.mit.edu/tds/papers/Attiya/PODC90.pdf), and a [Multi-Paxos](https://lamport.azurewebsites.net/pubs/lamport-paxos.pdf) protocol.


### Structure
The source code for the tool can be found in the "Tool" folder. The source code for both use cases is inside the "Use Cases" folder: "ABD" contains the code for the majority quorum example, while "MultiPaxos" for the Multi-Paxos implementation (obviosuly).
