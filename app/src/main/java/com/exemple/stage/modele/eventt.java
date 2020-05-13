package com.exemple.stage.modele;

/**
 * Created By GHOUADN AYOUB
 */


public class eventt {
    private String Text;
    private String Date;
    private String gmail;

    public eventt() {
    }

    public eventt(String text, String date, String gmail) {
        this.Text = text;
        this.Date = date;
        this.gmail = gmail;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
}
