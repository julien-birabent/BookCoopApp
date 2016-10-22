package model;

import java.util.Date;

/**
 * Created by Julien on 22/10/2016.
 */

public class Copy {

    private int numCopy;
    private Date depositeDate;
    private availability availability;
    private physicalState physicalState;

    public enum availability{
        AVAILABLE,
    }
    public enum physicalState{
        LIKE_NEW, USED, VERY_USED
    }

    public Copy(int numCopy, Date depositeDate, Copy.availability availability, Copy.physicalState physicalState) {
        this.numCopy = numCopy;
        this.depositeDate = depositeDate;
        this.availability = availability;
        this.physicalState = physicalState;
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

    public Copy(int numCopy, Date depositeDate) {
        this.numCopy = numCopy;
        this.depositeDate = depositeDate;
    }

    public int getNumCopy() {
        return numCopy;
    }

    public void setNumCopy(int numCopy) {
        this.numCopy = numCopy;
    }

    public Date getDepositeDate() {
        return depositeDate;
    }

    public void setDepositeDate(Date depositeDate) {
        this.depositeDate = depositeDate;
    }
}
