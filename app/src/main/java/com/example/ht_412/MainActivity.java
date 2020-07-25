package com.example.ht_412;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TITLE = "TITLE";
    private static final String SUBTITLE = "SUBTITLE";
    private static final String SHARED_PREFS_NAME = "SHARED_PREFS_NAME";
    private static final String SHARED_PREFS_KEY = "SHARED_PREFS_KEY";
    List<Map<String, String>> lines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lines = prepareContent();
        final BaseAdapter adapter = createAdapter(lines);

        init();

        ListView list = findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lines.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        list.setAdapter(adapter);

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lines.clear();
                init();
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    public void init() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        String savedString = sharedPreferences.getString(SHARED_PREFS_KEY, "");
        if (savedString.isEmpty()) {

            lines.addAll(prepareContent());
            sharedPreferences.edit().putString(SHARED_PREFS_KEY, String.valueOf(lines)).apply();
        } else lines.addAll(prepareContent());
    }

    public List<Map<String, String>> prepareContent() {
        String[] lines = getString(R.string.large_text).split("\n\n");
        List<Map<String, String>> list = new ArrayList<>();
        for (String string :
                lines) {
            Map<String, String> map = new HashMap<>();
            map.put(TITLE, string);
            map.put(SUBTITLE, string.length() + "");
            list.add(map);
        }
        return list;
    }

    public BaseAdapter createAdapter(List<Map<String, String>> list) {
        String[] from = {TITLE, SUBTITLE};
        int[] to = {R.id.text1, R.id.text2};
        return new SimpleAdapter(this, list, R.layout.textviews, from, to);
    }
}