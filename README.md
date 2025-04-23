# Student Information System

A Java-based desktop application for managing student information, grades, and attendance.

## Features

- **Student Management**: Add, edit, and delete student records
- **Grade Tracking**: Record and view student grades
- **Attendance Monitoring**: Track student attendance
- **Modern UI**: Clean and intuitive user interface

## Technologies Used

- Java 17
- Swing for UI
- MongoDB for data storage
- Gradle for build management

## Getting Started

### Prerequisites

- Java 17 or higher
- MongoDB (local or Atlas)
- Gradle

### Installation

1. Clone the repository:

   ```
   git clone https://github.com/yourusername/student-info-system.git
   ```

2. Navigate to the project directory:

   ```
   cd student-info-system
   ```

3. Build the project:

   ```
   ./gradlew build
   ```

4. Run the application:
   ```
   ./gradlew run
   ```

## Configuration

The application connects to MongoDB. You can configure the connection in the `MongoDBService` class.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- This project was created as part of an Object-Oriented Programming course.

## Project Structure

### Model Classes

- `Student`: Represents a student with basic information
- `Grade`: Represents a student's grade in a subject
- `Attendance`: Represents a student's attendance record

### Service Classes

- `DatabaseService`: Interface defining database operations
- `MongoDBService`: MongoDB implementation of the database service

### GUI

The application uses Java Swing for the graphical user interface, providing:

- Student management (add, edit, delete)
- Grade management
- Attendance tracking
- Data viewing capabilities

## Business Rules

1. Student ID cannot be empty
2. Student first and last names cannot be empty
3. Student email must contain '@' symbol
4. Grade scores must be between 0 and 100
5. Attendance dates cannot be null
6. All IDs must be unique

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

## Inheritance and Interfaces

- `Student` implements `Comparable<Student>` for sorting
- `DatabaseService` interface is implemented by `MongoDBService`
- `StudentInfoException` extends `RuntimeException`

## Error Handling

The application includes custom exception handling through the `StudentInfoException` class, which is used to handle various error scenarios such as:

- Invalid input data
- Database connection issues
- Data validation failures

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
