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
import javafx.application.Platform;

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

    // Register elements
    @FXML
    private TextField registerUsername;
    @FXML
    private TextField registerEmail;
    @FXML
    private TextField registerPassword;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Label registerStatus;

    // Tab elements
    @FXML
    private Tab chatTab;
    @FXML
    private Tab predictionTab;
    @FXML
    private Tab adminTab;

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
        // Patikrinam ar turime issaugota token
        boolean isAuthenticated = SecureStorage.getToken() != null;

        // Nustatome UI elementu pradine busena
        authPanelBox.setVisible(!isAuthenticated);
        chatTab.setDisable(!isAuthenticated);
        predictionTab.setDisable(!isAuthenticated);
        mainTabLabel.setText(SecureStorage.getToken());

        // Uztikriname, kad atsijungimo mygtukas butu matomas tik jei vartotojas prisijunges
        logoutBtn.setVisible(isAuthenticated);

        // Pradine teksto reiksme
        mainTabLabel.setText("");

        // Initialize ChatBot
        chatBotAnswerTextArea.setText("Sveiki! Užduokite klausimą iš Java programavimo kalbos.\n");
        chatBotMessageInput.setOnKeyPressed(this::handleKeyPress);

        // Initialize Prediction sliders
        setupSliderListeners();

        // Initialize history table
        initializeHistoryTable();

        // Roliu upzildymas
        roleComboBox.getItems().addAll("USER", "TEACHER");
        roleComboBox.setValue("USER");

        // Pradzioje admin skirtuko nerodom
        adminTab.setDisable(true);

        // Patikrinti ar vartotojas yra administratorius po prisijungimo
        // Tai bus daroma login metode
    }

    // Metodas roles patikrinimui
    private void checkUserRole(UserProfile userProfile) {
        if (userProfile != null && "ADMIN".equals(userProfile.getRole())) {
            adminTab.setDisable(false);
        } else {
            adminTab.setDisable(true);
        }
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
                    Platform.runLater(() -> {
                        chatBotAnswerTextArea.appendText("Pokalbių roboto atsakymas\n");
                        chatBotAnswerTextArea.appendText("\t" + message + "\n");
                    });
                }

                @Override
                public void onLoginFailure(String errorMessage) {
                    Platform.runLater(() -> {
                        chatBotAnswerTextArea.appendText("Klaida: " + errorMessage + "\n");
                    });
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
                    Platform.runLater(() -> {
                        authPanelBox.setVisible(false);
                        logoutBtn.setVisible(true);
                        chatTab.setDisable(false);
                        predictionTab.setDisable(false);

                        // Gaukite naudotojo informaciją
                        userService.getCurrentUser(new UserCallback() {
                            @Override
                            public void onUserFetched(UserProfile userProfile) {
                                Platform.runLater(() -> {
                                    String userName = userProfile.getUsername();
                                    mainTabLabel.setText("Sveiki prisijungę, " + userName);

                                    // Patikrinti rolę
                                    checkUserRole(userProfile);
                                });
                            }

                            @Override
                            public void onUserFetchFailed(String errorMessage) {
                                Platform.runLater(() -> {
                                    mainTabLabel.setText("Sveiki prisijungę");
                                });
                            }
                        });
                    });
                }

                @Override
                public void onLoginFailure(String errorMessage) {
                    Platform.runLater(() -> {
                        mainTabLabel.setText("Klaida: " + errorMessage);
                    });
                }
            });
    }

    @FXML
    void registerUser(ActionEvent event) {
        String username = registerUsername.getText();
        String email = registerEmail.getText();
        String password = registerPassword.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            registerStatus.setText("Visi laukai turi buti uzpildyti");
            registerStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        authService.register(username, email, password, role, new RegisterCallback() {
            @Override
            public void onRegisterSuccess(UserProfile userProfile) {
                Platform.runLater(() -> {
                    registerStatus.setText("Vartotojas " + userProfile.getUsername() + " sėkmingai užregistruotas");
                    registerStatus.setStyle("-fx-text-fill: green;");

                    // Išvalyti laukus
                    registerUsername.clear();
                    registerEmail.clear();
                    registerPassword.clear();
                    roleComboBox.setValue("USER");
                });
            }

            @Override
            public void onRegisterFailure(String errorMessage) {
                registerStatus.setText("Klaida: " + errorMessage);
                registerStatus.setStyle("-fx-text-fill: red;");
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
        adminTab.setDisable(true);

        // Isvalyti sveiknimo teksta
        mainTabLabel.setText("");
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
                Platform.runLater(() -> {
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
                });
            }

            @Override
            public void onPredictionFailure(String errorMessage) {
                Platform.runLater(() -> {
                    resultLabel.setText("Klaida: " + errorMessage);
                    resultLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                });
            }
        });
    }

    @FXML
    void showHistory(ActionEvent event) {
        predictionService.getPredictionHistory(new HistoryCallback() {
            @Override
            public void onHistorySuccess(List<GradeHistory> history) {
                Platform.runLater(() -> {
                    historyData.clear();
                    historyData.addAll(history);
                });
            }

            @Override
            public void onHistoryFailure(String errorMessage) {
                Platform.runLater(() -> {
                    resultLabel.setText("Klaida gaunant istoriją: " + errorMessage);
                    resultLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                });
            }
        });
    }
}