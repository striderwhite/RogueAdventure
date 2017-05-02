package com.game.rogueadventure;


public class Player extends Tile 
{
    int x, y;
    World world;
    Game game;

    //Directions
    protected final int NORTH          = 0;
    protected final int SOUTH          = 1;
    protected final int EAST           = 2;
    protected final int WEST           = 3;
    protected final int SOUTH_EAST     = 4;
    protected final int SOUTH_WEST     = 5;
    protected final int NORTH_EAST     = 6;
    protected final int NORTH_WEST     = 7;
    
    Player(int x, int y)
    {
        this.x = x;
        this.y = y;
        wall = false;
    }
    
    protected void setWorld(World world)
    {
        this.world = world;
    }
    
    protected void setGame(Game game)
    {
        this.game = game;
    }
        
     public void move(int direction)
     {
         
         world.tiles[x][y] = new Tile();

         
            if(direction == NORTH)
            {
                if(!world.tiles[x][y-1].wall)
                {
                    y--;
                }

            }
            if(direction == SOUTH)
            {                
                if(!world.tiles[x][y+1].wall)
                {
                    y++;
                }

            }
            if(direction == EAST)
            {
                if(!world.tiles[x+1][y].wall)
                {
                    x++;
                }
            }
            if(direction == WEST)
            {
                if(!world.tiles[x-1][y].wall)
                {
                    x--;
                }                
            }
            if(direction == SOUTH_EAST)
            {
                y--;
                x++;
            }
            if(direction == SOUTH_WEST)
            {
                y--;
                x--;
            }
            if(direction == NORTH_EAST)
            {
                y++;
                x++;
            }
            if(direction == NORTH_WEST)
            {
                y++;
                x--;
            }
            
            world.tiles[x][y] = this;
            
            game.setDebugText("player x: " + x + " player y: " + y);
            game.graphics.invalidate();
            
        }
}
