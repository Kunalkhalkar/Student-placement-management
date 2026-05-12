package com.placement.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import com.placement.Model.StudentModel;


@WebServlet("/StudentRegisterServlet")
public class StudentRegisterServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		//HttpSession session = request.getSession(); //no need to create session
		StudentModel student = new StudentModel();
		student.setFname(request.getParameter("fname"));
		student.setLname(request.getParameter("lname"));
		student.setEmail(request.getParameter("email"));
		student.setOemail(request.getParameter("optional_email"));
		student.setContact(request.getParameter("contact"));
		student.setUsername(request.getParameter("username"));
		student.setPassword(request.getParameter("password"));
		student.setSsc(Double.parseDouble(request.getParameter("ssc")));
		student.setHsc(Double.parseDouble(request.getParameter("hsc")));
		student.setDiploma(Double.parseDouble(request.getParameter("diploma")));
		student.setDbranch(request.getParameter("diploma_branch"));
		student.setGradbranch(request.getParameter("graduation_branch"));
		student.setGraduation(Double.parseDouble(request.getParameter("graduation")));
		student.setYearofgrad(Integer.parseInt(request.getParameter("graduation_year")));
		student.setPograduationbranch(request.getParameter("pgraduation_branch"));
		student.setPostgrad(Double.parseDouble(request.getParameter("setPostgrad")));
		//send this to the service layer.
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
