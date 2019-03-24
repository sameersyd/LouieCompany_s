package com.kreator.sameer.louie;

public class JoinFirmObject {

    String firm_name,firm_key;

    public JoinFirmObject(String firm_name, String firm_key) {
        this.firm_name = firm_name;
        this.firm_key = firm_key;
    }
    public String getFirm_name() {
        return firm_name;
    }
    public void setFirm_name(String firm_name) {
        this.firm_name = firm_name;
    }
    public String getFirm_key() {
        return firm_key;
    }
    public void setFirm_key(String firm_key) {
        this.firm_key = firm_key;
    }
}
