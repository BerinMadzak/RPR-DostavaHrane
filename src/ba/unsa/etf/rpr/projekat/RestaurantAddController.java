package ba.unsa.etf.rpr.projekat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RestaurantAddController {
    public TextField nameField;
    public TextField addressField;
    public TextField descField;
    public ChoiceBox categoryBox;

    private Pattern pattern = Pattern.compile("[A-Za-z0-9_ ]+");

    private ObservableList<CategoryRestaurant> categoryList = FXCollections.observableArrayList();

    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();
    private boolean[] correctFields = new boolean[4];

    @FXML
    public void initialize(){
        categoryList.clear();
        ArrayList<CategoryRestaurant> categories = deliveryDAO.getAllRestaurantCategories();
        for(CategoryRestaurant cr : categories){
            categoryList.add(cr);
        }
        categoryBox.setItems(categoryList);

        nameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(validateName(newValue)){
                    nameField.getStyleClass().removeAll("fieldInvalid");
                    nameField.getStyleClass().add("fieldValid");
                    correctFields[0] = true;
                }else {
                    nameField.getStyleClass().removeAll("fieldValid");
                    nameField.getStyleClass().add("fieldInvalid");
                    correctFields[0] = false;
                }
            }
        });

        addressField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    addressField.getStyleClass().removeAll("fieldInvalid");
                    addressField.getStyleClass().add("fieldValid");
                    correctFields[1] = true;
                }else {
                    addressField.getStyleClass().removeAll("fieldValid");
                    addressField.getStyleClass().add("fieldInvalid");
                    correctFields[1] = false;
                }
            }
        });

        descField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    descField.getStyleClass().removeAll("fieldInvalid");
                    descField.getStyleClass().add("fieldValid");
                    correctFields[3] = true;
                }else {
                    descField.getStyleClass().removeAll("fieldValid");
                    descField.getStyleClass().add("fieldInvalid");
                    correctFields[3] = false;
                }
            }
        });
    }

    public void addPressed(ActionEvent actionEvent) {
        if(categoryBox.getSelectionModel().getSelectedItem() != null)
            correctFields[2] = true;
        for(int i = 0; i < 4; i++){
            if(correctFields[i] == false) {
                return;
            }
        }

        CategoryRestaurant cat = deliveryDAO.findRestaurantCategoryByName(categoryBox.getSelectionModel().getSelectedItem().toString());

        deliveryDAO.addRestaurant(new Restaurant(0, nameField.textProperty().get(), addressField.textProperty().get(), cat, descField.textProperty().get()));
        Stage stage = (Stage)nameField.getScene().getWindow();
        stage.close();
    }

    private boolean validateName(String name){
        if(name.isEmpty() || !pattern.matcher(name).matches())
            return false;

        return !deliveryDAO.restaurantExists(name);
    }
}
