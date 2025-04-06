package eif.viko.lt.predictionappclient.Services;

public interface PredictionCallback {
    void onPredictionSuccess(String predictedGrades);
    void onPredictionFailure(String errorMessage);
}
