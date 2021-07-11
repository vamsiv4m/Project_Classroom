package model;

public class AttendanceMonthSheetModel {
    String monthyear;
    String classcode;
    String date;


    public AttendanceMonthSheetModel(String monthyear, String classcode) {
        this.monthyear = monthyear;
        this.classcode = classcode;
        date="";
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