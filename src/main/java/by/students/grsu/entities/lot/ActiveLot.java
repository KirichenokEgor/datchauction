package by.students.grsu.entities.lot;

public interface ActiveLot {
    LotStatus getStatus();

    double getCurrentPrice();

    void setCurrentPrice(double currentPrice);

    double getPriceStep();

    double getMinPrice();

    void setStatus(LotStatus status);
}
