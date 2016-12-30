package com.r21nomi.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.r21nomi.sample.Item;
import com.r21nomi.sample.ItemAdapter;
import com.r21nomi.sample.R;
import com.r21nomi.sample.ResourceUtil;

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
            public void onClick(View blobLayout, Item item) {
                Intent intent = DetailActivity.createIntent(MainActivity.this, blobLayout);
                // Fade activity background if over LOLLIPOP.
                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this,
                        blobLayout,
                        ""
                ).toBundle();
                startActivity(intent, options);
                overridePendingTransition(0, 0);
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
}
