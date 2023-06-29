package Sidequests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import Early4.JSONBucket;

public class ConceptDF {
    ArrayList<JSONBucket> dataPointsAsBuckets;
    ArrayList<ArrayList<Double>> vectors;
    String symbol;

    ConceptDF(String symbol) throws IOException {
        this.symbol = symbol;
        Path filename = Path.of("/home/kadika/Coding/ML/Sidequests/jsons/" + this.symbol + "json.txt");
        String json = Files.readString(filename);
        JSONBucket bucket = new JSONBucket(json);
        ArrayList<JSONBucket> dataPointsAsBucketsReversed = (ArrayList<JSONBucket>) bucket.getValue("values");
        // rearrange datapoints to be in chronological order (they are currently in reverse chronological order)
        this.dataPointsAsBuckets = new ArrayList<JSONBucket>();
        for (int i = dataPointsAsBucketsReversed.size()-1; i >= 0; i--) {
            this.dataPointsAsBuckets.add(dataPointsAsBucketsReversed.get(i));
        }

        // build list of vectors
        this.vectors = new ArrayList<ArrayList<Double>>();
        for (int i = 1; i < this.dataPointsAsBuckets.size(); i++) {
            this.vectors.add(buildVector(this.dataPointsAsBuckets.get(i), this.dataPointsAsBuckets.get(i-1)));
        }
    }

    private ArrayList<Double> buildVector(JSONBucket currentDay, JSONBucket previousDay) {
        ArrayList<Double> vector = new ArrayList<Double>();

        // basic first feature of the vector: price change as a percentage
        Double currentDayClose = Double.parseDouble((String)currentDay.getValue("close"));
        Double previousDayClose = Double.parseDouble((String)previousDay.getValue("close"));
        Double priceChangePercentage = 100 * (currentDayClose - previousDayClose) / previousDayClose;
        vector.add(priceChangePercentage);

        return vector;
    }

    private Double vectorDifference(ArrayList<Double> vector1, ArrayList<Double> vector2) {
        if (vector1.size() != vector2.size()) {
            throw new Error("Vectors do not have same number of dimensions");
        }

        Double dif = 0.0;
        
        // use distance formula
        for (int i = 0; i < vector1.size(); i++) {
            dif += Math.pow(vector2.get(i) - vector1.get(i), 2);
        }
        dif = Math.sqrt(dif);

        return dif;
    }

    private Double averageVectorDifference(ArrayList<ArrayList<Double>> vectorPattern1, ArrayList<ArrayList<Double>> vectorPattern2) {
        if (vectorPattern1.size() != vectorPattern2.size()) {
            throw new Error("Vector patterns are of differring lengths");
        }

        Double avDif = 0.0;

        for (int i = 0; i < vectorPattern1.size(); i++) {
            avDif += vectorDifference(vectorPattern1.get(i), vectorPattern2.get(i));
        }

        avDif = avDif / vectorPattern1.size();

        return avDif;
    }

    private Double makePrediction(ArrayList<ArrayList<Double>> vectorPattern) {
        Integer mostSimilarVectorsIterator = 0;
        Double lowestAverageVectorDifference = null;

        // compare vectorPattern to all vector patterns
        for (int i = vectorPattern.size()-1; i < this.vectors.size()-1; i++) {
            ArrayList<ArrayList<Double>> vectorsSlice = new ArrayList<ArrayList<Double>>();
            for (int j = i - vectorPattern.size()+1; j < i+1; j++) {
                vectorsSlice.add(this.vectors.get(j));
            }
            if (lowestAverageVectorDifference == null || averageVectorDifference(vectorPattern, vectorsSlice) < lowestAverageVectorDifference) {
                lowestAverageVectorDifference = averageVectorDifference(vectorPattern, vectorsSlice);
                mostSimilarVectorsIterator = i;
            }
        }

        // Print out and return the predicted daily price change percentage
        JSONBucket currentDay = this.dataPointsAsBuckets.get(mostSimilarVectorsIterator+1);
        JSONBucket nextDay = this.dataPointsAsBuckets.get(mostSimilarVectorsIterator+2);
        Double currentDayCP = Double.parseDouble((String)currentDay.getValue("close"));
        Double nextDayCP = Double.parseDouble((String)nextDay.getValue("close"));
        Double prediction = 100 * ((nextDayCP - currentDayCP) / currentDayCP);
        System.out.println(prediction);
        return prediction;
    }

    private Double makePredictionForTesting(ArrayList<ArrayList<Double>> vectorPattern, Integer cutoff) {
        Integer mostSimilarVectorsIterator = 0;
        Double lowestAverageVectorDifference = null;

        // compare vectorPattern to all vector patterns
        for (int i = vectorPattern.size()-1; i < cutoff; i++) {
            ArrayList<ArrayList<Double>> vectorsSlice = new ArrayList<ArrayList<Double>>();
            for (int j = i - vectorPattern.size()+1; j < i+1; j++) {
                vectorsSlice.add(this.vectors.get(j));
            }
            if (lowestAverageVectorDifference == null || averageVectorDifference(vectorPattern, vectorsSlice) < lowestAverageVectorDifference) {
                lowestAverageVectorDifference = averageVectorDifference(vectorPattern, vectorsSlice);
                mostSimilarVectorsIterator = i;
            }
        }

        // Return the predicted daily price change percentage
        JSONBucket currentDay = this.dataPointsAsBuckets.get(mostSimilarVectorsIterator+1);
        JSONBucket nextDay = this.dataPointsAsBuckets.get(mostSimilarVectorsIterator+2);
        Double currentDayCP = Double.parseDouble((String)currentDay.getValue("close"));
        Double nextDayCP = Double.parseDouble((String)nextDay.getValue("close"));
        Double prediction = 100 * ((nextDayCP - currentDayCP) / currentDayCP);
        return prediction;
    }

    private Double getVectorDifference(ArrayList<ArrayList<Double>> vectorPattern, Integer cutoff) {
        Double lowestAverageVectorDifference = null;

        // compare vectorPattern to all vector patterns
        for (int i = vectorPattern.size()-1; i < cutoff; i++) {
            ArrayList<ArrayList<Double>> vectorsSlice = new ArrayList<ArrayList<Double>>();
            for (int j = i - vectorPattern.size()+1; j < i+1; j++) {
                vectorsSlice.add(this.vectors.get(j));
            }
            if (lowestAverageVectorDifference == null || averageVectorDifference(vectorPattern, vectorsSlice) < lowestAverageVectorDifference) {
                lowestAverageVectorDifference = averageVectorDifference(vectorPattern, vectorsSlice);
            }
        }

        return lowestAverageVectorDifference;
    }

    private void testPredictionQuality(Integer patternSize) {
        ArrayList<ArrayList<Double>> realVSprediction = new ArrayList<ArrayList<Double>>();
        for (int i = this.vectors.size()-1; i >= patternSize; i--) {
            // i+1 is at the day we want to predict
            JSONBucket nextDay = this.dataPointsAsBuckets.get(i+1);
            JSONBucket currentDay = this.dataPointsAsBuckets.get(i);
            Double nextDayCP = Double.parseDouble((String)nextDay.getValue("close"));
            Double currentDayCP = Double.parseDouble((String)currentDay.getValue("close"));
            Double realCPP = 100 * ((nextDayCP - currentDayCP) / currentDayCP);

            ArrayList<ArrayList<Double>> pattern = new ArrayList<ArrayList<Double>>();
            for (int j = i - patternSize + 1; j <= i; j++) {
                pattern.add(this.vectors.get(j));
            }

            Double prediction = this.makePredictionForTesting(pattern, i);
            Double vectorDifference = this.getVectorDifference(pattern, i);

            ArrayList<Double> thisComparison = new ArrayList<Double>();
            thisComparison.add(realCPP);
            thisComparison.add(prediction);
            thisComparison.add(vectorDifference);
            realVSprediction.add(thisComparison);
        }

        try {
            FileWriter myWriter = new FileWriter("Sidequests/rvps/" + this.symbol + "rvp" + patternSize + ".csv");
            myWriter.write("real,prediction,vectorDif");
            for (int i = 0; i < realVSprediction.size(); i++) {
                myWriter.write("\n" + Double.toString(realVSprediction.get(i).get(0)) + "," + Double.toString(realVSprediction.get(i).get(1)) + "," + Double.toString(realVSprediction.get(i).get(2)));
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Error while writing to file");
        }
    }

    private ArrayList<ArrayList<Double>> getMostRecentPattern(int patternSize) {
        ArrayList<ArrayList<Double>> pattern = new ArrayList<ArrayList<Double>>();
        for (int i = this.vectors.size() - patternSize; i < this.vectors.size(); i++) {
            pattern.add(this.vectors.get(i));
        }
        return pattern;
    }

    public static void main(String[] args) throws IOException {        

        try {
            ConceptDF cdf = new ConceptDF("sbux");
            cdf.testPredictionQuality(2);
            cdf.makePrediction(cdf.getMostRecentPattern(2));
        } catch (IOException e) {
            System.out.println("Lmao it errored");
        }
        
    }
}