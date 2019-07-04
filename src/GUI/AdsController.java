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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class AdsController implements Initializable {

    private List<String> adTitles;
    TabPane tabPane;
    private Ad[] ads;
    private Ad selectedAd;
    Tab adsTab;
    private Ad findAd;
    @FXML
    private ListView<String> adsView;
    private ObservableList<String> adsList = FXCollections.observableArrayList();
    @FXML
    private Label errLbl;
    @FXML
    private Button btnNewAd;
    @FXML
    private Button btnOpenAd;
    @FXML
    private Button btnUpdate;

    private Locale locale;
    private ResourceBundle bundle;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField searchField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adTitles = new ArrayList<String>();
        // TODO
    }

    public void addAdToListView(String title) {
        adsList.add(title);
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equalsIgnoreCase("View Ad")) {
                this.adsTab = tab;
            }

        }
    }

    @FXML
    private void handleNewAd(ActionEvent event) throws IOException {
        if (ClientController.getAccount() == null || !ClientController.getAccount().isValidated()) {
            errLbl.setText("You need to be logged in to create an ad!");

        } else {
            errLbl.setText("");
            FXMLLoader myLoader;
            myLoader = new FXMLLoader(getClass().getResource("NewAd.fxml"));
            Tab tab = new Tab();
            tab.setText("Create New Ad");
            tab.setContent((Node) myLoader.load());
            tabPane.getTabs().add(tab);
            NewAdController nadController = myLoader.<NewAdController>getController();
            nadController.setTabPane(tabPane, tab);
            tabPane.getSelectionModel().select(tab);
        }

    }

    @FXML
    private void handleOpenAd(ActionEvent event) {
        int adIndex = adsView.getSelectionModel().getSelectedIndex();
        try {
            selectedAd = ads[adIndex];
        } catch (Exception e) {
            errLbl.setText("You need to select an ad first!");
            return;
        }
        errLbl.setText("");
        FXMLLoader myLoader;
        myLoader = new FXMLLoader(getClass().getResource("ViewAd.fxml"));
        Tab tab = new Tab();
        tab.setText("View Ad");
        try {
            tab.setContent((Node) myLoader.load());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        tabPane.getTabs().add(tab);
        ViewAdController vac = myLoader.<ViewAdController>getController();
        vac.setAd(selectedAd);
        try {
            vac.setCar(ClientController.getConnection().getCar(selectedAd.getAdId()));
        } catch (RemoteException ex) {
            Logger.getLogger(AdsController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("No connection");
        }

        vac.setTabPane(tabPane, tab);
        tabPane.getSelectionModel().select(tab);
    }

    public void setAds(Ad[] ads) {
        this.ads = ads;
        if (!(ads.length == 0)) {
            for (Ad ad : ads) {
                adTitles.add(ad.getTitle());
            }
            for (String s : adTitles) {
                addAdToListView(s);
                try {
                    adsList.remove("There are no ads yet!");
                } catch (Exception e) {
                    System.out.println("It wasn't there!");
                }
            }
        } else {
            addAdToListView("There are no ads yet!");
        }

        adsView.setItems(adsList);
    }

    @FXML
    private void handleUpdate(ActionEvent event) {

        try {
            ads = ClientController.getConnection().getAllAds();
        } catch (RemoteException ex) {
            Logger.getLogger(AdsController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("No connection");
        }
        adTitles.clear();
        adsView.getItems().clear();
        for (Ad ad : ads) {
            adTitles.add(ad.getTitle());
        }
        for (String s : adTitles) {
            {
                addAdToListView(s);
            }

        }
        adsView.setItems(adsList);
    }

    public void loadLang(String lang) {
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("GUI.lang", locale);
        btnNewAd.setText(bundle.getString("NewAd"));
        btnOpenAd.setText(bundle.getString("OpenAd"));
        btnUpdate.setText(bundle.getString("Update"));
        btnSearch.setText(bundle.getString("Search"));
        try {
            setAds(ClientController.getConnection().getAllAds());
        } catch (RemoteException ex) {
            Logger.getLogger(AdsController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("No connection");
        }

    }

    @FXML
    private void handleSearch(ActionEvent event) {
        adsList.clear();
        for (Ad ad : ads) {
            if (searchField.getText() != null || !searchField.getText().isEmpty()) {
                if (!ad.getTitle().toLowerCase().contains(searchField.getText().toLowerCase())) {
                    continue;
                }
                adsList.add(ad.getTitle());
            }

        }
    }
}
