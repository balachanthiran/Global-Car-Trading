/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GlobalCarTrading;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Jesper
 */
public class Ad implements Serializable {

    private byte[] image;
    private String title;
    private String description;
    private Date date;
    private String author;
    private int adId;

    public Ad(String title, String description, String author, byte[] image, int adId) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.author = author;
        this.adId = adId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getAuthor() {
        return author;
    }

    public int getAdId() {
        return adId;
    }

    public byte[] getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
    public void setImage(BufferedImage image){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            this.image = imageInByte;
        } catch (IOException ex) {
            Logger.getLogger(Ad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Image getImageFX(){
         Image image = new Image("Resources/image-placeholder.png");
        if (this.image == null) {
            return image;
        }
        try {
            InputStream in = new ByteArrayInputStream(this.image);
            BufferedImage bImageFromConvert = ImageIO.read(in);
            image = SwingFXUtils.toFXImage(bImageFromConvert, null);
        } catch (IOException ex) {
            Logger.getLogger(Ad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
