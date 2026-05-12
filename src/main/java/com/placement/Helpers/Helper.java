package com.placement.Helpers;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Helper {
	public static boolean validateLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Integer student_id = (Integer) session.getAttribute("student_id");
		return session != null || student_id != null ? true: false;
	}
}
