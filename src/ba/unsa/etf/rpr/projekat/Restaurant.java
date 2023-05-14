package ba.unsa.etf.rpr.projekat;

public class Restaurant {

    private int id;
    private String name;
    private String address;
    private CategoryRestaurant category;
    private String description;

    public Restaurant(int id, String name, String address, CategoryRestaurant category, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category;
        this.description = description;
    }

    public Restaurant(){
        id = 0;
        name = "";
        address = "";
        category = null;
        description = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CategoryRestaurant getCategory() {
        return category;
    }

    public void setCategory(CategoryRestaurant category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return getName() + " - " + getAddress() + "\n" + getDescription();
    }
}
