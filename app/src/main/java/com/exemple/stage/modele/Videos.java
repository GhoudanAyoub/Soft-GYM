package com.exemple.stage.modele;

import java.util.List;

/**
 * Created By GHOUADN AYOUB
 */


public class Videos {

    private String ID;
    private String video;
    private String videoName;
    private String Name;
    private String image;
    private String Time;
    private List<String> Chapter;

    public Videos() {
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public List<String> getChapter() {
        return Chapter;
    }

    public void setChapter(List<String> chapter) {
        Chapter = chapter;
    }
}
