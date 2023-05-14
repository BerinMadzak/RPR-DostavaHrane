package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class MenuEditController {
    public ListView list;

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
                    foodOL.add(food);
                    continue outer;
                }
            }
        }
        list.setItems(foodOL);
    }

    public void removeClicked(ActionEvent actionEvent) {
        if(list.getSelectionModel().getSelectedItem() == null)
            return;

        deliveryDAO.deleteMenu(new Menu(1, currentRestaurant, (Food)list.getSelectionModel().getSelectedItem()));

        foodOL.remove(list.getSelectionModel().getSelectedIndex());
    }

}
