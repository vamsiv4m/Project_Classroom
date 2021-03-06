package model;

public class FetchData {
    String subject;
    String section;
    String classname;
    String imageurl;
    String class_code;
    int type;

//    public FetchData(String subject, String section, String classname, String imageUrl) {
//        this.subject = subject;
//        this.section = section;
//        this.classname = classname;
//        this.imageUrl = imageUrl;
//    }


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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getClass_code() {
        return class_code;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
