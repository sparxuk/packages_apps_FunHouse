/*
 * Copyright (C) 2014-2016 The Dirty Unicorns Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gzr.funhouse.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.gzr.funhouse.preference.CustomSeekBarPreference;
import com.gzr.funhouse.preference.SystemSettingSwitchPreference;
 
public class ButtonBacklights extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private CustomSeekBarPreference mBrightness;
    private CustomSeekBarPreference mTimeout;
    private SystemSettingSwitchPreference mEnabled;
    private SystemSettingSwitchPreference mTouch;
    private SystemSettingSwitchPreference mScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.button_lights);

        final ContentResolver resolver = getActivity().getContentResolver();

        boolean isEnabled = Settings.System.getIntForUser(resolver,
                Settings.System.BUTTON_BACKLIGHT_ENABLE, 1, UserHandle.USER_CURRENT) == 1;
        mEnabled = (SystemSettingSwitchPreference) findPreference("button_backlight_enable");
        mEnabled.setChecked(isEnabled);
        mEnabled.setOnPreferenceChangeListener(this);

        boolean isTouch = Settings.System.getIntForUser(resolver,
                Settings.System.BUTTON_BACKLIGHT_ON_TOUCH_ONLY, 1, UserHandle.USER_CURRENT) == 1;
        mTouch = (SystemSettingSwitchPreference) findPreference("button_backlight_on_touch_only");
        mTouch.setChecked(isTouch);
        mTouch.setOnPreferenceChangeListener(this);

        boolean isScreen = Settings.System.getIntForUser(resolver,
                Settings.System.CUSTOM_BUTTON_USE_SCREEN_BRIGHTNESS, 1, UserHandle.USER_CURRENT) == 1;
        mScreen = (SystemSettingSwitchPreference) findPreference("custom_button_use_screen_brightness");
        mScreen.setChecked(isScreen);
        mScreen.setOnPreferenceChangeListener(this);

        int value = Settings.System.getIntForUser(resolver,
                Settings.System.CUSTOM_BUTTON_BRIGHTNESS, 1, UserHandle.USER_CURRENT);
        mBrightness = (CustomSeekBarPreference) findPreference("custom_button_brightness");
        mBrightness.setValue(value);
        mBrightness.setOnPreferenceChangeListener(this);
        mBrightness.setEnabled(isEnabled);

        int timeoutValue = Settings.System.getIntForUser(resolver,
                Settings.System.BUTTON_BACKLIGHT_TIMEOUT, 1, UserHandle.USER_CURRENT);
        mTimeout = (CustomSeekBarPreference) findPreference("button_backlight_timeout");
        mTimeout.setValue(timeoutValue);
        mTimeout.setOnPreferenceChangeListener(this);
        mTimeout.setEnabled(isEnabled);

    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.VALIDUS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mEnabled) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.BUTTON_BACKLIGHT_ENABLE, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            mEnabled.setChecked(value);
            mTimeout.setEnabled(value);
            mBrightness.setEnabled(value);
            return true;
        } else if (preference == mTouch) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.BUTTON_BACKLIGHT_ON_TOUCH_ONLY, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            mTouch.setChecked(value);
            return true;
        } else if (preference == mScreen) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.CUSTOM_BUTTON_USE_SCREEN_BRIGHTNESS, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            mScreen.setChecked(value);
            return true;
        } else if (preference == mBrightness) {
            int val = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.CUSTOM_BUTTON_BRIGHTNESS, val,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mTimeout) {
            int val = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.BUTTON_BACKLIGHT_TIMEOUT, val,
                    UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }
}
