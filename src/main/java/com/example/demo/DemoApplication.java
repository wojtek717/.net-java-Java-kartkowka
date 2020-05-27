package com.example.demo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.net.ssl.HttpsURLConnection;
import java.io.Console;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.sql.*;

/*
Autor: Wojciech Konury 241488
Wykonano: 27.05.2020 @ 17:30
*/

class Entry {
	int pk;
	String title;
	String submitter;
}

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		String json = "";
		Gson gson = new Gson();

		try {
			URL url = new URL("https://api.e-science.pl/api/azon/entry/432/");
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Api-Key", "XtdKZDAHckEZ7zhukQ1vKLeYQFbZzoMH1Y5Vgt6F");
			con.setRequestProperty("accept", "application/json");

			try(Scanner in = new Scanner(con.getInputStream())){
				while (in.hasNextLine()){
					json = in.nextLine();
					System.out.println(json);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Entry entry = gson.fromJson(json, Entry.class);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection(
					"jdbc:mysql://localhost:8889/zasoby","admin","admin");

			PreparedStatement preparedStatement = con.prepareStatement("insert into entries (pk, title, submitter) values (?, ?, ?)");
			preparedStatement.setString(1, (Integer.toString(entry.pk)));
			preparedStatement.setString(2, ("'" + entry.title + "'"));
			preparedStatement.setString(3, ("'" + entry.submitter + "'"));

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
