/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GlobalCarTrading.Account;
import GlobalCarTrading.Ad;
import GlobalCarTrading.Message;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class MyProfileController implements Initializable {

    private TabPane tabPane;
    private Account account;
    private ObservableList<String> inboxMessages = FXCollections.observableArrayList();
    private ObservableList<String> myAds = FXCollections.observableArrayList();
    private Message[] messageArray;
    private Ad[] myAdsArray;
    private Label helloLbl;
    @FXML
    private TextField addressTxt;
    Tab loginTab;
    Ad selectedAd;
    @FXML
    private TextField emailTxt;
    @FXML
    private TextField phoneTxt;
    @FXML
    private TextField passTxt;
    @FXML
    private ComboBox<String> countryBox;
    @FXML
    private ListView<String> inboxView;
    @FXML
    private Label lblHello;
    @FXML
    private Label lblAddress;
    @FXML
    private Button btnSaveInfo;
    @FXML
    private Label lblPhone;
    private Label lblName;
    @FXML
    private Label lblPass;
    private Button btnSendMessage;
    @FXML
    private Label lblInbox;
    @FXML
    private Button btnUpdateInbox;
    private Locale locale;
    private ResourceBundle bundle;
    private Tab profileTab;
    @FXML
    private Label lblMyads;
    @FXML
    private Button btnDeleteMessage;
    @FXML
    private Button btnHandleReply;
    @FXML
    private Button btnUpdateMyads;
    @FXML
    private Button btnDeleteAd;
    @FXML
    private Button btnViewAd;
    @FXML
    private ListView<String> myAdsView;
    @FXML
    private Label errLbl;
    private Message selectedMessage;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @FXML
    private Button btnLogout;
    @FXML
    private Label lblEmail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> languages = FXCollections.observableArrayList("Danish", "English", "Spanish", "Chinese");
        countryBox.setItems(languages);
    }

    public void setAccount(Account account) {
        this.account = account;
        addressTxt.setText(account.getAddress());
        emailTxt.setText(account.getMail());
        lblHello.setText(lblHello.getText() + account.getName());
        phoneTxt.setText(String.valueOf(account.getPhoneNumber()));
    }

    void setTabPane(TabPane tabPane, Tab loginTab, Tab profileTab) {
        this.tabPane = tabPane;
        this.loginTab = loginTab;
        this.profileTab = profileTab;
        loginTab.setClosable(false);
        setAccount(ClientController.getAccount());
    }

    @FXML
    private void handleSaveInfo(ActionEvent event) throws IOException {
        ArrayList<String> usernames = ClientController.getConnection().getUsernames();
        if (!(ClientController.getAccount().getMail().equals(emailTxt.getText()))) {
            if (usernames.contains(emailTxt.getText())) {
                errLbl.setText("That e-mail has already been registered!");
                return;
            }
        }

        if (!(validate(emailTxt.getText()))) {
            errLbl.setText("Invalid e-mail");
            return;
        }

        if (!(phoneTxt.getText().matches("[0-9]+")) || phoneTxt.getText().length() < 8) {
            errLbl.setText("Invalid phone number");
            return;
        }
        String address = addressTxt.getText();
        String mail = emailTxt.getText();
        int phone = Integer.valueOf(phoneTxt.getText());
        String password = passTxt.getText();
        account = new Account(account.getName(), address, mail, phone, password);
        ClientController.getConnection().saveAccount(account);
        errLbl.setText("Info has been updated");
    }

    @FXML
    private void handleChangeLanguage(ActionEvent event) throws IOException {
        //get all controllers
        AdsController ac = getController("Ads");

        //use the controllers to change the language in all fxml documents
        if (countryBox.getSelectionModel().getSelectedIndex() == 0) {

            loadLang("da");
            ac.loadLang("da");

        }
        if (countryBox.getSelectionModel().getSelectedIndex() == 1) {
            loadLang("en");
            ac.loadLang("en");
        }
        if (countryBox.getSelectionModel().getSelectedIndex() == 2) {
            loadLang("es");
            ac.loadLang("es");
        }
        if (countryBox.getSelectionModel().getSelectedIndex() == 3) {
            loadLang("zh_cn");
            ac.loadLang("zh_cn");
        }
    }

    @FXML
    private void handleUpdateInbox(ActionEvent event) throws IOException {
        updateInbox();
    }

    public void updateInbox() {
        try {
            messageArray = ClientController.getConnection().receiveMessages(account.getName());
        } catch (RemoteException ex) {
            Logger.getLogger(MyProfileController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("No connection");
        }
        if (messageArray.length == 0) {
            inboxMessages.clear();
            inboxMessages.add("No messages.");
        } else {
            inboxMessages.clear();
            for (Message m : messageArray) {
                String from = "FROM " + m.getSender() + ": ";
                String message = m.getMessage();
                inboxMessages.add(from + message);
            }
        }
        inboxView.setItems(inboxMessages);

    }

    public void loadLang(String lang) throws RemoteException {
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("GUI.lang", locale);
        lblHello.setText(bundle.getString("Hello") + ", " + ClientController.getAccount().getName());
        lblAddress.setText(bundle.getString("Address"));
        lblInbox.setText(bundle.getString("Inbox"));
        lblPass.setText(bundle.getString("Pass"));
        lblPhone.setText(bundle.getString("Phone"));
        lblMyads.setText(bundle.getString("Myads"));
        lblEmail.setText(bundle.getString("Email"));
        btnSaveInfo.setText(bundle.getString("SaveInfo"));
        btnUpdateInbox.setText(bundle.getString("Update"));
        btnUpdateMyads.setText(bundle.getString("Update"));
        btnDeleteAd.setText(bundle.getString("Delete"));
        btnDeleteMessage.setText(bundle.getString("Delete"));
        btnHandleReply.setText(bundle.getString("Reply"));
        btnLogout.setText(bundle.getString("Logout"));
        btnViewAd.setText(bundle.getString("View"));

    }

    public <T> T getController(String tabName) {
        T controller = null;
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equalsIgnoreCase(tabName)) {
                FXMLLoader myLoader;
                myLoader = new FXMLLoader(getClass().getResource(tabName + ".fxml"));
                try {
                    tab.setContent((Node) myLoader.load());
                } catch (IOException ex) {
                    Logger.getLogger(MyProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
                controller = myLoader.<T>getController();
            }
        }
        return controller;
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        ClientController.getAccount().setValidated(false);
        tabPane.getTabs().add(loginTab);
        tabPane.getSelectionModel().select(loginTab);
        tabPane.getTabs().remove(profileTab);

    }

    public void updateMyads() {
        try {
            myAdsArray = ClientController.getConnection().getMyAds(account.getName());
        } catch (RemoteException ex) {
            Logger.getLogger(MyProfileController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("No connection");
        }
        if (myAdsArray.length == 0) {
            myAds.clear();
            myAds.add("No ads.");
        } else {
            myAds.clear();
            for (Ad ad : myAdsArray) {
                myAds.add(ad.getTitle());
            }
        }
        myAdsView.setItems(myAds);

    }

    @FXML
    private void handleDeleteMessage(ActionEvent event) {
        int messageIndex = inboxView.getSelectionModel().getSelectedIndex();
        try {
            selectedMessage = messageArray[messageIndex];
        } catch (Exception e) {
            errLbl.setText("Select a message first");
            return;
        }
        try {
            ClientController.getConnection().deleteMessage(selectedMessage.getMessageID());
        } catch (RemoteException ex) {
            Logger.getLogger(MyProfileController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("No connection");
        }
        inboxMessages.remove(messageIndex);
    }

    @FXML
    private void handleReply(ActionEvent event) {
        int messageIndex = inboxView.getSelectionModel().getSelectedIndex();
        try {
            selectedMessage = messageArray[messageIndex];
        } catch (Exception e) {
            errLbl.setText("Select a message first");
            return;
        }
        String message = JOptionPane.showInputDialog("Send Message");
        if (message == null || message.isEmpty()) {
            return;
        }
        Message newMessage;
        try {
            newMessage = new Message(ClientController.getAccount().getName(), message, selectedMessage.getSender(), 0);
            ClientController.getConnection().sendMessage(newMessage);
        } catch (RemoteException ex) {
            Logger.getLogger(MyProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleUpdateMyads(ActionEvent event) {
        updateMyads();
    }

    @FXML
    private void handleDeleteAd(ActionEvent event) {
        int adIndex = myAdsView.getSelectionModel().getSelectedIndex();
        try {
            selectedAd = myAdsArray[adIndex];
        } catch (Exception e) {
            errLbl.setText("Select an ad first");
            return;
        }
        try {
            ClientController.getConnection().deleteAd(selectedAd.getAdId());
        } catch (RemoteException ex) {
            Logger.getLogger(MyProfileController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("No connection");
        }
        myAds.remove(adIndex);
    }

    @FXML
    private void handleViewAd(ActionEvent event) {
        int adIndex = myAdsView.getSelectionModel().getSelectedIndex();
        try {
            selectedAd = myAdsArray[adIndex];
        } catch (Exception e) {
            errLbl.setText("Select an ad first");
            return;
        }
        FXMLLoader myLoader;
        myLoader = new FXMLLoader(getClass().getResource("ViewAd.fxml"));
        Tab tab = new Tab();
        tab.setText("View Ad");
        try {
            tab.setContent((Node) myLoader.load());
        } catch (IOException ex) {
            System.out.println(ex);
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

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
