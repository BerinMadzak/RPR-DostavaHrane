package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class MenuController {

    public enum Account {Administrator, Customer}

    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();

    public Label wrongUserLabel;
    public Label wrongPasswordLabel;
    public TextField usernameField;
    public PasswordField passwordField;

    public void loginPressed(ActionEvent actionEvent) {
        String username = usernameField.textProperty().get();
        String password = passwordField.textProperty().get();

        if(!deliveryDAO.userExists(username)){
            wrongPasswordLabel.setVisible(false);
            wrongUserLabel.setVisible(true);
            return;
        }

        User user = deliveryDAO.findUserByUsername(username);

        if(!deliveryDAO.checkPassword(password, Integer.parseInt(user.getPassword()))){
            wrongUserLabel.setVisible(false);
            wrongPasswordLabel.setVisible(true);
            return;
        }

        Account account = Account.Customer;

        if(user.getEmail().contains("@dostava.com")){
            account = Account.Administrator;
        }

        login(user, account);
    }

    public void registerPressed(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
            stage.setTitle("Dostava");
            stage.setResizable(false);
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(User user, Account account){
        try {
            MainController.user = user;
            MainController.account = account;
            Stage stage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            stage.setTitle("Dostava");
            stage.setResizable(false);
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.show();
            Stage stage1 = (Stage)usernameField.getScene().getWindow();
            stage1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
