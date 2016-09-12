package co.brighterbrain.glide.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.brighterbrain.app.AppController.R;
import co.brighterbrain.glide.adapter.GalleryAdapter;
import co.brighterbrain.glide.adapter.GalleryAdapter.Image;
import co.brighterbrain.glide.app.AppController;

import static co.brighterbrain.glide.adapter.GalleryAdapter.*;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "http://api.androidhive.info/json/glide.json";
    private ArrayList<GalleryAdapter.Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button verticalView = (Button) findViewById(R.id.verticalview);
        Button horiozntalView = (Button) findViewById(R.id.horizontalview);
        Button gridView = (Button) findViewById(R.id.gridview);
        Button staggeredgridView = (Button) findViewById(R.id.staggerdgridview);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);
        recyclerView.setAdapter(mAdapter);

        verticalView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,false));


            }
        });

        horiozntalView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false));

            }
        });
        staggeredgridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL
                ));
            }
        });
        gridView.setOnClickListener(new  View.OnClickListener(){


            @Override
            public void onClick(View view) {
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }
        });
        fetchImages();
    
          
    }

    private void fetchImages() {

        pDialog.setMessage("Downloading JSon..");
        pDialog.show();

        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET,
                endpoint,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        images.clear();
                        for (int i = 0; response.length() > i; i++) {
                            try {
                                Log.d(TAG, "Retrieving JSON Object " + i);
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();


                                image.setName(object.getString("name"));

                                JSONObject url = object.getJSONObject("url");
                                image.setSmall(url.getString("small"));
                                image.setMedium(url.getString("medium"));
                                image.setLarge(url.getString("large"));
                                image.setTimestamp(object.getString("timestamp"));

                                images.add(image);

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
        requestQueue.add(req);
    }

    }


