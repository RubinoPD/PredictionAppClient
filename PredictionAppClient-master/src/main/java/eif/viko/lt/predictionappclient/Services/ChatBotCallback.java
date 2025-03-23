package eif.viko.lt.predictionappclient.Services;

public interface ChatBotCallback {
    void onLoginSuccess(String message);
    void onLoginFailure(String errorMessage);
}
