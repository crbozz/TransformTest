package org.sidia.transformtest;

import android.os.Bundle;

import org.gearvrf.GVRActivity;

public class MainActivity extends GVRActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setMain(new Main());
    }
}
