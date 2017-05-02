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


import java.io.File;
import java.util.Scanner;

import com.example.rogueadventure.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class Graphics extends View
{
	World world;
	Point viewPoint;
	Paint paint;
	Player player;
	protected final int TILE_SPACING = 0;
	private LruCache<String, Bitmap> imageMemoryCache;
	Bitmap block;

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
		//block = decodeSampledBitmapFromResource(this.getResources() , R.drawable.block , 25 , 25 );

		block = BitmapFactory.decodeResource(this.getResources(), R.drawable.block);
		block = Bitmap.createScaledBitmap(block, 50,50 , false);
		
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

				    if(paint.getColor() != Color.RED && paint.getColor() != Color.MAGENTA && paint.getColor() != Color.WHITE  && paint.getColor() != Color.BLACK && paint.getColor() != Color.DKGRAY  )
				    {
				    	canvas.drawBitmap(block, viewPoint.x + locX, viewPoint.y + locY, paint);
				    }
				    else
				    {
				    	canvas.drawRect(viewPoint.x + locX, viewPoint.y + locY,  viewPoint.x + locX + world.tileSize , viewPoint.y + locY + world.tileSize, paint);
				    }
				    
				    
				    
				    
				    locY += getTileSpacing();  
				    

				    
				    
				    
				    
	            }
		    } 
    	}
    }
	public int getTileSpacing() {
		return TILE_SPACING;
	}
	
	
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
	{
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	    
	}

    //### PARCEL STUFF http://stackoverflow.com/questions/12214847/pass-2d-array-to-another-activity

}
