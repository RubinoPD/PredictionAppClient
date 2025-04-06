package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.UserProfile;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("api/users/me")
    Call<UserProfile> getCurrentUser();
}
