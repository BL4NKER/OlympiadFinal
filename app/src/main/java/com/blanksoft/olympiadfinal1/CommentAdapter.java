package com.blanksoft.olympiadfinal1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<Comment> comments;


    public CommentAdapter(Context context, List<Comment> singModels) {
        this.context = context;
        this.comments = singModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_comment,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Comment comment = comments.get(position);
        holder.Comment.setText(comment.getComment());
        holder.Users.setText(comment.getUsers());





    }

    @Override
    public int getItemCount() {
        return comments.size();

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Comment;
        TextView Users;

        public ViewHolder(android.view.View itemView) {
            super(itemView);

            Comment = (TextView) itemView.findViewById(R.id.song);
            Users = (TextView) itemView.findViewById(R.id.singer);

        }
    }
}