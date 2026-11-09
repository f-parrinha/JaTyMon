# JaTyMon

JaTyMon is a Java tool for generating runtime monitors for [Babel](https://asc.di.fct.unl.pt/~jleitao/ngstorage/pubs/fouto-srds22.pdf) protocols associated with typestate definitions. It is directly associated with Babel, an event-driven Java framework for creating distributed systems. JaTyMon, implemented as Java annotation processor, collects __@Typestate__ annotated classes -- an annotation for specifying the location of a file with a typestate specification -- generating monitor classes for each. Monitors are subclasses of their original annotated ones. They implement and override methods that are also present in the typestate, wrapping the respective  __super__ call in conditional branches matching the corresponding transitions in the typestate. Therefore, a monitor represents the state-machine of the corresponding typestate.  
  
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

<br>

We test and evaluate our tool with two implementations of well-known consensus protocols: a read/write majority qurom inspired by the work of [Attya, Bar-Noy, and Dolev](https://groups.csail.mit.edu/tds/papers/Attiya/PODC90.pdf), and a [Multi-Paxos](https://lamport.azurewebsites.net/pubs/lamport-paxos.pdf) consensus protocol.

# Getting Started
The repository contains four folders: 
- **Tool**, where the source code is;
- **Scripts** with many scripts to run examples and the execution pipeline in a docker environment;
- **Use Cases**, containing the source code of the two consensus protocols (i.e., a read/write majority quorum named, and a Multi-Paxos protocol);
- **Failure Examples**, which contains projects with ill-formed typestates for observing __JaTyMon__ detecting and logging errors.  

Each folder contains a README file with their own relevant instructions.

We also collected benchmarks to test the differences between executing the examples with monitor on and off, to evaluate whether they incur significant performance losses. A file called **benchmarks.xlxs** exists inside the **Use Cases** folder, containing all collected benchmarks. The tests are reproducible with the provided scripts.


## Using the tool
JaTyMon is a Java annotation processor. This means that to execute __JaTyMon__, one only needs to provide the annotation processor to the normal Java compiler (using the tool's jar file). Note that it may be needed to provided all project dependencies to the compiler. It is also recommended to disable any output from the **javac** command. This will ensure that the only ouputs are the generated monitor classes.

Nonetheless, we provide a **build.sh** script inside the folder named **Tool** which builds the tool and generates two scripts:
 - **install-JaTyMon-maven.sh**: removes previous versions of __JaTyMon__ from the Maven local repository and adds the latest generated version
 - **run-JaTyMon-maven.sh**: collects dependencies from the the intended Maven project and executes __JaTyMon__ to generate monitors


Considering the tool is in the Maven local repository, it is possible to add our provided library (``lib`` jar file) as a dependency in a Maven project like below:
````
<dependency>
    <groupId>jatymon</groupId>
    <artifactId>lib</artifactId>
    <version>1.0</version>
</dependency>
````

Developers create typestates in separate files (ending with _.protocol_) and assign them to classes using a @Typestate annotation containing the path to the typestate file. Compiling the code with JaTyMon as its annotation processor will result in the generation of monitor classes. All use cases show how to annotated Babel classes and setup typestate files. We illustrate below how to use both (i.e., ``@Typestate`` and ``@Ext``) annotations:
````
@Typestate("MultiPaxos")
public class MultiPaxosProtocol extends GenericProtocol {
  ...
  @Ext protected int peersSize;     // 'Ext' field in the typestate
  
````

We add ``@Typestate("MultiPaxos")`` to associate the ``MultiPaxosProtocol`` class with a typestate in the ``MultiPaxos.protocol`` file. Then, we assume the typestate contains the read-only counter ``Ext peersSize``, hence the @Ext annotation in the protected variable ``protected int peersSize``. Read-only counters must also be declared in the ``@Typestate`` annotated class, they must be marked with ``@Ext``.

