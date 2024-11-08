package contactmanagerapp;

public class Contact {
    private int id;
    private String name;
    private String phone;
    private String category;

    public Contact(int id, String name, String phone, String category) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCategory() {
        return category;
    }
}
