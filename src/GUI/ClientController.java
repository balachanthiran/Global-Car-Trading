/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GlobalCarTrading.Account;
import GlobalCarTrading.IServer;
import com.sun.deploy.si.SingleInstanceManager;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jesper
 */
public class ClientController {


    private static Account account;
    private static IServer serverConnection;
    
    public static synchronized IServer getConnection() throws RemoteException {
    if(serverConnection == null){
        try {
            connectToServer();
        } catch (NotBoundException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        return serverConnection;
    }
  
        static void setAccount(Account account) {
        ClientController.account = account;
        account.setValidated(true);
    }
    private static void connectToServer() throws RemoteException, NotBoundException{
        Registry r = LocateRegistry.getRegistry("localhost", 3000);
        serverConnection = (IServer) r.lookup("server");
    }
    static Account getAccount(){
        return account;
    }
    
    public void logout(){
        account.setValidated(false);
    }
}

