package io.qthjen_dev.docbao24h.Model;

public class ItemFavorited {

    private int idFAV;
    private String titleFav, dateFav, linkFav, imageFav;

    public ItemFavorited() {
    }

    public ItemFavorited(int idFAV, String titleFav, String dateFav, String linkFav, String imageFav) {
        this.idFAV = idFAV;
        this.titleFav = titleFav;
        this.dateFav = dateFav;
        this.linkFav = linkFav;
        this.imageFav = imageFav;
    }

    public String getTitleFav() {
        return titleFav;
    }

    public void setTitleFav(String titleFav) {
        this.titleFav = titleFav;
    }

    public String getDateFav() {
        return dateFav;
    }

    public void setDateFav(String dateFav) {
        this.dateFav = dateFav;
    }

    public String getLinkFav() {
        return linkFav;
    }

    public void setLinkFav(String linkFav) {
        this.linkFav = linkFav;
    }

    public int getIdFAV() {
        return idFAV;
    }

    public void setIdFAV(int idFAV) {
        this.idFAV = idFAV;
    }

    public String getImageFav() {
        return imageFav;
    }

    public void setImageFav(String imageFav) {
        this.imageFav = imageFav;
    }
}
