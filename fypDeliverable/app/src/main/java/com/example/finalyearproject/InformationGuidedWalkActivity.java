package com.example.finalyearproject;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class InformationGuidedWalkActivity extends AppCompatActivity {

    private static Map<String, String> webScrape;
    private static String webString;
    private LinearLayout layout;
    private TextView text;
    private ImageView image;
    private TextView tv;
    private TextView tv2;

    public static boolean getInstance() {
        return instance;
    }

    public static void setInstance(boolean instance) {
        InformationGuidedWalkActivity.instance = instance;
    }

    private static boolean instance = false;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInstance(true);
        setContentView(R.layout.activity_scrolling);
        Bundle bundle = getIntent().getExtras();

        String thumbnail = bundle.getString("thumbnailURL");

        layout = findViewById(R.id.linearLayout);
        text = findViewById(R.id.content);
        image = findViewById(R.id.backdrop);

        Glide.with(getApplicationContext()).load(thumbnail).into(image);

        getText();

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });




    }

    public static void setMapContent(Map<String, String> x){
        webScrape = x;
    }

    public static void setStringContent(String x){
        webString = x;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_voice) {
            Iterator it = webScrape.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String,String> pair = (Map.Entry)it.next();

                String x = pair.getKey();
                String y = pair.getValue();

                tts.speak(y, TextToSpeech.QUEUE_ADD, null);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getText(){
        if(webScrape.size()>0) {
            Iterator it = webScrape.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = (Map.Entry) it.next();

                String x = pair.getKey();
                String y = pair.getValue();

                tv = new TextView(this);
                tv.setText(x);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(tv);

                tv2 = new TextView(this);
                tv2.setText(y);
                tv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(tv2);
            }
        }else{
            tv2 = new TextView(this);
            tv2.setText(webString);
            tv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(tv2);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webString="";
        webScrape.clear();
        setInstance(false);
        return;
    }

}
