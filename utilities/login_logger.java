package utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class login_logger
{
	public static void  user_logger(String user, boolean in)
	{
		try
		{
			FileWriter file = new FileWriter("Login Logger.txt", true);
			PrintWriter log = new PrintWriter(file);
			LocalDateTime time = LocalDateTime.now();
			log.println(user + " access time: " + time);
			log.close();
			System.out.println(user + " written to log");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
