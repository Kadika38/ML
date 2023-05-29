- ML App
    - Manages:
        - Model
        - Feature manager
        - Data set acquisition
        - Hyperparameters
        - Model adjustment / loss compution
        - Results
    
    - Order of Operation:
        1. build base model
            - choose model type (only linear regression at first)
            - instantiate
        2. acquire dataset
        3. feature management
            - instantiate dataset as features
            - manual adjustment
            - automatic adjustment
        4. hyperparameter adjustment
        5. loss compution / model adjustment
        6. print results

- Model
    - just the model
    1. get weights and bias
    2. get num of features/weights
    3. get partial derivatives
