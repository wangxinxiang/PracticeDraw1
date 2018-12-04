package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Practice10HistogramView extends View {

    private Paint mPaint, mTextPaint;
    private int width, height, margin;
    private float mTextHeight, mNameX;
    private List<DataModel> datas;
    private List<Float> textXList;
    private List<Rect> rectList;

    private static final int MARGIN = 100;
    private static final String NAME = "直方图";

    public Practice10HistogramView(Context context) {
        super(context);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(dip2px(context, 1));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(dip2px(context, 12));
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        initData();
        width = (w - MARGIN * 2) / datas.size();
        margin = width / 5;
        width = width - margin;

        rectList = new ArrayList<>();
        textXList = new ArrayList<>();
        for (int i = 0;i < datas.size();i++) {
            int left = margin * (i + 1) + width * i + MARGIN;
            int top = (int) (MARGIN + height - datas.get(i).getAngle());
            int right = (margin + width) * (i + 1) + MARGIN;
            int bottom = MARGIN + height;
            Rect rect = new Rect(left, top, right, bottom);
            rectList.add(rect);

            float textWidth = mTextPaint.measureText(datas.get(i).getName());
            textXList.add(left + (width - textWidth) / 2);
            if (i == 0) {
                Paint.FontMetrics fm = mTextPaint.getFontMetrics();
                mTextHeight = (float)Math.ceil(fm.descent - fm.ascent);
            }
        }

        float textWidth = mTextPaint.measureText(NAME);
        mNameX = w / 2 - textWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画直方图
        mPaint.setColor(Color.WHITE);
        canvas.drawLine(MARGIN, MARGIN, MARGIN, getHeight() - MARGIN * 2, mPaint);
        canvas.drawLine(getWidth() - MARGIN, getHeight() - MARGIN * 2, MARGIN, getHeight() - MARGIN * 2, mPaint);

        mPaint.setColor(Color.GREEN);
        mTextPaint.setTextSize(dip2px(getContext(), 12));
        for (int i = 0; i < datas.size();i ++) {
            canvas.drawRect(rectList.get(i), mPaint);

            canvas.drawText(datas.get(i).getName(), textXList.get(i), MARGIN + height + mTextHeight, mTextPaint);
        }

        mTextPaint.setTextSize(dip2px(getContext(), 18));
        canvas.drawText(NAME, mNameX, getHeight() - MARGIN / 2, mTextPaint);
    }

    private void initData() {
        datas = new ArrayList<>();
        double maxNum = 0;
        for (int i = 0;i < 7; i++) {
            DataModel dataModel = new DataModel();
            dataModel.setNums(Math.random());
            dataModel.setName("test" + i);
            datas.add(dataModel);
            if (maxNum < dataModel.getNums()) {
                maxNum = dataModel.getNums();
            }
        }

        height = getHeight() - MARGIN * 3;
        for (DataModel dataModel : datas) {
            dataModel.setAngle((float) ((height - MARGIN / 2) * dataModel.getNums() / maxNum));
        }
    }

    /**
     * dip转化成px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
