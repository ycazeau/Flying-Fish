package com.example.flyingfishgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FlyingFishView  extends View
{
    private Bitmap fish[] = new Bitmap[2];
    private int fishX = 10;
    private int fishY;
    private int fishSpeed;

    private int canvasWidth, canvasHeight;

    private int yellowX, yellowY, yellowSpeed = 16;
    private Paint yellowPaint = new Paint();

    private int greenX, greenY, greenSpeed = 20;
    private Paint greenPaint = new Paint();

    private int redX, redY, redSpeed = 25;
    private Paint redPaint = new Paint();

    private int score, fishLifeCounter;

    private boolean touchScreen = false;

    private Bitmap backgroundImage;

    private Paint scorePaint = new Paint();

    private Bitmap life[] = new Bitmap[2];

    public FlyingFishView(Context context)
    {
        super(context);

        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2);

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background  );

        yellowPaint.setColor(Color.YELLOW );
        yellowPaint.setAntiAlias(false);

        greenPaint.setColor(Color.GREEN );
        greenPaint.setAntiAlias(false);

        redPaint.setColor(Color.RED );
        redPaint.setAntiAlias(false);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(50);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);

        fishY = 550;
        score = 0;
        fishLifeCounter = 3;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawBitmap(backgroundImage, 0, 0, null);

        int minFishY = fish[0].getHeight();
        int maxFishY = canvasHeight - fish[0].getHeight() * 3;
        fishY = fishY + fishSpeed;

        if (fishY < minFishY)
        {
            fishY = minFishY;
        }
        if (fishY > maxFishY)
        {
            fishY = maxFishY;
        }

        fishSpeed = fishSpeed + 2;

        if (touchScreen)
        {
            canvas.drawBitmap(fish[1], fishX, fishY, null);
            touchScreen = false;
        }
        else {
            canvas.drawBitmap(fish[0], fishX, fishY, null);
        }

        yellowX = yellowX - yellowSpeed;

        // When fish gets a Yellow ball increase the score by 10
        if (hitBallChecker(yellowX,yellowY))
        {
            score = score + 10;
            yellowX = - 100;
        }

        if (yellowX < 0)
        {
            yellowX = canvasWidth + 21;
            yellowY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }

        // Draw the Yellow Ball
        canvas.drawCircle(yellowX, yellowY, 20, yellowPaint);

        // The green Ball
        greenX = greenX - greenSpeed;

        // When fish gets a Green ball increase the score by 10
        if (hitBallChecker(greenX,greenY))
        {
            score = score + 20;
            greenX = - 100;
        }

        if (greenX < 0)
        {
            greenX = canvasWidth + 21;
            greenY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }

        // Draw the Green Ball
        canvas.drawCircle(greenX, greenY, 20, greenPaint);

        // The Red Ball
        redX = redX - redSpeed;

        // When fish gets a Red ball one life will be destroy
        if (hitBallChecker(redX,redY))
        {
           redX = - 100;
           fishLifeCounter--;

           if (fishLifeCounter == 0)
           {
               Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();

               Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
               gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               gameOverIntent.putExtra("score", score);
               getContext().startActivity(gameOverIntent);
           }

        }

        if (redX < 0)
        {
            redX = canvasWidth + 21;
            redY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }

        // Draw the Red Ball
        canvas.drawCircle(redX, redY, 25, redPaint);

        // Display the score
        canvas.drawText("Score : " + score, 20, 60, scorePaint);

        for (int i = 0; i < 3; i++)
        {
            int x = (int)(480 + life[0].getWidth() * 1.5 * i);
            int y = 30;

            if (i < fishLifeCounter)
            {
                canvas.drawBitmap(life[0], x, y,null);
            }
            else
            {
                canvas.drawBitmap(life[1], x, y,null);
            }
        }
    }

    public boolean hitBallChecker(int x, int y)
    {
        if(fishX < x && x < (fishX + fish[0].getWidth()) && fishY < y && y < (fishY + fish[0].getHeight()))
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            touchScreen = true;

            fishSpeed = -22;
        }
        return true;
    }
}
