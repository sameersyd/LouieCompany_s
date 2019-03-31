package com.kreator.sameer.louie.ViewContractor;

public class ViewContractorObject {

    String name,email,phone,no;

    public ViewContractorObject(String name, String email, String phone, String no) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.no = no;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
