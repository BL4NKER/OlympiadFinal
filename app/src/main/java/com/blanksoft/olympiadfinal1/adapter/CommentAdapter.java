package com.blanksoft.olympiadfinal1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blanksoft.olympiadfinal1.model.Comment;
import com.blanksoft.olympiadfinal1.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

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

        final String URL =  "http://218.155.147.128:3000/profileimage";
        StringRequest request = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("image").equals("null")){
                                holder.profileImage.setImageResource(R.drawable.user);
                            }else {

                                byte[] bytePlainOrg = Base64.decode(jsonObject.optString("image"), 0);
                                RequestOptions requestOptions = new RequestOptions();

                                Glide.with(context)
                                        .asBitmap()
                                        .load(bytePlainOrg)
                                        .apply(requestOptions.fitCenter().circleCrop())
                                        .into(holder.profileImage);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<>();
                params.put("userid", comment.getUsers());
                return params;
            }
        };

        request.setShouldCache(true);
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                200000 ,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(request);

    }

    @Override
    public int getItemCount() {
        return comments.size();

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Comment;
        TextView Users;
        ImageView profileImage;
        ImageButton btPopup;

        public ViewHolder(android.view.View itemView) {
            super(itemView);

            Comment = (TextView) itemView.findViewById(R.id.song);
            Users = (TextView) itemView.findViewById(R.id.singer);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
            btPopup = (ImageButton) itemView.findViewById(R.id.btpopup);

        }
    }
}