package com.studentinfo.service;

import java.util.List;

import com.studentinfo.model.Attendance;
import com.studentinfo.model.Grade;
import com.studentinfo.model.Student;

// Interface defining all database operations for the student information system
public interface DatabaseService {
    // Student operations
    void createStudent(Student student);
    void updateStudent(Student student);
    void deleteStudent(String studentId);
    Student getStudent(String studentId);
    List<Student> getAllStudents();

    // Grade operations
    void createGrade(Grade grade);
    void updateGrade(Grade grade);
    void deleteGrade(String gradeId);
    List<Grade> getStudentGrades(String studentId);

    // Attendance operations
    void createAttendance(Attendance attendance);
    void updateAttendance(Attendance attendance);
    void deleteAttendance(String attendanceId);
    List<Attendance> getStudentAttendance(String studentId);
} 