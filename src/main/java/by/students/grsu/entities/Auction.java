package by.students.grsu.entities;

import java.util.ArrayList;
import java.util.List;

//аукционы происходят каждые 2 часа: от 0:00 до 22:00
//enum AUCTION_TIME {AUCTIONat0,AUCTIONat2,AUCTIONat4,AUCTIONat6,AUCTIONat8,AUCTIONat12,AUCTIONat14,AUCTIONat16,AUCTIONat18,AUCTIONat20,AUCTIONat22}

public class Auction {
    List lots;
    //start time, end time? mb only start time (fixed duration)? or using Enum?

    Auction(){
        lots = new ArrayList<Lot>();
    }

    Auction(ArrayList<Lot> lots){
        this.lots = (ArrayList)lots.clone();
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



}
