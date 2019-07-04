/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GlobalCarTrading;

import java.io.Serializable;

/**
 *
 * @author Jesper
 */
public class Account implements Serializable {
    private String name;
    private String mail;
    private String address;
    private int phoneNumber;
    private String password;
    private boolean validated;

    public Account(String name, String address,String mail, int phoneNumber,String password) {
        this.name = name;
        this.mail = mail;
        this.address = address;
        this.password = password;
        this.phoneNumber = phoneNumber;
        validated = false;
        
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
    public String getName() {
        return name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
    

    public String getMail() {
        return mail;
    }

    public String getAddress() {
        return address;
    }
    
    
}
