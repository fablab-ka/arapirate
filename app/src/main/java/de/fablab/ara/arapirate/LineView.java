package de.fablab.ara.arapirate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;

public class LineView extends View {

    private static final int INVALID_POINTER_ID = -1;
    private LineScrollOffsetChangeListener scrollOffsetChangeListener = null;

    private Paint paint = new Paint();
    private double scrollOffset;
    private double zoom;
    private double[] samples;
    private double totalLength;
    private boolean lowActive;

    private int mActivePointerId = INVALID_POINTER_ID;
    private float mLastTouchX;
    private ScaleGestureDetector mScaleDetector;

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(getResources().getColor(R.color.line_color));

        zoom = 1;

        scrollOffset = 0;

        samples = new double[0];

        totalLength = 1;

        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public void setScrollOffsetChangeListener(LineScrollOffsetChangeListener listener) {
        this.scrollOffsetChangeListener = listener;
    }

    private int getLineMargin() {
        int height = getHeight();
        return (int)(height * 0.15);
    }

    private double getPixelPerMs() {
        int width = getWidth();
        return (width - getLineMargin()) / (totalLength * zoom);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int height = getHeight();
        int lineMargin = getLineMargin();

        double pixelPerMs = getPixelPerMs();

        int currentX = (int)this.scrollOffset;
        boolean isDown = lowActive;

        for (double sample : samples) {
            int pixelSample = (int)(sample * pixelPerMs);

            Log.d("pixel sample ", Integer.toString(pixelSample));

            if (isDown) {
                canvas.drawLine(currentX, lineMargin, currentX + pixelSample, lineMargin, paint);
            } else {
                canvas.drawLine(currentX, height - lineMargin, currentX + pixelSample, height - lineMargin, paint);
            }

            canvas.drawLine(currentX + pixelSample, height - lineMargin, currentX + pixelSample, lineMargin, paint);

            currentX += pixelSample;
            isDown = !isDown;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        mScaleDetector.onTouchEvent(event);

        switch(action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final float x = MotionEventCompat.getX(event, pointerIndex);

                mLastTouchX = x;
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);

                final float x = MotionEventCompat.getX(event, pointerIndex);

                final float dx = x - mLastTouchX;

                this.scrollOffset += dx;

                mLastTouchX = x;

                int width = getWidth();
                double pixelPerMs = getPixelPerMs();

                if (this.scrollOffset < -width / 2) {
                    this.scrollOffset = -width / 2;
                } else if (this.scrollOffset > pixelPerMs * totalLength) {
                    this.scrollOffset = pixelPerMs * totalLength;
                }

                if (this.scrollOffsetChangeListener != null) {
                    this.scrollOffsetChangeListener.onScrollOffsetChanged(this.scrollOffset);
                }

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);

                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(event, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    public void setScrollOffset(double scrollOffset) {
        this.scrollOffset = scrollOffset;
        invalidate();
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        invalidate();
    }

    public void setData(Channel channel) {
        this.samples = channel.getSamples();
        this.totalLength = channel.getRecordingLength();
        invalidate();
    }

    public interface LineScrollOffsetChangeListener {
        public void onScrollOffsetChanged(double scrollOffset);

        void onZoomChanged(double zoom);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            zoom *= detector.getScaleFactor();

            zoom = Math.max(0.1f, Math.min(zoom, 5.0f));

            scrollOffsetChangeListener.onZoomChanged(zoom);
            return true;
        }
    }
}
