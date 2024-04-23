package data.entity;

import java.util.Objects;

/**
 * Гражданин
 */
public final class Citizen {
    private final String surname;
    private final String name;
    private final String phoneNumber;
    private final String email;
    private Long id;

    /**
     * Конструктор
     *
     * @param surname     фамилия
     * @param name        имя
     * @param phoneNumber номер телефона
     * @param email       электронная почта
     */
    public Citizen(String surname, String name, String phoneNumber, String email) {
        this.surname = surname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * Конструктор
     *
     * @param id          id гражданина
     * @param surname     фамилия
     * @param name        имя
     * @param phoneNumber номер телефона
     * @param email       электронная почта
     */
    public Citizen(Long id, String surname, String name, String phoneNumber, String email) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Citizen citizen = (Citizen) o;
        return Objects.equals(surname, citizen.surname) && Objects.equals(name, citizen.name) && Objects.equals(phoneNumber, citizen.phoneNumber) && Objects.equals(email, citizen.email) && Objects.equals(id, citizen.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, phoneNumber, email, id);
    }
}
