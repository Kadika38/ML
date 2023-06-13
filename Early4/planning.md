Todo:
    - Model.java
    - Dataset.java
    - DatasetBuilder.java
    - Modeler.java

Minimum Viable Product (MVP):

Model.java
    - weights and bias
    - public getter/setter methods for weight/bias reading and manipulation

Modeler.java
    - manages hyperparameters
    - performs 'the math' via injection of a Dataset
    - creates a Model using the Dataset

Dataset.java
    - an array of data
    - feature name referencing

DatasetBuilder.java
    - Manages creation of Datasets from raw data