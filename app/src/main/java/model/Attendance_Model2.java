package model;

public class Attendance_Model2 {
    String sno;
    String studentname;
    String status;
    String section;
    String subjectname;


    public Attendance_Model2(String sno, String studentname, String status, String section, String subjectname) {
        this.sno = sno;
        this.studentname = studentname;
        this.status = status;
        this.section = section;
        this.subjectname = subjectname;
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
}
