package com.studentinfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.studentinfo.model.Attendance;
import com.studentinfo.model.Grade;
import com.studentinfo.model.Student;
import com.studentinfo.service.DatabaseService;
import com.studentinfo.service.MongoDBService;

public class Main {
    private static DatabaseService dbService;
    private static JFrame mainFrame;
    private static JTable studentTable;
    private static DefaultTableModel studentTableModel;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(0, 32, 91); // Navy blue
    private static final Color SECONDARY_COLOR = new Color(0, 48, 135); // Lighter navy blue
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245); // Light gray-blue
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // White
    private static final Color ACCENT_COLOR = new Color(230, 126, 34);

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize MongoDB connection
        String connectionString = "mongodb+srv://shubhamdahal13:ZXCzxc789@cluster0.f3lfhnf.mongodb.net/studentInfo?retryWrites=true&w=majority&appName=Cluster0";
        dbService = new MongoDBService(connectionString);

        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        mainFrame = new JFrame("Student Information System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLayout(new BorderLayout(10, 10));

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create menu bar with modern styling
        JMenuBar menuBar = createStyledMenuBar();
        mainFrame.setJMenuBar(menuBar);

        // Create table with modern styling
        JPanel tablePanel = createStyledTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Create status bar
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        mainFrame.add(mainPanel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        // Load existing students
        refreshStudentTable();
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Student Information System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private static JMenuBar createStyledMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(PRIMARY_COLOR);
        menuBar.setBorder(null); // Remove border

        // Student menu
        JMenu studentMenu = createStyledMenu("Students");
        JMenuItem addStudentItem = createStyledMenuItem("Add Student", "➕");
        JMenuItem editStudentItem = createStyledMenuItem("Edit Student", "✏️");
        JMenuItem deleteStudentItem = createStyledMenuItem("Delete Student", "🗑️");
        JMenuItem viewAllStudentsItem = createStyledMenuItem("View All Students", "👥");
        
        addStudentItem.addActionListener(e -> showAddStudentDialog());
        editStudentItem.addActionListener(e -> showEditStudentDialog());
        deleteStudentItem.addActionListener(e -> showDeleteStudentDialog());
        viewAllStudentsItem.addActionListener(e -> refreshStudentTable());
        
        studentMenu.add(addStudentItem);
        studentMenu.add(editStudentItem);
        studentMenu.add(deleteStudentItem);
        studentMenu.addSeparator();
        studentMenu.add(viewAllStudentsItem);

        // Grade menu
        JMenu gradeMenu = createStyledMenu("Grades");
        JMenuItem addGradeItem = createStyledMenuItem("Add Grade", "📝");
        JMenuItem viewGradesItem = createStyledMenuItem("View Grades", "📊");
        
        addGradeItem.addActionListener(e -> showAddGradeDialog());
        viewGradesItem.addActionListener(e -> showViewGradesDialog());
        
        gradeMenu.add(addGradeItem);
        gradeMenu.add(viewGradesItem);

        // Attendance menu
        JMenu attendanceMenu = createStyledMenu("Attendance");
        JMenuItem addAttendanceItem = createStyledMenuItem("Add Attendance", "✅");
        JMenuItem viewAttendanceItem = createStyledMenuItem("View Attendance", "📅");
        
        addAttendanceItem.addActionListener(e -> showAddAttendanceDialog());
        viewAttendanceItem.addActionListener(e -> showViewAttendanceDialog());
        
        attendanceMenu.add(addAttendanceItem);
        attendanceMenu.add(viewAttendanceItem);

        menuBar.add(studentMenu);
        menuBar.add(gradeMenu);
        menuBar.add(attendanceMenu);

        return menuBar;
    }

    private static JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(TEXT_COLOR);
        menu.setFont(new Font("Arial", Font.BOLD, 14));
        return menu;
    }

    private static JMenuItem createStyledMenuItem(String text, String icon) {
        JMenuItem menuItem = new JMenuItem(icon + " " + text);
        menuItem.setFont(new Font("Arial", Font.PLAIN, 14));
        menuItem.setForeground(PRIMARY_COLOR);
        menuItem.setBackground(Color.WHITE);
        menuItem.setOpaque(true);
        return menuItem;
    }

    private static JPanel createStyledTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create table
        String[] columnNames = {"ID", "First Name", "Last Name", "Email"};
        studentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(studentTableModel);
        
        // Style the table
        studentTable.setRowHeight(35);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 14));
        studentTable.setSelectionBackground(SECONDARY_COLOR);
        studentTable.setSelectionForeground(TEXT_COLOR);
        studentTable.setGridColor(new Color(200, 200, 200));
        studentTable.setBackground(Color.WHITE);
        studentTable.setForeground(new Color(33, 33, 33));
        
        // Style the table header
        JTableHeader header = studentTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBorder(null);

        // Add table to scroll pane with modern styling
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(PRIMARY_COLOR);
        statusBar.setPreferredSize(new Dimension(0, 5)); // Make it very thin, just for visual separation
        statusBar.setBorder(null);
        return statusBar;
    }

    private static void showAddStudentDialog() {
        JDialog dialog = new JDialog(mainFrame, "Add Student", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Get the next available ID
        List<Student> students = dbService.getAllStudents();
        int maxId = 0;
        for (Student student : students) {
            try {
                int id = Integer.parseInt(student.getId());
                maxId = Math.max(maxId, id);
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }
        String nextId = String.valueOf(maxId + 1);

        // Create styled text fields
        JTextField idField = createStyledTextField(nextId);
        idField.setEditable(false); // Make ID field read-only
        JTextField firstNameField = createStyledTextField("");
        JTextField lastNameField = createStyledTextField("");
        JTextField emailField = createStyledTextField("");

        // Add components
        addLabelAndField(mainPanel, "ID:", idField, gbc, 0);
        addLabelAndField(mainPanel, "First Name:", firstNameField, gbc, 1);
        addLabelAndField(mainPanel, "Last Name:", lastNameField, gbc, 2);
        addLabelAndField(mainPanel, "Email:", emailField, gbc, 3);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                Student student = new Student(
                    idField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText()
                );
                dbService.createStudent(student);
                refreshStudentTable();
                dialog.dispose();
            } catch (Exception ex) {
                showErrorDialog(dialog, ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private static JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text, 20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(new MatteBorder(1, 1, 1, 1, SECONDARY_COLOR));
        return field;
    }

    private static void addLabelAndField(JPanel panel, String labelText, Component field, 
                                       GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
        gbc.weightx = 0.0;
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(new Color(255, 255, 255)); // Pure white for better contrast
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setOpaque(true); // Make sure the background is painted
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }

    private static void showErrorDialog(JDialog parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    private static void showEditStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a student to edit", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentId = (String) studentTable.getValueAt(selectedRow, 0);
        Student student = dbService.getStudent(studentId);

        JDialog dialog = new JDialog(mainFrame, "Edit Student", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create styled text fields
        JTextField idField = createStyledTextField(student.getId());
        JTextField firstNameField = createStyledTextField(student.getFirstName());
        JTextField lastNameField = createStyledTextField(student.getLastName());
        JTextField emailField = createStyledTextField(student.getEmail());

        // Add components
        addLabelAndField(mainPanel, "ID:", idField, gbc, 0);
        addLabelAndField(mainPanel, "First Name:", firstNameField, gbc, 1);
        addLabelAndField(mainPanel, "Last Name:", lastNameField, gbc, 2);
        addLabelAndField(mainPanel, "Email:", emailField, gbc, 3);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                Student updatedStudent = new Student(
                    idField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText()
                );
                dbService.updateStudent(updatedStudent);
                refreshStudentTable();
                dialog.dispose();
            } catch (Exception ex) {
                showErrorDialog(dialog, ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private static void showDeleteStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a student to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentId = (String) studentTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(mainFrame,
            "Are you sure you want to delete this student?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dbService.deleteStudent(studentId);
            refreshStudentTable();
        }
    }

    private static void showAddGradeDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a student first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentId = (String) studentTable.getValueAt(selectedRow, 0);

        JDialog dialog = new JDialog(mainFrame, "Add Grade", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create styled text fields
        JTextField subjectField = createStyledTextField("");
        JTextField scoreField = createStyledTextField("");
        JTextField dateField = createStyledTextField(LocalDate.now().toString());

        // Add components
        addLabelAndField(mainPanel, "Subject:", subjectField, gbc, 0);
        addLabelAndField(mainPanel, "Score:", scoreField, gbc, 1);
        addLabelAndField(mainPanel, "Date (YYYY-MM-DD):", dateField, gbc, 2);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                Grade grade = new Grade(
                    UUID.randomUUID().toString(),
                    subjectField.getText(),
                    Double.parseDouble(scoreField.getText()),
                    LocalDate.parse(dateField.getText()),
                    studentId
                );
                dbService.createGrade(grade);
                dialog.dispose();
            } catch (Exception ex) {
                showErrorDialog(dialog, ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private static void showAddAttendanceDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a student first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentId = (String) studentTable.getValueAt(selectedRow, 0);

        JDialog dialog = new JDialog(mainFrame, "Add Attendance", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create styled text fields
        JTextField dateField = createStyledTextField(LocalDate.now().toString());
        JCheckBox presentCheckBox = new JCheckBox("Present");

        // Add components
        addLabelAndField(mainPanel, "Date (YYYY-MM-DD):", dateField, gbc, 0);
        addLabelAndField(mainPanel, "Present:", presentCheckBox, gbc, 1);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                Attendance attendance = new Attendance(
                    UUID.randomUUID().toString(),
                    LocalDate.parse(dateField.getText()),
                    presentCheckBox.isSelected(),
                    studentId
                );
                dbService.createAttendance(attendance);
                dialog.dispose();
            } catch (Exception ex) {
                showErrorDialog(dialog, ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private static void showViewGradesDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a student first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentId = (String) studentTable.getValueAt(selectedRow, 0);
        List<Grade> grades = dbService.getStudentGrades(studentId);

        JDialog dialog = new JDialog(mainFrame, "Student Grades", true);
        dialog.setLayout(new BorderLayout());

        String[] columnNames = {"Subject", "Score", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable gradeTable = new JTable(model);

        for (Grade grade : grades) {
            model.addRow(new Object[]{
                grade.getSubject(),
                grade.getScore(),
                grade.getDate()
            });
        }

        dialog.add(new JScrollPane(gradeTable), BorderLayout.CENTER);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private static void showViewAttendanceDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a student first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentId = (String) studentTable.getValueAt(selectedRow, 0);
        List<Attendance> attendanceList = dbService.getStudentAttendance(studentId);

        JDialog dialog = new JDialog(mainFrame, "Student Attendance", true);
        dialog.setLayout(new BorderLayout());

        String[] columnNames = {"Date", "Present"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable attendanceTable = new JTable(model);

        for (Attendance attendance : attendanceList) {
            model.addRow(new Object[]{
                attendance.getDate(),
                attendance.isPresent() ? "Yes" : "No"
            });
        }

        dialog.add(new JScrollPane(attendanceTable), BorderLayout.CENTER);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private static void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        List<Student> students = dbService.getAllStudents();
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
            });
        }
    }
} 