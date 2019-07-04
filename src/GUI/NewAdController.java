/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GlobalCarTrading.Ad;
import GlobalCarTrading.Car;
import GlobalCarTrading.ServerMain;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class NewAdController implements Initializable {

    private TabPane tabPane;
    private Tab myTab;
    private SingleSelectionModel<Tab> selectionModel;
    FileChooser fc;
    File file;
    BufferedImage bufferedImg;
    byte[] image;

    @FXML
    private TextField titleTxt;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField txtBrand;
    @FXML
    private TextField txtVIN;
    @FXML
    private TextField txtPrice;
    @FXML
    private ComboBox<String> conditionBox;
    @FXML
    private ImageView imageView;
    @FXML
    private Label ErrLbl;
    @FXML
    private TextField txtDistanceTraveled;
    @FXML
    private TextField txtAge;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fc = new FileChooser();
        ObservableList<String> conditions = FXCollections.observableArrayList("New", "Almost new", "Good, but used", "Worn down", "Defective");
        conditionBox.setItems(conditions);

    }

    void setTabPane(TabPane tabPane, Tab myTab) {
        this.tabPane = tabPane;
        this.myTab = myTab;
        selectionModel = tabPane.getSelectionModel();

    }

    @FXML
    private void handleUploadImage(ActionEvent event) throws IOException {
        Image placeholder = new Image("Resources/image-placeholder.png");
        fc.getExtensionFilters().addAll(new ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg"));
        file = fc.showOpenDialog(null);
        bufferedImg = ImageIO.read(file);
        Image img = SwingFXUtils.toFXImage(bufferedImg, null);
        if (img == null) {
            img = placeholder;
        }
        imageView.setImage(img);
    }

    @FXML
    private void handleDoneBtn(ActionEvent event) throws IOException {
        if (!(bufferedImg == null)) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImg, "jpg", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();
                this.image = imageInByte;

            } catch (IOException ex) {
                Logger.getLogger(Ad.class.getName()).log(Level.SEVERE, null, ex);
            }

            tabPane.getTabs().remove(myTab);
            selectionModel.select(0);
        }

        if (!(titleTxt.getText() == null || titleTxt.getText().isEmpty() || descriptionArea.getText() == null || descriptionArea.getText().isEmpty() || txtVIN.getText() == null || txtVIN.getText().isEmpty() || txtBrand.getText() == null || txtBrand.getText().isEmpty() || conditionBox.getValue() == null || conditionBox.getValue().isEmpty() || txtPrice.getText() == null) || txtPrice.getText().isEmpty()) {

            if (!(txtPrice.getText().matches("[0-9]+"))) {
                ErrLbl.setText("Price must be numbers only");
                return;  
            }
            if(txtPrice.getText().length() > 9){
                ErrLbl.setText("Price must not be higher than 10 digits");
                return;
            }  
            if (titleTxt.getText().length() > 20) {
                ErrLbl.setText("Title must not contain more than 20 characters");
                return;
            }
            if (descriptionArea.getText().length() > 200) {
                ErrLbl.setText("Description must be no longer than 200 characters");
                return;
            }
            if (txtBrand.getText().length() > 20) {
                ErrLbl.setText("Brand must be no longer than 20 characters");
                return;
            }
            if(txtVIN.getText().length() > 30){
            ErrLbl.setText("VIN must be no longer than 30 characters");
            return;
        }

            String title = titleTxt.getText();
            String description = descriptionArea.getText();
            String VIN = txtVIN.getText();
            String brand = txtBrand.getText();
            String condition = conditionBox.getValue();
            int price = Integer.parseInt(txtPrice.getText());

            int distanceTraveled;
            try {
                distanceTraveled = Integer.parseInt(txtDistanceTraveled.getText());
            } catch (Exception e) {
                distanceTraveled = 0;
            }
            int age;
            try {
                age = Integer.parseInt(txtAge.getText());
            } catch (Exception e) {
                age = 0;
            }

            Ad ad = new Ad(title, description, ClientController.getAccount().getName(), image, 0);
            Car car = new Car(brand, VIN, condition, distanceTraveled, age, price, 0);
            ClientController.getConnection().createAd(ad, car);

            tabPane.getTabs().remove(myTab);
            selectionModel.select(0);
        } else {
            ErrLbl.setText("Title, description and car info are required!");
        }
    }

    @FXML
    private void handleConditionSelected(ActionEvent event) {
        if (conditionBox.getSelectionModel().getSelectedIndex() != 0) {
            txtDistanceTraveled.setVisible(true);
            txtAge.setVisible(true);
        } else {
            txtDistanceTraveled.setText("0");
            txtAge.setText("0");
            txtDistanceTraveled.setVisible(false);
            txtAge.setVisible(false);
        }
    }
}
