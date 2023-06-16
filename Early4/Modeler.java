package Early4;

import java.util.ArrayList;

public class Modeler {

    Modeler() {

    }

    public Model model(Dataset dataset, Double lr, int numOfEpochs) {
        // initialize the model
        Model model = new Model(dataset);

        Double averageLossPrevious = model.linearRegression(lr);
        Double averageLossNew = model.linearRegression(lr);
        int epochs = 0;

        while (averageLossNew != averageLossPrevious && epochs < numOfEpochs) {
            model.linearRegression(lr);
            epochs++;
        }
        

        return model;
    }
    
}
