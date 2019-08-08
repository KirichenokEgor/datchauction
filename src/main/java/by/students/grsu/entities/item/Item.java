package by.students.grsu.entities.item;

public class Item implements ItemInfo {
    private int ID;
    private String name;
    private String description;
    private String owner;
    private int lotID;

    public Item(int ID, String name, String description, String owner,int lotID){
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.lotID=lotID;
        this.owner = owner;
    }

    public Item(int ID,String name,String description,String owner){
        this.ID = ID;
        this.name = name;
        this.description = description;
        lotID=0;
        this.owner = owner;
    }

    public boolean isOnLot(){
        if(lotID==0)return false;
        else return true;
    }
    public void lotCreated(int lotID){
        this.lotID=lotID;
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
        if(lotID!=0)return "on lot";
        else return "free";
    }
}
