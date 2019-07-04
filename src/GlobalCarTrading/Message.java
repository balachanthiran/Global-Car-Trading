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
public class Message implements Serializable {
    private String sender;
    private String message;
    private String recipient;
    private int messageID;
    
    public Message(String sender, String message, String recipient, int messageID){
        this.sender = sender;
        this.message = message;
        this.recipient = recipient;
        this.messageID = messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getMessageID() {
        return messageID;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    
}
