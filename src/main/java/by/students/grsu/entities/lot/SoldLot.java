package by.students.grsu.entities.lot;

public class SoldLot {
    private String sellerUsername;
    private String buyerUsername;
    private int lotId;
    private double price;
    //private String sellerEmail;
    public SoldLot(String buyerUsername,String sellerUsername, int lotId, double price/*, String sellerEmail*/){
        this.buyerUsername=buyerUsername;
        this.sellerUsername = sellerUsername;
        this.lotId=lotId;
        this.price=price;
        //this.sellerEmail = sellerEmail;
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
    public String getSellerUsername() {
        return sellerUsername;
    }

//    public String getSellerEmail() {
//        return sellerEmail;
//    }
}
