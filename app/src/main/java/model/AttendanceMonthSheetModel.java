package model;

public class AttendanceMonthSheetModel {
    String monthyear;
    String classcode;
    String date;
    String lastdate;

    public AttendanceMonthSheetModel(String monthyear, String classcode, String lastdate) {
        date="";
        this.monthyear = monthyear;
        this.classcode = classcode;
        this.lastdate = lastdate;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }

    public String getMonthyear() {
        return monthyear;
    }

    public void setMonthyear(String monthyear) {
        this.monthyear = monthyear;
    }

    public String getClasscode() {
        return classcode;
    }

    public void setClasscode(String classcode) {
        this.classcode = classcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}