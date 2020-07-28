package com.octa.coronatracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button btn_sync;
    TextView textView;
    int active, delActive, discharged, delDischarged, death, delDeath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sync = findViewById(R.id.btn_sync);
        textView = findViewById(R.id.textView);

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Mytag", "Button Clicked");
                getWebsite();
                splitTheText();
            }
        });




    }

    private void getWebsite() {
        Log.d("Mytag", "Get Website Processing started");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document document = Jsoup.connect(getString(R.string.web_url)).get();
                    for (Element row : document.select("div.col-xs-8.site-stats-count")) {
                        builder.append(row.select("span.mob-show strong").text());
                    }
                }catch (IOException e){
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }

    private void splitTheText() {
        Log.d("Mytag", "Split the text started");
        String[] casesDataArr = textView.getText().toString().split(" ");

        active = Integer.parseInt(casesDataArr[0]);
        delActive = Integer.parseInt(casesDataArr[1].substring(1, casesDataArr[1].length()-1));
        discharged = Integer.parseInt(casesDataArr[2]);
        delDischarged = Integer.parseInt(casesDataArr[3].substring(1, casesDataArr[3].length()-1));
        death = Integer.parseInt(casesDataArr[4]);
        delDeath = Integer.parseInt(casesDataArr[5].substring(1, casesDataArr[5].length()-1));

    }
}