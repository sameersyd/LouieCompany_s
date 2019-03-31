package com.kreator.sameer.louie.Contractor;

public class ContractorCustomersObject {

    String name,phone,email,profilePic,contractor_link_uid,customer_uid;

    public ContractorCustomersObject(String name, String phone, String email, String profilePic, String contractor_link_uid, String customer_uid) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.profilePic = profilePic;
        this.contractor_link_uid = contractor_link_uid;
        this.customer_uid = customer_uid;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getContractor_link_uid() {
        return contractor_link_uid;
    }

    public void setContractor_link_uid(String contractor_link_uid) {
        this.contractor_link_uid = contractor_link_uid;
    }

    public String getCustomer_uid() {
        return customer_uid;
    }

    public void setCustomer_uid(String customer_uid) {
        this.customer_uid = customer_uid;
    }
}
