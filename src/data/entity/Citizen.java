package data.entity;

public final class Citizen {
    private Long id;
    private final String surname;
    private final String name;
    private final String phoneNumber;
    private final String email;

    public Citizen(String surname, String name, String phoneNumber, String email) {
        this.surname = surname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Citizen[" +
                "id=" + id + ", " +
                "surname=" + surname + ", " +
                "name=" + name + ", " +
                "phoneNumber=" + phoneNumber + ", " +
                "email=" + email + ']';
    }

}
