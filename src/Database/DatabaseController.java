/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import GlobalCarTrading.Account;
import GlobalCarTrading.Ad;
import GlobalCarTrading.Car;
import GlobalCarTrading.Message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mohe
 */
public class DatabaseController {

    PreparedStatement ps = null;
    ResultSet rs = null;
    private static Connection con = null;
    private static DatabaseController instance;

    public static DatabaseController getInstance() {
        if (instance == null) {
            instance = new DatabaseController();
        }
        return instance;
    }

    public DatabaseController() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found!");
            return;
        }

        try {
            con = DriverManager.getConnection("jdbc:postgresql://tek-mmmi-db0a.tek.c.sdu.dk:5432/group_7_db", "group_7", "CUtmw8pG");
            //"jdbc:postgresql://tek-mmmi-db0a.tek.c.sdu.dk:5432/group_7_db", "group_7", "CUtmw8pG");
        } catch (SQLException ex) {
            System.out.println("Connection failed!" + ex);
            return;
        }
        System.out.println("Connection to database established ...");
    }

    public ArrayList<String> getUsernames() {
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                Statement s = con.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM \"account\"");
                ArrayList<String> usernames = new ArrayList<>();
                while (rs.next()) {
                    String username = rs.getString("email");
                    usernames.add(username);
                }
                return usernames;

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void createUser(Account account) {
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                ps = con.prepareStatement("INSERT INTO \"account\"(\"name\", \"address\", \"email\", \"phonenumber\", \"password\") VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, account.getName());
                ps.setString(2, account.getAddress());
                ps.setString(3, account.getMail());
                ps.setInt(4, account.getPhoneNumber());
                ps.setString(5, encrypt(account.getPassword()));
                ps.executeUpdate();

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public Account getAccount(String username, String password) {
        Account account;
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                ps = con.prepareStatement("SELECT \"account\".\"email\", \n"
                        + "  \"account\".\"password\", \n"
                        + "  \"account\".\"phonenumber\", \n"
                        + "  \"account\".\"name\", \n"
                        + "  \"account\".\"address\"\n"
                        + "FROM \n"
                        + "  public.\"account\"\n"
                        + "WHERE \n"
                        + "  \"account\".\"email\" = ? \n"
                        + "AND \n"
                        + "  \"account\".\"password\" = ?");
                ps.setString(1, username);
                ps.setString(2, encrypt(password));

                rs = ps.executeQuery();
                if (rs.next()) {
                    account = new Account(rs.getString("name"), rs.getString("address"), rs.getString("email"), rs.getInt("phonenumber"), rs.getString("password"));
                    return account;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void saveAccount(Account account) {
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null && !account.getPassword().equalsIgnoreCase("")) {
                ps = con.prepareStatement("UPDATE \"account\" SET \"phonenumber\"=?, \"password\"=?, \"name\"=?, \"address\"=?  WHERE \"email\" = ?");
                ps.setInt(1, account.getPhoneNumber());
                ps.setString(2, encrypt(account.getPassword()));
                ps.setString(3, account.getName());
                ps.setString(4, account.getAddress());
                ps.setString(5, account.getMail());
                ps.executeUpdate();

            } else {
                ps = con.prepareStatement("UPDATE \"account\" SET \"phonenumber\"=?, \"name\"=?, \"address\"=?  WHERE \"email\" = ?");
                ps.setInt(1, account.getPhoneNumber());
                ps.setString(2, account.getName());
                ps.setString(3, account.getAddress());
                ps.setString(4, account.getMail());
                ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createAd(Ad ad, Car car) {
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                ps = con.prepareStatement("INSERT INTO \"ad\"(\"title\", \"description\", \"author\", \"image\") VALUES (?, ?, ?, ?)");
                ps.setString(1, ad.getTitle());
                ps.setString(2, ad.getDescription());
                ps.setString(3, ad.getAuthor());
                ps.setBytes(4, ad.getImage());
                ps.executeUpdate();

                  ps = con.prepareStatement("SELECT currval('\"Ad_adId_seq\"');");
                  rs = ps.executeQuery();
                  rs.next();
                  int adId = rs.getInt("currval");
                  ad.setAdId(adId);

                ps = con.prepareStatement("INSERT INTO \"car\"(\"distanceTraveled\", \"age\", \"condition\", \"vin\", \"brand\", \"price\", \"FK_adId\") VALUES (?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, car.getDistanceTraveled());
                ps.setInt(2, car.getAge());
                ps.setString(3, car.getCondition());
                ps.setString(4, car.getVIN());
                ps.setString(5, car.getBrand());
                ps.setInt(6, car.getPrice());
                ps.setInt(7, adId);
                ps.executeUpdate();   
                car.setAdId(adId);

            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally{
            try {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Ad[] getAllAds() {
        Ad ad;
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                Statement s = con.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM \"ad\";");
                List<Ad> ads = new ArrayList<Ad>();
                while (rs.next()) {
                    ad = new Ad(rs.getString("title"), rs.getString("description"), rs.getString("author"), rs.getBytes("image"), rs.getInt("adId"));
                    ads.add(ad);
                }
                return ads.toArray(new Ad[ads.size()]);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void sendMessage(Message message) {
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                ps = con.prepareStatement("INSERT INTO \"message\"(\"sender\", \"message\", \"recipient\") VALUES (?, ?, ?)");
                ps.setString(1, message.getSender());
                ps.setString(2, message.getMessage());
                ps.setString(3, message.getRecipient());
                ps.executeUpdate();

                ps = con.prepareStatement("SELECT currval('\"message_messageID_seq\"');");
                rs = ps.executeQuery();

                rs.next();
                int messageID = rs.getInt("currval");
                ps = con.prepareStatement("INSERT INTO \"message\"(\"messageID\") VALUES (?);");
                ps.setInt(1, messageID);
                ps.executeUpdate();
                message.setMessageID(messageID);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public Message[] receiveMessages(String recipient) {
        Message message;
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                ps = con.prepareStatement("SELECT * FROM \"message\" WHERE \"recipient\" = ?;");
                ps.setString(1, recipient);
                rs = ps.executeQuery();
                List<Message> messages = new ArrayList<Message>();
                while (rs.next()) {
                    message = new Message(rs.getString("sender"), rs.getString("message"), rs.getString("recipient"), rs.getInt("messageID"));
                    messages.add(message);
                }
                return messages.toArray(new Message[messages.size()]);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public Car getCar(int adId) {
        Car car = null;
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                ps = con.prepareStatement("SELECT * FROM \"car\" WHERE \"FK_adId\" = ?;");
                ps.setInt(1, adId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    car = new Car(rs.getString("brand"), rs.getString("vin"), rs.getString("condition"), rs.getInt("distanceTraveled"), rs.getInt("age"), rs.getInt("price"), rs.getInt("FK_adId"));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return car;
    }

    public String encrypt(String password) {
        String result = "";
        int passwordLength = password.length();
        char c;
        for (int i = 0; i < passwordLength; i++) {
            c = password.charAt(i);
            c += 1024;
            result += c;
        }

        return result;
    }
    
    public Ad[] getMyAds(String author) {
        Ad ad;
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                 ps = con.prepareStatement("SELECT * FROM \"ad\" WHERE \"author\" = ?;");
                ps.setString(1, author);
                rs = ps.executeQuery();
                List<Ad> myAds = new ArrayList<Ad>();
                while (rs.next()) {
                    ad = new Ad(rs.getString("title"), rs.getString("description"), rs.getString("author"), rs.getBytes("image"), rs.getInt("adId"));
                    myAds.add(ad);
                }
                return myAds.toArray(new Ad[myAds.size()]);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
    
     public void deleteMessage(int messageID) {
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                 ps = con.prepareStatement("DELETE FROM \"message\" WHERE \"messageID\" = ?;");
                ps.setInt(1, messageID);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
  
    }
      public void deleteAd(int adId) {
        try {
            Class.forName("org.postgresql.Driver");
            if (con != null) {
                 ps = con.prepareStatement("DELETE FROM \"ad\" WHERE \"adId\" = ?;");
                ps.setInt(1, adId);
                ps.executeUpdate();
                 ps = con.prepareStatement("DELETE FROM \"car\" WHERE \"FK_adId\" = ?;");
                ps.setInt(1, adId);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
  
    }
}
