/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GlobalCarTrading;

import Database.DatabaseController;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Jesper
 */
public class ServerMain {

    /**
     * @param args the command line arguments
     */

    public ServerMain() throws RemoteException, AlreadyBoundException {
            System.out.println("The server has been started ...");
        try {
            Server serverConnection = new Server();
            Registry reg = LocateRegistry.createRegistry(3000);
            reg.bind("server", serverConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
           ServerMain sm = new ServerMain();

    }
}

class Server extends UnicastRemoteObject implements IServer {

    private DatabaseController dc;

    public Server() throws RemoteException {
        dc = DatabaseController.getInstance();
    }

    @Override
    public Account login(String accountName, String password) {
        return dc.getAccount(accountName, password);
        
    }

    @Override
    public void saveAccount(Account account) {
        dc.saveAccount(account);
    }

    @Override
    public void createAd(Ad ad, Car car) {
        dc.createAd(ad, car);
    }

    @Override
    public Ad[] getAllAds() {
        return dc.getAllAds();
    }

    @Override
    public void createAccount(Account acc) {
        dc.createUser(acc);
    }

    @Override
    public Message[] receiveMessages(String recipient) {
        return dc.receiveMessages(recipient);
    }

    @Override
    public void sendMessage(Message message) {
        dc.sendMessage(message);
    }

    @Override
    public Car getCar(int adId) throws RemoteException {
        return dc.getCar(adId);
    }

    @Override
    public ArrayList<String> getUsernames() throws RemoteException {
        return dc.getUsernames();
    }

    @Override
    public void deleteMessage(int messageID) {
        dc.deleteMessage(messageID);
    }

    @Override
    public void deleteAd(int adId) {
        dc.deleteAd(adId);
    }

    @Override
    public Ad[] getMyAds(String author) {
        return dc.getMyAds(author);
    }
    
}
