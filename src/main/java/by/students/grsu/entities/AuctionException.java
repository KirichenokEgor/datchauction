package by.students.grsu.entities;

public class AuctionException extends Exception {
    private int code;
    public AuctionException(String message, int code){
        super();
        this.code=code;
    }
    // 0  - Internal error
    // 11 - User not found
    // 12 - Wrong password
    // 13 - this email already using
    // 14 - this nickname already using
    // 21 - Item not found
    // 22 - This user has no items
    // 31 - Auction not found
    public int getCode() {
        return code;
    }
}
