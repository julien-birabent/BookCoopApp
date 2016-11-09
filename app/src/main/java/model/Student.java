package model;

import java.util.ArrayList;

/**
 * Created by Julien on 22/10/2016.
 */

public class Student {

    private String email;
    private String password;
    private int phoneNumber;
    private ArrayList<Book> booksList;



    public Student(){
        this.email = null;
        this.password = null;
        this.booksList = new ArrayList<Book>();

    }

    public Student(String email, String password){
        this.email = email;
        this.password = password;
        this.booksList = new ArrayList<Book>();

    }

    public Student(int phoneNumber, String password){
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.booksList = new ArrayList<Book>();


    }

    /**
     * Métthode convertissant la liste des livres d'un étduiant en unn ArrayList<String>
     * @return
     */
    public ArrayList<String> booksListToArrayListOfString(){

        ArrayList<String> books = new ArrayList<String>();

        for (Book book:booksList) {
            books.add(book.toString());
        }

        return books;
    }

    /** Convertit la liste de livre en tableau 1D
     *
      */

    public Book[] booksListToArray(){

        Book books[] = new Book[getBooksList().size()];

        for(int i = 0; i<books.length;i++){
            books[i] = getBooksList().get(i);
        }

        return books;
    }

    public ArrayList<Book> getBooksList() {
        return booksList;
    }

    public void setBooksList(ArrayList<Book> booksList) {
        this.booksList = booksList;
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
