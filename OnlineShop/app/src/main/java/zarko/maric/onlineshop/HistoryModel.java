package zarko.maric.onlineshop;

public class HistoryModel {

    private String date;
    private String status;
    private int price;
    private String user;
    public HistoryModel(String date, String status, int price, String user) {
        this.date = date;
        this.status = status;
        this.price = price;
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUser(){
        return user;
    }

}
