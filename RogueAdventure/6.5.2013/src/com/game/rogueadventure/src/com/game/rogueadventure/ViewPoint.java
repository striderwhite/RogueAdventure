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
                        game.graphics.view_point_x =  (int) ( game.graphics.view_point_x + ( ( event.getX() - pointerOneInitalX ) /24 ) ); // 24 for smoothing
                        game.graphics.view_point_y =  (int) ( game.graphics.view_point_y + ( ( event.getY() - pointerOneInitalY ) /24 ) );
                        game.graphics.invalidate(); // force redraw
                    }   
                    
                    case(MotionEvent.ACTION_UP):
                    {
                    	/*
                    	int difference_x = (int) (game.graphics.view_point_x - pointerOneInitalX);
                    	int difference_y = (int) (game.graphics.view_point_y - pointerOneInitalY);
                    	while(difference_x + difference_y != 0 )
                    	{
                    		if(difference_x < 0)
                    		{ 
                    			difference_x ++;
                    		}
                    		if(difference_x > 0)
                    		{
                    			difference_x--;
                    		}
                    		
                    		if(difference_y < 0)
                    		{
                    			difference_y ++;
                    		}
                    		if(difference_y > 0)
                    		{
                    			difference_y--;
                    		}
                    		
                    		game.graphics.invalidate(); 
                    	}*/
                    }

                }

        }		
		return true;
	}

}
