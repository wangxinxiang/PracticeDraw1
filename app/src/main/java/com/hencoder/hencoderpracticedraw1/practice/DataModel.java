package com.hencoder.hencoderpracticedraw1.practice;

/**
 * 作者   wang
 * 时间   2018/11/29 0029 09:24
 * 文件   PracticeDraw1
 * 描述
 */
public class DataModel {

    private String name;    //名称
    private double nums;    //数量

    private float angle;    //由百分比计算出来的角度
    private int color;      //填充颜色

    private float textWidth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNums() {
        return nums;
    }

    public void setNums(double nums) {
        this.nums = nums;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(float textWidth) {
        this.textWidth = textWidth;
    }
}
