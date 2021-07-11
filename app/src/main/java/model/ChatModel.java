package model;

public class ChatModel {
    String timesender;
    String msg;
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimesender() {
        return timesender;
    }

    public void setTimesender(String timesender) {
        this.timesender = timesender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
