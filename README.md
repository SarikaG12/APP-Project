# Student Registration Application

This Java project is a **Student Management Application** using **Swing GUI** and **MySQL database**.  
It allows users to **add, view, update, and delete student records**.

---

## Project Structure

- **StudentRegistrationForm.java**  
  - A Swing GUI form to register new students.  
  - Collects student details: Name, Roll Number, Email, Phone, Gender, Course.  
  - Validates inputs and shows a success message.  

- **ViewStudents.java**  
  - A simple Java program to fetch and display all student records from the database.  
  - Useful to check stored data.  

- **ManageStudents.java**  
  - A Swing GUI to **update or delete** existing student records.  
  - Search students by ID, update their details, or delete them from the database.  

---

## Database Setup

1. Install **MySQL Server**.  
2. Create the database and table:

```sql
CREATE DATABASE studentdb;
USE studentdb;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    roll VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(15),
    gender VARCHAR(10),
    course VARCHAR(50)
);
﻿# APP-ProjectConnection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/studentdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
    "root", // your MySQL username
    "YOUR_PASSWORD_HERE" // your MySQL password
);
Features

Add Student – Register new students via GUI.

View Students – See all records stored in MySQL.

Update Student – Modify details of an existing student by ID.

Delete Student – Remove a student record by ID.

Notes

Ensure MySQL server is running before running the Java programs.

Update the JDBC URL and credentials in the Java files as needed.

ViewStudents.java is for console-based viewing; ManageStudents.java provides a GUI for updates/deletions.


