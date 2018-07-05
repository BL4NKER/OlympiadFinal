package com.blanksoft.olympiadfinal1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Comment {
    private String comment;
    private String users;


    public String getComment() {
        return comment;

    }
    public String getUsers() {
        return users;

    }

    public Comment(String comment, String users){
        this.comment = comment;
        this.users = users;
    }


}
