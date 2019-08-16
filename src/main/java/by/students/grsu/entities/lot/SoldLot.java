package by.students.grsu.entities.lot;

public class SoldLot {
    private String buyerUsername;
    private int lotId;
    private double price;
    public SoldLot(String buyerUsername, int lotId, double price){
        this.buyerUsername=buyerUsername;
        this.lotId=lotId;
        this.price=price;
    }
    public String getBuyerUsername() {
        return buyerUsername;
    }
    public int getLotId() {
        return lotId;
    }

    public double getPrice() {
        return price;
    }
}
