package by.students.grsu.rowMappers;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.lot.Lot;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionWithLotsResultSetExtractor implements ResultSetExtractor<Auction> {

//    public Auction mapRow(ResultSet resultSet, int i) throws SQLException {
//        List<Lot> auctionLots = null;
//        try {
//            auctionLots = lotDao.getLotsByAuctionId(resultSet.getInt("ID"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new Auction(resultSet.getInt("ID"),resultSet.getString("description"),
//                resultSet.getInt("maxLots"),resultSet.getTime("beginTime").toLocalTime(),
//                resultSet.getInt("maxDuration"),resultSet.getString("status"),auctionLots);
//    }

    @Override
    public Auction extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        if(resultSet.next()) {
            //List<Lot> lotList = new ArrayList<Lot>();
            int id = resultSet.getInt(1), maxLots = resultSet.getInt("maxLots"), maxDuration = resultSet.getInt("maxDuration");
            String description = resultSet.getString(2), status = resultSet.getString(6);
            LocalTime beginTime = resultSet.getTime("beginTime").toLocalTime();

            List<Lot> auctionLots = new ArrayList<Lot>();
            //String prevLotName = "";
            int prevLotId = 0;
            if (resultSet.getString("lotName") != null) {
                int l_id = resultSet.getInt(8), auctionId = resultSet.getInt("auctionId");
                String lotName = resultSet.getString("lotName"), l_status = resultSet.getString("status");
                double startPrice = resultSet.getDouble("startPrice"), minPrice = resultSet.getDouble("minPrice");

                List<Item> itemList = new ArrayList<>();

                do {
                    if (prevLotId == resultSet.getInt(8)) {
                        //auctionLots.get(auctionLots.size() - 1).
                        itemList.add(new Item(resultSet.getInt(14), resultSet.getString("name"),
                                resultSet.getString(16), resultSet.getString("owner"),
                                resultSet.getInt("lotId")));
                    } else {
                        try {
                            auctionLots.add(new Lot(l_id, lotName, startPrice, minPrice, l_status, auctionId, itemList));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        prevLotId = l_id;

                        l_id = resultSet.getInt(8);
                        lotName = resultSet.getString("lotName");
                        startPrice = resultSet.getDouble("startPrice");
                        minPrice = resultSet.getDouble("minPrice");
                        l_status = resultSet.getString(12);
                        auctionId = resultSet.getInt("auctionId");
                        itemList = new ArrayList<Item>();
                    }

//                    if(prevLotName.equals(resultSet.getString("lotName"))){
//                        lotList.get(lotList.size() - 1).
//                    }
//                    else {
//                        try {
//                            lotList.add(new Lot(resultSet.getInt(8), resultSet.getString("lotName"),
//                                    resultSet.getDouble("startPrice"), resultSet.getDouble("minPrice"),
//                                    resultSet.getString("status"), resultSet.getInt("auctionId")), itemList);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                } while (resultSet.next());

            }
            return new Auction(id, description,
                    maxLots, beginTime,
                    maxDuration, status, auctionLots);
        }
        return null;
    }
}
