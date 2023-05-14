package ba.unsa.etf.rpr.projekat;

public class CategoryFood {

    private int id;
    private String name;

    public CategoryFood(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryFood(){
        id = 0;
        name = "";
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
}
