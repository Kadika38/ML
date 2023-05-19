Order of Things:
1. Instantiate empty model (weights and bias are 0s)
2. (Name feature values in model, useful for analysis)
3. Compare basic model to data, compute loss
4. Adjust weight values
5. Redo 3 & 4 until loss converges

- 3 4 & 5 are adjustable for things like learning rate, number of epochs, etc


STEP FOUR: ADJUST WEIGHT VALUES
...

Model
    this is the actual model
    y = wx + b (1d version)
    
    Attributes
    - weights array
    - bias

    Constructor:
        takes num of features
        instantiates weights array as 0s, size = num of features
        instantiates bias as 0
