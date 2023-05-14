package ba.unsa.etf.rpr.projekat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RestaurantChangeController {
    public TextField nameField;
    public TextField addressField;
    public TextField descField;
    public ChoiceBox categoryBox;
    public ListView restaurantList;

    private Pattern pattern = Pattern.compile("[A-Za-z0-9_ ]+");

    private ObservableList<CategoryRestaurant> categoryList = FXCollections.observableArrayList();
    private ObservableList<Restaurant> restaurants = FXCollections.observableArrayList();

    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();
    private boolean[] correctFields = new boolean[3];
    private Restaurant currentRestaurant = null;

    @FXML
    public void initialize(){

        setRestaurants();
        restaurantList.setItems(restaurants);

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
                    correctFields[2] = true;
                }else {
                    descField.getStyleClass().removeAll("fieldValid");
                    descField.getStyleClass().add("fieldInvalid");
                    correctFields[2] = false;
                }
            }
        });
    }

    private boolean validateName(String name){
        if(name.isEmpty() || !pattern.matcher(name).matches())
            return false;

        if(name.equals(currentRestaurant.getName()))
            return true;

        return !deliveryDAO.restaurantExists(name);
    }

    public void changeButtonPressed(ActionEvent actionEvent) {
        for(int i = 0; i < 3; i++){
            if(correctFields[i] == false){
                return;
            }
        }

        CategoryRestaurant categoryRestaurant = deliveryDAO.findRestaurantCategoryByName(categoryList.get(categoryBox.getSelectionModel().getSelectedIndex()).getName());

        currentRestaurant.setName(nameField.textProperty().get());
        currentRestaurant.setAddress(addressField.textProperty().get());
        currentRestaurant.setCategory(categoryRestaurant);
        currentRestaurant.setDescription(descField.textProperty().get());

        deliveryDAO.changeRestaurant(currentRestaurant);
    }

    private void resetFields(){
        for(int i = 0; i < 3; i++)
            correctFields[i] = true;
    }

    public void listClicked(MouseEvent mouseEvent) {

        if(restaurantList.getSelectionModel().getSelectedItem() == null)
            return;

        resetFields();
        currentRestaurant = restaurants.get(restaurantList.getSelectionModel().getSelectedIndex());
        nameField.textProperty().setValue(currentRestaurant.getName());
        addressField.textProperty().setValue(currentRestaurant.getAddress());
        descField.textProperty().setValue(currentRestaurant.getDescription());

        int catIndex = -1;
        for(int i = 0; i < categoryList.size(); i++){
            if(categoryList.get(i).getName().equals(currentRestaurant.getCategory().getName())) {
                catIndex = i;
                break;
            }
        }

        categoryBox.getSelectionModel().select(catIndex);
    }

    public void setRestaurants(){
        restaurants.clear();
        ArrayList<Restaurant> restaurants1 = deliveryDAO.getAllRestaurants();
        for(Restaurant restaurant : restaurants1){
            restaurants.add(restaurant);
        }
    }

}
