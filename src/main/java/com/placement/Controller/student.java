package com.placement.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
import java.sql.*;

@WebServlet("/student")
public class student extends HttpServlet {

    static class JobCard {
        String company;
        String role;
        String location;
        String pkg;
        JobCard(String company, String role, String location, String pkg) {
            this.company = company;
            this.role = role;
            this.location = location;
            this.pkg = pkg;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student_id") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Integer studentId = (Integer) session.getAttribute("student_id");

        String fullName = "Student";
        String branch = "Computer Engineering";
        double graduation = 0;
        int availableJobs = 0;
        int eligibleJobs = 0;
        int appliedJobs = 0;
        int selectedJobs = 0;
        List<JobCard> eligibleList = new ArrayList<>();
        List<JobCard> appliedList = new ArrayList<>();

        try {
        	
        		Class.forName("com.mysql.cj.jdbc.Driver");
        	  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/placement", "root", "root");

            PreparedStatement ps1 = conn.prepareStatement(
                "select s.fname, s.lname, e.graduation_branch, e.graduation from student s join education e on s.student_id=e.student_id where s.student_id=?"
            );
            ps1.setInt(1, studentId);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                fullName = rs1.getString("fname") + " " + rs1.getString("lname");
                branch = rs1.getString("graduation_branch");
                graduation = rs1.getDouble("graduation");
            }

            ResultSet rs2 = conn.prepareStatement("select count(*) from job").executeQuery();
            if (rs2.next()) availableJobs = rs2.getInt(1);

            PreparedStatement ps3 = conn.prepareStatement(
                "select count(*) from job j join criteria c on j.job_id=c.job_id join education e on e.student_id=? where (c.min_ssc is null or e.ssc >= c.min_ssc) and (c.min_hsc is null or e.hsc >= c.min_hsc) and (c.min_diploma is null or e.diploma >= c.min_diploma) and (c.min_grad is null or e.graduation >= c.min_grad)"
            );
            ps3.setInt(1, studentId);
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) eligibleJobs = rs3.getInt(1);

            PreparedStatement ps4 = conn.prepareStatement("select count(*) from placement where student_id=?");
            ps4.setInt(1, studentId);
            ResultSet rs4 = ps4.executeQuery();
            if (rs4.next()) appliedJobs = rs4.getInt(1);

            PreparedStatement ps5 = conn.prepareStatement("select count(*) from placement where student_id=? and status='Selected'");
            ps5.setInt(1, studentId);
            ResultSet rs5 = ps5.executeQuery();
            if (rs5.next()) selectedJobs = rs5.getInt(1);

            PreparedStatement ps6 = conn.prepareStatement(
                "select c.name, j.role, j.location, j.package from job j join company c on j.company_id=c.company_id join criteria cr on j.job_id=cr.job_id join education e on e.student_id=? where (cr.min_ssc is null or e.ssc >= cr.min_ssc) and (cr.min_hsc is null or e.hsc >= cr.min_hsc) and (cr.min_diploma is null or e.diploma >= cr.min_diploma) and (cr.min_grad is null or e.graduation >= cr.min_grad) limit 2"
            );
            ps6.setInt(1, studentId);
            ResultSet rs6 = ps6.executeQuery();
            while (rs6.next()) {
                eligibleList.add(new JobCard(rs6.getString(1), rs6.getString(2), rs6.getString(3), rs6.getString(4)));
            }

            PreparedStatement ps7 = conn.prepareStatement(
                "select c.name, j.role, j.location, j.package from placement p join job j on p.job_id=j.job_id join company c on p.company_id=c.company_id where p.student_id=? limit 4"
            );
            ps7.setInt(1, studentId);
            ResultSet rs7 = ps7.executeQuery();
            while (rs7.next()) {
                appliedList.add(new JobCard(rs7.getString(1), rs7.getString(2), rs7.getString(3), rs7.getString(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        out.println(studentId);
        out.println("""
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<meta name='viewport' content='width=device-width, initial-scale=1'>
<title>Student Dashboard</title>
<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>
<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css'>
<style>
body{background:#f5f7fb;font-family:Arial,sans-serif}
.profile-card,.main-card,.side-card,.job-card{border:none;border-radius:22px;box-shadow:0 10px 30px rgba(0,0,0,.08)}
.profile-img{width:100px;height:100px;border-radius:50%;border:4px solid #28a745;object-fit:cover}
.metric-card{border-radius:20px;background:white;padding:20px;text-align:center;box-shadow:0 6px 18px rgba(0,0,0,.08)}
.job-card{padding:25px;background:white;min-height:320px}
.company-logo{width:55px;height:55px;border-radius:12px;background:#eef2ff;display:flex;justify-content:center;align-items:center;font-weight:bold}
.skill-badge{background:#eef2ff;color:#0d6efd;padding:8px 14px;border-radius:20px;font-size:13px;margin:3px;display:inline-block}
.side-article img{border-radius:16px 16px 0 0;height:150px;object-fit:cover;width:100%}
.btn-pill{border-radius:30px}
</style>
</head>
<body>
<div class='container-fluid py-4 px-4'><div class='row g-4'>
""");

        out.println("<div class='col-lg-3'><div class='card profile-card p-4 text-center'><img src='https://i.pravatar.cc/150?img=12' class='profile-img mx-auto'><h4 class='mt-4 fw-bold'>"+fullName+"</h4><p class='mb-1'>"+branch+"</p><p class='text-muted small'>Graduation: "+graduation+"%</p><button class='btn btn-primary btn-pill px-4 mt-2'>Complete Profile</button><div class='card mt-4 p-3 border-0 bg-light'><h5 class='fw-bold'>Profile Performance</h5><div class='row mt-3'><div class='col-6'><h3 class='text-primary fw-bold'>"+appliedJobs+"</h3><small>Applications</small></div><div class='col-6'><h3 class='text-success fw-bold'>"+eligibleJobs+"</h3><small>Eligible</small></div></div></div><div class='mt-4 d-grid gap-2'><button class='btn btn-outline-primary btn-pill'>My Profile</button><button class='btn btn-outline-primary btn-pill'>Education</button><button class='btn btn-outline-primary btn-pill'>Skills</button><button class='btn btn-outline-primary btn-pill'>Applied Jobs</button><a href='LogoutServlet' class='btn btn-danger btn-pill'>Logout</a></div></div></div>");

        out.println("<div class='col-lg-6'><div class='card main-card p-4'><div><h2 class='fw-bold'>Welcome, "+fullName+"</h2><p class='text-muted'>Placement Tracking Dashboard</p></div><div class='row g-3 mb-4'><div class='col-md-3'><div class='metric-card'><h3 class='fw-bold text-primary'>"+availableJobs+"</h3><small>Available Jobs</small></div></div><div class='col-md-3'><div class='metric-card'><h3 class='fw-bold text-success'>"+eligibleJobs+"</h3><small>Eligible Jobs</small></div></div><div class='col-md-3'><div class='metric-card'><h3 class='fw-bold text-warning'>"+appliedJobs+"</h3><small>Applied</small></div></div><div class='col-md-3'><div class='metric-card'><h3 class='fw-bold text-danger'>"+selectedJobs+"</h3><small>Selected</small></div></div></div><div class='row g-4'>");

        for (JobCard job : eligibleList) {
            out.println("<div class='col-md-6'><div class='job-card'><h4 class='fw-bold'>"+job.role+"</h4><p class='text-muted'>"+job.company+" / "+job.location+"</p><h5 class='mt-4'>"+job.pkg+" LPA</h5><div class='mt-4'><span class='skill-badge'>Career</span><span class='skill-badge'>Opportunity</span></div><div class='d-flex gap-2 mt-4'><div class='company-logo'>"+job.company.charAt(0)+"</div></div><button class='btn btn-primary btn-pill mt-4 w-100'>Apply Now</button></div></div>");
        }
        out.println("</div></div></div>");

        out.println("<div class='col-lg-3'><div class='card side-card p-0 side-article overflow-hidden'><img src='https://images.unsplash.com/photo-1521737604893-d14cc237f11d'><div class='p-3'><h5 class='fw-bold'>Applied Jobs</h5>");
        for (JobCard job : appliedList) {
            out.println("<p class='border-bottom pb-2'><strong>"+job.role+"</strong><br>"+job.company+"</p>");
        }
        out.println("</div></div></div></div></div></body></html>");
    }
}
