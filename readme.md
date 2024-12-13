# Subsumer calculator

It reads OWL ontology and computes the subsumers of a given class.

Bellow are the inference rules used for the EL-complete algorith:

| Rule      | Class name                      |
|-----------|---------------------------------|
| ⊤-Rule	   | TopClassAssignmentInferenceRule |
| ⊓-Rule 1	 | ConjunctionDecompositionRule    |
| ⊓-Rule 2	 | ConjunctionCompositionRule      |
| ∃-Rule 1	 | ExistentialExpansionRule        |
| ∃-Rule 2	 | ExistentialIntroductionRule     |
| ⊑-Rule	   | SubsumptionPropagationRule      |

## Running from a jar file:

A jar file is available in the root of this project. To use it, run the following command:

`java -jar Salad-Bar-1.0.jar path_to_ontology class_name`

Eg:  `java -jar Salad-Bar-1.0.jar path/to/Salad-Bar/salad_bar.owx`

### Building and running the kotlin application

#### Dependencies

- [Kotlin](https://kotlinlang.org/) 2.0 or higher
- [Java JDK](https://www.oracle.com/java/technologies/downloads/) 21 or higher
- [Gradle](https://gradle.org/) - Optional

[SDKMan](https://sdkman.io/) can be used to install and manage versions for all dependencies listed above.

#### Build and Install kotlin dependencies

To install dependencies run the following command on the terminal in case is installed: `gradle build`. This command
will install all dependencies required to run this application and compile the code.

Alternatively, gradle wrapper can be used, in case gradle is not installed. Run one of the commands bellow according to
your OS:

- **Windows:** `.\gradlew.bat build`
- **Unix-based:** `./gradlew build`

#### Running application

Run the `main` method on class `ReasonerRunner` to compute subsumers. Make sure to change variables `ontologyFileName`
and `className` with the desired values. With the current implementation the ontology file must be in the following
folder: `src/main/kotlin/resources`

## Running experiments

An experimentation script was implemented to facilitate comparisons between EL reasoner, ELK and Hermit. This script
computes subsumers for each class on each ontology file and collects metrics from all reasoners.

To run an experiment, follow the steps bellow:

1. Add the all the ontology files you wish to experiment with to the resources folder (src/main/resources)
2. Execute the method `main()` in class `Experiment`.

Once the experiment script is finished, a file named `experiment_results.csv` will be created in the root of this
project.
