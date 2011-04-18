package util;

import org.dijonparking.gui.ListParking;

import greendroid.app.GDApplication;

public class DijonParkingApplication extends GDApplication {

    @Override
    public Class<?> getHomeActivityClass() {
        return ListParking.class;
    }
}
