package com.lawrencium.basil.James.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;



/**
 * The Objectify object model for device registrations we are persisting
 */
@Entity
public class RegistrationRecord {

//    @Id
//    Long id;
    @Id
    String emailAddress;

    @Index
    private String regId;
    @Index
    private String userName;
    //private String emailAddress;
    /* you can add more fields... */

    public RegistrationRecord() {
    }
    public RegistrationRecord(RegistrationRecord record){
        if(record != null)
            this.userName = record.getUserName();
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}