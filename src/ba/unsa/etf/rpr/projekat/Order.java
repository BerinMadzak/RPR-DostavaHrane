package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Order {

    private int id;
    private User user;
    private Restaurant restaurant;
    private LocalDate date;
    private double price;


    public Order(int id, User user, Restaurant restaurant, LocalDate date, double price) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
    }

    public Order(){
        id = 0;
        user = null;
        restaurant = null;
        date = null;
        price = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return restaurant.getName() + " " + date.toString() + " - " + price;
    }
}
