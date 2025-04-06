package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.GradeHistory;

public interface UpdateGradeCallback {
    void onUpdateSuccess(GradeHistory updateHistory);
    void onUpdateFailure(String errorMessage);
}
