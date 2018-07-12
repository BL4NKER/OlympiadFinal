package com.blanksoft.olympiadfinal1.model;

public class Comment {
    private String comment;
    private String users;
    private String comentId;


    public String getComment() {
        return comment;

    }
    public String getUsers() {
        return users;

    }
    public String getComentId() {
        return comentId;
    }

    public Comment(String comment, String users, String comentId){
        this.comment = comment;
        this.users = users;
        this.comentId = comentId;
    }



}
