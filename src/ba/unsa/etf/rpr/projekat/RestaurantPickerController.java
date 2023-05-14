package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class RestaurantPickerController {
    public ListView list;

    public static boolean update = false;

    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();
    private ObservableList<Restaurant> restaurants = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        ArrayList<Restaurant> restaurants1 = deliveryDAO.getAllRestaurants();
        for(Restaurant restaurant : restaurants1){
            restaurants.add(restaurant);
        }
        list.setItems(restaurants);
    }

    public void listClicked(MouseEvent mouseEvent) {
        if (list.getSelectionModel().getSelectedItem() == null)
            return;

        if (update) {
            MenuEditController.currentRestaurant = restaurants.get(list.getSelectionModel().getSelectedIndex());

            try {
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/menuEdit.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FoodToRestaurantController.currentRestaurant = restaurants.get(list.getSelectionModel().getSelectedIndex());

            try {
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/foodToRestaurant.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
