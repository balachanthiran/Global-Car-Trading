/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GlobalCarTrading.Ad;
import GlobalCarTrading.Car;
import GlobalCarTrading.Message;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class ViewAdController implements Initializable {

    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label authorLbl;
    @FXML
    private ImageView imageView;
    Tab myTab;
    TabPane tabPane;

    Tab adTab;
    @FXML
    private Label titleLabel;
    String author;
    @FXML
    private Label lblBrand;
    @FXML
    private Label lblVIN;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblCondition;
    @FXML
    private Label lblDistanceTraveled;
    @FXML
    private Label lblAge;
    @FXML
    private Label txtDistanceTraveled;
    @FXML
    private Label txtAge;
    @FXML
    private Label errLbl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setAd(Ad selectedAd) {
        titleLabel.setText(selectedAd.getTitle());
        descriptionArea.setText(selectedAd.getDescription());
        authorLbl.setText(selectedAd.getAuthor());
        imageView.setImage(selectedAd.getImageFX());
    }

    public void setCar(Car adCar) {
        lblBrand.setText(adCar.getBrand());
        lblVIN.setText(adCar.getVIN());
        lblCondition.setText(adCar.getCondition());
        lblDistanceTraveled.setText(String.valueOf(adCar.getDistanceTraveled()));
        lblAge.setText(String.valueOf(adCar.getAge()));
        lblPrice.setText(String.valueOf(adCar.getPrice()));

        if (adCar.getCondition().equals("New")) {
            lblDistanceTraveled.setVisible(false);
            lblAge.setVisible(false);
            txtAge.setVisible(false);
            txtDistanceTraveled.setVisible(false);
        }

    }

    void setTabPane(TabPane tabPane, Tab myTab) {
        this.tabPane = tabPane;
        this.myTab = myTab;
    }

    @FXML
    private void handleSendMessage(ActionEvent event) {
        if (ClientController.getAccount() == null || !ClientController.getAccount().isValidated()) {
            errLbl.setText("You need to be logged in");

        } else {
            errLbl.setText("");
            String message = JOptionPane.showInputDialog("Send Message");
            if (message == null || message.isEmpty()) {
                return;
            }
            try {
                Message newMessage = new Message(ClientController.getAccount().getName(), message, authorLbl.getText(), 0);
                ClientController.getConnection().sendMessage(newMessage);
            } catch (RemoteException ex) {
                Logger.getLogger(ViewAdController.class.getName()).log(Level.SEVERE, null, ex);
                throw new Error("No connection");
            }
        }

    }
}
