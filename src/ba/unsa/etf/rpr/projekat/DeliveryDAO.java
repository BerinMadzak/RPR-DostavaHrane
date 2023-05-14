package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DeliveryDAO {

    private enum Table {Restaurant, User, Food, Menu, CategoryRestaurant, CategoryFood, Order, OrderItem};

    private static DeliveryDAO instance = null;
    private Connection conn;

    private PreparedStatement getNextIDFromRestaurantQuery, getNextIDFromUserQuery, getNextIDFromFoodQuery, getNextIDFromMenuQuery, getNextIDFromCategoryFoodQuery, getNextIDFromCategoryRestaurantQuery,
                      getRestaurantByNameQuery, getUserByUsernameQuery, getFoodCategoryByNameQuery, getRestaurantCategoryByNameQuery,
                      getFoodFromRestaurantQuery, getRestaurantCategoryByIDQuery, getFoodCategoryByIDQuery, addRestaurantQuery, addUserQuery, addFoodQuery, addRestaurantCategoryQuery,
                      deleteRestaurantQuery, deleteUserByUsernameQuery, deleteRestaurantCategoryQuery, addFoodCategoryQuery, deleteFoodCategoryQuery,
                      deleteFoodQuery, getFoodByNameQuery, addMenuQuery, deleteMenuQuery, getMenuQuery, getRestaurantByIDQuery, getFoodByIDQuery, getRestaurantIDFromNameQuery,
                      getFoodIDFromNameQuery, getRestaurantsByCategoryQuery, getRestaurantCategoriesNamesQuery, addOrderQuery, addOrderItemQuery, getNextIDFromOrderQuery, getNextIDFromOrderItemQuery,
                      getOrdersByUserQuery, getUserByIdQuery, getFoodFromOrderQuery, changeAmountOfOrderItemQuery, getItemFromOrderByNameQuery, getAmountFromItemQuery, getNotDeliveredOrdersQuery,
                      getAllFoodQuery, getAllRestaurantsQuery, getAllMenusQuery, getAllRestaurantCategoriesQuery, getAllFoodCategoriesQuery, getAllUsersQuery,
                      changeRestaurantQuery, deleteMenuFromRestaurantQuery, deleteMenuByFoodQuery, changeFoodQuery, changeRestaurantCategoryQuery, changeFoodCategoryQuery,
                      updateUserQuery, changePasswordQuery, setOrderAsDeliveredQuery;

    private static void initialize(){
        instance = new DeliveryDAO();
    }

    private DeliveryDAO(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");

            //RESTAURANT
            getNextIDFromRestaurantQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM restaurant");
            getRestaurantByNameQuery = conn.prepareStatement("SELECT * FROM restaurant WHERE name = ?");
            addRestaurantQuery = conn.prepareStatement("INSERT INTO restaurant VALUES (?, ?, ?, ?, ?)");
            deleteRestaurantQuery = conn.prepareStatement("DELETE FROM restaurant WHERE name = ?");
            getRestaurantByIDQuery = conn.prepareStatement("SELECT * FROM restaurant WHERE id = ?");
            getRestaurantIDFromNameQuery = conn.prepareStatement("SELECT id FROM restaurant WHERE name = ?");
            getRestaurantsByCategoryQuery = conn.prepareStatement("SELECT * FROM restaurant WHERE category = ?");
            getAllRestaurantsQuery = conn.prepareStatement("SELECT * FROM restaurant ORDER BY name");
            changeRestaurantQuery = conn.prepareStatement("UPDATE restaurant SET name = ?, address = ?, category = ?, description = ? WHERE id = ?");
            //
            //FOOD
            getNextIDFromFoodQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM food");
            getFoodFromRestaurantQuery = conn.prepareStatement("SELECT * FROM food WHERE id IN (SELECT food_id FROM menu WHERE restaurant_id = ?)");
            addFoodQuery = conn.prepareStatement("INSERT INTO food VALUES (?, ?, ?, ?, ?)");
            deleteFoodQuery = conn.prepareStatement("DELETE FROM food WHERE name = ?");
            getFoodByNameQuery = conn.prepareStatement("SELECT * FROM food WHERE name = ?");
            getFoodByIDQuery = conn.prepareStatement("SELECT * FROM food WHERE id = ?");
            getFoodIDFromNameQuery = conn.prepareStatement("SELECT id FROM food WHERE name = ?");
            getAllFoodQuery = conn.prepareStatement("SELECT * FROM food ORDER BY name");
            changeFoodQuery = conn.prepareStatement("UPDATE food SET name = ?, price = ?, category = ?, description = ? WHERE id = ?");
            //
            //USER
            getNextIDFromUserQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM user");
            getUserByUsernameQuery = conn.prepareStatement("SELECT * FROM user WHERE username = ?");
            addUserQuery = conn.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            deleteUserByUsernameQuery = conn.prepareStatement("DELETE FROM user WHERE username = ?");
            getUserByIdQuery = conn.prepareStatement("SELECT * FROM user WHERE id = ?");
            getAllUsersQuery = conn.prepareStatement("SELECT * FROM user ORDER BY username");
            updateUserQuery = conn.prepareStatement("UPDATE user SET username = ?, address = ?, email = ?, phone_number = ? WHERE id = ?");
            changePasswordQuery = conn.prepareStatement("UPDATE user SET password = ? WHERE id = ?");
            //
            //CATEGORY RESTAURANT
            getNextIDFromCategoryRestaurantQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM category_restaurant");
            getRestaurantCategoryByNameQuery = conn.prepareStatement("SELECT * FROM category_restaurant WHERE name = ?");
            getRestaurantCategoryByIDQuery = conn.prepareStatement("SELECT * FROM category_restaurant WHERE id = ?");
            addRestaurantCategoryQuery = conn.prepareStatement("INSERT INTO category_restaurant VALUES (?, ?)");
            deleteRestaurantCategoryQuery = conn.prepareStatement("DELETE FROM category_restaurant WHERE name = ?");
            getRestaurantCategoriesNamesQuery = conn.prepareStatement("SELECT name FROM category_restaurant");
            getAllRestaurantCategoriesQuery = conn.prepareStatement("SELECT * FROM category_restaurant ORDER BY name");
            changeRestaurantCategoryQuery = conn.prepareStatement("UPDATE category_restaurant SET name = ? WHERE id = ?");
            //
            //CATEGORY FOOD
            getNextIDFromCategoryFoodQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM category_food");
            getFoodCategoryByNameQuery = conn.prepareStatement("SELECT * FROM category_food WHERE name = ?");
            getFoodCategoryByIDQuery = conn.prepareStatement("SELECT * FROM category_food WHERE id = ?");
            addFoodCategoryQuery = conn.prepareStatement("INSERT INTO category_food VALUES (?, ?)");
            deleteFoodCategoryQuery = conn.prepareStatement("DELETE FROM category_food WHERE name = ?");
            getAllFoodCategoriesQuery = conn.prepareStatement("SELECT * FROM category_food ORDER BY name");
            changeFoodCategoryQuery = conn.prepareStatement("UPDATE category_food SET name = ? WHERE id = ?");
            //
            //MENU
            getNextIDFromMenuQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM menu");
            addMenuQuery = conn.prepareStatement("INSERT INTO menu VALUES (?, ?, ?)");
            deleteMenuQuery = conn.prepareStatement("DELETE FROM menu WHERE restaurant_id = ? AND food_id = ?");
            getMenuQuery = conn.prepareStatement("SELECT * FROM menu WHERE restaurant_id = ? AND food_id = ?");
            getAllMenusQuery = conn.prepareStatement("SELECT * FROM menu");
            deleteMenuFromRestaurantQuery = conn.prepareStatement("DELETE FROM menu WHERE restaurant_id = ?");
            deleteMenuByFoodQuery = conn.prepareStatement("DELETE FROM menu WHERE food_id = ?");
            //
            //ORDER
            getNextIDFromOrderQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM orders");
            getNextIDFromOrderItemQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM order_item");
            addOrderQuery = conn.prepareStatement("INSERT INTO orders VALUES (?, ?, ?, ?, ?, ?)");
            addOrderItemQuery = conn.prepareStatement("INSERT INTO order_item VALUES (?, ?, ?, ?)");
            getOrdersByUserQuery = conn.prepareStatement("SELECT * FROM orders WHERE user = ?");
            getFoodFromOrderQuery = conn.prepareStatement("SELECT * FROM food WHERE id IN (SELECT food FROM order_item WHERE order_id = ?)");
            changeAmountOfOrderItemQuery = conn.prepareStatement("UPDATE order_item SET amount = ? WHERE id = ?");
            getItemFromOrderByNameQuery = conn.prepareStatement("SELECT id FROM order_item WHERE order_id = ? AND food = (SELECT id FROM food WHERE name = ?)");
            getAmountFromItemQuery = conn.prepareStatement("SELECT amount FROM order_item WHERE id = ?");
            getNotDeliveredOrdersQuery = conn.prepareStatement("SELECT * FROM orders WHERE delivered = 0");
            setOrderAsDeliveredQuery = conn.prepareStatement("UPDATE orders SET delivered = 1 WHERE id = ?");
            //
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DeliveryDAO getInstance(){
        if(instance == null)
            initialize();
        return instance;
    }

    public static void removeInstance(){
        if(instance != null) {
            instance.close();
            instance = null;
        }
    }

    //RESTAURANT ----------------------------------------------------------------------------------------------------------------------

    public ArrayList<Restaurant> getRestaurantsByCategory(String category){
        try {
            ArrayList<Restaurant> restaurants = new ArrayList<>();
            CategoryRestaurant categoryRestaurant = findRestaurantCategoryByName(category);
            getRestaurantsByCategoryQuery.setInt(1, categoryRestaurant.getId());
            ResultSet rs = getRestaurantsByCategoryQuery.executeQuery();
            while (rs.next()){
                restaurants.add(getRestaurantFromResultSet(rs));
            }
            return restaurants;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addRestaurant(Restaurant restaurant){
        try {
            if(restaurantExists(restaurant.getName()))
                return;

            int id = getNextID(Table.Restaurant);

            addRestaurantQuery.setInt(1, id);
            addRestaurantQuery.setString(2, restaurant.getName());
            addRestaurantQuery.setString(3, restaurant.getAddress());

            CategoryRestaurant categoryRestaurant = findRestaurantCategoryByName(restaurant.getCategory().getName());

            addRestaurantQuery.setInt(4, categoryRestaurant.getId());
            addRestaurantQuery.setString(5, restaurant.getDescription());
            addRestaurantQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRestaurant(String name){
        try {
            deleteRestaurantQuery.setString(1, name);
            deleteRestaurantQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Restaurant findRestaurantByName(String name){
        try {
            getRestaurantByNameQuery.setString(1, name);
            ResultSet rs = getRestaurantByNameQuery.executeQuery();
            return getRestaurantFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Restaurant findRestaurantByID(int id){
        try {
            getRestaurantByIDQuery.setInt(1, id);
            ResultSet rs = getRestaurantByIDQuery.executeQuery();
            return getRestaurantFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Restaurant getRestaurantFromResultSet(ResultSet rs){
        try {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String address = rs.getString(3);
            int categoryId = rs.getInt(4);
            String description = rs.getString(5);
            CategoryRestaurant category = findRestaurantCategoryByID(categoryId);
            return new Restaurant(id, name, address, category, description);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean restaurantExists(String name){
        try {
            getRestaurantByNameQuery.setString(1, name);
            ResultSet rs = getRestaurantByNameQuery.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Restaurant> getAllRestaurants(){
        try {
            ArrayList<Restaurant> list = new ArrayList<>();
            ResultSet rs = getAllRestaurantsQuery.executeQuery();
            while(rs.next()){
                list.add(getRestaurantFromResultSet(rs));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void changeRestaurant(Restaurant restaurant){
        try {
            changeRestaurantQuery.setString(1, restaurant.getName());
            changeRestaurantQuery.setString(2, restaurant.getAddress());
            changeRestaurantQuery.setInt(3, restaurant.getCategory().getId());
            changeRestaurantQuery.setString(4, restaurant.getDescription());
            changeRestaurantQuery.setInt(5, restaurant.getId());
            changeRestaurantQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------------------------------------------------- RESTAURANT

    //RESTAURANT CATEGORY -------------------------------------------------------------------------------------------------------------

    public ObservableList<String> getRestaurantCategoriesNames(){
        try {
            ObservableList<String> categoryRestaurants = FXCollections.observableArrayList();
            ResultSet rs = getRestaurantCategoriesNamesQuery.executeQuery();
            while (rs.next()){
                categoryRestaurants.add(rs.getString(1));
            }
            return categoryRestaurants;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addRestaurantCategory(CategoryRestaurant categoryRestaurant){
        try {
            if(restaurantCategoyExists(categoryRestaurant.getName()))
                return;

            int id = getNextID(Table.CategoryRestaurant);

            addRestaurantCategoryQuery.setInt(1, id);
            addRestaurantCategoryQuery.setString(2, categoryRestaurant.getName());
            addRestaurantCategoryQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRestaurantCategory(String name){
        try {
            deleteRestaurantCategoryQuery.setString(1, name);
            deleteRestaurantCategoryQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CategoryRestaurant findRestaurantCategoryByName(String name){
        try {
            getRestaurantCategoryByNameQuery.setString(1, name);
            ResultSet rs = getRestaurantCategoryByNameQuery.executeQuery();
            return getRestaurantCategoryFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CategoryRestaurant findRestaurantCategoryByID(int id){
        try {
            getRestaurantCategoryByIDQuery.setInt(1, id);
            ResultSet rs = getRestaurantCategoryByIDQuery.executeQuery();
            return getRestaurantCategoryFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CategoryRestaurant getRestaurantCategoryFromResultSet(ResultSet rs){
        try {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            return new CategoryRestaurant(id, name);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean restaurantCategoyExists(String name){
        try {
            getRestaurantCategoryByNameQuery.setString(1, name);
            ResultSet rs = getRestaurantCategoryByNameQuery.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<CategoryRestaurant> getAllRestaurantCategories(){
        try {
            ArrayList<CategoryRestaurant> list = new ArrayList<>();
            ResultSet rs = getAllRestaurantCategoriesQuery.executeQuery();
            while(rs.next()){
                list.add(getRestaurantCategoryFromResultSet(rs));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void changeRestaurantCategory(CategoryRestaurant cr){
        try {
            changeRestaurantCategoryQuery.setString(1, cr.getName());
            changeRestaurantCategoryQuery.setInt(2, cr.getId());
            changeRestaurantCategoryQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------- RESTAURANT CATEGORY

    //FOOD ----------------------------------------------------------------------------------------------------------------------------

    public void addFood(Food food){
        try{
            if(foodExists(food.getName()))
                return;

            int id = getNextID(Table.Food);

            addFoodQuery.setInt(1, id);
            addFoodQuery.setString(2, food.getName());
            addFoodQuery.setDouble(3, food.getPrice());

            CategoryFood categoryFood = findFoodCategoryByName(food.getCategory().getName());

            addFoodQuery.setInt(4, categoryFood.getId());
            addFoodQuery.setString(5, food.getDescription());
            addFoodQuery.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteFood(String name){
        try {
            deleteFoodQuery.setString(1, name);
            deleteFoodQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Food findFoodByName(String name){
        try {
            getFoodByNameQuery.setString(1, name);
            ResultSet rs = getFoodByNameQuery.executeQuery();
            return getFoodFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Food findFoodByID(int id){
        try {
            getFoodByIDQuery.setInt(1, id);
            ResultSet rs = getFoodByIDQuery.executeQuery();
            return getFoodFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Food getFoodFromResultSet(ResultSet rs){
        try {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            double price = rs.getDouble(3);
            int categoryId = rs.getInt(4);
            String description = rs.getString(5);
            CategoryFood category = findFoodCategoryByID(categoryId);
            return new Food(id, name, price, category, description);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean foodExists(String name){
        try {
            getFoodByNameQuery.setString(1, name);
            ResultSet rs = getFoodByNameQuery.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Food> getAllFood(){
        try {
            ArrayList<Food> list = new ArrayList<>();
            ResultSet rs = getAllFoodQuery.executeQuery();
            while(rs.next()){
                list.add(getFoodFromResultSet(rs));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void changeFood(Food food){
        try {
            changeFoodQuery.setString(1, food.getName());
            changeFoodQuery.setDouble(2, food.getPrice());
            changeFoodQuery.setInt(3, food.getCategory().getId());
            changeFoodQuery.setString(4, food.getDescription());
            changeRestaurantQuery.setInt(5, food.getId());
            changeRestaurantQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------- FOOD

    //FOOD CATEGORY -------------------------------------------------------------------------------------------------------------------

    public void addFoodCategory(CategoryFood categoryFood){
        try {
            if (foodCategoryExists(categoryFood.getName()))
                return;

            int id = getNextID(Table.CategoryFood);

            addFoodCategoryQuery.setInt(1, id);
            addFoodCategoryQuery.setString(2, categoryFood.getName());
            addFoodCategoryQuery.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteFoodCategory(String name){
        try {
            deleteFoodCategoryQuery.setString(1, name);
            deleteFoodCategoryQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CategoryFood findFoodCategoryByName(String name){
        try {
            getFoodCategoryByNameQuery.setString(1, name);
            ResultSet rs = getFoodCategoryByNameQuery.executeQuery();
            return getFoodCategoryFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CategoryFood findFoodCategoryByID(int id){
        try {
            getFoodCategoryByIDQuery.setInt(1, id);
            ResultSet rs = getFoodCategoryByIDQuery.executeQuery();
            return getFoodCategoryFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CategoryFood getFoodCategoryFromResultSet(ResultSet rs){
        try {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            return new CategoryFood(id, name);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean foodCategoryExists(String name){
        try{
            getFoodCategoryByNameQuery.setString(1, name);
            ResultSet rs = getFoodCategoryByNameQuery.executeQuery();
            return rs.next();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<CategoryFood> getAllFoodCategories(){
        try {
            ArrayList<CategoryFood> list = new ArrayList<>();
            ResultSet rs = getAllFoodCategoriesQuery.executeQuery();
            while(rs.next()){
                list.add(getFoodCategoryFromResultSet(rs));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void changeFoodCategory(CategoryFood cf){
        try {
            changeFoodCategoryQuery.setString(1, cf.getName());
            changeFoodCategoryQuery.setInt(2, cf.getId());
            changeFoodCategoryQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------- FOOD CATEGORY

    //MENU ----------------------------------------------------------------------------------------------------------------------------

    public void addMenu(Menu menu){
        try {
            getRestaurantIDFromNameQuery.setString(1, menu.getRestaurant().getName());
            ResultSet rs = getRestaurantIDFromNameQuery.executeQuery();
            int idRes = rs.getInt(1);

            getFoodIDFromNameQuery.setString(1, menu.getFood().getName());
            ResultSet rs2 = getFoodIDFromNameQuery.executeQuery();
            int idFood = rs2.getInt(1);

            if(menuExists(idRes, idFood))
                return;

            addMenuQuery.setInt(1, getNextID(Table.Menu));
            addMenuQuery.setInt(2, idRes);
            addMenuQuery.setInt(3, idFood);
            addMenuQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenu(Menu menu){
        try {
            getRestaurantIDFromNameQuery.setString(1, menu.getRestaurant().getName());
            ResultSet rs = getRestaurantIDFromNameQuery.executeQuery();
            int idRes = rs.getInt(1);

            getFoodIDFromNameQuery.setString(1, menu.getFood().getName());
            ResultSet rs2 = getFoodIDFromNameQuery.executeQuery();
            int idFood = rs2.getInt(1);

            if(!menuExists(idRes, idFood))
                return;

            deleteMenuQuery.setInt(1, idRes);
            deleteMenuQuery.setInt(2, idFood);
            deleteMenuQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Food> findFoodFromRestaurant(String name) {
        try {
            ArrayList<Food> food = new ArrayList<>();

            getRestaurantByNameQuery.setString(1, name);
            ResultSet rs = getRestaurantByNameQuery.executeQuery();
            Restaurant restaurant = getRestaurantFromResultSet(rs);

            if(restaurant == null)
                return null;

            getFoodFromRestaurantQuery.setInt(1, restaurant.getId());
            ResultSet rs2 = getFoodFromRestaurantQuery.executeQuery();
            while(rs2.next()) {
                food.add(getFoodFromResultSet(rs2));
            }

            return food;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Menu findMenuByRestaurantAndFoodID(int restaurantID, int foodID) {
        try {
            getMenuQuery.setInt(1, restaurantID);
            getMenuQuery.setInt(2, foodID);
            ResultSet rs = getMenuQuery.executeQuery();
            return getMenuFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Menu getMenuFromResultSet(ResultSet rs){
        try {
            int id = rs.getInt(1);
            Restaurant restaurant = findRestaurantByID(rs.getInt(2));
            Food food = findFoodByID(rs.getInt(3));
            return new Menu(id, restaurant, food);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean menuExists(int restaurantID, int foodID) {
        try {
            getMenuQuery.setInt(1, restaurantID);
            getMenuQuery.setInt(2, foodID);
            ResultSet rs = getMenuQuery.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Menu> getAllMenus(){
        try {
            ArrayList<Menu> list = new ArrayList<>();
            ResultSet rs = getAllMenusQuery.executeQuery();
            while(rs.next()){
                list.add(getMenuFromResultSet(rs));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteMenuFromRestaurant(int id){
        try {
            deleteMenuFromRestaurantQuery.setInt(1, id);
            deleteMenuFromRestaurantQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenuByFood(int id){
        try {
            deleteMenuByFoodQuery.setInt(1, id);
            deleteMenuByFoodQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------- MENU

    //USER ----------------------------------------------------------------------------------------------------------------------------

    public void addUser(User user) {
        try{
            int id = getNextID(Table.User);

            if(userExists(user.getUsername()))
                return;

            addUserQuery.setInt(1, id);
            addUserQuery.setString(2, user.getUsername());
            addUserQuery.setInt(3, hashFunction(user.getPassword()));
            addUserQuery.setString(4, user.getEmail());
            addUserQuery.setString(5, user.getFirstName());
            addUserQuery.setString(6, user.getLastName());
            addUserQuery.setString(7, user.getAddress());
            addUserQuery.setString(8, user.getPhoneNumber());
            addUserQuery.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteUser(String username) {
        try {
            if(!userExists(username))
                return;

            deleteUserByUsernameQuery.setString(1, username);
            deleteUserByUsernameQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findUserByUsername(String username) {
        try {
            getUserByUsernameQuery.setString(1, username);
            ResultSet rs = getUserByUsernameQuery.executeQuery();
            return getUserFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserByID(int id){
        try {
            getUserByIdQuery.setInt(1, id);
            ResultSet rs = getUserByIdQuery.executeQuery();
            return getUserFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserFromResultSet(ResultSet rs) {
        try{
            int id = rs.getInt(1);
            String username = rs.getString(2);
            Integer hashPassword = rs.getInt(3);
            String password = hashPassword.toString();
            String email = rs.getString(4);
            String firstName = rs.getString(5);
            String lastName = rs.getString(6);
            String address = rs.getString(7);
            String phone = rs.getString(8);
            return new User(id, username, password, email, firstName, lastName, address, phone);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean userExists(String username) {
        try{
            getUserByUsernameQuery.setString(1, username);
            ResultSet rs = getUserByUsernameQuery.executeQuery();
            return rs.next();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private int hashFunction(String password) {
        int hash = password.hashCode();
        hash *= (hash%13)*0.2;
        return hash;
    }

    public boolean checkPassword(String password, int hash){
        return hashFunction(password) == hash;
    }

    public ArrayList<User> getAllUsers(){
        try {
            ArrayList<User> list = new ArrayList<>();
            ResultSet rs = getAllUsersQuery.executeQuery();
            while(rs.next()){
                list.add(getUserFromResultSet(rs));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUserQuery(User user){
        try {
            updateUserQuery.setString(1, user.getUsername());
            updateUserQuery.setString(2, user.getAddress());
            updateUserQuery.setString(3, user.getEmail());
            updateUserQuery.setString(4, user.getPhoneNumber());
            updateUserQuery.setInt(5, user.getId());
            updateUserQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeUserPassword(User user, String pass){
        try {
            changePasswordQuery.setInt(1, hashFunction(pass));
            changePasswordQuery.setInt(2, user.getId());
            changePasswordQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------- USER

    //ORDER ---------------------------------------------------------------------------------------------------------------------------

    public void addNewOrder(ObservableList<Food> order, User user, Restaurant restaurant){
        try{
            User user2 = findUserByUsername(user.getUsername());
            Restaurant restaurant2 = findRestaurantByName(restaurant.getName());

            int id = getNextID(Table.Order);

            float price = 0;

            for(Food food : order){
                price += food.getPrice();
            }

            addOrderQuery.setInt(1, id);
            addOrderQuery.setInt(2, user2.getId());
            addOrderQuery.setInt(3, restaurant2.getId());
            addOrderQuery.setDate(4, Date.valueOf(LocalDate.now()));
            addOrderQuery.setDouble(5, price);
            addOrderQuery.setBoolean(6, false);
            addOrderQuery.executeUpdate();

            for(Food food : order){
                addNewOrderItem(food, id);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addNewOrderItem(Food food, int orderID){
        try {
            Food foodNew = findFoodByName(food.getName());

            int id = getNextID(Table.OrderItem);

            if (itemExistsInOrder(orderID, food)) {
                int itemID = getOrderItemID(orderID, food.getName());
                getAmountFromItemQuery.setInt(1, itemID);
                ResultSet rs = getAmountFromItemQuery.executeQuery();
                float value = rs.getFloat(1) + 1;
                changeAmountOfOrderItemQuery.setFloat(1, value);
                changeAmountOfOrderItemQuery.setInt(2, itemID);
                changeAmountOfOrderItemQuery.executeUpdate();
            } else {
                addOrderItemQuery.setInt(1, id);
                addOrderItemQuery.setInt(2, orderID);
                addOrderItemQuery.setInt(3, foodNew.getId());
                addOrderItemQuery.setInt(4, 1);
                addOrderItemQuery.executeUpdate();
            }
            } catch(SQLException e){
                e.printStackTrace();
            }
    }

    public boolean itemExistsInOrder(int orderID, Food food){
        ArrayList<Food> foodList = getFoodFromOrder(orderID);
        for(Food food1 : foodList){
            if(food1.getName().equals(food.getName()))
                return true;
        }
        return false;
    }

    public int getOrderItemID(int orderID, String name){
        try {
            getItemFromOrderByNameQuery.setInt(1, orderID);
            getItemFromOrderByNameQuery.setString(2, name);
            ResultSet rs = getItemFromOrderByNameQuery.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Order> getAllOrdersByUser(User user){
        try{
            User user2 = findUserByUsername(user.getUsername());

            ArrayList<Order> orders = new ArrayList<>();

            getOrdersByUserQuery.setInt(1, user2.getId());
            ResultSet rs = getOrdersByUserQuery.executeQuery();
            while(rs.next()){
                orders.add(getOrderFromResultSet(rs));
            }

            return orders;

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Food> getFoodFromOrder(int orderID){
        try {
            ArrayList<Food> foodList = new ArrayList<>();

            getFoodFromOrderQuery.setInt(1, orderID);
            ResultSet rs = getFoodFromOrderQuery.executeQuery();
            while(rs.next()) {
                foodList.add(getFoodFromResultSet(rs));
            }

            return foodList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Order getOrderFromResultSet(ResultSet rs){
        try {
            int id = rs.getInt(1);
            int userID = rs.getInt(2);
            int resID = rs.getInt(3);
            LocalDate date = rs.getDate(4).toLocalDate();
            User user = findUserByID(userID);
            Restaurant restaurant = findRestaurantByID(resID);
            double price = rs.getDouble(5);

            return new Order(id, user, restaurant, date, price);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Order> getNotDeliveredOrders(){
        try {
            ArrayList<Order> orders = new ArrayList<>();
            ResultSet rs = getNotDeliveredOrdersQuery.executeQuery();
            while(rs.next()){
                orders.add(getOrderFromResultSet(rs));
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setOrderAsDelivered(Order order){
        try {
            setOrderAsDeliveredQuery.setInt(1, order.getId());
            setOrderAsDeliveredQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------- ORDER

    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNextID(Table table){
        try {
            int id = 1;
            ResultSet rs = null;
            switch (table) {
                case Restaurant:
                    rs = getNextIDFromRestaurantQuery.executeQuery();
                    break;
                case User:
                    rs = getNextIDFromUserQuery.executeQuery();
                    break;
                case Food:
                    rs = getNextIDFromFoodQuery.executeQuery();
                    break;
                case Menu:
                    rs = getNextIDFromMenuQuery.executeQuery();
                    break;
                case CategoryRestaurant:
                    rs = getNextIDFromCategoryRestaurantQuery.executeQuery();
                    break;
                case CategoryFood:
                    rs = getNextIDFromCategoryFoodQuery.executeQuery();
                    break;
                case Order:
                    rs = getNextIDFromOrderQuery.executeQuery();
                    break;
                case OrderItem:
                    rs = getNextIDFromOrderItemQuery.executeQuery();
                    break;
            }

            if(rs.next())
                id = rs.getInt(1);

            if(id == 0)
                id = 1;

            return id;
        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
}
