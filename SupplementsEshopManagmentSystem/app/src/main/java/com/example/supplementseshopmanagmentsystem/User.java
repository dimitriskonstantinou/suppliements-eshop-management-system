package com.example.supplementseshopmanagmentsystem;

public class User {

   private String orderid;
   private String firstname;
    private String lastname;
    private String email;
    private String phonenumber;
    private String username;
    private int totalitems;
    private int totalcost;




    public User(){}

    public User(String orderid, String username, String firstname, String lastname, String email, String phonenumber,int totalitems,int totalcost) {
        this.orderid = orderid;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.totalcost = totalcost;
        this.totalitems = totalitems;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalitems() {return totalitems;}

    public void setTotalitems(int totalitems) {this.totalitems = totalitems;}

    public int getTotalcost() { return totalcost; }

    public void setTotalcost(int totalcost) {this.totalcost = totalcost;}
}
