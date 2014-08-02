package de.fablab.ara.arapirate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LineView extends View {

    private Paint paint = new Paint();
    private double scrollOffset;
    private double zoom;
    private double[] samples;
    private double totalLength;
    private boolean lowActive;

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(getResources().getColor(R.color.line_color));

        zoom = 1;

        scrollOffset = 0;

        samples = new double[] {
            10, 10, 10, 10, 10, 10, 10, 10, 10, 10
        };

        totalLength = 100;
    }

    @Override
    public void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int lineMargin = (int)(height * 0.15);

        double pixelPerMs = (width - lineMargin) / (totalLength * zoom);

        int currentX = 0;
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

    public void setScrollOffset(double scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public void setLowActive(boolean lowActive) {
        this.lowActive = lowActive;
    }

    public void setData(double[] samples, double length) {
        this.samples = samples;
        this.totalLength = length;
    }
}
