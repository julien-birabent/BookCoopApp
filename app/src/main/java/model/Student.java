package model;

/**
 * Created by Julien on 22/10/2016.
 */

public class Student {

    private String email;
    private String password;
    private int phoneNumber;



    public Student(){
        this.email = null;
        this.password = null;

    }

    public Student(String email, String password){
        this.email = email;
        this.password = password;

    }

    public Student(int phoneNumber, String password){
        this.phoneNumber = phoneNumber;
        this.password = password;


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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
