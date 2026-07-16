# JaTyMon
JaTyMon is a tool for the Babel framework for generating monitor classes from stateful mixed-choice typestates for veryfing protocol compliance at runtime. Developers write an augmented version of typestate specifications tailored for the distributed environment, and annotate their Babel protocol classes using a provided library.

The paper for Stateful Mixed-Choice Typestates was published in PLACES 2026 and is available at: [https://arxiv.org/abs/2604.06874](https://arxiv.org/abs/2604.06874).

<br>

We test and evaluate our tool with two implementations of well-known consensus protocols: a read/write majority qurom inspired by the work of [Attya, Bar-Noy, and Dolev](https://groups.csail.mit.edu/tds/papers/Attiya/PODC90.pdf), and a [Multi-Paxos](https://lamport.azurewebsites.net/pubs/lamport-paxos.pdf) consensus protocol.

# Getting Started
The repository contains two folders: **Tool**, where the source code is, and **Use Cases**, containing the source code for with implementations of the two consenus protocols.

Inside **Use Cases** there are two folders: **ABD** and **Multi-Paxos**. Each contains an .MD file with the instructions on how to run the examples. As short summary, both projects contain a **run.sh** script to execute the examples with five different replicas. This is configurable inside the script. They also contain a script **run-JaTyMon.sh** to execute JaTyMon and generate **monitor classes**. Therefore:
 - run.sh: starts the execution of the examples with a (configurable) number of replicas
 - run-JaTyMon-maven.sh: executes JaTyMon and generates monitor classes.

We also collected benchmarks to test the differences between executing the examples with monitor on and off, to evaluate whether they incur significant performance losses. A file called **benchmarks.xlxs** exists inside the **Use Cases** folder, containing all collected benchmarks. The tests are reproducible with the provided scripts.

## Using the tool
JaTyMon is a Java annotation processor. This means that to execute JaTyMon, one only needs to provide the annotation processor to the normal Java compiler (using the tool's jar file). Note that it may be needed to provided all project dependencies to the compiler (check **run-JaTyMon-maven.sh**. The script adds existing dependencies in a Maven project to the compiler and also provides JaTyMon to the normal **javac** command). It is also recommended to disable any output from the **javac** command. This will ensure that the only ouputs are the generated monitor classes.

Developers create typestates in separate files (ending with _.protocol_) and assign them to classes using a @Typestate annotation containing the path to the typestate file. Compiling the code with JaTyMon as its annotation processor will result in the generation of monitor classes.
