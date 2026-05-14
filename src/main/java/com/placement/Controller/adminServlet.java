package com.placement.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin")
public class adminServlet extends HttpServlet {

    static class ApplicationRow {
        String studentName;
        String company;
        String role;
        String status;

        ApplicationRow(String studentName, String company, String role, String status) {
            this.studentName = studentName;
            this.company = company;
            this.role = role;
            this.status = status;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession();
        if (session == null) {
            response.sendRedirect("login.html");
            return;
        }

        int totalStudents = 0;
        int totalCompanies = 0;
        int totalJobs = 0;
        int totalApplications = 0;
        int selectedStudents = 0;

        List<ApplicationRow> applications = new ArrayList<>();

        try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/placement", "root", "root");

            ResultSet rs1 = conn.prepareStatement("select count(*) from student").executeQuery();
            if (rs1.next()) totalStudents = rs1.getInt(1);

            ResultSet rs2 = conn.prepareStatement("select count(*) from company").executeQuery();
            if (rs2.next()) totalCompanies = rs2.getInt(1);

            ResultSet rs3 = conn.prepareStatement("select count(*) from job").executeQuery();
            if (rs3.next()) totalJobs = rs3.getInt(1);

            ResultSet rs4 = conn.prepareStatement("select count(*) from placement").executeQuery();
            if (rs4.next()) totalApplications = rs4.getInt(1);

            ResultSet rs5 = conn.prepareStatement("select count(*) from placement where status='Selected'").executeQuery();
            if (rs5.next()) selectedStudents = rs5.getInt(1);

            PreparedStatement ps = conn.prepareStatement(
                "select concat(s.fname,' ',s.lname), c.name, j.role, p.status " +
                "from placement p " +
                "join student s on p.student_id=s.student_id " +
                "join company c on p.company_id=c.company_id " +
                "join job j on p.job_id=j.job_id"
            );

            ResultSet rs6 = ps.executeQuery();
            while (rs6.next()) {
                applications.add(new ApplicationRow(
                    rs6.getString(1),
                    rs6.getString(2),
                    rs6.getString(3),
                    rs6.getString(4)
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        out.println("""
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<meta name='viewport' content='width=device-width, initial-scale=1'>
<title>Admin Dashboard</title>
<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>
<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css'>
<style>
body{background:#f5f7fb;font-family:Arial,sans-serif}
.sidebar-card,.main-card,.table-card{border:none;border-radius:22px;box-shadow:0 10px 30px rgba(0,0,0,.08)}
.metric-card{border-radius:20px;background:white;padding:20px;text-align:center;box-shadow:0 6px 18px rgba(0,0,0,.08)}
.btn-pill{border-radius:30px}
.sidebar-link{display:block;padding:12px 16px;border:1px solid #dee2e6;border-radius:30px;text-decoration:none;color:#0d6efd;margin-bottom:10px;text-align:center}
.sidebar-link:hover{background:#0d6efd;color:white}
</style>
</head>
<body>
<div class='container-fluid py-4 px-4'>
<div class='row g-4'>
""");

        out.println("<div class='col-lg-3'>");
        out.println("<div class='card sidebar-card p-4 text-center'>");
        out.println("<i class='fa-solid fa-user-shield fa-4x text-primary'></i>");
        out.println("<h3 class='fw-bold mt-3'>Admin Panel</h3>");
        out.println("<p class='text-muted'>Placement Management System</p>");
        out.println("<a href='#' class='sidebar-link'>Dashboard</a>");
        out.println("<a href='#' class='sidebar-link'>Students</a>");
        out.println("<a href='#' class='sidebar-link'>Companies</a>");
        out.println("<a href='#' class='sidebar-link'>Jobs</a>");
        out.println("<a href='#' class='sidebar-link'>Applications</a>");
        out.println("<a href='LogoutServlet' class='btn btn-danger btn-pill mt-3'>Logout</a>");
        out.println("</div></div>");

        out.println("<div class='col-lg-9'>");
        out.println("<div class='card main-card p-4'>");
        out.println("<h2 class='fw-bold'>Admin Dashboard</h2>");
        out.println("<p class='text-muted'>Manage placement activities</p>");

        out.println("<div class='row g-3 mb-4'>");
        out.println("<div class='col-md-2'><div class='metric-card'><h4>"+totalStudents+"</h4><small>Students</small></div></div>");
        out.println("<div class='col-md-2'><div class='metric-card'><h4>"+totalCompanies+"</h4><small>Companies</small></div></div>");
        out.println("<div class='col-md-2'><div class='metric-card'><h4>"+totalJobs+"</h4><small>Jobs</small></div></div>");
        out.println("<div class='col-md-3'><div class='metric-card'><h4>"+totalApplications+"</h4><small>Applications</small></div></div>");
        out.println("<div class='col-md-3'><div class='metric-card'><h4>"+selectedStudents+"</h4><small>Selected</small></div></div>");
        out.println("</div>");

        out.println("<div class='mb-4'>");
        out.println("<a href='AddCompanyServlet' class='btn btn-primary btn-pill me-2'><i class='fa-solid fa-plus'></i> Add Company</a>");
        out.println("<a href='AddJobServlet' class='btn btn-success btn-pill'><i class='fa-solid fa-briefcase'></i> Add Job</a>");
        out.println("</div>");

        out.println("<div class='card table-card p-4'>");
        out.println("<h4 class='fw-bold mb-3'>Applied Students</h4>");
        out.println("<table class='table table-hover'>");
        out.println("<thead><tr><th>Student</th><th>Company</th><th>Role</th><th>Status</th></tr></thead><tbody>");

        for (ApplicationRow row : applications) {
            out.println("<tr>");
            out.println("<td>" + row.studentName + "</td>");
            out.println("<td>" + row.company + "</td>");
            out.println("<td>" + row.role + "</td>");
            out.println("<td><span class='badge bg-primary'>" + row.status + "</span></td>");
            out.println("</tr>");
        }

        out.println("</tbody></table></div></div></div></div></body></html>");
    }
}

