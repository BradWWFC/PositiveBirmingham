package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalyearproject.Adapters.GuidedWalkLaunchListAdapter;
import com.example.finalyearproject.model.GuidedWalkPlaylist;
import com.example.finalyearproject.model.Location;
import com.example.finalyearproject.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class GuidedWalkLaunchActivity extends AppCompatActivity {

    private ArrayList<Location> playlist = new ArrayList<>();
    private NearestNeighbour nn;
    private double[] myLocation;
    private int journeyDuration;
    private TextView journeyDurationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guided_walk_launch);
        journeyDurationText = findViewById(R.id.duration);
        Button journeyStartButton = findViewById(R.id.startWalk);

        Bundle bundle = getIntent().getExtras();
        myLocation = new double[]{bundle.getDouble("positionLat"), bundle.getDouble("positionLng")};
        nn = new NearestNeighbour();
        playlist = nn.calculate(GuidedWalkPlaylist.getWalkLocations(),myLocation);
        generateAPIRequest();

        RecyclerView recyclerView = findViewById(R.id.GWrecyclerView);
        GuidedWalkLaunchListAdapter adapter = new GuidedWalkLaunchListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setGuidedWalks(playlist);


        journeyStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuidedWalkPlaylist.setWalkLocations(playlist);

                Intent intent = new Intent(GuidedWalkLaunchActivity.this, NavigationView.class);
                startActivity(intent);

                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }







    public void generateAPIRequest() {
        String http = "https://api.mapbox.com/directions/v5/mapbox/walking/" + myLocation[1]+"%2C"+ myLocation[0];
        String additions = "?alternatives=false&geometries=geojson&steps=true&access_token=pk.eyJ1IjoiYnJhZHd3ZmMiLCJhIjoiY2szNGVkaGg5MGh1bDNpcGoxdWd1bHlybSJ9.nN4gYnJZAte1-7rvkcLk9Q";

        for (Location x: playlist) {
            http = http + "%3B" + x.getLONGITUDE() + "%2C" + x.getLATITUDE();
        }
            http = http+additions;

        new RetrieveURL(http).execute();

    }
    public void setJourneyDuration(int myValue) {
        journeyDuration = myValue;
        String string = "Walk Duration: " + String.valueOf(journeyDuration) + " minutes";
        journeyDurationText.setText(string);
    }

    public class RetrieveURL extends AsyncTask<Void, Void, Integer> {

        private String http;
        String inline = "";
        private double duration;

        public RetrieveURL(String http) {
            super();
            this.http = http;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(http);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            try {
                conn.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int responsecode = 0;
            try {
                responsecode = conn.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if(responsecode != 200){
                throw new RuntimeException("HttpResponseCode: " +responsecode);}
            else
            {
                Scanner sc = null;
                try {
                    sc = new Scanner(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while(sc.hasNext())
                {
                    inline+=sc.nextLine();
                }
                sc.close();
            }

            try {
                JSONObject reader = new JSONObject(inline);

                JSONArray sys  = reader.getJSONArray("routes");


                for (int i = 0; i < sys.length(); i++) {
                    JSONObject c = sys.getJSONObject(i);
                    String durationString = c.getString("duration");
                    duration = Double.parseDouble(durationString)/60;
}

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Call activity method with results
            setJourneyDuration((int) duration);
        }

    }
}
