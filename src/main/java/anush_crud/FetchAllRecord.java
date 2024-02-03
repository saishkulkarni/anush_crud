package anush_crud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/fetchall")
public class FetchAllRecord extends HttpServlet {

	Connection connection = null;

	@Override
	public void init() throws ServletException {
		loadClass();
		establishConnection();
		createTable();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("select * from employee");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				resp.getWriter().print("<h1>Employee Code: " + resultSet.getString(1) + "</h1>");
				resp.getWriter().print("<h1>Employee Name: " + resultSet.getString(2) + "</h1>");
				resp.getWriter().print("<h1>Employee Address: " + resultSet.getString(3) + "</h1>");
				resp.getWriter().print("<h1>Employee Dob: " + resultSet.getDate(4) + "</h1>");
				resp.getWriter().print("<h1>Employee Phone: " + resultSet.getLong(5) + "</h1>");
			}
		} catch (SQLException e) {

		}

	}

	public void loadClass() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Class did not get Loaded");
		}
	}

	public void establishConnection() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/anush?createDatabaseIfNotExist=true",
					"root", "root");
		} catch (SQLException e) {
			System.out.println("Failed to establish Connection");
		}
	}

	public void createTable() {
		try {
			Statement statement = connection.createStatement();
			statement.execute(
					"create table employee(ecode varchar(50) primary key,ename varchar(50),eaddress varchar(50),edob date,ephone bigint)");
		} catch (SQLException e) {
			System.out.println("Table did not Create either it Exists or Query is Wrong");
		}
	}

	@Override
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
