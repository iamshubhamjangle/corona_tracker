package com.octa.coronatracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView textView, textViewActive, textViewDeath, textViewRecovered, textViewTotal,
            textViewDelActive, textViewDelRecovered, textViewDelDeath;
    String[] casesDataArray;
    int active, delActive, recovered, delRecovered, death, delDeath, total;
    Boolean flag = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tvTitle);
        textViewActive = findViewById(R.id.textViewActive);
        textViewDelActive = findViewById(R.id.textViewDelActive);
        textViewRecovered = findViewById(R.id.textViewRecovered);
        textViewDelRecovered = findViewById(R.id.textViewDelRecovered);
        textViewDeath = findViewById(R.id.textViewDeath);
        textViewDelDeath = findViewById(R.id.textViewDelDeath);
        textViewTotal = findViewById(R.id.textViewTotal);

        getWebsite();

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
                    flag = Boolean.TRUE;
                }catch (IOException e){
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    builder.append(getString(R.string.connectionError));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Split the text to array
                        textView.setText(builder);

//                        Log.d("Mytag", "cases data array");
//                        for(int i=0; i< casesDataArray.length; i++){
//                            Log.d("Mytag", casesDataArray[i]);
//                        }
                        if(flag == Boolean.TRUE) {
                            textView.setVisibility(View.INVISIBLE);
                            casesDataArray = textView.getText().toString().split(" ");
                            getData();
                            setData();
                        }else{
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

    private void getData() {
        active = Integer.parseInt(casesDataArray[0]);
        delActive = Integer.parseInt(casesDataArray[1].substring(1, casesDataArray[1].length()-1));
        recovered = Integer.parseInt(casesDataArray[2]);
        delRecovered = Integer.parseInt(casesDataArray[3].substring(1, casesDataArray[3].length()-1));
        death = Integer.parseInt(casesDataArray[4]);
        delDeath = Integer.parseInt(casesDataArray[5].substring(1, casesDataArray[5].length()-1));
        total = active + recovered + death;
    }

    private void setData() {
        textViewActive.setText("" + active);
        textViewDelActive.setText("+" + delActive);
        textViewRecovered.setText("" + recovered);
        textViewDelRecovered.setText("+" + delRecovered);
        textViewDeath.setText("" + death);
        textViewDelDeath.setText("+" + delDeath);
        textViewTotal.setText("" + total);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_sync:
                getWebsite();
                return true;
            case R.id.action_about:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}