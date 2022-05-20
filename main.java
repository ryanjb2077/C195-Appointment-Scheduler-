
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.database_connector;


public class main extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("/views/login_screen.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Scheduler Login In");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		database_connector.connection_open();
		launch(args);
		database_connector.connection_close();
	}

}

