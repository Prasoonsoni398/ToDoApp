package internTasks;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class ToDoApp extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskField;
    private JButton addButton, deleteButton, doneButton;

    private static final String FILE_NAME = "tasks.txt"; // File to store tasks

    public ToDoApp() {
        // Window Title
        setTitle("To-Do List App");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for input and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        taskField = new JTextField(20);
        addButton = new JButton("Add Task");
        deleteButton = new JButton("Delete Task");
        doneButton = new JButton("Mark as Done");

        panel.add(taskField);
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(doneButton);

        // Task list
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        // Add components to frame
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load tasks from file
        loadTasks();

        // Button Actions
        addButton.addActionListener(e -> {
            String task = taskField.getText().trim();
            if (!task.isEmpty()) {
                taskListModel.addElement(task);
                taskField.setText("");
                saveTasks();
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a task!");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskListModel.remove(selectedIndex);
                saveTasks();
            } else {
                JOptionPane.showMessageDialog(null, "Please select a task to delete!");
            }
        });

        doneButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String task = taskListModel.getElementAt(selectedIndex);
                if (!task.endsWith("✔ (Done)")) {
                    taskListModel.set(selectedIndex, task + " ✔ (Done)");
                    saveTasks();
                } else {
                    JOptionPane.showMessageDialog(null, "This task is already marked as done!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a task to mark as done!");
            }
        });
    }

    // Save tasks to file
    private void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskListModel.size(); i++) {
                writer.println(taskListModel.getElementAt(i));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving tasks: " + e.getMessage());
        }
    }

    // Load tasks from file
    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    taskListModel.addElement(scanner.nextLine());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading tasks: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ToDoApp().setVisible(true);
        });
    }
}
