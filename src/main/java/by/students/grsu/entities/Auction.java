package by.students.grsu.entities;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class Auction {
    private static Integer minFreeId = 1;
    private Integer id;
    private List lots;
    private Integer durationMin;
    private Time start_time;
    private final static Integer step_duration = 5;

    //start time, end time? mb only start time (fixed duration)? or using Enum?

    public Auction(){
        id = minFreeId++;
        lots = new ArrayList<Lot>();
        durationMin = 60;
        start_time = new Time(0,0,0);//do smth
    }

    Auction(ArrayList<Lot> lots, Integer durationMin, Time start_time){
        this.lots = (ArrayList) lots.clone();
        id = minFreeId++;
        this.durationMin = durationMin;
        this.start_time = start_time;
    }

    public static Integer getMinFreeId() {
        return minFreeId;
    }

    public static void setMinFreeId(Integer minFreeId) {
        Auction.minFreeId = minFreeId;
    }

    public static Integer getStep_duration() {
        return step_duration;
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
