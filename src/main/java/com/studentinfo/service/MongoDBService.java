package com.studentinfo.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.studentinfo.exception.StudentInfoException;
import com.studentinfo.model.Attendance;
import com.studentinfo.model.Grade;
import com.studentinfo.model.Student;

// Main service class handling all MongoDB operations for student information system
public class MongoDBService implements DatabaseService {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> studentsCollection;
    private final MongoCollection<Document> gradesCollection;
    private final MongoCollection<Document> attendanceCollection;

    public MongoDBService(String connectionString) {
        try {
            this.mongoClient = MongoClients.create(connectionString);
            this.database = mongoClient.getDatabase("studentInfo");
            this.studentsCollection = database.getCollection("students");
            this.gradesCollection = database.getCollection("grades");
            this.attendanceCollection = database.getCollection("attendance");
        } catch (Exception e) {
            throw new StudentInfoException("Failed to connect to MongoDB", e);
        }
    }

    @Override
    public void createStudent(Student student) {
        Document doc = new Document()
                .append("_id", student.getId())
                .append("firstName", student.getFirstName())
                .append("lastName", student.getLastName())
                .append("email", student.getEmail());
        studentsCollection.insertOne(doc);
    }

    @Override
    public void updateStudent(Student student) {
        Document update = new Document()
                .append("firstName", student.getFirstName())
                .append("lastName", student.getLastName())
                .append("email", student.getEmail());
        studentsCollection.updateOne(
                Filters.eq("_id", student.getId()),
                Updates.combine(Updates.set("firstName", student.getFirstName()),
                        Updates.set("lastName", student.getLastName()),
                        Updates.set("email", student.getEmail()))
        );
    }

    @Override
    public void deleteStudent(String studentId) {
        studentsCollection.deleteOne(Filters.eq("_id", studentId));
        gradesCollection.deleteMany(Filters.eq("studentId", studentId));
        attendanceCollection.deleteMany(Filters.eq("studentId", studentId));
    }

    @Override
    public Student getStudent(String studentId) {
        Document doc = studentsCollection.find(Filters.eq("_id", studentId)).first();
        if (doc == null) return null;
        return new Student(
                doc.getString("_id"),
                doc.getString("firstName"),
                doc.getString("lastName"),
                doc.getString("email")
        );
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        studentsCollection.find().forEach(doc -> 
            students.add(new Student(
                doc.getString("_id"),
                doc.getString("firstName"),
                doc.getString("lastName"),
                doc.getString("email")
            ))
        );
        return students;
    }

    @Override
    public void createGrade(Grade grade) {
        Document doc = new Document()
                .append("_id", grade.getId())
                .append("subject", grade.getSubject())
                .append("score", grade.getScore())
                .append("date", grade.getDate().toString())
                .append("studentId", grade.getStudentId());
        gradesCollection.insertOne(doc);
    }

    @Override
    public void updateGrade(Grade grade) {
        gradesCollection.updateOne(
                Filters.eq("_id", grade.getId()),
                Updates.combine(
                        Updates.set("subject", grade.getSubject()),
                        Updates.set("score", grade.getScore()),
                        Updates.set("date", grade.getDate().toString())
                )
        );
    }

    @Override
    public void deleteGrade(String gradeId) {
        gradesCollection.deleteOne(Filters.eq("_id", gradeId));
    }

    @Override
    public List<Grade> getStudentGrades(String studentId) {
        List<Grade> grades = new ArrayList<>();
        gradesCollection.find(Filters.eq("studentId", studentId)).forEach(doc ->
            grades.add(new Grade(
                doc.getString("_id"),
                doc.getString("subject"),
                doc.getDouble("score"),
                java.time.LocalDate.parse(doc.getString("date")),
                doc.getString("studentId")
            ))
        );
        return grades;
    }

    @Override
    public void createAttendance(Attendance attendance) {
        Document doc = new Document()
                .append("_id", attendance.getId())
                .append("date", attendance.getDate().toString())
                .append("present", attendance.isPresent())
                .append("studentId", attendance.getStudentId());
        attendanceCollection.insertOne(doc);
    }

    @Override
    public void updateAttendance(Attendance attendance) {
        attendanceCollection.updateOne(
                Filters.eq("_id", attendance.getId()),
                Updates.combine(
                        Updates.set("date", attendance.getDate().toString()),
                        Updates.set("present", attendance.isPresent())
                )
        );
    }

    @Override
    public void deleteAttendance(String attendanceId) {
        attendanceCollection.deleteOne(Filters.eq("_id", attendanceId));
    }

    @Override
    public List<Attendance> getStudentAttendance(String studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        attendanceCollection.find(Filters.eq("studentId", studentId)).forEach(doc ->
            attendanceList.add(new Attendance(
                doc.getString("_id"),
                java.time.LocalDate.parse(doc.getString("date")),
                doc.getBoolean("present"),
                doc.getString("studentId")
            ))
        );
        return attendanceList;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
} 