package com.example.excaliberani;

public class Emailndname {
    private String name;
    private String email;

    public Emailndname() {
    }

    public Emailndname(String email, String name) {
        this.name = name;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
