package io.qthjen_dev.docbao24h.Model;

import android.graphics.drawable.Drawable;

public class BaoNCC {

    private String name;
    private String link;
    private Drawable image;

    public BaoNCC(String name, String link, Drawable image) {
        this.name = name;
        this.link = link;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

}
