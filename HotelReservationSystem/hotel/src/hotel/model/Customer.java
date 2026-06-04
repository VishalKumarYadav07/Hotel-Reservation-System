package hotel.model;

public class Customer {
    private final String customerId;
    private String name;
    private String phone;
    private String email;
    private String address;

    public Customer(String customerId, String name, String phone, String email, String address) {
        this.customerId = customerId;
        this.name      = name;
        this.phone     = phone;
        this.email     = email;
        this.address   = address;
    }

    public String getCustomerId() { return customerId; }
    public String getName()       { return name; }
    public String getPhone()      { return phone; }
    public String getEmail()      { return email; }
    public String getAddress()    { return address; }

    public void setName(String n)    { this.name    = n; }
    public void setPhone(String p)   { this.phone   = p; }
    public void setEmail(String e)   { this.email   = e; }
    public void setAddress(String a) { this.address = a; }

    @Override
    public String toString() {
        return String.format("ID: %-8s | %-20s | %s | %s", customerId, name, phone, email);
    }
}
