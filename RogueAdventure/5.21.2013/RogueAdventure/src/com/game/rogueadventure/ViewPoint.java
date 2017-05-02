package com.game.rogueadventure;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ViewPoint implements OnTouchListener 
{
	int pointerOneInitalX;
	int pointerOneInitalY;

	Game game;
	ViewPoint(Game game)
	{
		this.game = game;
	}
	
	@Override 
	public boolean onTouch(View v, MotionEvent event) 
	{
        if(v == game.graphics)
        {
            switch(event.getAction())
                {        
                    case(MotionEvent.ACTION_DOWN):
                    {
                        pointerOneInitalX =  (int) event.getX();
                        pointerOneInitalY =  (int) event.getY();
                        
                    }
                    case(MotionEvent.ACTION_MOVE):
                    {
                        //this simulates a camera, by dragging the screen the viewpoint is adjusted
                        game.graphics.viewPoint.x =  (int) ( game.graphics.viewPoint.x + ( ( event.getX() - pointerOneInitalX ) /24 ) ); // 24 for smoothing
                        game.graphics.viewPoint.y =  (int) ( game.graphics.viewPoint.y + ( ( event.getY() - pointerOneInitalY ) /24 ) );
                        game.graphics.invalidate(); // force redraw
                    }   

                }

        }		
		return true;
	}

}
