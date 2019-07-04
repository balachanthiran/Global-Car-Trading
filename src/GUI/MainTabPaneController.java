/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GlobalCarTrading.Ad;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class MainTabPaneController implements Initializable {
    
    @FXML
    private TabPane tabPane;
    private Tab adsTab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Ad[] ads = null;
        try {
            ads = ClientController.getConnection().getAllAds();
        } catch (RemoteException ex) {
            Logger.getLogger(MainTabPaneController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("Connection failed");
        }
        FXMLLoader adsLoader = new FXMLLoader(getClass().getResource("Ads.fxml"));
        Tab adsTab = new Tab();
        adsTab.setClosable(false);
        adsTab.setText("Ads");
        try {
            adsTab.setContent((Node) adsLoader.load());
        } catch (IOException ex) {
            Logger.getLogger(MainTabPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tabPane.getTabs().add(adsTab);
        AdsController ac = adsLoader.<AdsController>getController();
        ac.setTabPane(tabPane);
        ac.setAds(ads);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Tab loginTab = new Tab();
        loginTab.setText("Login");
        try {
            loginTab.setContent((Node) loginLoader.load());
        } catch (IOException ex) {
            Logger.getLogger(MainTabPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tabPane.getTabs().add(loginTab);
        LoginController lc = loginLoader.<LoginController>getController();
        lc.setTabPane(tabPane);
        // TODO

    }

    private <T> T addTab(String resourceUrl, String title) {
        T controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceUrl));
            Tab tab = new Tab();
            tab.setText(title);
            tab.setContent((Node) loader.load());
            tabPane.getTabs().add(tab);
            controller = loader.<T>getController();
        } catch (IOException ex) {
            Logger.getLogger(MainTabPaneController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex);
        }
        return controller;
    }

}
