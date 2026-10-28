# JaTyMon
- Explain what JaTyMon is: a monitor generator for Babel protocols from stateful mixed-choice typestates.
- Present published papers

## Use cases
- The repository contains two use cases to test the usability of JaTyMon. They are both two simple replicated storage system that use different consensus protocols: a read/write majority quorum and a MultiPaxos protocol.

### Attya Bar-Noy Dolev read/write quorum
- Present how the protocol works
- Present configuration options
- Explain how to build it and execute it

### MultiPaxos
- Presents the basic functionality of the protocol
- Present configuration options
- Explain how to build and execute it

## Benchmarking
- Introduce section: results, methodology and scripts used for collecting benchmarks to compare latency averages in Babel projects with and without synthesized monitors.

### Results
- Present results in ECOOP paper

### Methodology
- We use a docker env
- Conisists in the following pipeline: 
    - Copy contents to docker
    - Build tool. This generates a run script and a install lib script
    - Install lib
    - Run JaTyMon to create monitors
    - Checksum older monitors and recent monitors (TODO)
    - Build use cases (Docker image is built here)
    - Run run-orchestrator.sh 10 times for both use cases with monitors on and off, producing 100000 ops each
    - The last step produces benchmarks for both use cases. Execute gen-benchmarks to produce a csv file with latency averages and standard deviation


### Available scripts
- Introduce section: a guide explaining each script. What they do, which options they accept, and how they are used in the benchmark collection pipeline. 
- Show a usage example for each script.
- common.sh. Present this script here because it contains global values

#### Building
- build-tool.sh
- build-usecases.sh
- gen-monitors.sh
 
#### Main execution
- run-orchestrator.sh
- run.sh
- gen-benchmarks.sh
- clear-benchmarks.sh


## Fail Tests
- Introduce section: project that show errors being displayed by JaTyMon from ill-formed typestates. We present nine rules in the ECOOP paper.
- Explain how to execute each project

### Rule 1: No Duplicate State Name
### Rule 2: Valid Ratio Sum
### Rule 3: Enumare All Decisions
### Rule 4: Closed Assignments and Predicates
### Rule 5: Well-Scopred Expressions
### Rule 6: Read-only Constants
### Rule 7: Well-Typed Relational Terms
### Rule 8: Useful States
### Rule 9: Deterministic


