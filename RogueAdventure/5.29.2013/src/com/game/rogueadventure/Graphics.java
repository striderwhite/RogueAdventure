/*	*	*	*	*	
 * This is a view object that is overwritten to provide a drawing mechanism which displays a grid of tiles.
 * The array of tiles held in the world class.
 * The class figures out what texture to use based on the type of tile (returned by the getType() method). 
 * Note that getType() takes an enumeration of Tiles (which represents different the tile types).
 * Also a view point is implemented hooking the callback method onTouch() and testing for finger location. 
 * 
 * The class must have a reference to the world and the player objects which are given through their respective setters.
 */

package com.game.rogueadventure;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class Graphics extends View
{
	World world;
	Point viewPoint;
	Paint paint;
	Player player;
	protected final int TILE_SPACE = 15;

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
		
		viewPoint = new Point(0,0);
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
		        locX += world.tileSize + getTileSpacing();
		        locY = 0;
			
		        for(int y = 0 ; y < world.height ; y++)
		        {
	            Tile tile = world.tiles[x][y];
		            switch(tile.getType())
		            {
                        case GRASS:
                            paint.setColor(Color.GREEN);
                            break;
                            
                        case ICE:
                            paint.setColor(Color.CYAN);
                            break;
                            
                        case METAL:
                            paint.setColor(Color.LTGRAY);
                            break;
                            
                        case ROCK:
                            paint.setColor(Color.rgb(184, 138, 0));
                            break;                     
                            
                        case WATER:
                            paint.setColor(Color.BLUE);
                            break;
                            
                        case WALL:
                            paint.setColor(Color.BLACK);
                            break;
                            
                        case PLAYER:
                        	paint.setColor(Color.RED);
                        	break;
                        	
                        case ENEMY:
                            paint.setColor(Color.WHITE);
                            break; 
                            
                        case STAIRS:
                            paint.setColor(Color.MAGENTA);
                            break;
                            
                        case NONE:
                            continue; //continue for loop (dont draw anything)
                            
                        default:
                            break;
		            }
		            
		            if((player.getFov().contains(x,y)))
		            {
		                world.tiles[x][y].discovered = true;
		            }
		            else
		            {
		                if(world.tiles[x][y].discovered && world.tiles[x][y].isTraversableAndNotEnemy())
		                {
		                    paint.setColor(Color.DKGRAY);
		                }
		                else
		                {
		                    paint.setColor(Color.BLACK);
		                }
		            }
		                    

		            
				    locY += world.tileSize;
				    canvas.drawRect(viewPoint.x + locX, viewPoint.y + locY,  viewPoint.x + locX + world.tileSize , viewPoint.y + locY + world.tileSize, paint);
				    locY += getTileSpacing();  
	            }
		    } 
    	}
    }
	public int getTileSpacing() {
		return TILE_SPACE;
	}

    //### PARCEL STUFF http://stackoverflow.com/questions/12214847/pass-2d-array-to-another-activity

}
