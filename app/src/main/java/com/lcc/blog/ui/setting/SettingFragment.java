package com.lcc.blog.ui.setting;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.lcc.blog.R;

/**
 * Created by lcc_luffy on 2016/3/9.
 */
public class SettingFragment extends PreferenceFragmentCompat{
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.setting);
    }
}
