package ba.unsa.etf.rpr.projekat;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RestaurantController {

    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();

    public ListView foodList;
    public ListView orderList;
    public Label priceField;
    public Label restaurantName;

    private SimpleStringProperty price;

    public static Restaurant currentRestaurant = null;
    private ObservableList<Food> currentOrder;
    private ObservableList<Food> currentFood;

    public RestaurantController(){
        currentOrder = FXCollections.observableArrayList();
        currentFood = FXCollections.observableArrayList();
    }

    public SimpleStringProperty priceProperty(){
        return price;
    }

    public String getPrice(){
        return price.get();
    }

    @FXML
    public void initialize(){
        setValues();
        price = new SimpleStringProperty("0");
        priceField.textProperty().bindBidirectional(price);
    }

    private void setValues(){
        restaurantName.textProperty().setValue(currentRestaurant.getName());
        foodList.setItems(currentFood);
        setFood();
    }

    private void setFood(){
        ArrayList<Food> foodListA = deliveryDAO.findFoodFromRestaurant(currentRestaurant.getName());
        for(Food food : foodListA){
            currentFood.add(food);
        }
        currentOrder.clear();
    }

    private void clearFood(){
        currentFood.clear();
    }

    public void addToOrderButtonPressed(ActionEvent actionEvent) {
        if(foodList.getSelectionModel().getSelectedItem() == null)
            return;
        ArrayList<Food> foodListA = deliveryDAO.findFoodFromRestaurant(currentRestaurant.getName());
        Food food = foodListA.get(foodList.getSelectionModel().getSelectedIndex());
        currentOrder.add(food);
        Double dl = (Double.parseDouble(price.get()) + food.getPrice());
        priceProperty().setValue(dl.toString());
    }

    public void orderPressed(ActionEvent actionEvent) {
        if(currentOrder.size() == 0 || Calendar.HOUR_OF_DAY < 8 || Calendar.HOUR_OF_DAY >= 23){
            return;
        }

        deliveryDAO.addNewOrder(currentOrder, MainController.user, currentRestaurant);
        currentOrder.clear();
        Stage stage = (Stage)foodList.getScene().getWindow();
        MainController.selectionWindowOpen = false;
        stage.close();
    }

    public void removeButtonPressed(ActionEvent actionEvent) {
        if(orderList.getSelectionModel().getSelectedItem() == null)
            return;
        ObservableList<Food> foodListA = orderList.getItems();
        currentOrder.remove(foodListA.get(orderList.getSelectionModel().getSelectedIndex()));
        Double dl = (Double.parseDouble(price.get()) - foodListA.get(orderList.getSelectionModel().getSelectedIndex()).getPrice());
        priceProperty().setValue(dl.toString());
    }

    public void orderSelected(Event event) {
        orderList.setItems(currentOrder);
    }
}
