package de.fablab.ara.arapirate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LineView extends View {

    private Paint paint = new Paint();
    private double scrollOffset;
    private double zoom;
    private double[] data;

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(getResources().getColor(R.color.line_color));
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawLine(0, 0, 20, 20, paint);
        canvas.drawLine(20, 0, 0, 20, paint);
    }

    public void setScrollOffset(double scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
}
