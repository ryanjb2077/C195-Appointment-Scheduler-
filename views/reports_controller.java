package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import utilities.database_connector;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.ResourceBundle;



public class reports_controller implements Initializable
{
	@FXML
	private TextArea text_report_1;
	@FXML
	private TextArea text_report_2;
	@FXML
	private TextArea text_report_3;
	@FXML
	private Label label_current_user;
	@FXML
	private ComboBox<String> combo_consultant;

	public static String filter_consultant;
	public ObservableList<String> contact_fill = FXCollections.observableArrayList();



	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		label_current_user.setText(views.login_screen_controller.get_current_user());

		//report 1 total monthly
		try
		{
			Statement statement = database_connector.getConnection().createStatement();
			ResultSet querry_type = statement.executeQuery( "SELECT type, MONTHNAME(start) as month, COUNT(*) as total FROM appointment GROUP BY type, MONTH(START) ORDER BY month");

			StringBuilder report = new StringBuilder();

			report.append(String.format("%1$-30s %2$-30s %3$s \n", "Month", "Appointment Type", "Total"));
			report.append(String.join("", Collections.nCopies(163, "-")));
			report.append("\n");

			while(querry_type.next())
			{
				String month = querry_type.getString("Month");
				String type = querry_type.getString("type");
				int total = querry_type.getInt("Total");

				report.append(String.format("%1$-" + (30) +"s %2$-" +(30) + "s %3$s \n", month, type,total ));
			}

			text_report_1.setText(report.toString());

		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		//contact filler for report 2
		try
		{
			Statement statement = database_connector.getConnection().createStatement();
			ResultSet contact_list = statement.executeQuery("SELECT userId ,userName FROM user");

			while(contact_list.next())
			{
				String current_id = contact_list.getString("userId");
				String current_name = contact_list.getString("userName");
				contact_fill.add(current_id + " " + current_name);
			}
			combo_consultant.setItems(contact_fill);
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}

		//report 3 Appointments per location
		try
		{
			Statement statement = database_connector.getConnection().createStatement();
			ResultSet querry_type = statement.executeQuery( "SELECT location, COUNT(*) as total FROM appointment GROUP BY location ORDER BY location");

			StringBuilder report = new StringBuilder();

			report.append(String.format("%1$-30s %2$-30s \n", "Location", "Total"));
			report.append(String.join("", Collections.nCopies(108, "-")));
			report.append("\n");

			while(querry_type.next())
			{
				String location = querry_type.getString("location");
				int total = querry_type.getInt("total");

				report.append(String.format("%1$-" + (30) +"s %2$s \n", location, total));
			}

			text_report_3.setText(report.toString());

		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}
	}


	public void report_2()
	{
		text_report_2.clear();

		try {

			Statement statement = database_connector.getConnection().createStatement();
			String consultantQueryResults = "SELECT appointment.contact, appointment.title, customer.customerName, appointment.start, appointment.end FROM ((appointment JOIN customer ON customer.customerId = appointment.customerId) " +
					"JOIN user ON appointment.userId = user.userId) WHERE user.userId = " + filter_consultant;

			ResultSet query = statement.executeQuery(consultantQueryResults);

			StringBuilder report = new StringBuilder();
			report.append(String.format("%1$-20s %2$-20s %3$-20s %4$-20s %5$s \n", "Consultant", "Title", "Customer", "Start", "End"));
			report.append(String.join("", Collections.nCopies(108, "-")));
			report.append("\n");

			while(query.next())
			{
				String contact = query.getString("contact");
				String description = query.getString("title");
				String customer = query.getString("customerName");
				String start = query.getString("start");
				start = start.substring(0,16);
				String end = query.getString("end");
				end = end.substring(0,16);

				report.append(String.format("%1$-20s %2$-20s %3$-20s %4$-20s %5$s \n", contact,description,customer,start,end));
			}

			text_report_2.setText(report.toString());

		}
		catch(SQLException ex) {
			System.out.println("Error " + ex.getMessage());
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
	private void handler_combo_consultant(ActionEvent event)
	{
		filter_consultant = ((contact_fill.get((combo_consultant.getSelectionModel().getSelectedIndex()))).split(" ", 2))[0];
		report_2();
	}

}
