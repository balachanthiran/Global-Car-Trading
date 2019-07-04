/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GlobalCarTrading.Account;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class NewAccountController implements Initializable {

    TabPane tabPane;
    Tab myTab;
    SingleSelectionModel<Tab> selectionModel;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField addressTxt;
    @FXML
    private TextField mailTxt;
    @FXML
    private PasswordField passTxt;
    @FXML
    private Label errLbl;
    @FXML
    private TextField phoneTxt;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setTabPane(TabPane tabPane, Tab myTab) {
        this.tabPane = tabPane;
        this.myTab = myTab;
        selectionModel = tabPane.getSelectionModel();

    }

    //check if user failed to fill out the required information
    @FXML
    public void onDoneAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (!(nameTxt.getText().equalsIgnoreCase("") || addressTxt.getText().equalsIgnoreCase("") || mailTxt.getText().equalsIgnoreCase("") || phoneTxt.getText().isEmpty() || passTxt.getText().equalsIgnoreCase(""))) {
         
            if (nameTxt.getText().length() > 50) {
                errLbl.setText("Name must not be longer than 20 characters");
                return;
            }
               if (addressTxt.getText().length() > 50) {
                errLbl.setText("Address must be no longer than 50 characters");
                return;
            }
            if (mailTxt.getText().length() > 50) {
                errLbl.setText("E-mail must be no longer than 50 characters");
                return;
            }
            if (!(phoneTxt.getText().matches("[0-9]+")) || phoneTxt.getText().length() < 8 || phoneTxt.getText().length() > 10) {
                errLbl.setText("Invalid phone number");
                return;
            }
            if (passTxt.getText().length() > 30) {
                errLbl.setText("Password must be no longer than 30 characters");
                return;
            }
         
            String name = nameTxt.getText();
            String address = addressTxt.getText();
            String email = mailTxt.getText();
            int phone = Integer.valueOf(phoneTxt.getText());
            String password = passTxt.getText();

            //check for valid e-mail
            if (!(validate(email))) {
                errLbl.setText("Invald e-mail");
                return;
            }

            //check if usernames already exists
            ArrayList<String> usernames = ClientController.getConnection().getUsernames();
            if (usernames != null) {

                if (usernames.contains(email)) {
                    errLbl.setText("That e-mail has already been registered!");
                    return;
                }

            }
            //create the account
            Account newAccount = new Account(name, address, email, phone, password);
            try {
                ClientController.getConnection().createAccount(newAccount);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("No connection");

            }
            tabPane.getTabs().remove(myTab);
            selectionModel.select(0);

        } else {
            errLbl.setText("Some information is missing!");
        }

    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
