package Early4;

public class App {
    
    public static void main(String[] args) {
        // get data from api (manually for now)
        // use DatasetBuilder to create a Dataset from the data
        // use Modeler to create a Model using the Dataset
        // make a prediction using the Model

        //TESTING
        // Create a randomized Dataset object using the DataGenerator class
        DataGenerator dg = new DataGenerator();
        System.out.println(dg.getActualFunction());
    }
}
