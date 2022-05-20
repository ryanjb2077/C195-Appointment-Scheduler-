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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.database_connector;
import utilities.login_logger;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class login_screen_controller implements Initializable {

    @FXML
    private TextField field_username;
    @FXML
    private TextField field_password;
    @FXML
    private Button login_button;
    @FXML
    private Label label_username;
    @FXML
    private Label label_password;
    @FXML
    private Label text_credentials;
    @FXML
    private Label text_welcome;
    @FXML
    private Label text_appointments;

    private static String current_user;


    public static Locale get_locale()
    {
        return Locale.getDefault();
    }

    Locale[] locale_languages =
            {
                    Locale.ENGLISH,
                    Locale.ITALIAN
            };


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ResourceBundle user_language;
        Locale current = get_locale();
        user_language = ResourceBundle.getBundle("language", current);

        text_welcome.setText(user_language.getString("welcome"));
        text_appointments.setText(user_language.getString("appointments"));
        text_credentials.setText(user_language.getString("credentials"));
        label_username.setText(user_language.getString("username"));
        label_password.setText(user_language.getString("password"));
    }


    @FXML
    private void handler_button_login(ActionEvent event) throws IOException
    {

        String username = field_username.getText();
        String password = field_password.getText();
        Locale tester = get_locale();

        boolean correct_credentials = login_attempt(username, password);

        if(correct_credentials)
        {
            current_user = username.substring(0, 1).toUpperCase() + username.substring(1);

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/views/main_menu.fxml"));
            stage.setScene(new Scene((Parent)scene));
            stage.show();


            login_logger.user_logger(username, true);
        }
        else
        {
            if(get_locale().toString().equals("en_US"))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect username or password");
                alert.showAndWait();

                login_logger.user_logger(username, false);
            }
            if(get_locale().toString().equals("it_IT"))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "nome utente o password corretti");
                alert.showAndWait();

                login_logger.user_logger(username, false);
            }
        }
    }


    public static Boolean login_attempt(String username, String password)
    {
        try
        {
            Statement statement = database_connector.getConnection().createStatement();

            ResultSet query = statement.executeQuery("SELECT * FROM user WHERE userName ='" + username + "' AND password = '" + password + "'");

            if (!query.isBeforeFirst() )
            {
                return false;
            }
            else
                {
                return true;
            }
        }
        catch (Exception except)
        {
            System.out.println(except.getMessage());
            return false;
        }
    }


public Locale get_user_locale()
{
    return Locale.getDefault();
}

public static String get_current_user()
{
    return current_user;
}

}