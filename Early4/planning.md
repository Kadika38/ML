Minimum Viable Product (MVP):

Model.java
    - weights and bias
    - public getter/setter methods for weight/bias reading and manipulation

ML.java (better name TBD)
    - has a Model
    - manages hyperparameters
    - performs 'the math' via injection of a Dataset

Dataset.java
    - an array of data
    - feature name referencing

DatasetBuilder.java
    - Manages creation of Datasets from raw data