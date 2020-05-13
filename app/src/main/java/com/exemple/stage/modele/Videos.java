package com.exemple.stage.modele;

/**
 * Created By GHOUADN AYOUB
 */


public class Videos {

    private String ID;
    private String video;
    private String videoName;
    private String duree;
    private String Chapter1;
    private String Chapter2;
    private String Chapter3;
    private String Other;

    public Videos() {
    }

    public Videos(String ID, String video, String videoName, String duree, String chapter1, String chapter2, String chapter3, String other) {
        this.ID = ID;
        this.video = video;
        this.videoName = videoName;
        this.duree = duree;
        Chapter1 = chapter1;
        Chapter2 = chapter2;
        Chapter3 = chapter3;
        Other = other;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getChapter1() {
        return Chapter1;
    }

    public void setChapter1(String chapter1) {
        Chapter1 = chapter1;
    }

    public String getChapter2() {
        return Chapter2;
    }

    public void setChapter2(String chapter2) {
        Chapter2 = chapter2;
    }

    public String getChapter3() {
        return Chapter3;
    }

    public void setChapter3(String chapter3) {
        Chapter3 = chapter3;
    }

    public String getOther() {
        return Other;
    }

    public void setOther(String other) {
        Other = other;
    }


}
