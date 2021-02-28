package model;

import android.widget.ImageView;

public class FetchData {
    String subject;
    String section;
    String classname;
    String imageurl;

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

    public String getImageUrl() {
        return imageurl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageurl = imageurl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
