import java.util.ArrayList;

public class Early {
    ArrayList<Double> xdata;
    ArrayList<Double> ydata;

    Double w;
    Double b;

    Double lr = 0.01;
    

    Early() {
        this.xdata = new ArrayList<Double>();
        this.ydata = new ArrayList<Double>();
        this.w = 0.0;
        this.b = 0.0;

        this.xdata.add(1.0);
        this.xdata.add(2.0);
        this.xdata.add(3.0);
        this.xdata.add(4.0);
        this.xdata.add(5.0);
        this.xdata.add(5.0);
        this.xdata.add(6.0);

        this.ydata.add(1.0);
        this.ydata.add(3.0);
        this.ydata.add(3.0);
        this.ydata.add(3.0);
        this.ydata.add(2.0);
        this.ydata.add(4.0);
        this.ydata.add(6.0);
    }

    public void recomputeModel() {
        Double totalLossW = 0.0;
        Double totalLossB = 0.0;
        
        //compute loss for w and b
        for (int i = 0; i < this.xdata.size(); i++) {
            totalLossW += ((-2.0) * this.xdata.get(i) * (this.ydata.get(i) - ((this.w * this.xdata.get(i)) + this.b)));
            totalLossB += ((-2.0) * (this.ydata.get(i) - ((this.w * this.xdata.get(i)) + this.b)));
        }

        Double lossAdjW = totalLossW / this.xdata.size();
        Double lossAdjB = totalLossB / this.xdata.size();

        this.w = this.w - (this.lr * lossAdjW);
        this.b = this.b - (this.lr * lossAdjB);
    }

    public void printModel() {
        System.out.println("Model: w = " + this.w + " b = " + this.b);
    }

    public static void main(String[] args) {
        Early t = new Early();
        for (int i = 0; i < 20; i++) {
            t.recomputeModel();
            t.printModel();
        }
    }
}