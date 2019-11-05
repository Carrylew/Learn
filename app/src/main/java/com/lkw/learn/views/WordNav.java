package com.lkw.learn.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lkw.learn.R;


public class WordNav extends View {
    //绘制的字母列表导航字母
    private String words[] = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q"
            , "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint wordPaint;    //字母画笔
    private Paint bgPaint;   //字母背景画笔
    private int itemWidth;    //字母的高度
    private int itemHeight;    //字母的高度
    private int touchIndex; //手指按下的字母索引
    private OnWordChangeListener listener; //手指按下的字母改变接口
    private boolean isMoveDraw = false;

    public void setListener(OnWordChangeListener listener) {
        this.listener = listener;
    }


    public void setTouchIndex(String word) {
        for (int i = 0; i < words.length; i++) {
            if (word.equals(words[i])) {
                this.touchIndex = i;
                invalidate();
            }
        }
    }

    public WordNav(Context context, AttributeSet attrs) {
        super(context, attrs);
        wordPaint = new Paint();
        wordPaint.setTextSize(24);
        wordPaint.setTextAlign(Paint.Align.CENTER);
        bgPaint = new Paint();
        bgPaint.setColor(context.getResources().getColor(R.color.colorAccent));
    }

    //得到画布的宽度和每一个字母的所占的高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        int height = getMeasuredHeight(); //减去50是为了让上下留白，美观一点
        itemHeight = height / words.length; //得到27个小字母块的高度
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {
            Rect rect = new Rect();
            wordPaint.getTextBounds(words[i], 0, 1, rect); //获取该文字的宽高
            int wordWidth = rect.width();
            int wordHeight = rect.height();
            //设置横坐标点文字绘制的其实位置
            float wordX = itemWidth / 2;
            float wordY = itemHeight / 2 + i * itemHeight + wordHeight /2;
            //判断是不是我们按下的当前字母
            if (touchIndex == i) {
                //绘制文字圆形背景
                //圆心点为宽度的一半，item高度一半加上上面的高度
                wordPaint.setColor(Color.WHITE);
                canvas.drawCircle(itemWidth / 2, itemHeight / 2 + i * itemHeight, 20, bgPaint);
            } else {
                wordPaint.setColor(Color.GRAY);
            }
            canvas.drawText(words[i], wordX, wordY, wordPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Rect rect = new Rect();
                rect.left = 0;
                rect.top = 0;
                rect.bottom = itemHeight * words.length;
                rect.right = itemWidth;
                boolean b = rect.contains((int) event.getX(), (int) event.getY());
                if (!b) {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int index = (int) (y / itemHeight);
                if (index != touchIndex) {
                    touchIndex = index;
                }
                if (listener != null && 0 <= touchIndex && touchIndex <= words.length - 1) {
                    listener.wordChange(words[touchIndex]);
                }
                this.isMoveDraw = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                this.isMoveDraw = false;
                listener.wordChange(null);
                invalidate();
                break;
        }
        return true;
    }

    public interface OnWordChangeListener {
        void wordChange(String word);
    }
}
