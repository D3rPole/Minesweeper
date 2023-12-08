package com.example.minesweeper.Game;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ImageView;

import com.example.minesweeper.Physics.PhysicsObject;
import com.example.minesweeper.Physics.PhysicsScene;

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
                drawCell(i,j, distX, distY, mineField);
            }
        }
        // drawGrid
        drawGrid(distX, distY);
        view.postInvalidate();
    }

    private void drawCell(int x, int y, float distX, float distY, MineField mineField){
        if(!mineField.lookedAt[x][y]){
            paint.setColor(Color.rgb(100,100,100));
            Rect rect = new Rect((int) (x * distX), (int) (y * distY), (int) (x * distX + distX), (int) (y * distY + distY));
            canvas.drawRect(rect,paint);
            if(mineField.flagged[x][y]){
                // Place flag symbol
                drawTextCentered(distX * x + distX / 2, distY * y + distY / 2, "\uD83D\uDEA9");
            }
            return;
        }
        if(mineField.field[x][y]){
            drawTextCentered(distX * x + distX / 2, distY * y + distY / 2,"\uD83D\uDCA3");
            return;
        }
        int check = mineField.checkNeighbours(x,y);
        if(check != 0){
            // draw number for neighbouring mines
            float[] f = new float[]{120f - check * 15,1f,1f};
            paint.setColor(Color.HSVToColor(f));
            drawTextCentered(distX * x + distX / 2, distY * y + distY / 2, String.valueOf(check));
        }
        // Mine doesn't have to be drawn here since it only gets drawn if physics run, and they are drawn differently
    }

    private void drawGrid(float distX, float distY){
        paint.setColor(Color.rgb(200,200,200));
        for (int i = 0; i <= width; i++) {
            canvas.drawLine(i*distX,0, i*distX, height, paint);
        }
        for (int i = 0; i <= height; i++) {
            canvas.drawLine(0,i*distY,width ,i*distY, paint);
        }
    }

    void drawInMiddle(String text, int color){
        paint.setColor(Color.argb(100,0,0,0));
        canvas.drawRect(0, height / 2f - 70, width, height / 2f + 70, paint);

        paint.setColor(color);
        paint.setTextSize(128);
        Rect bounds = new Rect();
        paint.getTextBounds(String.valueOf(text),0,text.length()-1,bounds);
        canvas.drawText(text,width / 2f - bounds.width() / 2f,height / 2f + bounds.height() / 2f,paint);
        view.postInvalidate();
    }

    void drawPhysicsScene(PhysicsScene physicsScene){
        canvas.drawColor(Color.rgb(170,170,170));
        paint.setTextSize(textSize);
        float explosionX = 0;
        float explosionY = 0;

        for(PhysicsObject object : physicsScene.objects){
            paint.setColor(Color.rgb(120,120,120));

            if(!object.explosionSource) {
                canvas.drawRect(
                        object.pos.x - object.width / 2,
                        object.pos.y - object.height / 2,
                        object.pos.x + object.width / 2,
                        object.pos.y + object.height / 2, paint);
            }

            if(object.isMine){
                if(object.explosionSource){
                    explosionX = object.pos.x;
                    explosionY = object.pos.y;
                    continue;
                }
                drawTextCentered(object.pos.x, object.pos.y, "\uD83D\uDCA3");
            }else if(object.neighbours > 0){
                float[] f = new float[]{120f - object.neighbours * 15,1f,1f};
                paint.setColor(Color.HSVToColor(f));
                drawTextCentered(object.pos.x, object.pos.y, String.valueOf(object.neighbours));
            }
        }
        drawTextCentered(explosionX, explosionY, "\uD83D\uDCA5");
        view.invalidate();
        drawInMiddle("YOU LOST!", Color.RED);
    }

    void drawTextCentered(float x, float y, String text){
        float textWidth = paint.measureText(text);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        float yOffset = y + textHeight / 2 - fontMetrics.bottom;

        canvas.drawText(text, x - textWidth / 2, yOffset, paint);
    }
}
