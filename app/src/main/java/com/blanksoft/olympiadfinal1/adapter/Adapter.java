package com.blanksoft.olympiadfinal1.adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blanksoft.olympiadfinal1.activity.CommentActivity;
import com.blanksoft.olympiadfinal1.model.Content;
import com.blanksoft.olympiadfinal1.activity.LoginActivity;
import com.blanksoft.olympiadfinal1.activity.MainActivity;
import com.blanksoft.olympiadfinal1.activity.ModifyActivity;
import com.blanksoft.olympiadfinal1.fragment.PinDropFragment;
import com.blanksoft.olympiadfinal1.activity.PostDetailActivity;
import com.blanksoft.olympiadfinal1.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    List<Content> contents;
    boolean like;
    final String ON_CHECKED_TRUE = "1";
    final String ON_CHECKED_FALSE = "0";
    PopupMenu popup;


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
        if(content.getComchk().equals("1")){
            holder.chk.setVisibility(View.VISIBLE);

        }else {
            holder.chk.setVisibility(View.GONE);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        /*if(!content.getImage().equals(null)) {
            content.getImage().compress(Bitmap.CompressFormat.PNG, 60, stream);
            Glide.with(context)
                    .load(stream.toByteArray())
                    .asBitmap()
                    .error(R.drawable.ic_baseline_camera_alt_24px)
                    .into(holder.image);
        }*/

        //holder.image.setImageBitmap(content.getImage());

            if(content.getImage()==null){
             holder.image.setImageResource(0);
          }else{
        content.getImage().compress(Bitmap.CompressFormat.JPEG, 60, stream);
        Glide.with(context)
                .asBitmap()
                .load(stream.toByteArray())
                .into(holder.image);
       }

        holder.countLike.setText(content.getLike());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            Intent intent=new Intent(context,PostDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            @Override
            public void onClick(View v) {

                intent.putExtra("contentid", contents.get(position).getContentId());
                intent.putExtra("name", contents.get(position).getName());

                context.startActivity(intent);


            }
        });
        holder.chkLike.setOnCheckedChangeListener(null);
        if(content.getLikechk().equals("0")){
            holder.chkLike.setChecked(true);
        }else{
            holder.chkLike.setChecked(false);
        }

       /* holder.btDelete.setOnClickListener(new View.OnClickListener() {
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

        });*/


               holder.chkLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                       //set your object's last status
                       final String URL =  "http://218.155.147.128:3000/process/like";
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
                                   int a1=Integer.valueOf(content.getLike());

                                   params.put("chklike", ON_CHECKED_TRUE);
                                   params.put("contentid", content.getContentId());
                                   params.put("userid", LoginActivity.userId);
                               }else {
                                   holder.chkLike.isChecked();
                                   int a1=Integer.valueOf(content.getLike());

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


        holder.cmlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context, CommentActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.d("aid", content.getContentId());
                    intent.putExtra("contentId", content.getContentId());
                    intent.putExtra("kind", "content");
                    context.startActivity(intent);



            }
        });
        holder.btPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(context, holder.btPopup);

                popup.inflate(R.menu.menu_item);

                if (!LoginActivity.userName.equals(contents.get(position).getName())) {
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(2).setVisible(false);
                    popup.getMenu().getItem(3).setVisible(false);
                }
                if(content.getComchk().equals("1")){
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(1).setVisible(false);

                }
                if(!content.getName().equals(LoginActivity.userName)){
                    popup.getMenu().getItem(0).setVisible(false);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                final String URL = "http://218.155.147.128:3000/process/remove";
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
                                request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                                        200000 ,

                                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                Volley.newRequestQueue(context).add(request);
                                contents.remove(position);
                                notifyDataSetChanged();

                                return true;

                            case R.id.menu_modify:

                                Intent intent = new Intent(context, ModifyActivity.class);
                                intent.putExtra("modifyText", contents.get(position).getContent());
                                intent.putExtra("contentid", contents.get(position).getContentId());
                                intent.putExtra("kind", "post");

                                context.startActivity(intent);
                               // ((MainActivity)context).finish();
                                return true;
                            case R.id.menu_map:
                                final String URL2 = "http://218.155.147.128:3000/process/coment";
                                StringRequest request2 = new StringRequest(Request.Method.POST,
                                        URL2,new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);

                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                Double mlat = jsonObject.optDouble("makerlat");
                                                Double mlong = jsonObject.optDouble("makerlong");
                                                Fragment fragment = new PinDropFragment(); // Fragment 생성
                                                Bundle bundle = new Bundle(2); // 파라미터는 전달할 데이터 개수
                                                bundle.putDouble("mLat",mlat ); // key ,
                                                bundle.putDouble("mLong", mlong);
                                                fragment.setArguments(bundle);
                                            FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                                            transaction.replace(R.id.content, fragment).commit();


                                                //Log.d("comment", Array.getString(j));


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
                                request2.setShouldCache(true);
                                request2.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                                        200000 ,


                                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                Volley.newRequestQueue(context).add(request2);


                                return true;
                            case R.id.menu_complete:
                                popup.getMenu().getItem(0).setVisible(false);
                                final String URL1 = "http://218.155.147.128:3000/bComplete";
                                StringRequest request1 = new StringRequest(Request.Method.POST,
                                        URL1,new Response.Listener<String>() {
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
                                        params.put("bComplete", "1");

                                        return params;
                                    }
                                };
                                request1.setShouldCache(true);
                                request1.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                                        200000 ,

                                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                Volley.newRequestQueue(context).add(request1);
                                return true;

                        }
                        return false;


                    }
                });

                popup.show();
            }
        });
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
                params.put("userid", content.getName());
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
        return contents.size();

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView content;
        public TextView comment;
        public TextView name;
        public TextView date;
        public ImageView image;
        public ViewGroup cmlayout;
        public ViewGroup chkLayout;
        public EditText etComment;
        public CheckBox chkLike;
        public TextView countLike;
        public CardView cardView;
        public ImageButton btPopup;
        public ImageView chk;
        public ImageView profileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content1);
            cmlayout = (ViewGroup) itemView.findViewById(R.id.comment);
            chkLayout = (ViewGroup) itemView.findViewById(R.id.chklayout);
            btPopup = (ImageButton) itemView.findViewById(R.id.imageButton2);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            name = (TextView)itemView.findViewById(R.id.name);
            date =(TextView)itemView.findViewById(R.id.date);
            chk = (ImageView)itemView.findViewById(R.id.check);
            chkLike = (CheckBox) itemView.findViewById(R.id.checkBoxLike);
            image = (ImageView) itemView.findViewById(R.id.image);

            countLike = (TextView) itemView.findViewById(R.id.tvLikeCount);
        }
    }
}
