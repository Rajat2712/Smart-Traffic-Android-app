package sredoc.smart_traffic.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class MyTextView extends AppCompatTextView {

    public MyTextView(final Context context) {
        super(context);
    }

    public MyTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        if (attrs != null) {
            final int typefaceCode = TypefaceUtils.typefaceCodeFromAttribute(context, attrs);
            final Typeface typeface = TypefaceCache.get(context.getAssets(), typefaceCode);
            setTypeface(typeface);
        }
    }
}
