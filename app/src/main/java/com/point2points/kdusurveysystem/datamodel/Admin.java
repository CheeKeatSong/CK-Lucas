package com.point2points.kdusurveysystem.datamodel;

public class Admin {

    public String adminUid;
    public String adminID;
    public String adminEmail;
    public String adminPassword;
    public String adminName;
    public String adminUsername;
    public String date;

    public Admin(){}

    public Admin(String adminUid, String adminID, String adminEmail, String adminPassword, String adminName, String adminUsername, long date){
        this.adminUid = adminUid;
        this.adminID = adminID;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
        this.adminUsername = adminUsername;
        this.date = String.valueOf(date);
    }

    public String getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
