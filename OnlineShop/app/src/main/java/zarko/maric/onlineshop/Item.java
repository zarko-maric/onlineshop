package zarko.maric.onlineshop;

public class Item {

    public Item(int cena, String naziv, String slike, String category) {
        this.cena = cena;
        this.naziv = naziv;
        this.slike = slike;
        this.category = category;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getSlike() {
        return slike;
    }

    public void setSlike(String slike) {
        this.slike = slike;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    private int cena;
    private String naziv;
    private String slike;
    private String category;


}
