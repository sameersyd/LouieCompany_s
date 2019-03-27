package com.kreator.sameer.louie.Contractor;

public class ContractorReferralsObject {

    String referral_name,referral_phone,referral_email,referral_additional,referral_status,referral_key,referral_updated_timestamp,referral_submitted_uid,referral_updated_uid,referral_sentToCont_uid;

    public ContractorReferralsObject(String referral_name, String referral_phone, String referral_email, String referral_additional, String referral_status, String referral_key, String referral_updated_timestamp, String referral_submitted_uid, String referral_updated_uid, String referral_sentToCont_uid) {
        this.referral_name = referral_name;
        this.referral_phone = referral_phone;
        this.referral_email = referral_email;
        this.referral_additional = referral_additional;
        this.referral_status = referral_status;
        this.referral_key = referral_key;
        this.referral_updated_timestamp = referral_updated_timestamp;
        this.referral_submitted_uid = referral_submitted_uid;
        this.referral_updated_uid = referral_updated_uid;
        this.referral_sentToCont_uid = referral_sentToCont_uid;
    }

    public String getReferral_name() {
        return referral_name;
    }

    public void setReferral_name(String referral_name) {
        this.referral_name = referral_name;
    }

    public String getReferral_phone() {
        return referral_phone;
    }

    public void setReferral_phone(String referral_phone) {
        this.referral_phone = referral_phone;
    }

    public String getReferral_email() {
        return referral_email;
    }

    public void setReferral_email(String referral_email) {
        this.referral_email = referral_email;
    }

    public String getReferral_additional() {
        return referral_additional;
    }

    public void setReferral_additional(String referral_additional) {
        this.referral_additional = referral_additional;
    }

    public String getReferral_status() {
        return referral_status;
    }

    public void setReferral_status(String referral_status) {
        this.referral_status = referral_status;
    }

    public String getReferral_key() {
        return referral_key;
    }

    public void setReferral_key(String referral_key) {
        this.referral_key = referral_key;
    }

    public String getReferral_updated_timestamp() {
        return referral_updated_timestamp;
    }

    public void setReferral_updated_timestamp(String referral_updated_timestamp) {
        this.referral_updated_timestamp = referral_updated_timestamp;
    }

    public String getReferral_submitted_uid() {
        return referral_submitted_uid;
    }

    public void setReferral_submitted_uid(String referral_submitted_uid) {
        this.referral_submitted_uid = referral_submitted_uid;
    }

    public String getReferral_updated_uid() {
        return referral_updated_uid;
    }

    public void setReferral_updated_uid(String referral_updated_uid) {
        this.referral_updated_uid = referral_updated_uid;
    }

    public String getReferral_sentToCont_uid() {
        return referral_sentToCont_uid;
    }

    public void setReferral_sentToCont_uid(String referral_sentToCont_uid) {
        this.referral_sentToCont_uid = referral_sentToCont_uid;
    }
}
