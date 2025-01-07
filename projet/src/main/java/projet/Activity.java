package projet;

import java.math.BigDecimal;

public class Activity {
    private String name;
    private String category;
    private String address;
    private long date;
    private BigDecimal price;

    public Activity(String name, String category, String address, long date, BigDecimal price) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.date = date;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public String getAddress() {
        return this.address;
    }

    public long getDate() {
        return this.date;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
