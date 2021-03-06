package com.example.android.bgdb.model.task;

import android.net.Uri;
import android.util.Log;

import com.example.android.bgdb.model.SearchType;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Builds the {@link URL} using the provided {@link SearchType}.
 */
class UrlBuilder {

    private static final String TAG = UrlBuilder.class.getSimpleName();
    private static final String BASE_BGG_URL = "https://www.boardgamegeek.com/";
    private static final String API = "xmlapi2";
    private static final String HOT = "hot";
    private static final String BROWSE = "browse";
    private static final String TYPE = "type";
    private static final String BOARD_GAME = "boardgame";
    private static final String THING = "thing";
    private static final String ID = "id";
    private static final String STATS = "stats";

    private UrlBuilder() {

    }

    static URL getListUrl(SearchType searchType) {
        Uri uri = getListUri(searchType);
        if (uri == null) {
            return null;
        }
        URL url;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error getting url.", e);
            return null;
        }
        return url;
    }

    private static Uri getListUri(SearchType searchType) {
        Uri uri = Uri.parse(BASE_BGG_URL);
        switch (searchType) {
            case HOT:
                uri = uri.buildUpon()
                        .appendPath(API)
                        .appendPath(HOT)
                        .appendQueryParameter(TYPE, BOARD_GAME)
                        .build();
                break;
            case TOP:
                uri = uri.buildUpon()
                        .appendPath(BROWSE)
                        .appendPath(BOARD_GAME)
                        .build();
                break;
            default:
                return null;
        }
        return uri;
    }

    static URL getDetailUrl(String apiId) {
        Uri uri = getDetailUri(apiId);
        if (uri == null) {
            return null;
        }
        URL url;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error getting url.", e);
            return null;
        }
        return url;
    }

    private static Uri getDetailUri(String apiId) {
        return Uri.parse(BASE_BGG_URL)
                .buildUpon()
                .appendPath(API)
                .appendPath(THING)
                .appendQueryParameter(ID, apiId)
                .appendQueryParameter(STATS, "1")
                .build();
    }
}
