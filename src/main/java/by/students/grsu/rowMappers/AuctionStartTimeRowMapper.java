//package by.students.grsu.rowMappers;
//
//import by.students.grsu.entities.auction.AuctionStartTime;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class AuctionStartTimeRowMapper implements RowMapper<AuctionStartTime> {
//    @Override
//    public AuctionStartTime mapRow(ResultSet resultSet, int i) throws SQLException {
//        return new AuctionStartTime(resultSet.getInt("ID"),resultSet.getTime("beginTime").toLocalTime());
//    }
//}
