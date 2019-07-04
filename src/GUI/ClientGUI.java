/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 *
 * @author Nasibollah
 */
public class ClientGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("MainTabPane.fxml"));
        
        Scene scene = new Scene(root);
        
 
        scene.setFill(Color.TRANSPARENT);
        
        
        stage.setScene(scene);
        stage.show();

        stage.setTitle("Global Car Trading");

    }

    public static void main(String[] args) throws IOException {
        launch(args);

    }

    
}
