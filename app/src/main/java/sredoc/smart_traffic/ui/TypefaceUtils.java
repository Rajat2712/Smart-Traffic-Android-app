package sredoc.smart_traffic.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import sredoc.smart_traffic.R;

public class TypefaceUtils {

    private static final int[] FONT_STYLE_ATTRS = new int[]{R.attr.fontStyle};

    private TypefaceUtils() {}

    /**
     * Gets the integer value for the fontStyle attribute from an attribute set
     *
     * @param context A reference to a Context
     * @param attrs   The set of attributes
     * @return The value read from the attributeset for the font style attribute
     */
    public static int typefaceCodeFromAttribute(final Context context, final AttributeSet attrs) {

        int typefaceCode = -1;
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, FONT_STYLE_ATTRS);
        if (typedArray != null) {
            try {
                // First defined attribute
                typefaceCode = typedArray.getInt(0, -1);
            } catch (Exception ignore) {
                // Failed for some reason.
            } finally {
                typedArray.recycle();
            }
        }
        return typefaceCode;
    }
}
