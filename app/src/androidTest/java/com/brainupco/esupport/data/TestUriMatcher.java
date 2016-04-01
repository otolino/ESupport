package com.brainupco.esupport.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by jsantos on 31/mar/2016.
 */
/*
    Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
    constants that are declared with package protection inside of the UriMatcher, which is why
    the test must be in the same data package as the Android app code.  Doing the test this way is
    a nice compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String LOCATION_QUERY = "London, UK";
    private static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    // content://com.brainupco.esupport.app/location"
    private static final Uri TEST_LOCATION_DIR = ESupportContract.LocationEntry.CONTENT_URI;
    private static final Uri TEST_LOCATION_WITH_DATE_DIR = ESupportContract.LocationEntry.buildLocationUri(TEST_DATE);

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = ESupportProvider.buildUriMatcher();

        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_LOCATION_DIR), ESupportProvider.LOCATION);

        assertEquals("Error: The LOCATION WITH DATE URI was matched incorrectly.",
                testMatcher.match(TEST_LOCATION_WITH_DATE_DIR), ESupportProvider.LOCATION_WITH_DATE);
    }
}
