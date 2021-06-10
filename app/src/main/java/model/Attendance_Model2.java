package model;

public class Attendance_Model2 {
    String sname;
    String status;

    public Attendance_Model2(String sname, String status) {
        this.sname = sname;
        this.status = status;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
