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
import models.customer;
import utilities.database_connector;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class customer_main_controller implements Initializable {

    @FXML
    private TableView<customer> table_customer;
    @FXML
    private TableColumn<customer, Integer> column_id;
    @FXML
    private TableColumn<customer, String> column_name;
    @FXML
    private TableColumn<customer, String> column_address;
    @FXML
    private TableColumn<customer, String> column_city;
    @FXML
    private TableColumn<customer, String> column_country;
    @FXML
    private TableColumn<customer, String> column_postal;
    @FXML
    private TableColumn<customer, String> column_phone;
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
    private Button button_return_appointment;

    private ObservableList<customer> table_fill = FXCollections.observableArrayList();
    public static int selected_customer_id;


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

            ResultSet customer_info = statement.executeQuery("SELECT customer.customerId, customer.customerName, address.address, city.city, country.country, address.postalCode, address.phone" +
                    " FROM (((customer" +
                    " INNER JOIN address ON customer.addressId = address.addressId)" +
                    " INNER JOIN city ON address.cityId = city.cityId)" +
                    " INNER JOIN country ON city.countryId = country.countryId)" +
                    " ORDER BY customer.customerId");


            while(customer_info.next())
            {
                customer fill = new customer();
                fill.id.set(customer_info.getInt("customerId"));
                fill.name.set(customer_info.getString("customerName"));
                fill.address.set(customer_info.getString("address"));
                fill.city.set(customer_info.getString("city"));
                fill.country.set(customer_info.getString("country"));
                fill.postal_code.set(customer_info.getString("postalCode"));
                fill.phone.set(customer_info.getString("phone"));
                table_fill.add(fill);

            }
            table_customer.setItems(table_fill);

            //Lambda expression to fill tableview
            column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            column_address.setCellValueFactory(data -> data.getValue().addressProperty());
            column_city.setCellValueFactory(data -> data.getValue().cityProperty());
            column_country.setCellValueFactory(data -> data.getValue().countryProperty());
            column_postal.setCellValueFactory(data -> data.getValue().postal_codeProperty());
            column_phone.setCellValueFactory(data -> data.getValue().phoneProperty());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    @FXML
    private void handler_button_add(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/customer_add.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    private void handler_button_update(ActionEvent event) throws IOException
    {

        selected_customer_id = table_customer.getSelectionModel().getSelectedItem().getId();

        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/customer_update.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    private void handler_button_delete(ActionEvent event) throws IOException
    {

       selected_customer_id =  table_customer.getSelectionModel().getSelectedItem().getId();


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deleting customer " + selected_customer_id + " continue?");
        alert.setTitle("Customer Deletion");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            try
            {
                Statement statement = database_connector.getConnection().createStatement();

                ResultSet query = statement.executeQuery("SELECT addressId FROM customer where customerId = " + selected_customer_id);
                query.next();

                int delete_address_id = query.getInt("addressId");

                int delete_appointments = statement.executeUpdate("DELETE FROM appointment WHERE customerId = " + selected_customer_id);
                System.out.println("Customer " + selected_customer_id + " appointments where deleted from the database");

                int delete_customer = statement.executeUpdate("DELETE FROM customer WHERE customerId = " + selected_customer_id);
                System.out.println("Customer " + selected_customer_id + " was deleted from the database");

                int delete_address = statement.executeUpdate("DELETE FROM address WHERE addressId = " + delete_address_id);
                System.out.println("Address " + delete_address_id  + " address where deleted from the database");

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/views/customer_main.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();



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
    private void handler_button_return_appointment(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/appointment_main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    
}
