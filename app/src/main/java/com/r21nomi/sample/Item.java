package com.r21nomi.sample;

import android.net.Uri;

/**
 * Created by r21nomi on 2016/12/11.
 */

public class Item {
    private String title;
    private String description;
    private Uri thumb;

    public Item(String title, String description, Uri thumb) {
        this.title = title;
        this.description = description;
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Uri getThumb() {
        return thumb;
    }
}
