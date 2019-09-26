package com.example.excaliberani;

//class to store private details of user

public class Userdetails {

    private String username;
    private String email;
    private String phone;
    private String password;

    public Userdetails(){}

    public  Userdetails(String username){
        this.username=username;
    }

    public String getname(){
        return username;
    }

    public void setname(String username){
        this.username=username;
    }

    public String getemail(){
        return email;
    }

    public void setemail(String email){
        this.email=email;
    }

    public String getphonenumber(){
        return phone;
    }

    public void setphonenumber(String phone){
        this.phone=phone;
    }

    public String getpassword(){
        return password;
    }

    public void setpassword(String password){
        this.password=password;
    }
}

