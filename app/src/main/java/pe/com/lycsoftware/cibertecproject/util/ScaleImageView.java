package pe.com.lycsoftware.cibertecproject.util;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
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
    private RectF mCurrentViewport = new RectF(getX(), getY(), getWidth(), getHeight());
    private RectF mContentRect;

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
        mContentRect = new RectF(getX(), getY(), getWidth(), getHeight());
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

    private void setViewportBottomLeft(float x, float y) {
        /*
         * Constrains within the scroll range. The scroll range is simply the viewport
         * extremes (AXIS_X_MAX, etc.) minus the viewport size. For example, if the
         * extremes were 0 and 10, and the viewport size was 2, the scroll range would
         * be 0 to 8.
         */

        float curWidth = mCurrentViewport.width();
        float curHeight = mCurrentViewport.height();
        x = Math.max(getX(), Math.min(x, getWidth() - curWidth));
        y = Math.max(getY() + curHeight, Math.min(y, getHeight()));

        mCurrentViewport.set(x, y - curHeight, x + curWidth, y);

        // Invalidates the View to update the display.
        //ViewCompat.postInvalidateOnAnimation(this);
        //postInvalidateOnAnimation();
    }

    private class ScrollListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            /*float viewportOffsetX = distanceX * mCurrentViewport.width()
                    / mContentRect.width();
            float viewportOffsetY = -distanceY * mCurrentViewport.height()
                    / mContentRect.height();
            // Updates the viewport, refreshes the display.
            setViewportBottomLeft(
                    mCurrentViewport.left + viewportOffsetX,
                    mCurrentViewport.bottom + viewportOffsetY);*/
            scrollX += distanceX;
            scrollY += distanceY;

            //scrollX = Math.max(0f, Math.min(scrollX, getDrawable().getIntrinsicWidth()));
            //scrollY = Math.max(0f, Math.min(scrollY, getDrawable().getIntrinsicHeight()));
            scrollX = Math.max(0f, Math.min(scrollX, getWidth()));
            scrollY = Math.max(0f, Math.min(scrollY, getHeight()));

            scrollTo((int) scrollX, (int) scrollY);
            return true;
        }

    }
}
