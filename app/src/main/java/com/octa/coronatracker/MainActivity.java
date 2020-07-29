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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btn_sync;
    TextView textView, textViewActive, textViewDeath, textViewRecovered,
            textViewDelActive, textViewDelRecovered, textViewDelDeath;
    String[] casesDataArray;
    String active, delActive, recovered, delrecovered, death, delDeath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sync = findViewById(R.id.btn_sync);
        textView = findViewById(R.id.tvTitle);
        textViewActive = findViewById(R.id.textViewActive);
        textViewDelActive = findViewById(R.id.textViewDelActive);
        textViewRecovered = findViewById(R.id.textViewRecovered);
        textViewDelRecovered = findViewById(R.id.textViewDelRecovered);
        textViewDeath = findViewById(R.id.textViewDeath);
        textViewDelDeath = findViewById(R.id.textViewDelDeath);

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite();

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
                    textView.setVisibility(View.VISIBLE);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Split the text to array
                        textView.setText(builder);
                        casesDataArray = textView.getText().toString().split(" ");

                        Log.d("Mytag", "cases data array");
                        for(int i=0; i< casesDataArray.length; i++){
                            Log.d("Mytag", casesDataArray[i]);
                        }

                        getData();
                        setData();

                    }
                });
            }
        }).start();
    }

    private void getData() {
        active = casesDataArray[0];
        delActive = casesDataArray[1].substring(1, casesDataArray[1].length()-1);
        recovered = casesDataArray[2];
        delrecovered = casesDataArray[3].substring(1, casesDataArray[3].length()-1);
        death = casesDataArray[4];
        delDeath = casesDataArray[5].substring(1, casesDataArray[5].length()-1);
    }

    private void setData() {
        textViewActive.setText(active);
        textViewDelActive.setText("+" + delActive);
        textViewRecovered.setText(recovered);
        textViewDelRecovered.setText("+" + delrecovered);
        textViewDeath.setText(death);
        textViewDelDeath.setText("+" + delDeath);
    }
}