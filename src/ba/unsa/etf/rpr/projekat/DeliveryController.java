package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class DeliveryController {
    public ListView orderList;
    public ListView deliveryList;

    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();
    private ObservableList<Order> firstList = FXCollections.observableArrayList();
    private ObservableList<Order> secondList = FXCollections.observableArrayList();

     @FXML
     public void initialize(){
         ArrayList<Order> orders = deliveryDAO.getNotDeliveredOrders();

         for(Order order : orders){
             firstList.add(order);
         }

         orderList.setItems(firstList);
         deliveryList.setItems(secondList);
     }

    public void addToDeliveryPressed(ActionEvent actionEvent) {
         if(orderList.getSelectionModel().getSelectedItem() == null)
             return;

         secondList.add(firstList.get(orderList.getSelectionModel().getSelectedIndex()));
         firstList.remove(orderList.getSelectionModel().getSelectedIndex());
    }

    public void removeFromDeliveryPressed(ActionEvent actionEvent) {
        if(deliveryList.getSelectionModel().getSelectedItem() == null)
            return;

        firstList.add(secondList.get(deliveryList.getSelectionModel().getSelectedIndex()));
        secondList.remove(deliveryList.getSelectionModel().getSelectedIndex());
    }

    public void confirmPressed(ActionEvent actionEvent) {
        for(Order order : secondList)
            deliveryDAO.setOrderAsDelivered(order);

        Stage stage = (Stage)orderList.getScene().getWindow();
        stage.close();
    }
}
