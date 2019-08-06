package by.students.grsu.entities.lot;
import by.students.grsu.entities.item.ItemInfo;

import java.util.List;

public class Lot implements ActiveLot, LotInfo {
    private int ID;
    private String name;
    private double currentPrice;
    private double priceStep;
    private double minPrice;
    private List<ItemInfo> items;
    private LotStatus status;
    public Lot(String name, double startPrice,double priceStep, double minPrice, List<ItemInfo> items){
        ID=0;
        this.name=name;
        this.setCurrentPrice(startPrice);
        this.setPriceStep(priceStep);
        this.setMinPrice(minPrice);
        this.items = items;
        setStatus(LotStatus.Registered);
    }
    //<ONLY FOR TESTS>
    public Lot(String name, double startPrice,double priceStep, double minPrice){
        ID=0;
        this.name=name;
        this.setCurrentPrice(startPrice);
        this.setPriceStep(priceStep);
        this.setMinPrice(minPrice);
        setStatus(LotStatus.Registered);
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

    @Override
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public double getPriceStep() {
        return priceStep;
    }

    public void setPriceStep(double priceStep) {
        this.priceStep = priceStep;
    }

    @Override
    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setStatus(LotStatus status) {
        this.status = status;
    }

    @Override
    public List<ItemInfo> getItems() {
        return items;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
