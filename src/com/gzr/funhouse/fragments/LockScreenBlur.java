/*
 *  Copyright (C) 2015-2018 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
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

public class LockScreenBlur extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private CustomSeekBarPreference mLockscreenMediaBlur;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        addPreferencesFromResource(R.xml.lockscreenblur);

        final ContentResolver resolver = getActivity().getContentResolver();
		
        int defaultBlur = 25;

        int value = Settings.System.getIntForUser(resolver,
                Settings.System.OMNI_LOCKSCREEN_MEDIA_BLUR, defaultBlur, UserHandle.USER_CURRENT);
        mLockscreenMediaBlur = (CustomSeekBarPreference) findPreference("lockscreen_media_blur");
        mLockscreenMediaBlur.setValue(value);
        mLockscreenMediaBlur.setOnPreferenceChangeListener(this);
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
        if (preference == mLockscreenMediaBlur) {
            int val = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.OMNI_LOCKSCREEN_MEDIA_BLUR, val,
					UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }
}

