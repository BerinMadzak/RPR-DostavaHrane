package ba.unsa.etf.rpr.projekat;

public class Food {

    private int id;
    private String name;
    private double price;
    private CategoryFood category;
    private String description;

    public Food(int id, String name, double price, CategoryFood category, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
    }

    public Food() {
        id = 0;
        name = "";
        price = 0;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CategoryFood getCategory() {
        return category;
    }

    public void setCategory(CategoryFood category) {
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
        return name + " " + price + "\n" + description;
    }
}
