package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.GradeHistory;
import java.util.List;

public interface HistoryCallback {
    void onHistorySuccess(List<GradeHistory> history);
    void onHistoryFailure(String errorMessage);
}
