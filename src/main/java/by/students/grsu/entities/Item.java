package by.students.grsu.entities;

enum ITEM_STATUS {FREE, WITHIN_LOT}


public class Item {
//    сделать id цифрой и через статик задавать наименьший свободный или взять имя(проблема неуникальности имени)
    private static Integer minFreeId = 1;
    private Integer id;
    private String name;
    private String description;
    //private Auction auction;
    private ITEM_STATUS status;
    private String owner;

    public Item(){
        id = minFreeId++;
        name = "hhz";
        description = "a piece of sh*t.";
        status = ITEM_STATUS.FREE;
    }
    public Item(Integer id, String name, String description, String status, String owner){
        this.id = id;
        this.name = name;
        this.description = description;
        if(status.equals("WITHIN_LOT")) this.status = ITEM_STATUS.WITHIN_LOT;
        else this.status = ITEM_STATUS.FREE;
        this.owner = owner;
    }

    public static Integer getMinFreeId() {
        return minFreeId;
    }

    public static void setMinFreeId(Integer minFreeId) {
        Item.minFreeId = minFreeId;
    }

    public void setOwner(String newOwner){
        owner=newOwner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(ITEM_STATUS status) {
        this.status = status;
    }

    public ITEM_STATUS getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

//    public Auction getAuction() {
//        return auction;
//    }
//
//    public void setAuction(Auction auction) {
//        this.auction = auction;
//    }
}
