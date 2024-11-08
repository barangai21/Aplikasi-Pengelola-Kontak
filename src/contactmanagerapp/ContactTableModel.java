package contactmanagerapp;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class ContactTableModel extends AbstractTableModel {
    // Deklarasi kontak sebagai List<Contact>
    private List<Contact> contacts;
    
    // Nama-nama kolom yang ditampilkan di JTable
    private String[] columnNames = {"ID", "Nama", "Telepon", "Kategori"};

    // Konstruktor, inisialisasi kontak sebagai ArrayList kosong
    public ContactTableModel() {
        contacts = new ArrayList<>();  // Pastikan ArrayList diinisialisasi
    }

    @Override
    public int getRowCount() {
        return contacts.size();  // Mengembalikan jumlah baris berdasarkan banyaknya kontak
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;  // Mengembalikan jumlah kolom (4 kolom: ID, Nama, Telepon, Kategori)
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contact contact = contacts.get(rowIndex);  // Ambil kontak berdasarkan baris
        switch (columnIndex) {
            case 0: return contact.getId();  // Kolom ID
            case 1: return contact.getName();  // Kolom Nama
            case 2: return contact.getPhone();  // Kolom Telepon
            case 3: return contact.getCategory();  // Kolom Kategori
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];  // Mengembalikan nama kolom berdasarkan index
    }

    // Setter untuk daftar kontak, digunakan untuk memperbarui data pada tabel
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        fireTableDataChanged();  // Memberitahukan tabel bahwa data telah berubah
    }

    // Mengambil kontak berdasarkan indeks baris
    public Contact getContactAt(int rowIndex) {
        return contacts.get(rowIndex);  // Mengembalikan kontak berdasarkan indeks baris
    }
}