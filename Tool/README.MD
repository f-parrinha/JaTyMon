# Tool
This folder contains the source code of __JaTyMon__, a tool for the [Babel](https://asc.di.fct.unl.pt/~jleitao/ngstorage/pubs/fouto-srds22.pdf) framework, which generates monitors from [stateful mixed-choice typestate](https://arxiv.org/abs/2604.06874) definitions. The tool detects, at runtime, protocol violations and deviations from expected distributions of action executions.

## Project structure
The tool has two distinct roles: process typestates for generating monitors, and provide annotations and tools so developers can specify which protocols to be monitored. Therefore, the project is divided in two modules: ``core`` and ``lib``.   

The backend lives in ``core``. Here, typestates are processed -- detecting errors from any ill-formed typestates along the way -- and monitors are generated. The module contains the code responsible for parsing and and semantically validate typestates, for creating and checking the corresponding graphs, planning the the structure of the Java code for each monitor, and writing, using [JavaPoet](https://github.com/palantir/javapoet), the final Java classes for each monitor.  

The ``lib`` module contains the annotations developers may use (i.e., ``@Typestate`` and ``@Ext``), and auxilary classes used by synthesized monitors (e.g., ``JaTyMonLogger``, ``ActionsCounter``)


## Building JaTyMon
We provide a ``build.sh`` script which builds
