
/*
 *
 *  * Copyright (C) 2015 yelo.red
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */

package sredoc.smart_traffic.ui;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Typeface cache to cache the typefaces
 */
public class TypefaceCache {

    private static final Map<String, Typeface> CACHE = new HashMap<String, Typeface>((int) (7 * 1.33f));

    public static final String SF_THIN = "fonts/SanFrancisco-Thin.ttf";
    public static final String SF_REGULAR = "fonts/SanFrancisco-Regular.ttf";
    public static final String SF_MEDIUM = "fonts/SanFrancisco-Medium.ttf";
    public static final String SF_BOLD = "fonts/SanFrancisco-Bold.ttf";

    public static Typeface get(final AssetManager manager, final int typefaceCode) {
        synchronized (CACHE) {
            final String typefaceName = getTypefaceName(typefaceCode);
            if (!CACHE.containsKey(typefaceName)) {
                final Typeface t = Typeface
                        .createFromAsset(manager, typefaceName);
                CACHE.put(typefaceName, t);
            }
            return CACHE.get(typefaceName);
        }
    }

    public static Typeface get(final AssetManager manager,
                               final String typefaceName) {
        return get(manager, getCodeForTypefaceName(typefaceName));
    }

    private static int getCodeForTypefaceName(final String typefaceName) {
        switch (typefaceName) {
            case SF_THIN:
                return 0;
            case SF_REGULAR:
                return 1;
            case SF_MEDIUM:
                return 2;
            case SF_BOLD:
                return 3;
            default:
                return 1;
        }
    }

    private static String getTypefaceName(final int typefaceCode) {
        switch (typefaceCode) {
            case 0:
                return SF_THIN;
            case 1:
                return SF_REGULAR;
            case 2:
                return SF_MEDIUM;
            case 3:
                return SF_BOLD;
            default:
                return SF_REGULAR;
        }
    }
}
