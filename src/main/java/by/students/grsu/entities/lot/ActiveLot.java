package by.students.grsu.entities.lot;

public interface ActiveLot {
    LotStatus getStatus();

    double getCurrentPrice();

    void setCurrentPrice(double currentPrice);

    double getPriceStep();

    void calculatePriceStep(int ticks);

}
