package com.example.minesweeper.Game;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.widget.ImageView;

import com.example.minesweeper.Physics.PhysicsObject;
import com.example.minesweeper.Physics.Scene;
import com.example.minesweeper.Physics.Vec2;

public class UI {
    ImageView view;
    private final Paint paint;
    private final Canvas canvas;
    final int height;
    final int width;
    int textSize;
    @SuppressLint("ClickableViewAccessibility")
    UI(int width, int height, ImageView view){
        this.width = width;
        this.height = height;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        this.view = view;
        this.view.setImageBitmap(bitmap);

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

        textSize = 640/Math.max(mineFieldHeight, mineFieldWidth);
        paint.setTextSize(textSize);

        for (int i = 0; i < mineField.width; i++) {
            for (int j = 0; j < mineField.height; j++) {
                if(!mineField.lookedAt[i][j]){
                    paint.setColor(Color.rgb(100,100,100));
                    Rect rect = new Rect((int) (i * distX), (int) (j * distY), (int) (i * distX + distX), (int) (j * distY + distY));
                    canvas.drawRect(rect,paint);
                    if(mineField.flagged[i][j]){
                        paint.setColor(Color.rgb(255,50,50));
                        float textWidth = paint.measureText("\uD83D\uDEA9");
                        Rect bounds = new Rect();
                        paint.getTextBounds("\uD83D\uDEA9",0,1,bounds);
                        canvas.drawText("\uD83D\uDEA9", distX*i + distX / 2 - textWidth / 2f,distY*j + distY / 2 + bounds.height()/2f,paint);
                        //canvas.drawCircle(distX*i + distX/2,distY*j + distY/2,distX/5f,paint);
                    }
                    continue;
                }
                if(mineField.field[i][j]){
                    paint.setColor(Color.rgb(50,50,50));
                    canvas.drawCircle(distX*i + distX/2,distY*j + distY/2,distX/2.5f,paint);
                    if(mineField.flagged[i][j]){
                        paint.setColor(Color.rgb(255,50,50));
                        float textWidth = paint.measureText("\uD83D\uDEA9");
                        Rect bounds = new Rect();
                        paint.getTextBounds("\uD83D\uDEA9",0,1,bounds);
                        canvas.drawText("\uD83D\uDEA9", distX*i + distX / 2 - textWidth / 2f,distY*j + distY / 2 + bounds.height()/2f,paint);
                        //canvas.drawCircle(distX*i + distX/2,distY*j + distY/2,distX/5f,paint);
                    }
                    continue;
                }
                int check = mineField.checkNeighbours(i,j);
                if(check != 0){
                    float[] f = new float[]{120f - check * 15,1f,1f};
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

    void drawExploded(int x, int y, MineField mineField){
        paint.setColor(Color.RED);
        int mineFieldWidth = mineField.width;
        int mineFieldHeight = mineField.height;
        float distX = (float) width / mineFieldWidth;
        float distY = (float) height / mineFieldHeight;
        canvas.drawCircle(distX*x + distX/2,distY*y + distY/2,distX/2.5f,paint);
        view.postInvalidate();
    }

    void drawInMiddle(String text, int color){
        paint.setColor(color);
        paint.setTextSize(128);
        Rect bounds = new Rect();
        paint.getTextBounds(String.valueOf(text),0,text.length()-1,bounds);
        canvas.drawText(text,width / 2f - bounds.width() / 2f,height / 2f + bounds.height() / 2f,paint);
        view.postInvalidate();
    }

    void drawPhysicsScene(Scene scene){
        canvas.drawColor(Color.rgb(170,170,170));
        paint.setTextSize(textSize);
        for(PhysicsObject object : scene.objects){
            paint.setColor(Color.rgb(120,120,120));

            if(!object.explosionSource) {
                drawBoxRotated(object.pos, new Vec2(object.width, object.height),1);
                /*canvas.drawRect(
                        object.pos.x - object.width / 2,
                        object.pos.y - object.height / 2,
                        object.pos.x + object.width / 2,
                        object.pos.y + object.height / 2, paint);*/
            }

            if(object.isMine){
                if(object.explosionSource){
                    paint.setColor(Color.RED);
                }else {
                    paint.setColor(Color.rgb(50, 50, 50));
                }
                canvas.drawCircle(object.pos.x, object.pos.y, object.width / 2.5f, paint);
            }else if(object.neighbours > 0){
                float[] f = new float[]{120f - object.neighbours * 15,1f,1f};
                paint.setColor(Color.HSVToColor(f));
                float textWidth = paint.measureText(String.valueOf(object.neighbours));
                Rect bounds = new Rect();
                paint.getTextBounds(String.valueOf(object.neighbours),0,1,bounds);
                canvas.drawText(String.valueOf(object.neighbours), object.pos.x - textWidth / 2f,object.pos.y + bounds.height()/2f,paint);
            }
        }
        view.invalidate();
        drawInMiddle("YOU LOST!", Color.RED);
    }

    void drawBoxRotated(Vec2 pos, Vec2 size, float angle){
        Path path = new Path();
        path.moveTo(pos.x - size.x / 2, pos.y - size.y / 2);
        path.lineTo(pos.x + size.x / 2, pos.y - size.y / 2);
        path.lineTo(pos.x + size.x / 2, pos.y + size.y / 2);
        path.lineTo(pos.x - size.x / 2, pos.y + size.y / 2);
        canvas.drawPath(path,paint);
    }
}
