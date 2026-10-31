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


## Available scripts
- Introduce section: a guide explaining each script. What they do, which options they accept, and how they are used in the benchmark collection pipeline. 
- Show a usage example for each script.
- State that all scripts are meant to be executed in a linux environment (docker contains a linux distirbution)


#### common.sh
This script is meant to be sourced by the other scripts, not to be executed. Contains common attributes:
- ROOT: the root directory 
- JATYMON_OUT: the directory to where the build output of JaTyMon is placed.
- USE_CASES_DIR: the directory where all uses cases are
- SCRIPTS_DIR: the directory where all scripts are
- BENCHMARKS: the directory where benchmark results are placed

<br>

### Building
The following scripts are used to build the tool, setup JaTyMon library, generate monitors, and finally build all use cases

#### build-tool.sh
Builds the tool to a directory configured in **common.sh** (default: /opt/jatymon) using the tool's **build.sh**. First script to be executed in the benchmarking pipeline.

**Usage:** ./build-tool.sh

**Requires:**
- common.sh

---

#### build-usecases.sh

---
#### gen-monitors.sh

<br>

### Main execution
The following scripts are used to execute and test the tool on the different use cases. It is assumed everything is already build and set up. We also include, in these scripts procedures, for automatically generating benchmarks 

#### run-orchestrator.sh

---
#### run.sh

---
#### gen-benchmarks.sh

---
#### clear-benchmarks.sh

<br>

## Fail Tests
- Introduce section: project that show errors being displayed by JaTyMon from ill-formed typestates. We present nine rules in the ECOOP paper.
- Explain how to execute each project

#### Rule 1: No Duplicate State Name
This project breaks Rule 1 (No Duplicate State Name) of our well-formedness rules. It contains one typestate with two states sharing the same name. In this project, JaTyMon must report: "Duplicate state 'State0'".

To execute and confirm that JaTyMon properly presents the error, execute the run.sh script in the project directory.

#### Rule 2: Valid Ratio Sum
#### Rule 3: Enumare All Decisions
#### Rule 4: Closed Assignments and Predicates
#### Rule 5: Well-Scopred Expressions
#### Rule 6: Read-only Constants
#### Rule 7: Well-Typed Relational Terms
#### Rule 8: Useful States
#### Rule 9: Deterministic


