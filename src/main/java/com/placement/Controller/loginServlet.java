package com.placement.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.*;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/login")
public class loginServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/placement", "root", "root");
			out.println("<h1>hello i am a login servlet</h1>");
			if(con!= null) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
		
			PreparedStatement psmt = con.prepareStatement("select * from login where username =? and password = ?");
			psmt.setString(1, username);
			psmt.setString(2, password);
			ResultSet rs = psmt.executeQuery();
			if(rs.next()) {
				HttpSession session = request.getSession(true);
				session.setAttribute("student_id", rs.getString("student_id"));
				out.println(session.getAttribute("student_id"));
				response.sendRedirect("student");
			}
			else {
				response.sendRedirect("login.html");
			}
			
			}
			else {
				out.println("error occured");
			}
		}
		catch(Exception ex) {
			out.println("error is : "+ ex);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
