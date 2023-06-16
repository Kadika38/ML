package Early4;

import java.util.ArrayList;

public class Modeler {

    Modeler() {

    }

    public Model model(Dataset dataset) {
        // initialize the model
        Model model = new Model(dataset);

        Double lr = 0.01;
        Double lrReductionRate = 0.1;
        Double averageLossPrevious = 0.0;
        Double averageLossNew = null;
        int epochs = 0;

        

        return model;
    }
    
}
