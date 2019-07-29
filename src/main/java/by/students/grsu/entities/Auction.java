package by.students.grsu.entities;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//аукционы происходят каждые 2 часа: от 0:00 до 22:00
//enum AUCTION_TIME {AUCTIONat0,AUCTIONat2,AUCTIONat4,AUCTIONat6,AUCTIONat8,AUCTIONat12,AUCTIONat14,AUCTIONat16,AUCTIONat18,AUCTIONat20,AUCTIONat22}

public class Auction {
    static Integer minFreeId = 1;
    Integer id;
    List lots;
    Integer durationMin;
    Time start_time;

    //start time, end time? mb only start time (fixed duration)? or using Enum?

    public Auction(){
        id = minFreeId++;
        lots = new ArrayList<Lot>();
        durationMin = 60;
        start_time = new Time(0,0,0);//do smth
    }

    Auction(ArrayList<Lot> lots, Integer durationMin, Time start_time){
        this.lots = (ArrayList)lots.clone();
        id = minFreeId++;
        this.durationMin = durationMin;
        this.start_time = start_time;
    }

    void iteration(){
        Lot lot;
        //тут либо делать проверку на статус лота, либо сразу убирать купленные
        for(int i = 0; i < lots.size(); i++){
            lot = (Lot)lots.get(i);
            if(lot.getPrice() == lot.getMin_price()) return;
            lot.setPrice(lot.getPrice() - lot.getStep());
        }
    }

    public void addLot(Lot lot){
        lots.add(lot);
    }

    public Integer getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(Integer durationMin) {
        this.durationMin = durationMin;
    }

    public List getLots() {
        return lots;
    }

    public void setLots(List lots) {
        this.lots = lots;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
