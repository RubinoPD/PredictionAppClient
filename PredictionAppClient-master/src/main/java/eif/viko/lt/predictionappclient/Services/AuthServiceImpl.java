package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.LoginRequest;
import eif.viko.lt.predictionappclient.Dto.LoginResponse;
import eif.viko.lt.predictionappclient.Dto.RegisterRequest;
import eif.viko.lt.predictionappclient.Dto.UserProfile;
import eif.viko.lt.predictionappclient.SecureStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthServiceImpl {
    private final AuthService authService;

    public AuthServiceImpl() {
        Retrofit client = ApiClient.getClient();
        authService = client.create(AuthService.class);
    }

    public void login(String username, String password, LoginCallback callback) {

        LoginRequest request = new LoginRequest(username, password);
        Call<LoginResponse> call = authService.login(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    System.out.println("JWT TOKENAS: " + token);
                    // Issaugome tokena
                    SecureStorage.saveToken(token);
                    callback.onLoginSuccess(token);
                }else{
                    System.out.println("LOGIN FAILED: " + response.code() + " - " + response.message());
                    callback.onLoginFailure("Statusas: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                System.out.println("LOGIN FAILED: " + throwable.getMessage());
                callback.onLoginFailure(throwable.getMessage());
            }
        });
    }

    public void register(String username, String email, String password, String role, final RegisterCallback callback) {
        RegisterRequest request = new RegisterRequest(email, password, username, role);
        Call<UserProfile> call = authService.register(request);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onRegisterSuccess(response.body());
                } else {
                    callback.onRegisterFailure("Registracija nepavyko: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                callback.onRegisterFailure("Klaida: " + t.getMessage());
            }
        });
    }

}
