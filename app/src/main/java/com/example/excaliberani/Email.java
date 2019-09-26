package com.example.excaliberani;

// modifies and restores email for making key in database
public class Email {
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Email(String email) {
        this.email = email;
    }

    public String convert_mail(){
        String email = this.email;
        int n=email.length();
        for(int i=0;i<n;i++){

            String tmp=email.substring(i,i+1);
            if(email.charAt(i)=='.'){
                email=email.substring(0,i)+"_dot_"+email.substring(i+1,n);
                n+=4;
                i+=5;
            }
        }
        return email;
    }

    public String extract_mail(){

        int n=email.length();
        for(int i=0;i<n-5;i++){
            String tmp=email.substring(i,i+5);
            if(tmp.equals("_dot_")){
                String temp1=email.substring(0,i);
                String temp2=email.substring(i+5,n);
                email=temp1+"."+temp2;
                n-=4;
            }
        }
        return email;
    }
}
