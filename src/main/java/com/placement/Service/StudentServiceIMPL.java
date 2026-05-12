package com.placement.Service;

import com.placement.Model.StudentModel;
import com.placement.Repository.DBconfig;
import com.placement.Repository.StudentRepositoryIMPL;

public class StudentServiceIMPL implements StudentService {
	
	StudentRepositoryIMPL studentrepo =  new StudentRepositoryIMPL();
	
	public boolean isAddStudent(StudentModel studModel) {
		return studentrepo.isAddStudent(studModel);
	}
}
