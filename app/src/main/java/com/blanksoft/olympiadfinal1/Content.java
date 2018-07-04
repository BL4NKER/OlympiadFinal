package com.blanksoft.olympiadfinal1;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Content {


    private String content;
    private String name;
    private String date;
    private String contentId;
    private String coment;
    private String like;
    private Bitmap image;
    private String likechk;
    private Double lat;
    private Double lng;


    public String getContent() {
        return content;

    }
    public Bitmap getImage(){
        return  image;
    }
    public String getName(){
        return name;
    }
    public String getDate(){
        return date;
    }
    public String getContentId(){
        return contentId;
    }
    public String getLikechk(){
        return likechk;
    }
    public String getComent(){
        return coment;
    }
    public String getLike() {
        return like;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setLikechk(String likechk){
        this.likechk = likechk;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setImage(Bitmap image){
        this.image = image;
    }
    public void setContentId(String contentId){
        this.contentId = contentId;
    }
    public void setComent(String coment){
        this.coment = coment;
    }
    public void setLike(String like){
        this.like = like;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }
    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Content(String coment){
        this.coment = coment;

    }

    public Content(String content, String name, String date, Bitmap image, String like, String contentId, String likechk) {
        this.name = name;
        this.date = date;
        this.content = content;
        this.image = image;
        this.like = like;
        this.contentId = contentId;
        this.likechk = likechk;
    }

    public Content(Double lat, Double lng) {
        this.name = name;
        this.date = date;
        this.content = content;
        this.image = image;
        this.like = like;
        this.contentId = contentId;
        this.likechk = likechk;
        this.lat = lat;
        this.lng = lng;
    }

}