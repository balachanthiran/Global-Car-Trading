/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GlobalCarTrading;

import java.io.Serializable;

/**
 *
 * @author Jesper
 */
public class Car implements Serializable {

    private int distanceTraveled;
    private int age;
    private String condition;
    private String vin;
    private String brand;
    private int price;
    private int adId;

    public Car(String brand, String vin, String condition, int distanceTraveled, int age, int price, int adId) {
        this.adId = adId;
        this.brand = brand;
        this.age = age;
        this.condition = condition;
        this.price = price;
        this.distanceTraveled = distanceTraveled;
        this.vin = vin;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getDistanceTraveled() {
        return distanceTraveled;
    }

    public int getAge() {
        return age;
    }

    public String getCondition() {
        return condition;
    }

    public String getVIN() {
        return vin;
    }

    public String getBrand() {
        return brand;
    }

    public int getPrice() {
        return price;
    }

    public int getAdId() {
        return adId;
    }

}
