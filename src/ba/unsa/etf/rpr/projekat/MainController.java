package ba.unsa.etf.rpr.projekat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class MainController {

    public static MenuController.Account account;
    public static User user;
    public Tab ordersMenu;
    public ListView restaurantList;
    public ChoiceBox restaurantCategoryChoiceBox;
    public TextField restaurantSearchField;
    public Button restaurantSearchButton;
    public Tab administratorMenu;
    public ListView orderList;
    public ChoiceBox actionChoiceBox;

    private DeliveryDAO deliveryDAO = DeliveryDAO.getInstance();

    private String currentSearch = "";
    private String categoryName = "All";
    private ObservableList<String> categoryRestaurants;
    private ObservableList<String> actions;
    private ArrayList<Restaurant> restaurants;

    private ObservableList<Order> userOrders = FXCollections.observableArrayList();

    private ChangeListener<Number> listener;
    public static boolean selectionWindowOpen = false;

    @FXML
    public void initialize(){
        if(account == MenuController.Account.Customer) {
            administratorMenu.getTabPane().getTabs().remove(administratorMenu);
        }

        loadRestaurants();
        loadOrders();

        orderList.setItems(userOrders);

            listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue != oldValue){
                    categoryName = restaurantCategoryChoiceBox.getItems().get((Integer)newValue).toString();
                    restaurantsByCategory(restaurantCategoryChoiceBox.getItems().get((Integer)newValue).toString());
                }
            }
        };

        reloadCategories();
    }

    private void loadOrders(){
        userOrders.clear();
        ArrayList<Order> orders = deliveryDAO.getAllOrdersByUser(user);
        for(Order order : orders){
            userOrders.add(order);
        }
    }

    private void loadRestaurants(){
        clearList();
        restaurants = deliveryDAO.getAllRestaurants();
        for(Restaurant restaurant : restaurants){
            if(currentSearch.trim() == "")
                restaurantList.getItems().add(restaurant.getName() + " - " + restaurant.getAddress() + "\n" + restaurant.getDescription());
            else{
                if(restaurant.getName().contains(currentSearch))
                    restaurantList.getItems().add(restaurant.getName() + " - " + restaurant.getAddress() + "\n" + restaurant.getDescription());
            }
        }
    }

    private void restaurantsByCategory(String category){
        clearList();
        if(category == "All"){
            loadRestaurants();
            return;
        }
        ArrayList<Restaurant> restaurants = deliveryDAO.getRestaurantsByCategory(category);
        for(Restaurant restaurant : restaurants){
            if(currentSearch.trim() == "")
                restaurantList.getItems().add(restaurant.getName() + " - " + restaurant.getAddress() + "\n" + restaurant.getDescription());
            else{
                if(restaurant.getName().toLowerCase().contains(currentSearch.toLowerCase()))
                    restaurantList.getItems().add(restaurant.getName() + " - " + restaurant.getAddress() + "\n" + restaurant.getDescription());
            }
        }
    }

    private void restaurantTextSearch(String search){
        clearList();
        ArrayList<Restaurant> restaurants2 = new ArrayList<>();
        currentSearch = search;
        if(categoryName == "All")
            restaurants2 = deliveryDAO.getAllRestaurants();
        else
            restaurants2 = deliveryDAO.getRestaurantsByCategory(categoryName);

        for(Restaurant restaurant : restaurants2){
            if(restaurant.getName().toLowerCase().contains(search.toLowerCase())){
                restaurantList.getItems().add(restaurant.getName() + " - " + restaurant.getAddress() + "\n" + restaurant.getDescription());
            }
        }
    }

    private ArrayList<Restaurant> getRestaurantsBySearch(){
        ArrayList<Restaurant> restaurants2 = new ArrayList<>();
        for(Restaurant restaurant : restaurants){
            if(restaurant.getName().toLowerCase().contains(currentSearch.toLowerCase()))
                restaurants2.add(restaurant);
        }
        return restaurants2;
    }

    private ArrayList<Restaurant> getRestaurantsByCategoryAndSearch(){
        ArrayList<Restaurant> restaurants2 = new ArrayList<>();
        ArrayList<Restaurant> restaurantsCat = deliveryDAO.getRestaurantsByCategory(categoryName);
        for(Restaurant restaurant : restaurantsCat){
            if(restaurant.getName().toLowerCase().contains(currentSearch.toLowerCase()))
                restaurants2.add(restaurant);
        }
        return restaurants2;
    }

    private void clearList(){
        restaurantList.getItems().clear();
    }

    private void reloadCategories(){
        categoryRestaurants = deliveryDAO.getRestaurantCategoriesNames();
        categoryRestaurants.add("All");
        Collections.sort(categoryRestaurants);
        restaurantCategoryChoiceBox.setItems(categoryRestaurants);
        restaurantCategoryChoiceBox.getSelectionModel().selectFirst();
        restaurantCategoryChoiceBox.getSelectionModel().selectedIndexProperty().addListener(listener);
    }

    public void searchRestaurants(ActionEvent actionEvent) {
        if(restaurantSearchField.textProperty().get() == "")
            loadRestaurants();
        else
            restaurantTextSearch(restaurantSearchField.textProperty().get());
    }

    public void ordersSelected(Event event) {
        loadOrders();
    }

    public void browseSelected(Event event) {

    }

    public void listClick(MouseEvent mouseEvent) {

        if(restaurantList.getSelectionModel().getSelectedItem() == null)
            return;

        Restaurant restaurant = null;
        if(categoryName == "All" && currentSearch.trim() == ""){
            restaurant = restaurants.get(restaurantList.getSelectionModel().getSelectedIndex());
        } else if (categoryName != "All" && currentSearch.trim() == ""){
            ArrayList<Restaurant> restaurants2 = deliveryDAO.getRestaurantsByCategory(categoryName);
            restaurant = restaurants2.get(restaurantList.getSelectionModel().getSelectedIndex());
        } else if (categoryName == "All" && currentSearch.trim() != ""){
            ArrayList<Restaurant> restaurants2 = getRestaurantsBySearch();
            restaurant = restaurants2.get(restaurantList.getSelectionModel().getSelectedIndex());
        } else if(categoryName != "All" && currentSearch.trim() != ""){
            ArrayList<Restaurant> restaurants2 = getRestaurantsByCategoryAndSearch();
            restaurant = restaurants2.get(restaurantList.getSelectionModel().getSelectedIndex());
        }

        if(restaurant != null && !selectionWindowOpen)
            startSelectionWindow(restaurant);
    }

    private void startSelectionWindow(Restaurant restaurant){
        try {
            RestaurantController.currentRestaurant = restaurant;
            selectionWindowOpen = true;
            Stage stage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/fxml/restaurant.fxml"));
            stage.setTitle("Dostava");
            stage.setResizable(false);
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    selectionWindowOpen = false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void orderListClicked(MouseEvent mouseEvent) {

        if(orderList.getSelectionModel().getSelectedItem() == null)
            return;

        try {
            if(orderList.getSelectionModel().getSelectedItem() == null)
                return;
            Order order = userOrders.get(orderList.getSelectionModel().getSelectedIndex());

            OrderController.order = order;
            OrderController.title = order.getDate() + " - " + order.getRestaurant().getName() + " (" + order.getPrice() + ")";

            Stage stage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/fxml/order.fxml"));
            stage.setTitle("Dostava");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void adminRestaurantPressed(ActionEvent actionEvent) {
        if(actionChoiceBox.getSelectionModel().getSelectedItem().toString().equals("Dodaj")){
            try {
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/restaurantAdd.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(actionChoiceBox.getSelectionModel().getSelectedItem().toString().equals("Promijeni")){
            try {
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/restaurantChange.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(actionChoiceBox.getSelectionModel().getSelectedItem().toString());
    }

    public void adminFoodPressed(ActionEvent actionEvent) {
    }

    public void adminMenuPressed(ActionEvent actionEvent) {
        if(actionChoiceBox.getSelectionModel().getSelectedItem().toString().equals("Dodaj")) {
            try {
                RestaurantPickerController.update = false;
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/restaurantPicker.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(actionChoiceBox.getSelectionModel().getSelectedItem().toString().equals("Promijeni")){
            try {
                RestaurantPickerController.update = true;
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/restaurantPicker.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void adminRestaurantCategoryPressed(ActionEvent actionEvent) {
        if(actionChoiceBox.getSelectionModel().getSelectedItem().toString().equals("Dodaj")) {
            try {
                CategoryAddController.category = false;
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/categoryAdd.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(actionChoiceBox.getSelectionModel().getSelectedItem().toString().equals("Promijeni")) {
            try {
                CategoryChangeController.category = false;
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/categoryChange.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void adminFoodCategoryPressed(ActionEvent actionEvent) {
        if(actionChoiceBox.getSelectionModel().getSelectedItem().toString().equals("Dodaj")) {
            try {
                CategoryAddController.category = true;
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/categoryAdd.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(actionChoiceBox.getSelectionModel().getSelectedItem().toString().equals("Promijeni")) {
            try {
                CategoryChangeController.category = true;
                Stage stage = new Stage();
                Parent root = null;
                root = FXMLLoader.load(getClass().getResource("/fxml/categoryChange.fxml"));
                stage.setTitle("Dostava");
                stage.setResizable(false);
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void adminDeliveryPressed(ActionEvent actionEvent) {
        try {
            Stage primaryStage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/fxml/delivery.fxml"));
            primaryStage.setTitle("Dostava");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logoutPressed(ActionEvent actionEvent) throws WrongAccountState{
        if(MainController.user == null)
            throw new WrongAccountState();

        try {
            Stage primaryStage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
            primaryStage.setTitle("Dostava");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            primaryStage.show();

            Stage stage1 = (Stage)restaurantSearchField.getScene().getWindow();
            stage1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void settingsPressed(ActionEvent actionEvent) {
        try {
            Stage primaryStage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/fxml/settings.fxml"));
            primaryStage.setTitle("Dostava");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
