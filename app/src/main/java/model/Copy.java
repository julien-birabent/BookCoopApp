package model;

import java.util.Date;

/**
 * Created by Julien on 22/10/2016.
 */

public class Copy {

    private int copyId;
    private int bookId;
    private Date depositeDate;
    private availability availability;
    private physicalState physicalState;

    public enum availability{
        FOR_SALE,
    }
    public enum physicalState{
        LIKE_NEW, USED, VERY_USED
    }

    public Copy(int copyId, Date depositeDate, Copy.availability availability, Copy.physicalState physicalState) {
        this.copyId = copyId;
        this.depositeDate = depositeDate;
        this.availability = availability;
        this.physicalState = physicalState;
    }

    public Copy(int copyId, int bookId){
        this.copyId = copyId;
        this.bookId = bookId;

    }

    public Copy(){

    }

    public Copy.availability getAvailability() {
        return availability;
    }

    public void setAvailability(Copy.availability availability) {
        this.availability = availability;
    }

    public Copy.physicalState getPhysicalState() {
        return physicalState;
    }

    public void setPhysicalState(Copy.physicalState physicalState) {
        this.physicalState = physicalState;
    }

    public Copy(int copyId, Date depositeDate) {
        this.copyId = copyId;
        this.depositeDate = depositeDate;
    }

    public Date getDepositeDate() {
        return depositeDate;
    }

    public void setDepositeDate(Date depositeDate) {
        this.depositeDate = depositeDate;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }
}
