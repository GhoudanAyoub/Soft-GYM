package com.exemple.stage.modele;

/**
 * Created By GHOUADN AYOUB
 */


public class Exercice {
    private String ID;
    private String Name;
    private String image;
    private String isAddedAsFav = "0";

    public Exercice() {
    }

    public Exercice(String name, String image, String id) {
        Name = name;
        this.image = image;
        ID = id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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


    public String getIsAddedAsFav() {
        return isAddedAsFav;
    }

    public void setIsAddedAsFav(String isAddedAsFav) {
        this.isAddedAsFav = isAddedAsFav;
    }
}
