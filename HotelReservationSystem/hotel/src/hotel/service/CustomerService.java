package hotel.service;

import hotel.model.Customer;

import java.util.*;
import java.util.stream.Collectors;

public class CustomerService {
    private final Map<String, Customer> customers = new LinkedHashMap<>();
    private int idCounter = 100;

    public Customer registerCustomer(String name, String phone, String email, String address) {
        String id = "CUST-" + (++idCounter);
        Customer c = new Customer(id, name, phone, email, address);
        customers.put(id, c);
        return c;
    }

    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(customers.get(id));
    }

    public List<Customer> findByName(String name) {
        String lower = name.toLowerCase();
        return customers.values().stream()
                .filter(c -> c.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public boolean updateCustomer(String id, String name, String phone, String email, String address) {
        Customer c = customers.get(id);
        if (c == null) return false;
        if (!name.isBlank())    c.setName(name);
        if (!phone.isBlank())   c.setPhone(phone);
        if (!email.isBlank())   c.setEmail(email);
        if (!address.isBlank()) c.setAddress(address);
        return true;
    }

    public boolean removeCustomer(String id) {
        return customers.remove(id) != null;
    }

    public int getTotalCustomers() { return customers.size(); }
}
