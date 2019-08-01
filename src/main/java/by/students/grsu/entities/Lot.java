package by.students.grsu.entities;

import java.util.ArrayList;
import java.util.List;

enum LOT_STATUS {EXIST, ADDED_TO_AUCTION, ON_SELL, SOLD}

public class Lot {
    //    сделать id цифрой и через статик задавать наименьший свободный или взять имя(проблема неуникальности имени)
    private static Integer minFreeId = 1;
    private List<Item> items;
    private String name;
    private Integer id;
    private Double price;
    private Double min_price;
    private String description;
    private Double step;
    private Auction auction;
    //поставить аукцион не здесь, а добавлять лоты в аукцион
    private LOT_STATUS status;

    public Lot(){
        id = minFreeId++;
        items = new ArrayList<Item>();
        name = "hhz";
        price = 999.99;
        min_price = 0.01;
        description = "a piece of sh*t.";
        status = LOT_STATUS.EXIST;
    }

    public static Integer getMinFreeId() {
        return minFreeId;
    }

    public static void setMinFreeId(Integer minFreeId) {
        Lot.minFreeId = minFreeId;
    }

    public void addItem(Item item){
        items.add(item);
        item.setStatus(ITEM_STATUS.WITHIN_LOT);
    }

    public void countStep(){
        step = (price - min_price) / auction.getDurationMin() * Auction.getStep_duration();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMin_price() {
        return min_price;
    }

    public void setMin_price(Double min_price) {
        this.min_price = min_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LOT_STATUS getStatus() {
        return status;
    }

    public void setStatus(LOT_STATUS status) {
        this.status = status;
    }

    public Double getStep() {
        return step;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
        countStep();
        status = LOT_STATUS.ADDED_TO_AUCTION;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
//
//    public void setStep(Double step) {
//        this.step = step;
//    }
}
