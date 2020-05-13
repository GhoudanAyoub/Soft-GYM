package com.exemple.stage.modele;

/**
 * Created By GHOUADN AYOUB
 */


public class AboutUsItems {
    String Content, Title;
    int userPhoto;

    public AboutUsItems(String title, String content, int userPhoto) {
        Content = content;
        Title = title;
        this.userPhoto = userPhoto;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(int userPhoto) {
        this.userPhoto = userPhoto;
    }
}
