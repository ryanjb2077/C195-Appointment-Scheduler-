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
public class appointment_update_controller implements Initializable {

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
    ObservableList<String> type_fill = FXCollections.observableArrayList("Conference Call", "Team Meeting", "Project Review", "Miscellaneous", "Scrum", "Presentation");

    public ObservableList<String> customer_fill = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label_current_user.setText(views.login_screen_controller.get_current_user());

        try {
            Statement statement = database_connector.getConnection().createStatement();

            //combobox customer filler
            ResultSet customer_list = statement.executeQuery("SELECT customerId,customerName FROM customer");

            while (customer_list.next()) {
                String current_id = customer_list.getString("customerId");
                String current_name = customer_list.getString("customerName");
                customer_fill.add(current_id + " " + current_name);
            }
            combo_customer.setItems(customer_fill);


            //combobox type filler
            combo_type.setItems(type_fill);


            //combobox contact filler
            ResultSet contact_list = statement.executeQuery("SELECT userName FROM user");
            ObservableList<String> contact_fill = FXCollections.observableArrayList();

            while (contact_list.next()) {
                String current = contact_list.getString("userName");
                contact_fill.add(current);
            }
            combo_contact.setItems(contact_fill);


            //combobox location filler
            ResultSet city_list = statement.executeQuery("SELECT city FROM city");
            ObservableList<String> city_fill = FXCollections.observableArrayList();

            while (city_list.next()) {
                String current = city_list.getString("city");
                city_fill.add(current);
            }
            combo_location.setItems(city_fill);

            //combobox time filler
            combo_start.setItems(appointment_Time);
            combo_end.setItems(appointment_Time);


            //set selected appointment
            ResultSet appointment_info = statement.executeQuery("SELECT customer.customerId, customer.customerName, appointment.title, appointment.type, appointment.description,"
                    + " appointment.contact, appointment.location, appointment.url, appointment.start, appointment.end"
                    + " FROM (appointment"
                    + " INNER JOIN customer ON appointment.customerId = customer.customerId)"
                    + " WHERE appointment.appointmentId = " + appointment_main_controller.selected_appointment_id);

            while(appointment_info.next())
            {
                combo_customer.setValue(appointment_info.getString("customerId")  + " " + appointment_info.getString("customerName"));
                field_title.setText(appointment_info.getString("title"));
                combo_type.setValue((appointment_info.getString("type")));
                field_description.setText(appointment_info.getString("description"));
                combo_contact.setValue((appointment_info.getString("contact")));
                combo_location.setValue((appointment_info.getString("location")));
                field_url.setText(appointment_info.getString("url"));
                String start = appointment_info.getString("start");
                String end = appointment_info.getString("end");

                date_picker.setValue(LocalDate.parse(start.substring(0, start.indexOf(' '))));

                //adjust start and end time to user time zone
                ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
                ZoneId UTCZoneID = ZoneId.of("UTC");
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                start = start.substring(0, start.length() - 2);
                LocalDateTime start_date = LocalDateTime.parse(start, format);
                ZonedDateTime start_id = start_date.atZone(UTCZoneID);
                ZonedDateTime start_coverted = start_id.withZoneSameInstant(localZoneId);
                LocalDateTime start_time = start_coverted.toLocalDateTime();
                String start_str = start_time.toString();
                String start_final = start_str.replace("T", " ");

                combo_start.setValue(start_final.substring(start_final.indexOf(' ') + 1) +":00");

                end = end.substring(0, end.length() - 2);
                LocalDateTime end_date = LocalDateTime.parse(end, format);
                ZonedDateTime end_id = end_date.atZone(UTCZoneID);
                ZonedDateTime end_coverted = end_id.withZoneSameInstant(localZoneId);
                LocalDateTime end_time = end_coverted.toLocalDateTime();
                String end_str = end_time.toString();
                String end_final = end_str.replace("T", " ");

                combo_end.setValue(end_final.substring(end_final.indexOf(' ') + 1)  +":00");
            }


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


        start_date = start_date.substring(0,19);
        LocalDateTime start_date_formated = LocalDateTime.parse(start_date, format);
        ZonedDateTime start_date_zoned = ZonedDateTime.of(start_date_formated, user_zone_id);
        ZonedDateTime start_date_utc = start_date_zoned.withZoneSameInstant(ZoneId.of("UTC"));

        String start_date_final = (start_date_utc.getYear() + "-" + start_date_utc.getMonthValue() + "-" + start_date_utc.getDayOfMonth()
                + " " + start_date_utc.getHour() + ":" + start_date_utc.getMinute() + ":" + start_date_utc.getMinute() );


        end_date = end_date.substring(0,19);
        LocalDateTime end_date_formated = LocalDateTime.parse(end_date, format);
        ZonedDateTime end_date_zoned = ZonedDateTime.of(end_date_formated, user_zone_id);
        ZonedDateTime end_date_utc = end_date_zoned.withZoneSameInstant(ZoneId.of("UTC"));

        String end_date_final = (end_date_utc.getYear() + "-" + end_date_utc.getMonthValue() + "-" + end_date_utc.getDayOfMonth()
                + " " + end_date_utc.getHour() + ":" + end_date_utc.getMinute() + ":" + end_date_utc.getMinute() );




        if (appointment_add_controller.valid_title(title) && appointment_add_controller.valid_description(description) && appointment_add_controller.valid_url(url) && appointment_add_controller.valid_date(date_picker)
                && appointment_add_controller.valid_time(start_date_final,end_date_final))
        {
            try
            {

                Statement connection = database_connector.getConnection().createStatement();

                String appointment_query = "UPDATE appointment SET"
                        + " customerId = " + customer_id //customerId
                        + ", userId = (SELECT userId FROM user where userName = '" + contact + "')" //userId
                        + ", title = '"  + title  //title
                        + "', description = '" + description //description
                        + "', location = '" + location //location
                        + "', contact = '" + contact //contact
                        + "', type = '" + type //type
                        + "', url = '" + url //url
                        + "', start = '" + start_date_final //start
                        + "', end = '" + end_date_final //end
                        + "', lastUpdate = NOW()" //lastUpdate
                        + ", lastUpdateBy = '" + views.login_screen_controller.get_current_user() + "'" // lastUpdateBy
                        + " WHERE appointmentId = " + appointment_main_controller.selected_appointment_id ;

                int appointment_query_execute = connection.executeUpdate(appointment_query);

                if(appointment_query_execute == 1)
                {
                    System.out.println("Updating appointment " + title + " was successful.");
                }
            }


            catch (SQLException except_sql)
            {
                System.out.println("Error " + except_sql.getMessage());
            }

            catch (NumberFormatException except_format) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Inputs");
                alert.setContentText("Inputs invalid, please check and resubmit");
                alert.showAndWait();
            }

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/views/appointment_main.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }


    @FXML
    private void handler_button_cancel(javafx.event.ActionEvent event) throws IOException
    {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/appointment_main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

}