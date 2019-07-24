package by.students.grsu.entities;

enum STATUS {ON_SELL,SOLD,WAITING}


public class Lot {
//    сделать id цифрой и через статик задавать наименьший свободный или взять имя(проблема неуникальности имени)
    private String ID;
    private Double price;
    private Double min_price;
    private String description;
    private Double step;
    //поставить аукцион не здесь, а добавлять лоты в аукцион
    private STATUS status;

    final static int TIMES = 24;// 2 hours auction, every 5 minutes

//    Lot(String id, Double price, Double min_price, String description){
//        ID = id;
//        this.price = price;
//        this.min_price = min_price;
//        this.description = description;
//        step = (price - min_price)/TIMES;
//        status = STATUS.WAITING;
//    }

    public String getID() {
        return ID;
    }

    //mb delete
    public void setID(String ID) {
        this.ID = ID;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMin_price() {
        return min_price;
    }

    public void setMin_price(Double min_price) {
        this.min_price = min_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Double getStep() {
        return step;
    }
}
