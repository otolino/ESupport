package com.brainupco.esupport.data;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.brainupco.esupport.data.ESupportContract.LocationEntry;
/**
 * Created by jsantos on 31/mar/2016.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
 This test checks to make sure that the content provider is registered correctly.
 Students: Uncomment this test to make sure you've correctly registered the WeatherProvider.
*/
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                ESupportProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + ESupportContract.CONTENT_AUTHORITY,
                    providerInfo.authority, ESupportContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
        This test doesn't touch the database.  It verifies that the ContentProvider returns
        the correct type for each type of URI that it can handle.
        Students: Uncomment this test to verify that your implementation of GetType is
        functioning correctly.
     */
    public void testGetType() {
        // content://com.brainupco.esupport/location/
        String type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.brainupco.esupport/weather
        assertEquals("Error: the LocationEntry CONTENT_URI should return LocationEntry.CONTENT_TYPE",
                LocationEntry.CONTENT_TYPE, type);

        long testDate = 1419120000L; // December 21st, 2014
        // content://com.brainupco.esupport/location/20140612
        type = mContext.getContentResolver().getType(
                LocationEntry.buildLocationUri(testDate));
        // vnd.android.cursor.item/com.brainupco.esupport/weather/1419120000
        assertEquals("Error: the LocationEntry CONTENT_URI with date should return LocationEntry.CONTENT_ITEM_TYPE",
                LocationEntry.CONTENT_ITEM_TYPE, type);

    }

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                LocationEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals("Error: Records not deleted from Location table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }


}
