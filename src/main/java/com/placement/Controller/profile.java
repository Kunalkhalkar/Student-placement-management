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
import com.placement.Helpers.Helper;

@WebServlet("/profile")
public class profile extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		RequestDispatcher rd = request.getRequestDispatcher("nav.html");
		rd.include(request, response);
		out.println("this is the student profile");
		Integer student_id = (Integer) session.getAttribute("student_id");
		
		if(!(Helper.validateLogin(request, response))) {
			response.sendRedirect("login.html");
			return;
		}
		
			out.println("you are on right track");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
