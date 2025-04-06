// RegisterCallback.java - sukurkite naują callback interface
package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.UserProfile;

public interface RegisterCallback {
    void onRegisterSuccess(UserProfile userProfile);
    void onRegisterFailure(String errorMessage);
}