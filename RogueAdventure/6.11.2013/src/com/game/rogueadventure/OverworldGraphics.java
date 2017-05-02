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



import com.example.rogueadventure.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class OverworldGraphics extends View
{
	World world; //the world we will draw
	Point view_point; //the position of the camera view point
	Paint paint; //needed when rendering bitmaps
	Player player; 
	protected final int TILE_SPACING = 0; //blocks can be draw with spaces in between them
	Bitmap block_bitmap, rock_block_bitmap, water_block_bitmap, metal_block_bitmap, grass_block_bitmap, ice_block_bitmap, wall_block_bitmap, none_block_bitmap, player_block_bitmap, enemy_block_bitmap, upstairs_block_bitmap, downstairs_block_bitmap;
	protected int block_size = 50;
	public float view_point_x;
	public float view_point_y;

	

	public OverworldGraphics(Context context) 
	{
		super(context);
		init();
	}
	public OverworldGraphics(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init();
	}
	public OverworldGraphics(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

    private void init()
	{
		view_point_x = 0;
		view_point_y = 0;
			
		paint = new Paint();
		paint.setColor(Color.LTGRAY);
		paint.setStrokeWidth(0);
		//block = decodeSampledBitmapFromResource(this.getResources() , R.drawable.block , 25 , 25 );
		
		int dims = 50;

		block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.block);
		block_bitmap = Bitmap.createScaledBitmap(block_bitmap, dims,dims , false);
		
        rock_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.rock);
        rock_block_bitmap = Bitmap.createScaledBitmap(rock_block_bitmap, dims,dims , false);
        
        metal_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.metal);
        metal_block_bitmap = Bitmap.createScaledBitmap(metal_block_bitmap, dims,dims , false);
        
        water_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.water);
        water_block_bitmap = Bitmap.createScaledBitmap(water_block_bitmap, dims,dims , false);
        
        grass_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.grass);
        grass_block_bitmap= Bitmap.createScaledBitmap(grass_block_bitmap, dims,dims , false);
        
        ice_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ice);
        ice_block_bitmap = Bitmap.createScaledBitmap(ice_block_bitmap, dims,dims , false);
        
        wall_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.wall);
        wall_block_bitmap = Bitmap.createScaledBitmap(wall_block_bitmap, dims,dims , false);
        
        none_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.none);
        none_block_bitmap = Bitmap.createScaledBitmap(none_block_bitmap, dims,dims , false);
        
        player_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.player);
        player_block_bitmap = Bitmap.createScaledBitmap(player_block_bitmap, dims,dims , false);
        
        enemy_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.enemy);
        enemy_block_bitmap = Bitmap.createScaledBitmap(enemy_block_bitmap, dims,dims , false);
        
        upstairs_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.upstairs);
        upstairs_block_bitmap = Bitmap.createScaledBitmap(upstairs_block_bitmap, dims,dims , false);
        
        downstairs_block_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.downstairs);
        downstairs_block_bitmap = Bitmap.createScaledBitmap(downstairs_block_bitmap, dims,dims , false);

		
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
    	int tile_location_x = 0;
    	int tile_location_y = 0;
    	Block block;
    	
    	if(canvas != null && world != null)
    	{
		    for(int x = 0 ; x < world.width ; x++ )
		    {   
		    	
        
		        tile_location_x += block_size + getTileSpacing();
		        tile_location_y = 0;
			
		        for(int y = 0 ; y < world.height ; y++)
		        {
		        	
	             block = world.getBlock(x,y);
	             
	             //dont both rendering wall or block bitmaps
	             if(getBitmap(block) == wall_block_bitmap || getBitmap(block) == wall_block_bitmap )
	             { tile_location_y += getTileSpacing() + block_size; 
	             	continue;  
	             }
	             
		            if(player.getFov().contains(x,y)) //if in the field of view
		            {
		            	
		            	canvas.drawBitmap(getBitmap(block), view_point_x + tile_location_x, view_point_y + tile_location_y, paint);
		            	block.discovered = true;
		            }
		            
		            if(block.discovered && !player.getFov().contains(x,y)) //if discovered but not in the field of view
		            {
		            	//draws rectangle based on view point
		            	canvas.drawBitmap(getBitmap(block), view_point_x + tile_location_x, view_point_y + tile_location_y, paint);
		            }		      
				    tile_location_y += getTileSpacing() + block_size;    
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
	//block_bitmap, rock_block_bitmap, water_block_bitmap, , , ice_block_bitmap, wall_block_bitmap, , , , upstairs_block_bitmap,;
	Bitmap getBitmap(Block block)
	{
		switch(block.getType())
		{
		case DOWNSTAIRS:
			return  downstairs_block_bitmap;
		case ENEMY:
			return enemy_block_bitmap;
		case GRASS:
			return grass_block_bitmap;
		case ICE:
			return ice_block_bitmap;
		case METAL: 
			return metal_block_bitmap;
		case NONE:
			return none_block_bitmap;
		case PLAYER:
			return player_block_bitmap;
		case ROCK:
			return rock_block_bitmap;
		case UPSTAIRS:
			return upstairs_block_bitmap;
		case WALL:
			return wall_block_bitmap;
		case WATER:
			return water_block_bitmap;
		default:
			return block_bitmap;
		}	
	}


    //### PARCEL STUFF http://stackoverflow.com/questions/12214847/pass-2d-array-to-another-activity

}
