package com.example.minesweeper.Game;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.minesweeper.MainActivity;
import com.example.minesweeper.R;

public class UI {
    ImageView view;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;
    final int height;
    final int width;

    @SuppressLint("ClickableViewAccessibility")
    UI(int width, int height, ImageView view){
        this.width = width;
        this.height = height;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(this.bitmap);
        paint = new Paint();
        this.view = view;
        this.view.setImageBitmap(this.bitmap);

        this.view.setActivated(true);
        this.view.setClickable(true);
        this.view.setFocusable(true);
        this.view.setScaleType(ImageView.ScaleType.FIT_XY);
        this.view.setAdjustViewBounds(true);
    }

    public void drawMineField(MineField mineField){
        canvas.drawColor(Color.rgb(120,120,120));
        int mineFieldWidth = mineField.width;
        int mineFieldHeight = mineField.height;
        float distX = (float) width / mineFieldWidth;
        float distY = (float) height / mineFieldHeight;

        int textSize = 640/Math.max(mineFieldHeight, mineFieldWidth);
        paint.setTextSize(textSize);

        for (int i = 0; i < mineField.width; i++) {
            for (int j = 0; j < mineField.height; j++) {
                if(!mineField.lookedAt[i][j]){
                    paint.setColor(Color.rgb(100,100,100));
                    Rect rect = new Rect((int) (i * distX), (int) (j * distY), (int) (i * distX + distX), (int) (j * distY + distY));
                    canvas.drawRect(rect,paint);
                    if(mineField.flagged[i][j]){
                        paint.setColor(Color.rgb(255,50,50));
                        canvas.drawCircle(distX*i + distX/2,distY*j + distY/2,distX/5f,paint);
                    }
                    continue;
                }
                if(mineField.field[i][j]){
                    paint.setColor(Color.rgb(50,50,50));
                    canvas.drawCircle(distX*i + distX/2,distY*j + distY/2,distX/2.5f,paint);
                    continue;
                }
                int check = mineField.checkNeighbours(i,j);
                if(check != 0){
                    float f[] = new float[]{120f - check * 15,1f,1f};
                    paint.setColor(Color.HSVToColor(f));
                    float textWidth = paint.measureText(String.valueOf(check));
                    Rect bounds = new Rect();
                    paint.getTextBounds(String.valueOf(check),0,1,bounds);
                    canvas.drawText(String.valueOf(check), distX*i + distX / 2 - textWidth / 2f,distY*j + distY / 2 + bounds.height()/2f,paint);
                }
            }
        }
        paint.setColor(Color.rgb(200,200,200));
        for (int i = 0; i <= width; i++) {
            canvas.drawLine(i*distX,0, i*distX, height, paint);
        }
        for (int i = 0; i <= height; i++) {
            canvas.drawLine(0,i*distY,width ,i*distY, paint);
        }
        view.postInvalidate();
    }

}
