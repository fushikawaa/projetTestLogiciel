package projet;

import java.math.BigDecimal;

import projet.enums.ActivityType;

public class Activity {
    private String name;
    private ActivityType category;
    private String address;
    private long date;
    private BigDecimal price;

    public Activity(String name, ActivityType category, String address, long date, BigDecimal price) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.date = date;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public ActivityType getCategory() {
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

    public void setCategory(ActivityType category) {
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
