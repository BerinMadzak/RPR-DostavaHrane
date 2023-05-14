package ba.unsa.etf.rpr.projekat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

public class RegisterController {
    public TextField usernameField;
    public PasswordField passwordField;
    public PasswordField repeatPasswordField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField addressField;
    public TextField phoneNumberField;

    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();

    private boolean[] correctFields = new boolean[8];

    private Pattern pattern = Pattern.compile("[A-Za-z0-9_]+");
    private Pattern pattern2 = Pattern.compile("[A-Za-zŽžŠšĐđĆćČč]+");
    private Pattern pattern3 = Pattern.compile("[0-9]+");

    @FXML
    public void initialize(){
        usernameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(validateUsername(newValue)){
                    usernameField.getStyleClass().removeAll("fieldInvalid");
                    usernameField.getStyleClass().add("fieldValid");
                    correctFields[0] = true;
                }else {
                    usernameField.getStyleClass().removeAll("fieldValid");
                    usernameField.getStyleClass().add("fieldInvalid");
                    correctFields[0] = false;
                }
            }
        });

        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    passwordField.getStyleClass().removeAll("fieldInvalid");
                    passwordField.getStyleClass().add("fieldValid");
                    correctFields[1] = true;
                } else {
                    passwordField.getStyleClass().removeAll("fieldValid");
                    passwordField.getStyleClass().add("fieldInvalid");
                    correctFields[1] = false;
                }
            }
        });

        repeatPasswordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals(passwordField.textProperty().get())){
                    repeatPasswordField.getStyleClass().removeAll("fieldInvalid");
                    repeatPasswordField.getStyleClass().add("fieldValid");
                    correctFields[2] = true;
                } else {
                    repeatPasswordField.getStyleClass().removeAll("fieldValid");
                    repeatPasswordField.getStyleClass().add("fieldInvalid");
                    correctFields[2] = false;
                }
            }
        });

        firstNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(validateName(newValue)){
                    firstNameField.getStyleClass().removeAll("fieldInvalid");
                    firstNameField.getStyleClass().add("fieldValid");
                    correctFields[3] = true;
                } else {
                    firstNameField.getStyleClass().removeAll("fieldValid");
                    firstNameField.getStyleClass().add("fieldInvalid");
                    correctFields[3] = false;
                }
            }
        });

        lastNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(validateName(newValue)){
                    lastNameField.getStyleClass().removeAll("fieldInvalid");
                    lastNameField.getStyleClass().add("fieldValid");
                    correctFields[4] = true;
                } else {
                    lastNameField.getStyleClass().removeAll("fieldValid");
                    lastNameField.getStyleClass().add("fieldInvalid");
                    correctFields[4] = false;
                }
            }
        });

        emailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                EmailValidator emailValidator = EmailValidator.getInstance();
                if(emailValidator.isValid(newValue)){
                    emailField.getStyleClass().removeAll("fieldInvalid");
                    emailField.getStyleClass().add("fieldValid");
                    correctFields[5] = true;
                } else {
                    emailField.getStyleClass().removeAll("fieldValid");
                    emailField.getStyleClass().add("fieldInvalid");
                    correctFields[5] = false;
                }
            }
        });

        addressField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    addressField.getStyleClass().removeAll("fieldInvalid");
                    addressField.getStyleClass().add("fieldValid");
                    correctFields[6] = true;
                } else {
                    addressField.getStyleClass().removeAll("fieldValid");
                    addressField.getStyleClass().add("fieldInvalid");
                    correctFields[6] = false;
                }
            }
        });

        phoneNumberField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(validatePhone(newValue)){
                    phoneNumberField.getStyleClass().removeAll("fieldInvalid");
                    phoneNumberField.getStyleClass().add("fieldValid");
                    correctFields[7] = true;
                } else {
                    phoneNumberField.getStyleClass().removeAll("fieldValid");
                    phoneNumberField.getStyleClass().add("fieldInvalid");
                    correctFields[7] = false;
                }
            }
        });
    }

    public void registerPressed(ActionEvent actionEvent) {
        boolean valid = true;

        for(int i = 0; i < 8; i++)
            if(correctFields[i] == false) {
                valid = false;
                break;
            }

        if(valid) {
            register();
        } else {
            System.out.println(correctFields);
            System.out.println("Dodaj kod pri pogresnim podacima");
        }
    }

    public void register(){
        deliveryDAO.addUser(new User(1, usernameField.textProperty().get(), passwordField.textProperty().get(), emailField.textProperty().get(), firstNameField.textProperty().get(),
                lastNameField.textProperty().get(), addressField.textProperty().get(), phoneNumberField.textProperty().get()));
        Stage stage = (Stage)usernameField.getScene().getWindow();
        stage.close();
    }

    private boolean validateUsername(String username){
        if(username.isEmpty() || !pattern.matcher(username).matches())
            return false;

        return !deliveryDAO.userExists(username);
    }

    private boolean validateName(String name){
        if(name.isEmpty() || !pattern2.matcher(name).matches())
            return false;

        return true;
    }

    private boolean validatePhone(String phone){
        if(phone.isEmpty() || phone.length() < 5 || !pattern3.matcher(phone).matches())
            return false;

        return true;
    }
}
