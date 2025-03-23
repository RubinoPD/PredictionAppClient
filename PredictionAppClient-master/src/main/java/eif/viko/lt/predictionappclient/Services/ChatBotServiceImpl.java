package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.ChatBotResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatBotServiceImpl {

    private final ChatBotService chatBotService;

    public ChatBotServiceImpl() {
        Retrofit client = ApiClientWithAuth.getClient();
        chatBotService = client.create(ChatBotService.class);
    }

    public void sendMessage(String message, ChatBotCallback callback) {

        Call<ChatBotResponse> call = chatBotService.ask(message);

        call.enqueue(new Callback<ChatBotResponse>() {

            @Override
            public void onResponse(Call<ChatBotResponse> call, Response<ChatBotResponse> response) {
                if (response.isSuccessful()) {
                    ChatBotResponse chatBotResponse = response.body();
                    if (chatBotResponse != null) {
                        callback.onLoginSuccess(chatBotResponse.getBestCategory());
                        System.out.println("Best Category: " + chatBotResponse.getBestCategory());
                        System.out.println("All Categories: " + chatBotResponse.getAllCategories());
                    }else {
                        System.out.println("Request failed: " + response.code());
                    }
                }
            }
            @Override
            public void onFailure(Call<ChatBotResponse> call, Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
                callback.onLoginFailure(throwable.getMessage());
            }
        });
    }

}
