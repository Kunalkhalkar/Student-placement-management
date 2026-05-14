package com.placement.Repository;

import java.sql.*;

import com.placement.Model.StudentModel;

public class StudentRepositoryIMPL extends DBconfig implements StudentRepository {
		
	public boolean isAddStudent(StudentModel studentModel) {
		
		try {
			conn.setAutoCommit(false);
			
			psmt = conn.prepareStatement("insert into student(fname, lname, email, optional_email, contact) values( ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			psmt.setString(1, studentModel.getFname());
			psmt.setString(2, studentModel.getLname());
			psmt.setString(3, studentModel.getEmail());
			psmt.setString(4, studentModel.getOemail());
			psmt.setString(5, studentModel.getContact());
				psmt.executeUpdate();
				
				ResultSet rs = psmt.getGeneratedKeys(); //Fetches the auto-generated key after insert. here used for fetching student_id
				int student_id= 0;
			
				if(rs.next()) {
					student_id = rs.getInt(1);
				}
				
				String loginquery = "insert into login(username, password, student_id) values( ?, ?, ?)";
				psmtlogin = conn.prepareStatement(loginquery);
				psmtlogin.setString(1, studentModel.getUsername());
				psmtlogin.setString(2, studentModel.getPassword());
				psmtlogin.setInt(3, student_id);
			
				psmtlogin.executeUpdate();//login query executed
				
				String eduactionQuery = "insert into education(student_id, ssc, hsc, diploma, diploma_branch, graduation, graduation_branch, graduation_year, pograduation_branch, post_graduation) values(?,?, ?, ?, ?, ?, ?, ?, ?, ?)";
				psmteducation = conn.prepareStatement(eduactionQuery);
				psmteducation.setInt(1, student_id);
				psmteducation.setDouble(2, studentModel.getSsc());
				psmteducation.setDouble(3, studentModel.getHsc());
				psmteducation.setDouble(4, studentModel.getDiploma());
				psmteducation.setString(5, studentModel.getDbranch());
				psmteducation.setDouble(6, studentModel.getGraduation());
				psmteducation.setString(7, studentModel.getGradbranch());
				psmteducation.setInt(8, studentModel.getYearofgrad());
				psmteducation.setString(9, studentModel.getPograduationbranch());
				psmteducation.setDouble(10, studentModel.getPostgrad());
				
				int value = psmteducation.executeUpdate();
				conn.commit();
			return value > 0 ? true : false;
			
		}catch(Exception ex) {
			System.out.println("error is :"+ ex);
				try {
				conn.rollback();
				}
				catch(Exception e) {
					System.out.println("Error is : " + e);
				}
			return false;
		}
	}
	public StudentModel getStudent() {
		return;
	}
}
