package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.GradeHistory;
import eif.viko.lt.predictionappclient.Dto.PredictionRequest;
import eif.viko.lt.predictionappclient.Dto.PredictionResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictionServiceImpl {
    private final PredictionService predictionService;

    public PredictionServiceImpl() {
        Retrofit client = ApiClientWithAuth.getClient();
        predictionService = client.create(PredictionService.class);
    }

    public void predictGrade(double attendance, double assignments, double midterm, double finalExam,
                             PredictionCallback callback) {
        PredictionRequest request = new PredictionRequest(attendance, assignments, midterm, finalExam);
        Call<PredictionResponse> call = predictionService.predictGrade(request);

        call.enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onPredictionSuccess(response.body().getPredictedGrade());
                } else {
                    callback.onPredictionFailure("Failed to predict: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable t) {
                callback.onPredictionFailure("Error: " + t.getMessage());
            }
        });
    }

    public void getPredictionHistory(HistoryCallback callback) {
        Call<List<GradeHistory>> call = predictionService.getPredictionHistory();

        call.enqueue(new Callback<List<GradeHistory>>() {
            @Override
            public void onResponse(Call<List<GradeHistory>> call, Response<List<GradeHistory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onHistorySuccess(response.body());
                } else {
                    callback.onHistoryFailure("Failed to get history: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<GradeHistory>> call, Throwable t) {
                callback.onHistoryFailure("Error: " + t.getMessage());
            }
        });
    }

    public void updateActualGrade(Long predictionId, String actualGrade, UpdateGradeCallback callback) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("actualGrade", actualGrade);

        Call<GradeHistory> call = predictionService.updateActualGrade(predictionId, requestBody);

        call.enqueue(new Callback<GradeHistory>() {
            @Override
            public void onResponse(Call<GradeHistory> call, Response<GradeHistory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onUpdateSuccess(response.body());
                } else {
                    callback.onUpdateFailure("Failed to update: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GradeHistory> call, Throwable t) {
                callback.onUpdateFailure("Error: " + t.getMessage());
            }
        });
    }
}