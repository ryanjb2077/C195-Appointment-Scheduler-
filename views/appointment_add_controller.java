/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.database_connector;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class appointment_add_controller implements Initializable {

    @FXML
    private Label label_current_user;
    @FXML
    private ComboBox<String> combo_customer;
    @FXML
    private TextField field_title;
    @FXML
    private TextField field_description;
    @FXML
    private ComboBox<String> combo_type;
    @FXML
    private ComboBox<String> combo_contact;
    @FXML
    private ComboBox<String> combo_location;
    @FXML
    private TextField field_url;
    @FXML
    private ComboBox<String> combo_start;
    @FXML
    private ComboBox<String> combo_end;
    @FXML
    private Button button_cancel;
    @FXML
    private Button button_save;
    @FXML
    private DatePicker date_picker;

    ObservableList<String> appointment_Time = FXCollections.observableArrayList("09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00");
    ObservableList<String> type_fill = FXCollections.observableArrayList("Conference Call","Miscellaneous","Project Review", "Presentation" , "Scrum", "Team Meeting" );

    public ObservableList<String> customer_fill = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        label_current_user.setText(views.login_screen_controller.get_current_user());

        try
        {
            Statement statement = database_connector.getConnection().createStatement();

            //customer filler
            ResultSet customer_list = statement.executeQuery("SELECT customerId,customerName FROM customer");

            while(customer_list.next())
            {
                String current_id = customer_list.getString("customerId");
                String current_name = customer_list.getString("customerName");
                customer_fill.add(current_id + " " + current_name);
            }
            combo_customer.setItems(customer_fill);


            //type filler
            combo_type.setItems(type_fill);


            //contact filler
            ResultSet contact_list = statement.executeQuery("SELECT userName FROM user");
            ObservableList<String> contact_fill = FXCollections.observableArrayList();

            while(contact_list.next())
            {
                String current = contact_list.getString("userName");
                contact_fill.add(current);
            }
            combo_contact.setItems(contact_fill);


            //location filler
            ResultSet city_list = statement.executeQuery("SELECT city FROM city");
            ObservableList<String> city_fill = FXCollections.observableArrayList();

            while(city_list.next())
            {
                String current = city_list.getString("city");
                city_fill.add(current);
            }
            combo_location.setItems(city_fill);

            //time filler
            combo_start.setItems(appointment_Time);
            combo_end.setItems(appointment_Time);


        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }



    }


    @FXML
    private void handler_button_save(javafx.event.ActionEvent event) throws IOException
    {
        int customer_id = Integer.parseInt(((customer_fill.get((combo_customer.getSelectionModel().getSelectedIndex()))).split(" ", 2))[0]);
        String title = field_title.getText();
        String type = combo_type.getSelectionModel().getSelectedItem();
        String description = field_description.getText();
        String contact = combo_contact.getSelectionModel().getSelectedItem();
        String location = combo_location.getSelectionModel().getSelectedItem();
        String url = field_url.getText();
        LocalDate date = date_picker.getValue();
        String start = combo_start.getSelectionModel().getSelectedItem();
        String end = combo_end.getSelectionModel().getSelectedItem();

        String start_date = date + " " + start;
        String end_date = date + " " + end;

        ZoneId user_zone_id = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start_date_formated = LocalDateTime.parse(start_date, format);
        ZonedDateTime start_date_zoned = ZonedDateTime.of(start_date_formated, user_zone_id);
        ZonedDateTime start_date_utc = start_date_zoned.withZoneSameInstant(ZoneId.of("UTC"));

        String start_date_final = (start_date_utc.getYear() + "-" + start_date_utc.getMonthValue() + "-" + start_date_utc.getDayOfMonth()
        + " " + start_date_utc.getHour() + ":" + start_date_utc.getMinute() + ":" + start_date_utc.getMinute() );


        LocalDateTime end_date_formated = LocalDateTime.parse(end_date, format);
        ZonedDateTime end_date_zoned = ZonedDateTime.of(end_date_formated, user_zone_id);
        ZonedDateTime end_date_utc = end_date_zoned.withZoneSameInstant(ZoneId.of("UTC"));

        String end_date_final = (end_date_utc.getYear() + "-" + end_date_utc.getMonthValue() + "-" + end_date_utc.getDayOfMonth()
                + " " + end_date_utc.getHour() + ":" + end_date_utc.getMinute() + ":" + end_date_utc.getMinute() );




        if ((valid_title(title) && valid_description(description) && valid_url(url) && valid_date(date_picker)
        && valid_time(start_date_final, end_date_final)))
        {
            try
            {

                Statement connection = database_connector.getConnection().createStatement();

                String appointment_query = "INSERT INTO appointment"
                        + " VALUES (null " //appointmentId
                        + ", " + customer_id //customerId
                        + ", (SELECT userId FROM user where userName = '" + contact + "' ) " //userId
                        + ", '"  + title  //title
                        + "', '" + description //description
                        + "', '" + location //location
                        + "', '" + contact //contact
                        + "', '" + type //type
                        + "', '" + url //url
                        + "', '" + start_date_final //start
                        + "', '" + end_date_final //end
                        + "', NOW()"  // createDate
                        + ", '" + views.login_screen_controller.get_current_user() //createdBy
                        + "', NOW()" //lastUpdate
                        + ", '" + views.login_screen_controller.get_current_user() + "')"; // lastUpdateBy

                int appointment_query_execute = connection.executeUpdate(appointment_query);

                    if(appointment_query_execute == 1)
                    {
                        System.out.println("Inserting appointment " + title + " was successful.");
                    }
            }

            catch(SQLException except_sql)
            {
                System.out.println("Error " + except_sql.getMessage());
            }

            catch(NumberFormatException except_format)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Inputs");
                alert.setContentText("Inputs invalid, please check and resubmit");
                alert.showAndWait();
            }

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/views/appointment_main.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }


    @FXML
    private void handler_button_cancel(javafx.event.ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/appointment_main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }



    public static boolean valid_title(String title)
    {
        if(title.isEmpty() || (title.length() > 255 ))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setContentText("Invalid title, please check and resubmit");
            alert.showAndWait();
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean valid_description(String description)
    {
        if(description.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setContentText("Invalid description, please check and resubmit");
            alert.showAndWait();
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean valid_url(String url)
    {
        if(url.isEmpty() || (url.length() > 255 ))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setContentText("Invalid postal code, please check and resubmit");
            alert.showAndWait();
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean valid_date(DatePicker date)
    {
        if(date.getValue() == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setContentText("Invalid calendar date, please check and resubmit");
            alert.showAndWait();
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean valid_time(String start_date, String end_date)
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-M-d H:m:s");

        LocalDateTime start_date_formated = LocalDateTime.parse(start_date, format);
        LocalDateTime end_date_formated = LocalDateTime.parse(end_date, format);


        if(start_date_formated.isEqual(end_date_formated))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid appointment interval");
            alert.setContentText("Appointment end can not be the same as the start time, please select a new time");
            alert.showAndWait();
            return false;
        }
        if(end_date_formated.isBefore(start_date_formated))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid appointment interval");
            alert.setContentText("Appointment end can not be the before as the start time, please select a new time");
            alert.showAndWait();
            return false;
        }

        else
        {
            return true;
        }


    }

}
