# Tool
This folder contains the source code of __JaTyMon__, a tool for the [Babel](https://asc.di.fct.unl.pt/~jleitao/ngstorage/pubs/fouto-srds22.pdf) framework, which generates monitors from [stateful mixed-choice typestate](https://arxiv.org/abs/2604.06874) definitions. The tool detects, at runtime, protocol violations and deviations from expected distributions of action executions.  

We use Maven for building the tool and maintaining dependencies in the project. We also provide scripts for a linux environment.  

## Project structure
The tool has two distinct roles: process typestates for generating monitors, and provide annotations and tools so developers can specify which protocols to be monitored. Therefore, the project is divided in two modules: ``core`` and ``lib``. 

The backend lives in ``core``. Here, typestates are processed -- detecting errors from any ill-formed typestates along the way -- and monitors are generated. The module contains the code responsible for parsing and and semantically validate typestates, for creating and checking the corresponding graphs, planning the the structure of the Java code for each monitor, and writing, using [JavaPoet](https://github.com/palantir/javapoet), the final Java classes for each monitor.  

The ``lib`` module contains the annotations developers may use (i.e., ``@Typestate`` and ``@Ext``), and auxilary classes used by synthesized monitors (e.g., ``JaTyMonLogger``, ``ActionsCounter``)  

## Building JaTyMon
We provide a ``build.sh`` (``bash build.sh -o <output_dir>``) script which runs every unit test and builds the tool into two different modules: one for ``core`` and another for ``lib``. 
At the end, after the tool is built, it generates two scripts:
- install-JaTyMon-maven.sh (``bash install-JaTyMon-maven.sh``)
- run-JaTyMon-maven.sh  (``bash run-JaTyMon-maven.sh <project_dir> <confidence_level>``)  

The former for installing the ``lib`` module in the Maven local repository, and latter for executing __JaTyMon__ on a given Babel project and synthesize monitors. These scripts were made for projects using Maven.  
