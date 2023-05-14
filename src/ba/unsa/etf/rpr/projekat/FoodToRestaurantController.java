package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FoodToRestaurantController {
    public ListView foodList;

    public static Restaurant currentRestaurant = null;

    private ObservableList<Food> foodOL = FXCollections.observableArrayList();
    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();

    @FXML
    public void initialize(){
        ArrayList<Food> food1 = deliveryDAO.getAllFood();
        ArrayList<Food> food2 = deliveryDAO.findFoodFromRestaurant(currentRestaurant.getName());

        outer: for(Food food : food1){
            for(Food foodt : food2){
                if(food.getName().equals(foodt.getName())){
                    continue outer;
                }
            }
            foodOL.add(food);
        }
        foodList.setItems(foodOL);
    }

    public void addClicked(ActionEvent actionEvent) {
        if(foodList.getSelectionModel().getSelectedItem() == null)
            return;

        deliveryDAO.addMenu(new Menu(1, currentRestaurant, (Food)foodList.getSelectionModel().getSelectedItem()));

        foodOL.remove(foodList.getSelectionModel().getSelectedIndex());
    }
}
