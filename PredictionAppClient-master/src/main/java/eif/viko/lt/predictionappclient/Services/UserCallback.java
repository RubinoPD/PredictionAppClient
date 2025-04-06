package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.UserProfile;

public interface UserCallback {
    void onUserFetched(UserProfile userProfile);
    void onUserFetchFailed(String errorMessage);
}