/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

/**
 *
 * @author Nasibollah
 */
public class LoginController implements Initializable {
    
    @FXML
    private TextField mailTxt;
    @FXML
    private PasswordField passTxt;
    @FXML
    private Label errLbl;

    private TabPane tabPane;
    private Tab loginTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onCreateAction(ActionEvent event) throws IOException {
        FXMLLoader myLoader;
        myLoader = new FXMLLoader(getClass().getResource("NewAccount.fxml"));
        Tab tab = new Tab();
        tab.setText("New Account Registration");
        tab.setContent((Node) myLoader.load());
        tabPane.getTabs().add(tab);
        NewAccountController naController = myLoader.<NewAccountController>getController();
        naController.setTabPane(tabPane, tab);
        tabPane.getSelectionModel().select(tab);
    }
    
    @FXML
    private void handleLoginAction(ActionEvent event) throws IOException, ClassNotFoundException {
        if((ClientController.getConnection().login(mailTxt.getText(), passTxt.getText())) != null)   { 
            ClientController.setAccount(ClientController.getConnection().login(mailTxt.getText(), passTxt.getText()));
            FXMLLoader myLoader;
            myLoader = new FXMLLoader(getClass().getResource("MyProfile.fxml"));
            Tab tab = new Tab();
            tab.setText("My Profile");
            try {
                tab.setContent((Node) myLoader.load());
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tabPane.getTabs().add(tab);
            tab.setClosable(false);
            MyProfileController mpController = myLoader.<MyProfileController>getController();
            mpController.setTabPane(tabPane, loginTab, tab);
            mpController.updateInbox();
            mpController.updateMyads();
            tabPane.getTabs().remove(loginTab);
            tabPane.getSelectionModel().select(tab);
        } else {
            errLbl.setText("Wrong information!");
        }

    }
    

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equalsIgnoreCase("Login")) {
                this.loginTab = tab;
                loginTab.setClosable(false);
            }
        }
    }
}
    
