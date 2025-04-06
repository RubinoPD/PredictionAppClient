package eif.viko.lt.predictionappclient.Dto;

public class PredictionResponse {
    private String predictedGrade;

    public PredictionResponse() {
    }

    public String getPredictedGrade() {
        return predictedGrade;
    }

    public void setPredictedGrade(String predictedGrade) {
        this.predictedGrade = predictedGrade;
    }
}
