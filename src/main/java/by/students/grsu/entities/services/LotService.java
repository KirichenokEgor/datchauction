package by.students.grsu.entities.services;

import by.students.grsu.entities.lot.LotInfo;

public class LotService {
    public LotInfo createLot(int auctionId, String name, double startPrice, double minPrice) throws AuctionException {
        int index=0;
        for(int i=0;i<auctions.size();i++){
            if(auctions.get(i).getID()==auctionId)index=i;
        }
        return auctions.get(index).createLot(name,startPrice, 0.5, minPrice);
    }
}
