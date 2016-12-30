package com.r21nomi.sample.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.r21nomi.bloblayout.BlobLayout;
import com.r21nomi.sample.Item;
import com.r21nomi.sample.ItemAdapter;
import com.r21nomi.sample.R;
import com.r21nomi.sample.ResourceUtil;
import com.r21nomi.sample.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ItemAdapter adapter = new ItemAdapter(getDataSet(), new ItemAdapter.Listener() {
            @Override
            public void onClick(BlobLayout blobLayout, Item item) {
                Intent intent = DetailActivity.createIntent(MainActivity.this, blobLayout);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    // Fade activity background if over LOLLIPOP.
                    Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            MainActivity.this,
                            getPair()
                    ).toBundle();
                    startActivity(intent, options);
                } else {
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private List<Item> getDataSet() {
        return Arrays.asList(
                new Item("Item 1", "description 1", ResourceUtil.getDrawableAsUri(this, R.drawable.img)),
                new Item("Item 2", "description 2", ResourceUtil.getDrawableAsUri(this, R.drawable.img)),
                new Item("Item 3", "description 3", ResourceUtil.getDrawableAsUri(this, R.drawable.img)),
                new Item("Item 4", "description 4", ResourceUtil.getDrawableAsUri(this, R.drawable.img)),
                new Item("Item 5", "description 5", ResourceUtil.getDrawableAsUri(this, R.drawable.img)),
                new Item("Item 6", "description 6", ResourceUtil.getDrawableAsUri(this, R.drawable.img))
        );
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Pair[] getPair() {
        View statusBar = findViewById(android.R.id.statusBarBackground);
        View navigationBar = findViewById(android.R.id.navigationBarBackground);
        View actionBar = ViewUtil.getActionBar(this);

        List<Pair<View, String>> pairs = new ArrayList<>();
        if (statusBar != null) {
            pairs.add(Pair.create(statusBar, statusBar.getTransitionName()));
        }
        if (navigationBar != null) {
            pairs.add(Pair.create(navigationBar, navigationBar.getTransitionName()));
        }
        if (actionBar != null) {
            pairs.add(Pair.create(actionBar, actionBar.getTransitionName()));
        }
        return pairs.toArray(new Pair[pairs.size()]);
    }
}
