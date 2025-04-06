package eif.viko.lt.predictionappclient.Dto;

import java.util.List;
import java.util.Map;

public class ChatBotResponse {

        private String bestCategory;
        private String response;
        private Map<String, Double>
                allCategories;
        public ChatBotResponse() {
        }

    public String getBestCategory() {
        return bestCategory;
    }

    public void setBestCategory(String bestCategory) {
        this.bestCategory = bestCategory;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Map<String, Double> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(Map<String, Double> allCategories) {
        this.allCategories = allCategories;
    }
}
