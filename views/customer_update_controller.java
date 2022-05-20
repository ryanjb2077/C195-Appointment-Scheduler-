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
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class customer_update_controller implements Initializable {

    @FXML
    private Label label_current_user;
    @FXML
    private TextField field_address;
    @FXML
    private ComboBox<String> combo_city;
    @FXML
    private Button button_cancel;
    @FXML
    private Button button_save;
    @FXML
    private TextField field_name;
    @FXML
    private TextField field_postal;
    @FXML
    private TextField field_number;
    @FXML
    private Label label_country;

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

            ResultSet city_list = statement.executeQuery("SELECT city FROM city");
            ObservableList<String> city_fill = FXCollections.observableArrayList();

            while(city_list.next())
            {
                String current = city_list.getString("city");
                city_fill.add(current);
            }

            combo_city.setItems(city_fill);


            ResultSet customer_info = statement.executeQuery("SELECT customer.customerName, address.address, city.city, country.country, address.postalCode, address.phone" +
                    " FROM (((customer" +
                    " INNER JOIN address ON customer.addressId = address.addressId)" +
                    " INNER JOIN city ON address.cityId = city.cityId)" +
                    " INNER JOIN country ON city.countryId = country.countryId)" +
                    " Where customer.customerId = " + customer_main_controller.selected_customer_id);

            while(customer_info.next())
            {
                field_name.setText(customer_info.getString("customerName"));
                field_address.setText(customer_info.getString("address"));
                combo_city.setValue((customer_info.getString("city")));
                label_country.setText(customer_info.getString("country"));
                field_postal.setText(customer_info.getString("postalCode"));
                field_number.setText(customer_info.getString("phone"));
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }


    }

    @FXML
    private void handler_combo_city(javafx.event.ActionEvent event)
    {
        try
        {
            Statement statement = database_connector.getConnection().createStatement();
            String customer_city = combo_city.getSelectionModel().getSelectedItem();

            ResultSet city_query_result = statement.executeQuery("SELECT country.country from city INNER JOIN country ON city.countryId=country.countryId where city.city='" + customer_city + "'");

            city_query_result.next();
            label_country.setText(city_query_result.getString("country"));
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    @FXML
    private void handler_button_save(javafx.event.ActionEvent event) throws IOException
    {
        Integer customer_city_id = combo_city.getSelectionModel().getSelectedIndex() + 1;



        String customer_name = field_name.getText();
        String customer_address = field_address.getText();
        String customer_city = combo_city.getSelectionModel().getSelectedItem();
        String customer_country = label_country.getText();
        String customer_postal_code = field_postal.getText();
        String customer_phone_number = field_number.getText();

        if ((customer_add_controller.valid_name(customer_name) && customer_add_controller.valid_address(customer_address)
                && customer_add_controller.valid_postal(customer_postal_code) && customer_add_controller.valid_phone(customer_phone_number)))
        {
            try
            {

                Statement connection = database_connector.getConnection().createStatement();

                ResultSet query = connection.executeQuery("SELECT addressId FROM customer where customerid = " + customer_main_controller.selected_customer_id);
                query.next();

                int update_address_id = query.getInt("addressId");

                String address_query = "UPDATE address SET"
                        + " address = '" + customer_address //address
                        + "', cityId = "  + customer_city_id  //cityId
                        + ", postalCode = '" + customer_postal_code //postal code
                        + "', phone = '" + customer_phone_number //phone
                        + "', lastUpdate = NOW()" //lastUpdate
                        + ", lastUpdateBy = '" + views.login_screen_controller.get_current_user()  // lastUpdateBy
                        + "' WHERE addressId = " + update_address_id ;

                int address_query_excute = connection.executeUpdate(address_query);

                if(address_query_excute == 1)
                {
                    String customer_query = "UPDATE customer SET"
                            + " customerName = '" + customer_name //customerName
                            + "', lastUpdate = NOW()" //lastUpdate
                            + ", lastUpdateBy = '" + views.login_screen_controller.get_current_user()  // lastUpdateBy
                            + "' WHERE addressId = " + update_address_id ;

                    int customer_query_excute = connection.executeUpdate(customer_query);

                    if(customer_query_excute == 1)
                    {
                        System.out.println("Updating customer " + customer_name + " was successful.");
                    }

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
            Object scene = FXMLLoader.load(getClass().getResource("/views/customer_main.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    @FXML
    private void handler_button_cancel(javafx.event.ActionEvent event) throws IOException
    {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/views/customer_main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


}
