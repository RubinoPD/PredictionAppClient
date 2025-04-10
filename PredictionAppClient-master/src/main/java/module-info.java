module eif.viko.lt.predictionappclient {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.base;
    requires java.prefs;
    requires java.sql;
    requires com.google.gson;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires okhttp3;


    opens eif.viko.lt.predictionappclient.Dto to com.google.gson;

    opens eif.viko.lt.predictionappclient to javafx.fxml;
    exports eif.viko.lt.predictionappclient;

    // Eksportuojam papildomus paketus
    exports eif.viko.lt.predictionappclient.Services;
    exports eif.viko.lt.predictionappclient.Dto;
}