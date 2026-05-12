package com.placement.Repository;
import java.sql.*;

public class DBconfig {
	protected Connection conn;
	protected PreparedStatement psmt;
	protected PreparedStatement psmtlogin;
	protected PreparedStatement psmteducation;
	
	
	public DBconfig(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/placement", "root", "root");
		}
		catch(Exception ex) {
			System.out.println("error is : "+ex);
		}
	}
}
