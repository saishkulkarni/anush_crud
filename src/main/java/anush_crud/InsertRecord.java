package anush_crud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/insert")
public class InsertRecord extends HttpServlet {

	Connection connection = null;

	@Override
	public void init() throws ServletException {
		loadClass();
		establishConnection();
		createTable();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ecode = req.getParameter("ecode");
		String ename = req.getParameter("ename");
		String eaddress = req.getParameter("eaddress");
		Date edob = Date.valueOf(req.getParameter("edob"));
		long ephone = Long.parseLong(req.getParameter("ephone"));

		try {
			PreparedStatement preparedStatement = connection.prepareStatement("insert into employee values(?,?,?,?,?)");
			preparedStatement.setString(1, ecode);
			preparedStatement.setString(2, ename);
			preparedStatement.setString(3, eaddress);
			preparedStatement.setDate(4, edob);
			preparedStatement.setLong(5, ephone);
			preparedStatement.execute();

			resp.getWriter().print("<h1>Data Inserted Success</h1>");
			req.getRequestDispatcher("index.html").include(req, resp);

		} catch (SQLException e) {
			resp.getWriter().print("<h1>Data Already exists</h1>");
			req.getRequestDispatcher("index.html").include(req, resp);
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
