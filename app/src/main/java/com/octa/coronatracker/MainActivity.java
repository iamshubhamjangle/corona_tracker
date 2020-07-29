package com.octa.coronatracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_sync;
    TextView textView;
    String[] casesDataArray;
    PieChart pieChart;
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Entry> yvalues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sync = findViewById(R.id.btn_sync);
        textView = findViewById(R.id.textView);
        pieChart = findViewById(R.id.pie_chart);

        setupPieChart();

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite();
                splitTheText();
                updatePieChart();
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
                        textView.setText(builder);
                    }
                });
            }
        }).start();
    }

    private void splitTheText() {
        Log.d("Mytag", "split the text Processing started");
        Log.d("Mytag", textView.getText().toString());
        casesDataArray = textView.getText().toString().split(" ");
        Log.d("Mytag", "Values added to the arrayList");
    }

    private void setupPieChart() {
        yvalues.add(new Entry(1, 0));
        yvalues.add(new Entry(1, 1));
        yvalues.add(new Entry(1, 2));

        xVals.add("Active");
        xVals.add("Recovered");
        xVals.add("Death");

        PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.chart_label));
        PieData data = new PieData(xVals, dataSet);
        pieChart.setData(data);
        pieChart.setDescription(getString(R.string.chart_desc));
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieChart.animateXY(1400, 1400);
        data.setValueTextSize(13f);
    }

    private void updatePieChart() {
        double newValue1 = Integer.parseInt(casesDataArray[0]);
        double newValue2 = Integer.parseInt(casesDataArray[2]);
        double newValue3 = Integer.parseInt(casesDataArray[4]);

        Entry entry1 = new Entry((float) newValue1, 0);
        Entry entry2 = new Entry((float) newValue2, 1);
        Entry entry3 = new Entry((float) newValue3, 2);

        yvalues.add(entry1);
        yvalues.add(entry2);
        yvalues.add(entry3);

        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }
}