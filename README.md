A Java-based desktop application for managing student information, grades, and attendance.

## Features

- **Student Management**: Add, edit, and delete student records
- **Grade Tracking**: Record and view student grades
- **Attendance Monitoring**: Track student attendance
- **Modern UI**: Clean and intuitive user interface

- Java 17
- Swing for UI
- MongoDB for data storage
- Gradle for build management

## UML Diagram

```
+----------------+       +----------------+       +----------------+
|    Student     |       |     Grade      |       |   Attendance   |
+----------------+       +----------------+       +----------------+
| - id: String   |       | - id: String   |       | - id: String   |
| - firstName    |       | - subject      |       | - date         |
| - lastName     |       | - score        |       | - present      |
| - email        |       | - date         |       | - studentId    |
| - grades       |<>-----| - studentId    |       +----------------+
| - attendance   |       +----------------+              ^
+----------------+                                       |
       ^                                                 |
       |                                                 |
+----------------+                                       |
| DatabaseService|                                       |
+----------------+                                       |
       ^                                                 |
       |                                                 |
+----------------+                                       |
| MongoDBService |                                       |
+----------------+                                       |
                                                         |
+----------------+                                       |
|     Main       |--------------------------------------+
+----------------+
```


