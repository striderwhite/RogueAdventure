package com.game.rogueadventure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.BaseSavedState;

public class Graphics extends View
{
	World world;
	Point viewPoint;
	Paint paint;
	Player player;

	public Graphics(Context context) 
	{
		super(context);
		init();
	}
	public Graphics(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init();
	}
	public Graphics(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	
	private void init()
	{
		
		viewPoint = new Point(85,150);
		paint = new Paint();
		paint.setColor(Color.LTGRAY);
		paint.setStrokeWidth(0);
	}
	protected void setWorld(World world)
	{
		this.world = world;
	}
	
	protected void setPlayer(Player player)
	{
	    this.player = player;
	}
	
	
    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    	int locX = 0;
    	int locY = 0;
    	
    	if(canvas != null && world != null)
    	{
		    for(int x = 0 ; x < world.width ; x++ )
		    {
		        locX += world.tileSize + 1;
		        locY = 0;
			
		        for(int y = 0 ; y < world.height ; y++)
		        {
		            Tile tile = world.tiles[x][y];
		            
		            if(tile != player && !tile.wall) //IF NOT PLAYER AND NOT WALL (traversable terrian)
		            {
		                paint.setColor(Color.LTGRAY);
		            }
		            if(tile != player && tile.wall) //IF NOT PLAYER AND IS WALL (non traversable terrian)
		            {
		                paint.setColor(Color.DKGRAY); 
		            }
		            
	                if(tile == player) //IF PLAYER
	                {
	                    paint.setColor(Color.GREEN);   
	                }
		            
		            locY += world.tileSize;
		            if(!tile.invisible)
		            {
		            	canvas.drawRect(viewPoint.x + locX, viewPoint.y + locY,  viewPoint.x + locX + world.tileSize , viewPoint.y + locY + world.tileSize, paint);
		            }
		            locY += 1;
		        }
		    } 
    	}
    }

    //### PARCEL STUFF http://stackoverflow.com/questions/12214847/pass-2d-array-to-another-activity
    
    public static class GraphicsSavedState extends BaseSavedState 
    {

        public GraphicsSavedState(Parcelable p) 
        {
            super(p);
        }
        
        private GraphicsSavedState(Parcel in)
        {
            super(in);
            //READ VARS
        }
        
        @Override
        public void writeToParcel(Parcel out, int flags) 
        {
            super.writeToParcel(out, flags);
            //WRITE VARS HERE
        }


        public static final Parcelable.Creator<GraphicsSavedState> CREATOR
                = new Parcelable.Creator<GraphicsSavedState>() {
            public GraphicsSavedState createFromParcel(Parcel in) {
                return new GraphicsSavedState(in);
            }

            public GraphicsSavedState[] newArray(int size) {
                return new GraphicsSavedState[size];
            }
        };

    }

    
    @Override
    public Parcelable onSaveInstanceState() 
    {
      //begin boilerplate code that allows parent classes to save state
      Parcelable superState = super.onSaveInstanceState();

      GraphicsSavedState gss = new GraphicsSavedState(superState);
      //end

      return gss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
      //begin boilerplate code so parent classes can restore state
      if(!(state instanceof GraphicsSavedState)) 
      {
        super.onRestoreInstanceState(state);
        return;
      }

      GraphicsSavedState ss = (GraphicsSavedState)state;
      super.onRestoreInstanceState(ss.getSuperState());
      //end

      //this.stateToSave = ss.stateToSave;
    }

}
