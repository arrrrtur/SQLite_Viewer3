package viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SQLiteViewer extends JFrame {
    private JTextField fileNameTextField;
    private JButton openFileButton;
    private JComboBox<String> tableComboBox;
    private JTextArea queryTextArea;
    private JButton executeQueryButton;
    private JTable table;

    public SQLiteViewer() {
        super("SQLite Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);

        initComponents();

        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void initComponents() {
        fileNameTextField = new JTextField();
        fileNameTextField.setName("FileNameTextField");
        fileNameTextField.setBounds(5, 20, 590, 30);
        add(fileNameTextField);

        openFileButton = new JButton("Open");
        openFileButton.setName("OpenFileButton");
        openFileButton.setBounds(600, 25, 80, 20);
        add(openFileButton);

        tableComboBox = new JComboBox<>();
        tableComboBox.setName("TablesComboBox");
        tableComboBox.setBounds(5, 70, 675, 30);
        add(tableComboBox);

        queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        queryTextArea.setBounds(5, 110, 560, 120);
        queryTextArea.setEnabled(false);
        add(queryTextArea);

        executeQueryButton = new JButton("Execute");
        executeQueryButton.setName("ExecuteQueryButton");
        executeQueryButton.setBounds(570, 110, 110, 40);
        executeQueryButton.setEnabled(false);
        add(executeQueryButton);

        table = new JTable();
        table.setName("Table");
        table.setBounds(5, 240, 675, 460);
        add(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        getContentPane().add(scrollPane);

        openFileButton.addActionListener(e -> {
            tableComboBox.removeAllItems();  // Remove all last tables.

            String dataBaseName = fileNameTextField.getText();
            if (DataBase.connected(dataBaseName)) {
                queryTextArea.setEnabled(true);
                executeQueryButton.setEnabled(true);
                for (String tableName : DataBase.getTables()) {
                    tableComboBox.addItem(tableName);
                }
            } else {
                JOptionPane.showMessageDialog(new Frame(), "File doesn't exist!");
                queryTextArea.setEnabled(false);
                executeQueryButton.setEnabled(false);
            }
        });

        tableComboBox.addActionListener(e -> {
            String query = "SELECT * FROM " + tableComboBox.getSelectedItem() + ";";
            queryTextArea.setText(query);
        });

        executeQueryButton.addActionListener(e -> {
            if (queryTextArea.getText().equals("SELECT * FROM " + tableComboBox.getSelectedItem() + ";")) {
                Object[] columns = {"contact_id", "first_name", "last_name", "email", "phone"};

                Object[][] data = DataBase.getData(queryTextArea.getText());
                DefaultTableModel tableModel = new DefaultTableModel();

                tableModel.setColumnIdentifiers(columns);
                if (data != null) {
                    for (Object[] row : data) {
                        tableModel.addRow(row);
                    }
                }

                table.setModel(tableModel);
                table.repaint();
            } else {
                JOptionPane.showMessageDialog(new Frame(), "Wrong SQL query!");
            }
        });

        
    }
}
