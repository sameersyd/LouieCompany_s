package com.kreator.sameer.louie.Customer;

public class CustomerReferralObject {

    String name,phone,email,status,updated_name,updated_profileImg;

    public CustomerReferralObject(String name, String phone, String email, String status, String updated_name, String updated_profileImg) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.updated_name = updated_name;
        this.updated_profileImg = updated_profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_name() {
        return updated_name;
    }

    public void setUpdated_name(String updated_name) {
        this.updated_name = updated_name;
    }

    public String getUpdated_profileImg() {
        return updated_profileImg;
    }

    public void setUpdated_profileImg(String updated_profileImg) {
        this.updated_profileImg = updated_profileImg;
    }
}
