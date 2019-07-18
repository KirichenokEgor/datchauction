package by.students.grsu.entities;

import org.springframework.stereotype.Component;

//аукционы происходят каждые 2 часа: от 0:00 до 22:00
enum AUCTION_TIME {AUCTIONat0,AUCTIONat2,AUCTIONat4,AUCTIONat6,AUCTIONat8,AUCTIONat12,AUCTIONat14,AUCTIONat16,AUCTIONat18,AUCTIONat20,AUCTIONat22}

enum STATUS {ON_SELL,SOLD,WAITING}

@Component
public class Lot {
    String ID;
    Double price;
    Double min_price;
    String description;
    //поставить аукцион не здесь, а добавлять лоты в аукцион

}
