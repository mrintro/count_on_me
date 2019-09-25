package com.example.excaliberani;

public class FeedData {
    public String dropdown,email,pickup,req;

    public FeedData() {
    }

    public FeedData(String dropdown, String email, String pickup, String req) {
        this.dropdown = dropdown;
        this.email = email;
        this.pickup = pickup;
        this.req = req;
    }

    public String getDropdown() {
        return dropdown;
    }
    
    public void setDropdown(String dropdown) {
        this.dropdown = dropdown;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }
}
