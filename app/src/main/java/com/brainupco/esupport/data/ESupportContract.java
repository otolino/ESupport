package com.brainupco.esupport.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.brainupco.esupport.BuildConfig;

/**
 * Created by jsantos on 29/mar/2016.
 */
public class ESupportContract {

    // Defining URI Elements for Contract
    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_LOCATION = "location";

    /* Inner class that defines the table contents of the location table */
    public static final class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        // Table name
        public static final String TABLE_NAME = "location";

        // Fields
        public static final String COLUMN_LOC_DATE = "loc_date";
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

    }
}
