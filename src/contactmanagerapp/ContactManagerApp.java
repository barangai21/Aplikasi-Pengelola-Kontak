package contactmanagerapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactManagerApp extends JFrame {
    private JTextField txtName, txtPhone, txtSearch;
    private JComboBox<String> cmbCategory;
    private JButton btnAdd, btnEdit, btnDelete, btnSearch;
    private JTable tableContacts;
    private ContactTableModel tableModel;
    private Connection conn;

    public ContactManagerApp() {
        setTitle("Aplikasi Pengelolaan Kontak");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize UI Components
        initUI();
        connectToDatabase();
        loadContacts();
    }

    private void initUI() {
        // Panel Form for Input Fields (Nama, Nomor Telepon, Kategori)
        JPanel panelForm = new JPanel(new GridLayout(5, 2));
        
        panelForm.add(new JLabel("Nama Kontak:"));
        txtName = new JTextField();
        panelForm.add(txtName);
        
        panelForm.add(new JLabel("Nomor Telepon:"));
        txtPhone = new JTextField();
        panelForm.add(txtPhone);

        panelForm.add(new JLabel("Kategori:"));
        cmbCategory = new JComboBox<>(new String[]{"Keluarga", "Teman", "Kerja"});
        panelForm.add(cmbCategory);

        panelForm.add(new JLabel("Cari Kontak:"));
        txtSearch = new JTextField();
        panelForm.add(txtSearch);

        // Panel for Buttons (Add, Edit, Delete, Search)
        JPanel panelButtons = new JPanel(new FlowLayout());
        btnAdd = new JButton("Tambah");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Hapus");
        btnSearch = new JButton("Cari");

        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnSearch);

        // Table to display contacts
        tableModel = new ContactTableModel();
        tableContacts = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableContacts);

        // Adding components to the frame
        add(panelForm, BorderLayout.NORTH);
        add(panelButtons, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Action listeners for buttons
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editContact();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContact();
            }
        });
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:contacts.db");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadContacts() {
        List<Contact> contacts = new ArrayList<>();
        try {
            String query = "SELECT * FROM contacts";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String category = rs.getString("category");
                contacts.add(new Contact(id, name, phone, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableModel.setContacts(contacts);
    }

    private void addContact() {
        String name = txtName.getText();
        String phone = txtPhone.getText();
        String category = (String) cmbCategory.getSelectedItem();
        try {
            String query = "INSERT INTO contacts (name, phone, category) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, phone);
            pst.setString(3, category);
            pst.executeUpdate();
            loadContacts();
            JOptionPane.showMessageDialog(this, "Kontak berhasil ditambahkan");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editContact() {
        int row = tableContacts.getSelectedRow();
        if (row != -1) {
            Contact contact = tableModel.getContactAt(row);
            txtName.setText(contact.getName());
            txtPhone.setText(contact.getPhone());
            cmbCategory.setSelectedItem(contact.getCategory());
        }
    }

    private void deleteContact() {
        int row = tableContacts.getSelectedRow();
        if (row != -1) {
            Contact contact = tableModel.getContactAt(row);
            try {
                String query = "DELETE FROM contacts WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, contact.getId());
                pst.executeUpdate();
                loadContacts();
                JOptionPane.showMessageDialog(this, "Kontak berhasil dihapus");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchContact() {
        String searchTerm = txtSearch.getText();
        List<Contact> contacts = new ArrayList<>();
        try {
            String query = "SELECT * FROM contacts WHERE name LIKE ? OR phone LIKE ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, "%" + searchTerm + "%");
            pst.setString(2, "%" + searchTerm + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String category = rs.getString("category");
                contacts.add(new Contact(id, name, phone, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableModel.setContacts(contacts);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContactManagerApp().setVisible(true);
            }
        });
    }
}