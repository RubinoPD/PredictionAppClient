package eif.viko.lt.predictionappclient;

import eif.viko.lt.predictionappclient.Dto.GradeHistory;
import eif.viko.lt.predictionappclient.Dto.UserProfile;
import eif.viko.lt.predictionappclient.Services.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    // Authentication elements
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

    // Tab elements
    @FXML
    private Tab chatTab;
    @FXML
    private Tab predictionTab;

    // ChatBot elements
    @FXML
    private TextArea chatBotAnswerTextArea;
    @FXML
    private TextField chatBotMessageInput;

    // Prediction elements
    @FXML
    private Slider attendanceSlider;
    @FXML
    private Slider assignmentsSlider;
    @FXML
    private Slider midtermSlider;
    @FXML
    private Slider finalExamSlider;
    @FXML
    private Label attendanceLabel;
    @FXML
    private Label assignmentsLabel;
    @FXML
    private Label midtermLabel;
    @FXML
    private Label finalExamLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private TableView<GradeHistory> historyTable;
    @FXML
    private TableColumn<GradeHistory, String> dateColumn;
    @FXML
    private TableColumn<GradeHistory, Double> attendanceColumn;
    @FXML
    private TableColumn<GradeHistory, Double> assignmentsColumn;
    @FXML
    private TableColumn<GradeHistory, Double> midtermColumn;
    @FXML
    private TableColumn<GradeHistory, Double> finalExamColumn;
    @FXML
    private TableColumn<GradeHistory, String> predictedGradeColumn;

    // Services
    private final AuthServiceImpl authService = new AuthServiceImpl();
    private final ChatBotServiceImpl chatBotService = new ChatBotServiceImpl();
    private final PredictionServiceImpl predictionService = new PredictionServiceImpl();
    private final UserServiceImpl userService = new UserServiceImpl();

    // Data
    private ObservableList<GradeHistory> historyData = FXCollections.observableArrayList();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Check authentication
        boolean isAuthenticated = SecureStorage.getToken() == null;
        authPanelBox.setVisible(isAuthenticated);
        chatTab.setDisable(isAuthenticated);
        predictionTab.setDisable(isAuthenticated);
        mainTabLabel.setText(SecureStorage.getToken());

        // Initialize ChatBot
        chatBotAnswerTextArea.setText("Sveiki! Užduokite klausimą iš Java programavimo kalbos.\n");
        chatBotMessageInput.setOnKeyPressed(this::handleKeyPress);

        // Initialize Prediction sliders
        setupSliderListeners();

        // Initialize history table
        initializeHistoryTable();
    }

    private void setupSliderListeners() {
        // Attendance slider listener
        attendanceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int value = newValue.intValue();
            attendanceLabel.setText(String.valueOf(value));
        });

        // Assignments slider listener
        assignmentsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int value = newValue.intValue();
            assignmentsLabel.setText(String.valueOf(value));
        });

        // Midterm slider listener
        midtermSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int value = newValue.intValue();
            midtermLabel.setText(String.valueOf(value));
        });

        // Final exam slider listener
        finalExamSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int value = newValue.intValue();
            finalExamLabel.setText(String.valueOf(value));
        });
    }

    private void initializeHistoryTable() {
        dateColumn.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getPredictionDate();
            return new SimpleStringProperty(date != null ? date.format(DATE_FORMATTER) : "");
        });

        attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        assignmentsColumn.setCellValueFactory(new PropertyValueFactory<>("assignments"));
        midtermColumn.setCellValueFactory(new PropertyValueFactory<>("midterm"));
        finalExamColumn.setCellValueFactory(new PropertyValueFactory<>("finalExam"));
        predictedGradeColumn.setCellValueFactory(new PropertyValueFactory<>("predictedGrade"));

        historyTable.setItems(historyData);
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            askChatBot(new ActionEvent());
            chatBotMessageInput.clear();
        }
    }

    @FXML
    void askChatBot(ActionEvent event) {
        var question = chatBotMessageInput.getText();

        if (!question.isEmpty()) {
            chatBotAnswerTextArea.appendText("\nJūsų klausimas\n");
            chatBotAnswerTextArea.appendText("\t" + question + "\n");

            chatBotService.sendMessage(question, new ChatBotCallback() {
                @Override
                public void onLoginSuccess(String message) {
                    chatBotAnswerTextArea.appendText("Pokalbių roboto atsakymas\n");
                    chatBotAnswerTextArea.appendText("\t" + message + "\n");
                }

                @Override
                public void onLoginFailure(String errorMessage) {
                    chatBotAnswerTextArea.appendText("Klaida: " + errorMessage + "\n");
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

                    // Gauti naudotojo informacija
                    userService.getCurrentUser(new UserCallback() {
                        @Override
                        public void onUserFetched(UserProfile userProfile) {
                            String userName = userProfile.getUsername();
                            mainTabLabel.setText("Sveiki prisijunge, " + userName);
                        }

                        @Override
                        public void onUserFetchFailed(String errorMessage) {
                            mainTabLabel.setText("Sveiki prisijunge");
                        }
                    });
                }

                @Override
                public void onLoginFailure(String errorMessage) {
                    mainTabLabel.setText("Klaida: " + errorMessage);
                }
            });
    }

    @FXML
    void logout(ActionEvent event) {
        SecureStorage.clearToken();
        authPanelBox.setVisible(true);
        logoutBtn.setVisible(false);
        chatTab.setDisable(true);
        predictionTab.setDisable(true);
    }

    @FXML
    void predictGrade(ActionEvent event) {
        double attendance = attendanceSlider.getValue();
        double assignments = assignmentsSlider.getValue();
        double midterm = midtermSlider.getValue();
        double finalExam = finalExamSlider.getValue();

        predictionService.predictGrade(attendance, assignments, midterm, finalExam, new PredictionCallback() {
            @Override
            public void onPredictionSuccess(String predictedGrade) {
                resultLabel.setText("Prognozuojamas pažymys: " + predictedGrade);

                // Set color based on grade
                if ("A".equals(predictedGrade)) {
                    resultLabel.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
                } else if ("B".equals(predictedGrade)) {
                    resultLabel.setStyle("-fx-text-fill: darkgreen; -fx-font-size: 16px;");
                } else if ("C".equals(predictedGrade)) {
                    resultLabel.setStyle("-fx-text-fill: orange; -fx-font-size: 16px;");
                } else {
                    resultLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                }

                // Refresh history table
                showHistory(event);
            }

            @Override
            public void onPredictionFailure(String errorMessage) {
                resultLabel.setText("Klaida: " + errorMessage);
                resultLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            }
        });
    }

    @FXML
    void showHistory(ActionEvent event) {
        predictionService.getPredictionHistory(new HistoryCallback() {
            @Override
            public void onHistorySuccess(List<GradeHistory> history) {
                historyData.clear();
                historyData.addAll(history);
            }

            @Override
            public void onHistoryFailure(String errorMessage) {
                resultLabel.setText("Klaida gaunant istoriją: " + errorMessage);
                resultLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            }
        });
    }
}