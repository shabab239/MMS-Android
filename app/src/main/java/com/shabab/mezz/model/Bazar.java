package com.shabab.mezz.model;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Bazar {

    @SerializedName("id")
    private Long id;

    @SerializedName("description")
    private String description;

    @SerializedName("cost")
    private Double cost;

    @SerializedName("date")
    private Date date;

    @SerializedName("user")
    private User user;

    @SerializedName("messId")
    private Long messId;

    public Bazar() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getMessId() {
        return messId;
    }

    public void setMessId(Long messId) {
        this.messId = messId;
    }
}
