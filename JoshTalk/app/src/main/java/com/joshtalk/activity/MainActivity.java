package com.joshtalk.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.joshtalk.R;
import com.joshtalk.adapter.PostsDataAdapter;
import com.joshtalk.database.JoshTalkDbHelper;
import com.joshtalk.model.PostModel;
import com.joshtalk.util.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostsDataAdapter.PostsDataAdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<PostModel> contactList;
    private PostsDataAdapter mAdapter;
    JoshTalkDbHelper db;
    Boolean isConnectionExist = false;
    NetworkUtils cd;
    private Button bt_sort;

    private LinearLayoutManager mLayoutManager;
    private int pageNo = 0;
    private int perPageRecords = 7;
    private int listCount;
    private boolean isLoading = true;


    // url to fetch contacts json
    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        recyclerView = findViewById(R.id.recycler_view);
        bt_sort = findViewById(R.id.sort_by);

        contactList = new ArrayList<>();
        db = new JoshTalkDbHelper(this);
        cd = new NetworkUtils(getApplicationContext());
        isConnectionExist = cd.checkMobileInternetConn();

        if (!isConnectionExist) {
            contactList = db.getPostsData();
            if (contactList.size() > 0) {
                bt_sort.setVisibility(View.VISIBLE);
            } else {
                bt_sort.setVisibility(View.GONE);
            }

        } else {
            loadData(pageNo);
        }

        mAdapter = new PostsDataAdapter(this, contactList, this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        bt_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, bt_sort);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_main, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Date")) {
                            sortData(0);
                        } else if (item.getTitle().equals("View")) {
                            sortData(1);
                        } else if (item.getTitle().equals("Like")) {
                            sortData(2);
                        } else if (item.getTitle().equals("Share")) {
                            sortData(3);
                        }

                        mAdapter = new PostsDataAdapter(MainActivity.this, contactList, MainActivity.this);
                        recyclerView.setAdapter(mAdapter);

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isConnectionExist) {
                    checkIfItsLastPage();
                }
            }
        });

    }


    private void sortData(final int type) {

        if (type == 0) {
            Collections.sort(contactList, PostModel.date);
        } else if (type == 1) {
            Collections.sort(contactList, PostModel.view);
        } else if (type == 2) {
            Collections.sort(contactList, PostModel.like);
        } else if (type == 3) {
            Collections.sort(contactList, PostModel.share);

        }
    }

    /**
     * fetches json by making http calls
     */
    private void loadData(int which) {
        isLoading = false;

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        if (which == 0) {
            URL = "http://www.mocky.io/v2/59b3f0b0100000e30b236b7e";
            perPageRecords = 7;
            listCount = 7;
        } else if (which == 1) {
            URL = "http://www.mocky.io/v2/59ac28a9100000ce0bf9c236";
            perPageRecords = 8;
            listCount = 15;
        } else if (which == 2) {
            URL = "http://www.mocky.io/v2/59ac293b100000d60bf9c239";
            perPageRecords = 7;
            listCount = 22;
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            progressBar.setVisibility(View.GONE);

                            List<PostModel> items = new ArrayList<>();


                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response.toString());

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray heroArray = obj.getJSONArray("posts");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

//                                items = new Gson().fromJson(heroObject.toString(), new TypeToken<List<PostModel>>() {
//                                }.getType());

                                PostModel hero = new PostModel(heroObject.getString("id"), heroObject.getString("thumbnail_image"), heroObject.getString("event_name"), heroObject.getLong("event_date"), heroObject.getLong("views"), heroObject.getLong("likes"), heroObject.getLong("shares"));
                                items.add(hero);
                            }
                            // adding contacts to contacts list
                            // contactList.clear();
                            contactList.addAll(items);
                            boolean b = db.postsData(contactList);
                            Log.d("", String.valueOf(b));

                            // refreshing recycler view
                            mAdapter.notifyDataSetChanged();
                            isLoading = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("Volley", "Error");
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                isLoading = true;
            }
        });

        MyApplication.getInstance().addToRequestQueue(jsonObjReq);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPostDataSelected(PostModel contact) {
        Toast.makeText(getApplicationContext(), "Selected: " + contact.getId() + ", " + contact.getEvent_name(), Toast.LENGTH_LONG).show();
    }

    private void checkIfItsLastPage() {

        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

        if (isLoading) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                if (shouldCallApi()) {
                    ++pageNo;
                    loadData(pageNo);
                }
            }
        }

    }

    private boolean shouldCallApi() {
        return this.pageNo * perPageRecords <= listCount;
    }

}
