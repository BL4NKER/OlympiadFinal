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
import android.widget.CompoundButton;
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

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    List<Content> contents;
    boolean like;
    final String ON_CHECKED_TRUE = "1";
    final String ON_CHECKED_FALSE = "0";


    public Adapter(Context context, List<Content> singModels) {
        this.context = context;
        this.contents = singModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Content content = contents.get(position);

        holder.chkLike.setOnCheckedChangeListener(null);
        holder.content.setText(content.getContent());
        holder.name.setText(content.getName());
        holder.date.setText(content.getDate());
        holder.image.setImageBitmap(content.getImage());
        holder.countLike.setText(content.getLike());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            Intent intent=new Intent(context,PostDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            @Override
            public void onClick(View v) {
                intent.putExtra("author", contents.get(position).getName());
                intent.putExtra("content", contents.get(position).getContent());
                intent.putExtra("countLike", contents.get(position).getLike());
                intent.putExtra("contentid", contents.get(position).getContentId());
                intent.putExtra("likechk", contents.get(position).getLikechk());
                context.startActivity(intent);


            }
        });
        holder.chkLike.setOnCheckedChangeListener(null);
        if(content.getLikechk().equals("0")){
            holder.chkLike.setChecked(true);
        }else{
            holder.chkLike.setChecked(false);
        }if (LoginActivity.userName.equals(content.getName())) {
            holder.btDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btDelete.setVisibility(View.GONE);
        }

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String URL = "http://172.20.10.4:3000/process/remove";
                StringRequest request = new StringRequest(Request.Method.POST,
                        URL,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                )
                {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<>();
                        params.put("contentid", content.getContentId());

                        return params;
                    }
                };
                request.setShouldCache(true);
                Volley.newRequestQueue(context).add(request);
                contents.remove(position);
                notifyDataSetChanged();
                // contents = new ArrayList<>();
                //notifyItemRangeChanged(position, contents.size());
            }

        });

        holder.chkLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                //set your object's last status
                final String URL =  "http://172.20.10.4:3000/process/like";
                StringRequest request = new StringRequest(Request.Method.POST,
                        URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

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
                        if(holder.chkLike.isChecked()){
                            params.put("chklike", ON_CHECKED_TRUE);
                            params.put("contentid", content.getContentId());
                            params.put("userid", LoginActivity.userId);
                        }else {
                            holder.chkLike.isChecked();
                            params.put("chklike", ON_CHECKED_FALSE);
                            params.put("contentid", content.getContentId());
                            params.put("userid", LoginActivity.userId);
                        }

                        return params;
                    }
                };
                request.setShouldCache(true);
                Volley.newRequestQueue(context).add(request);

            }
        });




        holder.btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String URL = "http://172.20.10.4:3000/process/comentadd";
                StringRequest request = new StringRequest(Request.Method.POST,
                        URL, null, null
                ) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<>();
                        params.put("contentid", contents.get(position).getContentId());
                        params.put("coment", holder.etComment.getText().toString());
                        params.put("user", LoginActivity.userName);


                        return params;
                    }
                };

                request.setShouldCache(true);
                Volley.newRequestQueue(context).add(request);

            }
        });






    }

    @Override
    public int getItemCount() {
        return contents.size();

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView content;
        public TextView comment;
        public TextView name;
        public TextView date;
        public ImageView image;
        public Button btComment;
        public EditText etComment;
        public CheckBox chkLike;
        public TextView countLike;
        public CardView cardView;
        public Button btDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content1);

            cardView = (CardView) itemView.findViewById(R.id.cardview);
            name = (TextView)itemView.findViewById(R.id.name);
            date =(TextView)itemView.findViewById(R.id.date);
            btDelete = (Button)itemView.findViewById(R.id.btDelete);
            chkLike = (CheckBox) itemView.findViewById(R.id.checkBoxLike);
            image = (ImageView) itemView.findViewById(R.id.image);
            btComment = (Button) itemView.findViewById(R.id.btComment);
            etComment = (EditText) itemView.findViewById(R.id.etComment);
            countLike = (TextView) itemView.findViewById(R.id.tvLikeCount);
        }
    }
}
