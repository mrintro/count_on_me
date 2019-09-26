package com.example.excaliberani;

public class Emailndname {
    private String name;
    private String email;
    private String type;

    public Emailndname() {
    }

    public Emailndname(String email, String name, String type) {
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
