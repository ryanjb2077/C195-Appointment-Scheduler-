/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.appointment;
import utilities.database_connector;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class appointment_main_controller implements Initializable {

    @FXML
    private TableView<appointment> table_appointment;
    @FXML
    private TableColumn<appointment, Integer> column_customer_id;
    @FXML
    private TableColumn<appointment, String> column_customer_name;
    @FXML
    private TableColumn<appointment, String> column_contact;
    @FXML
    private TableColumn<appointment, String> column_title;
    @FXML
    private TableColumn<appointment, String> column_type;
    @FXML
    private TableColumn<appointment, String> column_start;
    @FXML
    private TableColumn<appointment, String> column_end;
    @FXML
    private RadioButton radio_during_month;
    @FXML
    private ComboBox<String> combo_customer_view;
    @FXML
    private RadioButton radio_during_week;
    @FXML
    private RadioButton radio_during_eternity;
    @FXML
    private Button button_add;
    @FXML
    private Button button_update;
    @FXML
    private Button button_delete;
    @FXML
    private Button button_return_main;
    @FXML
    private Label label_current_user;
    @FXML
    private Button button_return_customer;

    public static int selected_appointment_id;
    public static String filter_current_customer = "All";
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

            customer_fill.add("All ");

            while (customer_list.next()) {
                String current_id = customer_list.getString("customerId");
                String current_name = customer_list.getString("customerName");
                customer_fill.add(current_id + " " + current_name);
            }
            combo_customer_view.setItems(customer_fill);

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        table_appointment_fill();
    }

    @FXML
    private void handler_button_add(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/appointment_add.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    private void handler_button_update(ActionEvent event) throws IOException
    {

        try
        {
            Statement statement = database_connector.getConnection().createStatement();

            ResultSet id_fetch = statement.executeQuery("SELECT appointmentId FROM appointment " +
                    "WHERE (customerId = '" + table_appointment.getSelectionModel().getSelectedItem().getId() +
                    "' AND contact = '" + table_appointment.getSelectionModel().getSelectedItem().getContact() +
                    "' AND title = '" + table_appointment.getSelectionModel().getSelectedItem().getTitle() +
                    "' AND type = '" + table_appointment.getSelectionModel().getSelectedItem().getType() + "')");

           id_fetch.next();
           selected_appointment_id = Integer.parseInt(id_fetch.getString("appointmentId"));
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }


        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/appointment_update.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    private void handler_button_delete(ActionEvent event) throws IOException
    {
        try
        {
            Statement statement = database_connector.getConnection().createStatement();

            ResultSet id_fetch = statement.executeQuery("SELECT appointmentId FROM appointment " +
                    "WHERE (customerId = '" + table_appointment.getSelectionModel().getSelectedItem().getId() +
                    "' AND contact = '" + table_appointment.getSelectionModel().getSelectedItem().getContact() +
                    "' AND title = '" + table_appointment.getSelectionModel().getSelectedItem().getTitle() +
                    "' AND type = '" + table_appointment.getSelectionModel().getSelectedItem().getType() + "')");

            id_fetch.next();
            selected_appointment_id = Integer.parseInt(id_fetch.getString("appointmentId"));
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deleting appointment " + selected_appointment_id + " " + table_appointment.getSelectionModel().getSelectedItem().getTitle() + " continue?");
        alert.setTitle("appointment Deletion");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            try
            {
                Statement statement = database_connector.getConnection().createStatement();

                int delete_appointment = statement.executeUpdate("DELETE FROM appointment WHERE appointmentId = " + selected_appointment_id);

                if (delete_appointment == 1)
                {
                    System.out.println("Appointment " + selected_appointment_id + " " + table_appointment.getSelectionModel().getSelectedItem().getTitle() + " was deleted from the database");

                    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    Object scene = FXMLLoader.load(getClass().getResource("/views/appointment_main.fxml"));
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                }
            }
            catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    private void handler_button_return_main(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/main_menu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


    @FXML
    private void handler_button_return_customer(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/customer_main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


    private void table_appointment_fill() {
        try
        {

            LocalDateTime filter_start = LocalDateTime.now();
            LocalDateTime filter_end = LocalDateTime.now();

            Statement statement = database_connector.getConnection().createStatement();
            ResultSet appointment_info = null;


            //appointment range selector
            if(radio_during_eternity.isSelected())
            {
                filter_start = filter_start.minusYears(1000);
                filter_end = filter_end.plusYears(1000);
            }
            else if (radio_during_month.isSelected())
            {
                filter_start = filter_start.minusMonths(1);
                filter_end = filter_end.plusMonths(1);
            }
            else
            {
                filter_start = filter_start.minusWeeks(1);
                filter_end =  filter_end.plusWeeks(1);
            }


            //range formatter
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String filter_start_str = filter_start.toString();
            filter_start_str = filter_start_str.replace("T", " ");
            filter_start_str = filter_start_str.substring(0,filter_start_str.indexOf(" "));

            String filter_end_str = filter_end.toString();
            filter_end_str = filter_end_str.replace("T", " ");
            filter_end_str = filter_end_str.substring(0,filter_end_str.indexOf(" "));

            if(filter_current_customer.equals("All"))
            {
                appointment_info = statement.executeQuery("SELECT customer.customerId, customer.customerName, appointment.contact, appointment.title, appointment.type, appointment.start, appointment.end" +
                        " FROM (appointment" +
                        " INNER JOIN customer ON appointment.customerId = customer.customerId)" +
                        " WHERE appointment.start BETWEEN CAST('" + filter_start_str + "'  AS DATE) AND CAST('" + filter_end_str + "'  AS DATE)" +
                        " ORDER BY customer.customerId");
            }
            else
            {
                appointment_info = statement.executeQuery("SELECT customer.customerId, customer.customerName, appointment.contact, appointment.title, appointment.type, appointment.start, appointment.end" +
                        " FROM (appointment" +
                        " INNER JOIN customer ON appointment.customerId = customer.customerId)" +
                        " WHERE customer.customerId = " + filter_current_customer +
                        " AND appointment.start BETWEEN CAST('" + filter_start_str + "' AS DATE) AND CAST('" + filter_end_str + "' AS DATE)" +
                        " ORDER BY customer.customerId");
            }


            ObservableList<appointment> table_fill = FXCollections.observableArrayList();

            while (appointment_info.next()) {
                appointment fill = new appointment();
                fill.id.set(appointment_info.getInt("customerId"));
                fill.name.set(appointment_info.getString("customerName"));
                fill.contact.set(appointment_info.getString("contact"));
                fill.title.set(appointment_info.getString("title"));
                fill.type.set(appointment_info.getString("type"));
                String start = appointment_info.getString("start");
                String end = appointment_info.getString("end");


                ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
                ZoneId UTCZoneID = ZoneId.of("UTC");

                start = start.substring(0, start.length() - 2);
                LocalDateTime start_date = LocalDateTime.parse(start, format);
                ZonedDateTime start_id = start_date.atZone(UTCZoneID);
                ZonedDateTime start_coverted = start_id.withZoneSameInstant(localZoneId);
                LocalDateTime start_time = start_coverted.toLocalDateTime();
                String start_str = start_time.toString();
                String start_final = start_str.replace("T", " ");

                fill.start.set(start_final);

                end = end.substring(0, end.length() - 2);
                LocalDateTime end_date = LocalDateTime.parse(end, format);
                ZonedDateTime end_id = end_date.atZone(UTCZoneID);
                ZonedDateTime end_coverted = end_id.withZoneSameInstant(localZoneId);
                LocalDateTime end_time = end_coverted.toLocalDateTime();
                String end_str = end_time.toString();
                String end_final = end_str.replace("T", " ");

                fill.end.set(end_final);

                table_fill.add(fill);
            }
            table_appointment.setItems(table_fill);

            //Lambda expression to fill tableview
            column_customer_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            column_customer_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            column_contact.setCellValueFactory(data -> data.getValue().contactProperty());
            column_title.setCellValueFactory(data -> data.getValue().titleProperty());
            column_type.setCellValueFactory(data -> data.getValue().typeProperty());
            column_start.setCellValueFactory(data -> data.getValue().startProperty());
            column_end.setCellValueFactory(data -> data.getValue().endProperty());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    @FXML
    private void handler_radio_eternity(ActionEvent event)
    {
        if(radio_during_eternity.isSelected())
        {
            table_appointment_fill();

            radio_during_month.setSelected(false);
            radio_during_week.setSelected(false);
        }
    }

    @FXML
    private void handler_radio_month(ActionEvent event)
    {
        if(radio_during_month.isSelected())
        {
            radio_during_eternity.setSelected(false);
            radio_during_week.setSelected(false);

            table_appointment_fill();
        }
    }

    @FXML
    private void handler_radio_week(ActionEvent event)
    {
        if(radio_during_week.isSelected())
        {
            radio_during_month.setSelected(false);
            radio_during_eternity.setSelected(false);

            table_appointment_fill();
        }
    }

    @FXML
    private void handler_combo_customer(ActionEvent event)
    {
        filter_current_customer = ((customer_fill.get((combo_customer_view.getSelectionModel().getSelectedIndex()))).split(" ", 2))[0];
        table_appointment_fill();
    }
}
