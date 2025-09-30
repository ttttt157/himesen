package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userdata")
public class user {

    @Id
    private String username;

    private String password;
    private String havefood = "500";
    private String havefund = "500";
    private String havematerial = "500";
    private String userinformation = "1-1";

    // getter / setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getHavefood() { return havefood; }
    public void setHavefood(String havefood) { this.havefood = havefood; }
    public String getHavefund() { return havefund; }
    public void setHavefund(String havefund) { this.havefund = havefund; }
    public String getHavematerial() { return havematerial; }
    public void setHavematerial(String havematerial) { this.havematerial = havematerial; }
    public String getUserinformation() { return userinformation; }
    public void setUserinformation(String userinformation) { this.userinformation = userinformation; }
}
