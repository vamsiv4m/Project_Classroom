package model;

public class Attendance_model {
    String sno;
    String student_name;
    String status;

    public Attendance_model(String sno, String student_name) {
        this.sno = sno;
        this.student_name = student_name;
        status="";
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
}
