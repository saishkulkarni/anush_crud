package anush_crud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delete")
public class DeleteRecord extends HttpServlet {

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
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("delete from employee where ecode=?");
			preparedStatement.setString(1, ecode);
			int rows = preparedStatement.executeUpdate();
			if (rows > 0)
				resp.getWriter().print("<h1>Record Deleted Success</h1>");
			else
				resp.getWriter().print("<h1>Record Not Deleted, Code Does not match Any	</h1>");
			req.getRequestDispatcher("index.html").include(req, resp);

		} catch (SQLException e) {
			resp.getWriter().print("<h1>Record Not Deleted</h1>");
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
