package io.qthjen_dev.docbao24h.Model;

public class MyReader {

    private String title;
    private String date;
    private String image;
    private String link;

    public MyReader(String title, String date, String image, String link) {
        this.title = title;
        this.date = date;
        this.image = image;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
