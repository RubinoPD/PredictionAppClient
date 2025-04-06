package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.GradeHistory;
import eif.viko.lt.predictionappclient.Dto.PredictionRequest;
import eif.viko.lt.predictionappclient.Dto.PredictionResponse;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface PredictionService {
    @POST("api/predict/grade")
    Call<PredictionResponse> predictGrade(@Body PredictionRequest request);

    @GET("api/predict/grade")
    Call<List<GradeHistory>> getPredictionHistory();

    @GET("api/predict/analytics/weak-students")
    Call<List<GradeHistory>> getWeakStudents();

    @GET("api/predict/analytics/accuracy")
    Call<Map<String, Object>> getPredictionAccuracy();

    @PUT("api/predict/history/{id}/actual-grade")
    Call<GradeHistory> updateActualGrade(@Path("id") Long id, @Body Map<String, String> actualGrade);

}
