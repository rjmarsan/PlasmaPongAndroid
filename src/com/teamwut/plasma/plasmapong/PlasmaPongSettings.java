package com.teamwut.plasma.plasmapong;

/*
 * Copyright (C) 2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */



import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.teamwut.plasma.plasmapong.pong.Const;

public class PlasmaPongSettings extends PreferenceActivity {

    @Override
    protected void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        getPreferenceManager().setSharedPreferencesName(
                Const.SHARED_PREF_NAME);
        addPreferencesFromResource(R.xml.pongsettings);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}

