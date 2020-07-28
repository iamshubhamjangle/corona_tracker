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
    Boolean errorFlag = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sync = findViewById(R.id.btn_sync);
        textView = findViewById(R.id.textView);

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag = getWebsite();
                if(flag==Boolean.TRUE){
                    splitTheText();
                }

            }
        });


    }
    private boolean getWebsite() {
        Log.d("Mytag", "Get Website text here");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document document = Jsoup.connect(getString(R.string.web_url)).get();
                    for (Element row : document.select("div.col-xs-8.site-stats-count")) {
                        builder.append(row.select("span.mob-show strong").text());
                    }
                    errorFlag = Boolean.TRUE;
                }catch (IOException e){
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(builder);
                    }
                });
            }
        }).start();
        return errorFlag;
    }


    private void splitTheText() {
        Log.d("Mytag", "Splitted text here");
    }
}