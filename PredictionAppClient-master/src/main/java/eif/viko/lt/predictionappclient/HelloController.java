package eif.viko.lt.predictionappclient;

import eif.viko.lt.predictionappclient.Services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField password;

    @FXML
    private TextField username;

    @FXML
    private Button loginBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private VBox authPanelBox;


    @FXML
    private Text mainTabLabel;

    @FXML
    private Tab chatTab;

    @FXML
    private Tab predictionTab;


    @FXML
    private TextArea chatBotAnswerTextArea;

    @FXML
    private TextField chatBotMessageInput;


    private final AuthServiceImpl authService = new AuthServiceImpl();

    private final ChatBotServiceImpl chatBotService = new ChatBotServiceImpl();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //SecureStorage.clearToken();
        boolean isAuthenticated = SecureStorage.getToken() == null;
        authPanelBox.setVisible(isAuthenticated);
        chatTab.setDisable(isAuthenticated);
        predictionTab.setDisable(isAuthenticated);
        mainTabLabel.setText(SecureStorage.getToken());
        chatBotAnswerTextArea.setText("Sveiki! Užduokite klausimą iš Java programavimo kalbos.\n");

        //Enter simbolio paspaudimas
        chatBotMessageInput.setOnKeyPressed(this::handleKeyPress);

    }



    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            // Trigger the askChatBot method
            askChatBot(new ActionEvent());
            // Clear the input field after sending the message
            chatBotMessageInput.clear();
        }
    }

    @FXML
    void askChatBot(ActionEvent event) {

        var question = chatBotMessageInput.getText();

        if (!question.isEmpty()) {

            chatBotAnswerTextArea.appendText(
                    """
                    Jūsų klausimas
                    """);
            chatBotAnswerTextArea.appendText("\t"+question + "\n");


            chatBotService.sendMessage(question, new ChatBotCallback() {

                @Override
                public void onLoginSuccess(String message) {
                    System.out.println(message);
                    chatBotAnswerTextArea.appendText("""
                            Pokalbių roboto atsakymas
                            """);
                    chatBotAnswerTextArea.appendText("\t"+message+"\n");
                }

                @Override
                public void onLoginFailure(String errorMessage) {
                    System.out.println(errorMessage);
                }
            });
        }
    }


    @FXML
    void login(ActionEvent event) {

        String user = username.getText();
        String pass = password.getText();

        if (user != null && pass != null)
            authService.login(user, pass, new LoginCallback() {
                @Override
                public void onLoginSuccess(String token) {
                    authPanelBox.setVisible(false);
                    mainTabLabel.setText("Sveiki prisijungę");
                    logoutBtn.setVisible(true);
                    chatTab.setDisable(false);
                    predictionTab.setDisable(false);
                }

                @Override
                public void onLoginFailure(String errorMessage) {
                    chatTab.setDisable(false);
                    predictionTab.setDisable(false);
                }
            });

//        AuthServiceImpl authService = new AuthServiceImpl();
//        if (username != null && password != null)
//            authService.login(username.getText(), password.getText());
    }

    @FXML
    void logout(ActionEvent event) {
        SecureStorage.clearToken();
        authPanelBox.setVisible(true);
        logoutBtn.setVisible(false);
    }

}