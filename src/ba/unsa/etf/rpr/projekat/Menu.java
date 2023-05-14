package ba.unsa.etf.rpr.projekat;

public class Menu {

    private int id;
    private Restaurant restaurant;
    private Food food;

    public Menu(int id, Restaurant restaurant, Food food) {
        this.id = id;
        this.restaurant = restaurant;
        this.food = food;
    }

    public Menu(){
        id = 0;
        restaurant = null;
        food = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
