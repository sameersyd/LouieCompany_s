package com.kreator.sameer.louie.Customer;

public class CustomerReferralObject {

    String name,phone,email,status,updated_uid;

    public CustomerReferralObject(String name, String phone, String email, String status, String updated_uid) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.updated_uid = updated_uid;
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

    public String getUpdated_uid() {
        return updated_uid;
    }

    public void setUpdated_uid(String updated_uid) {
        this.updated_uid = updated_uid;
    }
}
