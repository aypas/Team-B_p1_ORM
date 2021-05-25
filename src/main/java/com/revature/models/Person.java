package com.revature.models;

import com.revature.util.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: James Bialon
 * Date: 5/14/2021
 * Time: 5:21 PM
 * Description: Person object for proof of concept.
 */
@Entity
@Table(table_name = "person")
public class Person {

    @Primary(name = "personal_id")
    private int id;

    @Column(name = "first_name", data_type = "var_char")
    private String firstName;

    @Column(name = "last_name", data_type = "var_char")
    private String lastName;

    @Column(name = "age", data_type = "smallint")
    private int age;

    @Column(name = "birthday", data_type = "var_char")
    private String birthday;

    @Username
    @Column(name = "username", data_type = "var_char")
    private String username;

    @Email
    @Column(name = "email", data_type = "var_char")
    private String email;

    @Password
    @Column(name = "password", data_type = "var_char")
    private String password;

    public Person(){}

    public Person(int id, String firstName, String lastName, int age, String birthday, String username, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birthday = birthday;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Person(String firstName, String lastName, int age, String birthday, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birthday = birthday;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", birthday='" + birthday + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
