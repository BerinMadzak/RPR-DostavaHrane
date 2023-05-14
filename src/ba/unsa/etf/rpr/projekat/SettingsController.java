package ba.unsa.etf.rpr.projekat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

public class SettingsController {
    public TextField usernameField;
    public TextField addressField;
    public PasswordField passField;
    public PasswordField repPassField;
    public TextField emailField;
    public TextField phoneField;

    private Pattern pattern = Pattern.compile("[A-Za-z0-9_]+");
    private Pattern pattern2 = Pattern.compile("[0-9]+");
    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();

    private boolean[] correctFields = new boolean[4];

    @FXML
    public void initialize(){

        setValues();

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

        emailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                EmailValidator emailValidator = EmailValidator.getInstance();
                if(emailValidator.isValid(newValue)){
                    emailField.getStyleClass().removeAll("fieldInvalid");
                    emailField.getStyleClass().add("fieldValid");
                    correctFields[1] = true;
                } else {
                    emailField.getStyleClass().removeAll("fieldValid");
                    emailField.getStyleClass().add("fieldInvalid");
                    correctFields[1] = false;
                }
            }
        });

        addressField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    addressField.getStyleClass().removeAll("fieldInvalid");
                    addressField.getStyleClass().add("fieldValid");
                    correctFields[2] = true;
                } else {
                    addressField.getStyleClass().removeAll("fieldValid");
                    addressField.getStyleClass().add("fieldInvalid");
                    correctFields[2] = false;
                }
            }
        });

        phoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(validatePhone(newValue)){
                    phoneField.getStyleClass().removeAll("fieldInvalid");
                    phoneField.getStyleClass().add("fieldValid");
                    correctFields[3] = true;
                } else {
                    phoneField.getStyleClass().removeAll("fieldValid");
                    phoneField.getStyleClass().add("fieldInvalid");
                    correctFields[3] = false;
                }
            }
        });
    }

    private boolean validatePhone(String phone){
        if(phone.isEmpty() || phone.length() < 5 || !pattern2.matcher(phone).matches())
            return false;

        return true;
    }

    private boolean validateUsername(String username){
        if(username.isEmpty() || !pattern.matcher(username).matches())
            return false;

        if(username.equals(MainController.user.getUsername()))
            return true;

        return !deliveryDAO.userExists(username);
    }

    public void updatePressed(ActionEvent actionEvent) {
        for(int i = 0; i < 4; i++){
            if(correctFields[i] == false)
                return;
        }

        User user = MainController.user;
        user.setUsername(usernameField.textProperty().get().trim());
        user.setAddress(addressField.textProperty().get());
        user.setEmail(emailField.textProperty().get());
        user.setPhoneNumber(phoneField.textProperty().get());
        deliveryDAO.updateUserQuery(user);
    }

    public void changePasswordPressed(ActionEvent actionEvent) {
        if(passField.textProperty().get().equals(repPassField.textProperty().get())){
            deliveryDAO.changeUserPassword(MainController.user, passField.textProperty().get());
        }
    }

    private void setValues(){
        usernameField.textProperty().setValue(MainController.user.getUsername());
        addressField.textProperty().setValue(MainController.user.getAddress());
        emailField.textProperty().setValue(MainController.user.getEmail());
        phoneField.textProperty().setValue(MainController.user.getPhoneNumber());
        for(int i = 0; i < 4; i++){
            correctFields[i] = true;
        }
    }
}
