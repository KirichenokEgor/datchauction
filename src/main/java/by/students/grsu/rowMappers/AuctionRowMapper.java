//package by.students.grsu.rowMappers;
//
//import by.students.grsu.entities.auction.Auction;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class AuctionRowMapper implements RowMapper<Auction> {
//    @Override
//    public Auction mapRow(ResultSet resultSet, int i) throws SQLException {
//        //mb todo check resultSet not null
//        return  new Auction(resultSet.getInt("ID"),resultSet.getString("description"),resultSet.getInt("maxLots"),
//                resultSet.getTime("beginTime").toLocalTime(),resultSet.getInt("maxDuration"),resultSet.getString("status"),resultSet.getInt("currentLots"));
//    }
//}
