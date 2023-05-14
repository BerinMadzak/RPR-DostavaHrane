package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class OrderController {

    public ListView foodList;
    public Label titleLabel;

    public static String title = "";
    public static Order order;

    private ObservableList<Food> food = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        titleLabel.textProperty().setValue(title);
        loadFood();
        foodList.setItems(food);
    }

    private void loadFood(){
        food.clear();
        ArrayList<Food> list = DeliveryDAO.getInstance().getFoodFromOrder(order.getId());
        for(Food foodTemp : list){
            food.add(foodTemp);
        }
    }

    public void statisticsButtonPressed(ActionEvent actionEvent) {
    }
}
