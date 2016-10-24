package model;

/**
 * Created by Julien on 22/10/2016.
 */

public class Book {

    private int isbn;
    private String author;
    private String title;
    private double price;
    private int nbPages;

    public Book(int isbn, String author, String title, double price, int nbPages) {
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.price = price;
        this.nbPages = nbPages;
    }

    public Book(){

    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn=" + isbn +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", nbPages=" + nbPages +
                '}';
    }
}
