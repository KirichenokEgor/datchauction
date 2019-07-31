package by.students.grsu.entities;

enum ITEM_STATUS {FREE, WITHIN_LOT}


public class Item {
//    сделать id цифрой и через статик задавать наименьший свободный или взять имя(проблема неуникальности имени)
    private String ID;
    private String description;
    //private Auction auction;
    ITEM_STATUS status;

    public Item(){
        ID = "hhz";
        description = "a piece of sh*t.";
        status = ITEM_STATUS.FREE;
    }

//    final static int TIMES = 24;// 2 hours auction, every 5 minutes

//    Lot(String id, Double price, Double min_price, String description){
//        ID = id;
//        this.price = price;
//        this.min_price = min_price;
//        this.description = description;
//        step = (price - min_price)/TIMES;
//        status = STATUS.WAITING;
//    }

    public String getID() {
        return ID;
    }

    //mb delete
    public void setID(String ID) {
        this.ID = ID;
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

//    public Auction getAuction() {
//        return auction;
//    }
//
//    public void setAuction(Auction auction) {
//        this.auction = auction;
//    }
}
