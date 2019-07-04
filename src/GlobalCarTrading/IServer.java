/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GlobalCarTrading;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Jesper
 */
public interface IServer extends Remote {


    void createAccount(Account acc) throws RemoteException;

    //brug credentials i stedet for mail + password
    Account login(String mail, String password) throws RemoteException;

    void saveAccount(Account acc) throws RemoteException;

    void createAd(Ad ad, Car car) throws RemoteException;


    //brug locks til datatilgang i databasen
    //brug synchronized på metoder der skal tilgå data samtidig fra flere klienter
    //equals og hashcode
    Ad[] getAllAds() throws RemoteException;

    void sendMessage(Message message) throws RemoteException;

    Message[] receiveMessages(String recipient) throws RemoteException;

    Car getCar(int adId) throws RemoteException;

    ArrayList<String> getUsernames() throws RemoteException;

    void deleteMessage(int messageID) throws RemoteException;

    void deleteAd(int adId) throws RemoteException;

    Ad[] getMyAds(String author) throws RemoteException;
}
