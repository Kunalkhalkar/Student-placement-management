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

@WebServlet("/student")
public class student extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		RequestDispatcher rd = request.getRequestDispatcher("nav.html");
		rd.include(request, response);
		HttpSession session = request.getSession();

		// this piece of code ensures the user is logged in or not
		if (!(Helper.validateLogin(request, response))) {
			response.sendRedirect("login.html");
			return;
		}

		out.println("""
				<!DOCTYPE html>
				<html>
				<head>
				    <title>Student Dashboard</title>
				    <meta name="viewport" content="width=device-width, initial-scale=1">

				    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

				    <link rel="stylesheet"
				    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
					<link rel="stylesheet" href="css/style.css">
				    
				</head>
				<body>

				<div class="container-fluid">
				    <div class="row">

				        <div class="col-md-3 sidebar">
				            <h3 class="text-center mb-4">
				                <i class="fa-solid fa-user-graduate"></i>
				                Student Panel
				            </h3>

				            <a href="#"><i class="fa-solid fa-house"></i> Dashboard</a>
				            <a href="#"><i class="fa-solid fa-user"></i> Profile</a>
				            <a href="#"><i class="fa-solid fa-book"></i> Education</a>
				            <a href="#"><i class="fa-solid fa-code"></i> Skills</a>
				            <a href="#"><i class="fa-solid fa-briefcase"></i> Available Jobs</a>
				            <a href="#"><i class="fa-solid fa-file"></i> Applied Jobs</a>
				            <a href="#"><i class="fa-solid fa-chart-line"></i> Placement Status</a>
				            <a href="LogoutServlet"><i class="fa-solid fa-right-from-bracket"></i> Logout</a>
				        </div>

				        <div class="col-md-9 p-4">

				            <h2>Welcome </h2>
				            <p class="text-muted">Placement Tracking Dashboard</p>

				            <div class="row mt-4">

				                <div class="col-md-3">
				                    <div class="card shadow p-3 text-center">
				                        <h5>12</h5>
				                        <p>Available Jobs</p>
				                    </div>
				                </div>

				                <div class="col-md-3">
				                    <div class="card shadow p-3 text-center">
				                        <h5>4</h5>
				                        <p>Applied Jobs</p>
				                    </div>
				                </div>

				                <div class="col-md-3">
				                    <div class="card shadow p-3 text-center">
				                        <h5>2</h5>
				                        <p>Interviews</p>
				                    </div>
				                </div>

				                <div class="col-md-3">
				                    <div class="card shadow p-3 text-center">
				                        <h5>1</h5>
				                        <p>Selected</p>
				                    </div>
				                </div>

				            </div>

				            <div class="card shadow mt-5 p-4">
				                <h4>Recent Job Openings</h4>

				                <table class="table mt-3">
				                    <thead>
				                        <tr>
				                            <th>Company</th>
				                            <th>Role</th>
				                            <th>Location</th>
				                            <th>Status</th>
				                        </tr>
				                    </thead>

				                    <tbody>
				                        <tr>
				                            <td>TCS</td>
				                            <td>Software Engineer</td>
				                            <td>Pune</td>
				                            <td><span class="badge bg-success">Open</span></td>
				                        </tr>

				                        <tr>
				                            <td>Infosys</td>
				                            <td>System Engineer</td>
				                            <td>Bangalore</td>
				                            <td><span class="badge bg-primary">Applied</span></td>
				                        </tr>
				                    </tbody>
				                </table>
				            </div>

				        </div>

				    </div>
				</div>

				</body>
				</html>
				""");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
