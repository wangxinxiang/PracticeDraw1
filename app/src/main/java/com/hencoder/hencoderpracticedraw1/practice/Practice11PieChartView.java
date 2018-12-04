package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Practice11PieChartView extends View {

    // 颜色表 (注意: 此处定义颜色使用的是ARGB，带Alpha通道的)
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    private Paint mPaint, mTextPaint, mLinePaint;
    private List<DataModel> mPieChartBeanList;
    private float mWidth, mHeight, mRadius;
    private float mTextOffset;
    private RectF rectF;
    private float startDrawAngle = 0;

    private static final int MARGIN = 100;
    private float translate_offset = 50;

    private float old_x,old_y;
    private float click_angle, old_click_angle;


    public Practice11PieChartView(Context context) {
        super(context);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initData();
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        float temp = Math.min(mWidth, mHeight) - MARGIN * 2;
        mRadius = temp / 2;
        translate_offset = mRadius / 9;
        rectF = new RectF(w / 2 - mRadius, MARGIN / 2, w / 2 + mRadius, 2 * mRadius + MARGIN / 2);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextOffset = (fm.descent - fm.ascent) / 2 - fm.descent;    //根据中心与基线的距离

        for (DataModel dataModel : mPieChartBeanList) {
            dataModel.setTextWidth(mTextPaint.measureText(dataModel.getName()));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画饼图
        float startDrawAngle = this.startDrawAngle;
//        canvas.translate(mRadius * 2, mRadius * 3 / 2);     //坐标中心移到圆中心
        if (mPieChartBeanList != null) {
            for (int i = 0; i < mPieChartBeanList.size(); i++) {
                canvas.save();
                translateView(canvas, startDrawAngle, mPieChartBeanList.get(i).getAngle());
                mPaint.setColor(mPieChartBeanList.get(i).getColor());
                float angle = mPieChartBeanList.get(i).getAngle();
                float start = startDrawAngle;
                if (angle > 10) {
                    angle -= 1;
                    start += 1;
                }
                canvas.drawArc(rectF, start, angle, true, mPaint);
                drawLine(canvas, startDrawAngle , mPieChartBeanList.get(i).getAngle(), mPieChartBeanList.get(i).getName(), mPieChartBeanList.get(i).getTextWidth());
                startDrawAngle += mPieChartBeanList.get(i).getAngle();
                canvas.restore();
            }
        }

        canvas.drawText("饼图", getWidth() / 2, getHeight() - MARGIN / 2, mTextPaint);
    }

    private void drawLine(Canvas canvas, float startDrawAngle, float angle, String text, float textWidth) {
        float[] xy = calculateTranslate(startDrawAngle, angle, mRadius);
        float x = xy[0] + getWidth() / 2;
        float y = xy[1] + MARGIN / 2 + mRadius;
        xy = calculateTranslate(startDrawAngle, angle, MARGIN / 2);
        float stop_x = x + xy[0];
        float stop_y = y + xy[1];
        if (stop_y < MARGIN / 2) stop_y = MARGIN / 2;
        if (stop_y > (MARGIN + mRadius * 2)) stop_y = MARGIN + mRadius * 2;
        canvas.drawLine(x, y, stop_x, stop_y, mLinePaint);
        x = stop_x;
        y = stop_y;
        if (xy[0] > 0) {
            stop_x = rectF.right + MARGIN;
        } else {
            stop_x = rectF.left - MARGIN;
        }
        canvas.drawLine(x, y, stop_x, stop_y, mLinePaint);

        if (xy[0] < 0) {
            stop_x = stop_x - textWidth - MARGIN / 5;
        } else {
            stop_x += MARGIN / 4;
        }
        canvas.drawText(text, stop_x, stop_y + mTextOffset, mTextPaint);
    }

    private void translateView(Canvas canvas, float startDrawAngle, float angle) {
        if (startDrawAngle <= click_angle && ((startDrawAngle + angle) > click_angle)) {
            float[] xy = calculateTranslate(startDrawAngle, angle, translate_offset);
            canvas.translate(xy[0], xy[1]);
        }
    }

    private float[] calculateTranslate(float startDrawAngle, float angle, double translate_offset) {
        startDrawAngle = (int) (startDrawAngle + angle / 2);

        float[] result = new float[2];
        result[0] = (float) (translate_offset * Math.cos(Math.toRadians(startDrawAngle)));
        result[1] = (float) (translate_offset * Math.sin(Math.toRadians(startDrawAngle)));

        return result;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(dip2px(getContext(), 10));

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setStrokeWidth(4);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                old_x = event.getX();
                old_y = event.getY();
                float[] xy_old = getRealtiveXY(old_x, old_y);
                old_click_angle = calculateAngle(xy_old[0], xy_old[1]);
                return true;
            case MotionEvent.ACTION_MOVE:
                float[] xy = getRealtiveXY(event.getX(), event.getY());
                invalidate();
                if (!isClickRound(xy[0], xy[1])) {
                    float click_angle = calculateAngle(xy[0], xy[1]);
                    startDrawAngle += (click_angle - old_click_angle);
                    this.click_angle += (click_angle - old_click_angle);
                    old_click_angle = click_angle;
                    Log.d("-------->", "startDrawAngle:" + startDrawAngle + ";  click_angle:" + this.click_angle);
                }
                return true;
            case MotionEvent.ACTION_UP:
                xy = getRealtiveXY(event.getX(), event.getY());
                xy_old = getRealtiveXY(old_x, old_y);
                if (isClickRound(xy[0], xy[1]) && isClickRound(xy_old[0], xy_old[1])) {
                    this.click_angle = calculateAngle(xy[0], xy[1]);
                    this.startDrawAngle = this.startDrawAngle % 360;
                    while (this.startDrawAngle < 0) {
                        this.startDrawAngle += 360;
                    }
                    if (this.click_angle < this.startDrawAngle) {
                        this.click_angle += 360;
                    }
                    invalidate();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private float[] getRealtiveXY(float x, float y) {
        float[] xy = new float[2];
        xy[0] =  x - getWidth() / 2;
        xy[1] = y - (MARGIN / 2 + mRadius);
        return xy;
    }

    /**
     * 点是否在圆内
     */
    private boolean isClickRound(float x, float y) {
        double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return length <= mRadius;
    }

    private float calculateAngle(float x, float y) {
        double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double angle = Math.toDegrees(Math.acos(x / length));
        if (y < 0) {
            angle = 360 - angle;
        }
        return (float) angle;
    }

    public void setPieChartBeanList(List<DataModel> pieChartBeanList) {
        mPieChartBeanList = pieChartBeanList;
        //设置所占度数
        if (mPieChartBeanList != null) {
            double total = 0;
            float count = 0;
            for (int i = 0; i < mPieChartBeanList.size(); i++) {
                total += mPieChartBeanList.get(i).getNums();
                mPieChartBeanList.get(i).setColor(mColors[i % mColors.length]);
            }
            for (DataModel pieChartBean : mPieChartBeanList) {
                pieChartBean.setAngle((float) (360 * pieChartBean.getNums() / total));
                count += pieChartBean.getAngle();
            }
            DataModel dataModel = pieChartBeanList.get(pieChartBeanList.size() - 1);
            dataModel.setAngle(dataModel.getAngle() - count + 360);

            invalidate();
        }
    }

    private void initData() {
        List<DataModel> datas = new ArrayList<>();
        for (int i = 0;i < 7; i++) {
            DataModel dataModel = new DataModel();
            dataModel.setNums(Math.random() * 100);
            dataModel.setName("test" + i);
            datas.add(dataModel);
        }

        setPieChartBeanList(datas);
    }

    /**
     * dip转化成px
     */
    public static int dip2px(Context context, float dipValue)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
