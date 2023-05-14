package ba.unsa.etf.rpr.projekat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CategoryChangeController {
    public TextField nameField;
    public ListView categoryList;

    public static boolean category;

    private ObservableList<CategoryRestaurant> restaurantCat = FXCollections.observableArrayList();
    private ObservableList<CategoryFood> foodCat = FXCollections.observableArrayList();

    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();
    private Pattern pattern = Pattern.compile("[A-Za-z0-9_ŠšĐđČčĆćŽž ]+");

    private CategoryRestaurant currentCR = null;
    private CategoryFood currentCF = null;

    private boolean correct = true;

    @FXML
    public void initialize(){
        restaurantCat.clear();
        foodCat.clear();
        if(!category){
            ArrayList<CategoryRestaurant> temp = deliveryDAO.getAllRestaurantCategories();
            for(CategoryRestaurant cr : temp){
                restaurantCat.add(cr);
            }
            categoryList.setItems(restaurantCat);
        } else {
            ArrayList<CategoryFood> temp = deliveryDAO.getAllFoodCategories();
            for(CategoryFood cf : temp){
                foodCat.add(cf);
            }
            categoryList.setItems(foodCat);
        }

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

    public void changeButtonPressed(ActionEvent actionEvent) {
        if(categoryList.getSelectionModel().getSelectedItem() == null || !correct)
            return;

        if(!category){
            CategoryRestaurant crX = deliveryDAO.findRestaurantCategoryByName(currentCR.getName());
            restaurantCat.get(categoryList.getSelectionModel().getSelectedIndex()).setName(nameField.textProperty().get());
            crX.setName(nameField.textProperty().get());
            deliveryDAO.changeRestaurantCategory(crX);
        } else {
            CategoryFood cfX = deliveryDAO.findFoodCategoryByName(currentCF.getName());
            foodCat.get(categoryList.getSelectionModel().getSelectedIndex()).setName(nameField.textProperty().get());
            cfX.setName(nameField.textProperty().get());
            deliveryDAO.changeFoodCategory(cfX);
        }
    }

    public void listClicked(MouseEvent mouseEvent) {
        if(!category){
            CategoryRestaurant cr = (CategoryRestaurant)categoryList.getSelectionModel().getSelectedItem();
            currentCR = cr;
            nameField.textProperty().setValue(cr.getName());
        } else {
            CategoryFood cf = foodCat.get(categoryList.getSelectionModel().getSelectedIndex());
            currentCF = cf;
            nameField.textProperty().setValue(cf.getName());
        }
        correct = true;
    }

    private boolean validateName(String name){
        if(name.isEmpty() || !pattern.matcher(name).matches())
            return false;

        if(!category) {
            if (name.equals(currentCR.getName()))
                return true;
            return !deliveryDAO.restaurantCategoyExists(name);
        } else {
            if(name.equals(currentCF.getName()))
                return true;
            return !deliveryDAO.foodCategoryExists(name);
        }
    }
}
