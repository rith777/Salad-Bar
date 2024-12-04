# Subsumer calculator

It reads OWL ontology and computes the subsumers of a given class.

Bellow are the inference rules used for the EL-complete algorith:

| Rule      | Class name                            |
|-----------|---------------------------------------|
| ⊤-Rule	   | class TopClassAssignmentInferenceRule |
| ⊓-Rule 1	 | class ConjunctionDecompositionRule    |
| ⊓-Rule 2	 | ConjunctionCompositionRule            |
| ∃-Rule 1	 | ExistentialExpansionRule              |
| ∃-Rule 2	 | ExistentialIntroductionRule           |
| ⊑-Rule	   | SubsumptionPropagationRule            |

## Dependencies

- [Kotlin](https://kotlinlang.org/) 2.0 or higher
- [Java JDK](https://www.oracle.com/java/technologies/downloads/) 21 or higher
- [Gradle](https://gradle.org/) - Optional

[SDKMan](https://sdkman.io/) can be used to install and manage versions for all dependencies listed above.

## Build and Install kotlin dependencies

To install dependencies run the following command on the terminal in case is installed: `gradle build`. This command
will install all dependencies required to run this application and compile the code.

Alternatively, gradle wrapper can be used, in case gradle is not installed. Run one of the commands bellow according to
your OS:

- **Windows:** `.\gradlew.bat build`
- **Unix-based:** `./gradlew build`

## Running application

Run the `main` method on class `Main` to compute subsumers. Make sure to change variables `ontologyFileName`
and `className` with the desired values. With the current implementation the ontology file must be in the following
folder: `src/main/kotlin/resources`

## Unit tests

This application uses unit tests to guarantee quality and correctness. The unit test suit can be run by using the
following command: `gradle test` or `./gradlew test`, if using the wrapper. 