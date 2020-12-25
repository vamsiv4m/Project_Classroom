package model;

public class Userdetailspojo {
    String username;
    String email;
    String code;
    String phoneno;
    String password;

    public Userdetailspojo(String username, String email, String code, String phoneno, String password) {
        this.username = username;
        this.email = email;
        this.code = code;
        this.phoneno = phoneno;
        this.password = password;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
