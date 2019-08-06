package by.students.grsu.entities.item;

public class Item implements ItemInfo {
    private int ID;
    private String name;
    private String description;
    private Boolean onLot;
    private String owner;
    //m.b. image
    public Item(int ID,String name,String description,String owner){
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.onLot = false;
        this.owner = owner;
    }
    public void changeOwner(String newOwner){
        owner=newOwner;
    }
    public boolean engage(){
        if(onLot==false)onLot=true;
        else return false;
        return true;
    }
    public boolean isEngaged(){
        return onLot;
    }

    public String itemInfo(){
        return ID + ": " + name + "("+description+")";
    }


    @Override
    public String getName() {
        return name;
    }


    @Override
    public String getDescription() {
        return description;
    }


    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getStatus(){
        if(onLot)return "on lot";
        else return "free";
    }
}
