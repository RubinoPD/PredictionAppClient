// UserServiceImpl.java
package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserServiceImpl {
    private final UserService userService;

    public UserServiceImpl() {
        Retrofit client = ApiClientWithAuth.getClient();
        userService = client.create(UserService.class);
    }

    public void getCurrentUser(final UserCallback callback) {
        Call<UserProfile> call = userService.getCurrentUser();

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onUserFetched(response.body());
                } else {
                    callback.onUserFetchFailed("Nepavyko gauti naudotojo informacijos");
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                callback.onUserFetchFailed(t.getMessage());
            }
        });
    }
}