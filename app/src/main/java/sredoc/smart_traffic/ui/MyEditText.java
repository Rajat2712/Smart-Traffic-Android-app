package sredoc.smart_traffic.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by aditya on 29/06/17.
 */

public class MyEditText extends AppCompatEditText {

    private ImeBackListener imeBackListener;

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
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

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (imeBackListener != null)
                imeBackListener.onImeBack(this);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setImeBackListener(ImeBackListener imeBackListener) {
        this.imeBackListener = imeBackListener;
    }

    public interface ImeBackListener {
        void onImeBack(MyEditText editText);
    }
}

