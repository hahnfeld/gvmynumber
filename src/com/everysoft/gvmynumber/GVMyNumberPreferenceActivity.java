package com.everysoft.gvmynumber;

import com.everysoft.gvmynumber.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class GVMyNumberPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
