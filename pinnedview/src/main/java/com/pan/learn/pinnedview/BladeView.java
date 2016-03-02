/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.pan.learn.pinnedview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by panhongchao on 16/3/2.
 */
public class BladeView extends View {
    OnItemClickListener mOnItemClickListener;
    String[] b = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z" };
    int choose = -1;
    int singleHeight = -1;
    Paint paint = new Paint();
    boolean showBkg = false;
    PopupWindow mPopupWindow;
    TextView mPopupText;
    Handler handler = new Handler();

    public BladeView(Context context) {
        super(context);
    }

    public BladeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BladeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (showBkg) {
            canvas.drawColor(Color.parseColor("#FF0000"));
        }
        int height = getHeight();
        int width = getWidth();
        singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.BLACK);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setFakeBoldText(true);
            paint.setTextSize(30);
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * (i + 1);
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int oldChoose = choose;
        int c = (int) (y / singleHeight);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c) {
                    if (c > 0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c) {
                    if (c > 0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                dismissPopup();
                invalidate();
                break;
        }
        return true;
    }

    private void showPopup(int item) {
        if (mPopupWindow == null) {
            mPopupText = new TextView(this.getContext());
            mPopupText.setBackgroundColor(Color.GRAY);
            mPopupText.setTextColor(Color.CYAN);
            mPopupText.setTextSize(50);
            mPopupText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            mPopupWindow = new PopupWindow(mPopupText, 130, 130);
        }

        String text = "";
        if (item == 0) {
            text = "#";
        } else {
            text = Character.toString((char) ('A' + item - 1));
        }
        mPopupText.setText(text);
        if (mPopupWindow.isShowing()) {
            mPopupWindow.update();
        } else {
            mPopupWindow.showAtLocation(getRootView(), Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0 , 0);
        }
    }

    private void dismissPopup() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        }, 100);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void performItemClicked(int item) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(b[item]);
            showPopup(item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String s);
    }
}
