package com.studentinfo.model;

import java.time.LocalDate;
import java.util.Objects;

public class Grade {
    private String id;
    private String subject;
    private double score;
    private LocalDate date;
    private String studentId;

    public Grade(String id, String subject, double score, LocalDate date, String studentId) {
        this.id = id;
        this.subject = subject;
        this.score = score;
        this.date = date;
        this.studentId = studentId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Grade ID cannot be empty");
        }
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be empty");
        }
        this.subject = subject;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        this.score = score;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty");
        }
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", score=" + score +
                ", date=" + date +
                '}';
    }
} 