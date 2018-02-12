package model;

public class Item {

    String name;
    String description;
    Integer id;
    public Item(String name, String description, Integer id){
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
