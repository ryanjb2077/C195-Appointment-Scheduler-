/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utilities.database_connector;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class main_menu_controller implements Initializable
{
    @FXML
    public Label label_current_user;
    @FXML
    private Button button_customer;
    @FXML
    private Button button_appointment;
    @FXML
    private Button button_reports;
    @FXML
    private Button button_view_log;


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

            ResultSet query = statement.executeQuery("SELECT customer.customerName, start FROM appointment JOIN customer ON customer.customerId = appointment.customerId WHERE DATE(start) = curdate()");

            LocalTime current_time = LocalTime.now();

            while(query.next()) {

                String name = query.getString("customerName");
                String start = query.getString("start");

                DateTimeFormatter format_date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                LocalDateTime start_formatted = LocalDateTime.parse(start, format_date);
                ZonedDateTime start_utc = start_formatted.atZone(ZoneId.of("UTC"));

                ZoneId user_zone = ZoneId.systemDefault();
                ZonedDateTime user_current = start_utc.withZoneSameInstant(user_zone);
                DateTimeFormatter format_time = DateTimeFormatter.ofPattern("kk:mm");
                LocalTime apt_time = LocalTime.parse(user_current.toString().substring(11,16), format_time);
                String appointment = apt_time.toString();

                long difference = ChronoUnit.MINUTES.between(current_time, apt_time);

                if(difference > 0 && difference <= 15) {

                    String alertMessage = String.format("You have an appointment with " + name + " at " + appointment);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Reminder");
                    alert.setHeaderText("Appointment in 15 minutes!");
                    alert.setContentText(alertMessage);
                    alert.showAndWait();
                    break;
                }
            }

        }
        catch (SQLException throwables)
        {

            throwables.printStackTrace();
        }


    }

    @FXML
    private void handler_button_customer(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/customer_main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    private void handler_button_appointment(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/appointment_main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    private void handler_button_report(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/reports.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    public void handler_button_log(ActionEvent event) throws IOException
    {
        File file = new File("login Logger.txt");
        if(file.exists())
        {
            if(Desktop.isDesktopSupported())
            {
                try
                {
                    Desktop.getDesktop().open(file);
                }
                catch (IOException except)
                {
                    System.out.println("Error opening log file: " + except.getMessage());
                }
            }
        }
    }
    
}
