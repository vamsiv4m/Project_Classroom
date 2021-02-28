package model;

public class JoinData {
    String subject;
    String section;
    String professor;

    public JoinData(String subject, String section, String professor) {
        this.subject = subject;
        this.section = section;
        this.professor = professor;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
