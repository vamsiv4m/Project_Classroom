package model;

public class Attendance_Model2 {
    String sno;
    String studentname;
    String status;
    String section;
    String subjectname;
    String date;
    String monthyear;
    long lastdate;

    public Attendance_Model2(String sno, String studentname, String status, String section, String subjectname, String date, String monthyear, long lastdate) {
        this.sno = sno;
        this.studentname = studentname;
        this.status = status;
        this.section = section;
        this.subjectname = subjectname;
        this.date = date;
        this.monthyear = monthyear;
        this.lastdate = lastdate;
    }

    public long getLastdate() {
        return lastdate;
    }

    public void setLastdate(int lastdate) {
        this.lastdate = lastdate;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonthyear() {
        return monthyear;
    }

    public void setMonthyear(String monthyear) {
        this.monthyear = monthyear;
    }
}