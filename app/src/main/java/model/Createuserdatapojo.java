package model;

public class Createuserdatapojo {
    String classname;
    String section;
    String room;
    String subject;
    String class_code;
    String imageUrl;
    String username;
    public Createuserdatapojo() {
    }

    public Createuserdatapojo(String username,String classname, String section, String room, String subject, String class_code, String imageurl) {
        this.classname = classname;
        this.section = section;
        this.room = room;
        this.subject = subject;
        this.class_code = class_code;
        this.imageUrl = imageurl;
        this.username = username;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClass_code() {
        return class_code;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }

    public String getImageurl() {
        return imageUrl;
    }

    public void setImageurl(String imageurl) {
        this.imageUrl = imageurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


