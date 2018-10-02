package pe.com.lycsoftware.cibertecproject.util;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class ScaleImageView extends AppCompatImageView {

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetectorCompat scrollGestureDetector;
    private float scaleFactor = 1f;

    private float scrollX = getScrollX();
    private float scrollY = getScrollY();

    public ScaleImageView(Context context) {
        super(context);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        scrollGestureDetector = new GestureDetectorCompat(getContext(), new ScrollListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        scrollGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            Matrix matrix = new Matrix(getImageMatrix());
            matrix.setScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            setImageMatrix(matrix);

            return true;
        }
    }

    private class ScrollListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollX += distanceX;
            scrollY += distanceY;

            scrollX = Math.max(0f, Math.min(scrollX, getDrawable().getIntrinsicWidth()));
            scrollY = Math.max(0f, Math.min(scrollY, getDrawable().getIntrinsicHeight()));

            scrollTo((int) scrollX, (int) scrollY);
            return true;
        }
    }
}
