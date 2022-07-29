## What is MecsycoScholar?

MecsycoScholar a Maven project containing MECSYCO example code. It contains co-simulation examples using ad-hoc Java models, [FMU](https://fmi-standard.org/) or even [NetLogo](https://ccl.northwestern.edu/netlogo/) models.

### Project overview

The *mecsycoscholar* project is a MECSYCO user project with the following structure:
* '*src/main/java*' contains the source code. All the examples are in the *mecsycoscholar.application* package.
* '*src/main/resources*' contains files to setupthe log level of the examples. Just choose the logging levent you want in the file '*mecsyco.properties*'.
* '*src/test/java*' and '*src/test/resources*' contains respectively the code and resources specific to the tests.
* '*My Models*' folder contains the models from other software (.fmu, .nlogo, ...).

### How to use MecsycoScholar?

MecsycoScholar is a set of examples, to run them the first step is to have MECSYCO. For sme example, a second wtep is to install the MECSYCO DSL, see the [MECSYCO DSL Gitlab project](https://gitlab.inria.fr/Simbiot/mecsyco/mecsycodsl) and its export section which also contains instructions for installation.

#### Install MECSYCO libraries

Follow the installation guide on [MECSYCO project](https://gitlab.inria.fr/Simbiot/mecsyco/mecsycojava).

#### Import and test

Developers on this project currently use [Eclipse](https://www.eclipse.org/downloads/) IDE. The following steps are specific to [Eclipse](https://www.eclipse.org/downloads/) but it will be the same process in other IDE.

Download the sources and import them in [Eclipse](https://www.eclipse.org/downloads/): 
* *Right click in the package explorer* -> *Import...* -> *Maven* -> *Existing maven project*.
* Put the path of the root directory ('*mecsycoscholar*') that contains the "pom.xml".

To check your installation:
* with [Eclipse](https://www.eclipse.org/downloads/):
    * *Right click on the project -> Run as -> Maven build...*
    * In *Goals*, write ``test -fae``, and *Run*.
* directly using Maven in the console:
    * in the *mecsycoscholar* folder, which contans a POM, open a terminal run ``mvn test -fae`` (Maven must be installed).

**Note**:
* Some examples are specific to Windows ([FMU](https://fmi-standard.org/)), you will experience some test errors in Linux. The Modelica script used to export the FMU is located in '*My Models/modelica*', you can use [OpenModelica](https://openmodelica.org/) to export the FMU by yourself in Linux and run the examples.

#### Usage

If tests succeed, you can run any example you want by running the corresponding Java file.

#### DSL examples

These examples are stored in the folders 'm2xml', 'mm2xml', 'cs2xml' and nl2xml', each folder contains a specific kind of description (respectively model, multi-model, experiment, NetLogo model).
Installation of [MECSYCO DSL](https://gitlab.inria.fr/Simbiot/mecsyco/mecsycodsl) enable to translate these scripts into XML description files.
The classes in 'mecsycoscholar.dsl.launcher' give examples on how to run simulations from these descriptions.
