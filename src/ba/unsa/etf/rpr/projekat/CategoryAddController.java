package ba.unsa.etf.rpr.projekat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class CategoryAddController {
    public TextField nameField;

    public static boolean category;

    private Pattern pattern = Pattern.compile("[A-Za-z0-9_ŠšĐđČčĆćŽž ]+");
    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();

    private boolean correct = false;

    @FXML
    public void initialize(){
        nameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(validateName(newValue)){
                    nameField.getStyleClass().removeAll("fieldInvalid");
                    nameField.getStyleClass().add("fieldValid");
                    correct = true;
                }else {
                    nameField.getStyleClass().removeAll("fieldValid");
                    nameField.getStyleClass().add("fieldInvalid");
                    correct = false;
                }
            }
        });
    }

    private boolean validateName(String name){
        if(name.isEmpty() || !pattern.matcher(name).matches())
            return false;

        return !deliveryDAO.restaurantCategoyExists(name);
    }

    public void addPressed(ActionEvent actionEvent) {
        if(correct){
            if(!category){
                deliveryDAO.addRestaurantCategory(new CategoryRestaurant(1, nameField.textProperty().get()));
            } else {
                deliveryDAO.addFoodCategory(new CategoryFood(1, nameField.textProperty().get()));
            }

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        }
    }
}
