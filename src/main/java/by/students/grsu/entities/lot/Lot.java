package by.students.grsu.entities.lot;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;

import java.util.ArrayList;
import java.util.List;

public class Lot implements LotInfo {
    private int ID;
    private String name;
    private double currentPrice;
    private double priceStep;
    private double minPrice;
    private List<Item> items;
    private LotStatus status;
    private int auctionId;
    public Lot(String name, double startPrice, double minPrice, List<Item> items){
        ID=0;
        this.name=name;
        this.currentPrice=startPrice;
        this.minPrice = minPrice;
        this.items = items;
        for(Item item : items)
            item.lotCreated(ID);
        this.status=LotStatus.Registered;
    }
    public Lot(int id,String name,double startPrice,double minPrice, String status,int auctionId){
        this.ID=id;
        this.name=name;
        this.currentPrice=startPrice;
        this.minPrice=minPrice;
        switch (status) {
            case "registered": {
                this.status = LotStatus.Registered;
                break;
            }
            case "sold": {
                this.status = LotStatus.Sold;
                break;
            }
            case "end": {
                this.status = LotStatus.End;
                break;
            }
        }
        this.auctionId=auctionId;
    }
    public Lot(int id,String name,double startPrice,double minPrice, String status,int auctionId,List<Item> itemList) throws Exception {
        this.ID=id;
        this.name=name;
        this.currentPrice=startPrice;
        this.minPrice=minPrice;
        switch (status) {
            case "registered": {
                this.status = LotStatus.Registered;
                break;
            }
            case "sold": {
                this.status = LotStatus.Sold;
                break;
            }
            case "end": {
                this.status = LotStatus.End;
                break;
            }
            default: {
                throw new Exception("Wrong status name");
            }
        }
        this.auctionId=auctionId;
        this.items=itemList;
    }
    public Lot(String name, double startPrice, double minPrice, List<Item> items,int auctionId){
        ID=0;
        this.name=name;
        this.currentPrice=startPrice;
        this.minPrice = minPrice;
        this.items = items;
        for(Item item : items)
            item.lotCreated(ID);
        this.status=LotStatus.Registered;
        this.auctionId=auctionId;
    }
    //<ONLY FOR TESTS>
    public Lot(String name, double startPrice, double minPrice){
        ID=0;
        this.name=name;
        this.currentPrice=startPrice;
        this.minPrice = minPrice;
        this.status=LotStatus.Registered;
    }
    //</ONLY FOR TESTS>
    @Override
    public LotStatus getStatus() {
        return status;
    }

    @Override
    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

//    public double getPriceStep() {
//        return priceStep;
//    }

    public void calculatePriceStep(int ticks) {
        priceStep = Math.round(((currentPrice-minPrice)/ticks)*100)/100.0;
    }

    @Override
    public List<ItemInfo> getItems() {
        List<ItemInfo> list = new ArrayList<>();
        for(ItemInfo item : items)
            list.add(item);
        return list;
    }
    public void setSold(){
        this.status = LotStatus.Sold;
    }

    public void makePriceStep(){
        this.currentPrice = Math.round((currentPrice - priceStep)*100)/100.0;
       // System.out.println("currentPrice=" + currentPrice);
    }

    public void makeEnded(){
        status=LotStatus.End;
    }
    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getAuctionId() {
        return auctionId;
    }
    public String getOwner(){ return items.get(0).getOwner();}
    public void setItems(List<Item> items) {
        this.items = items;
    }
}
