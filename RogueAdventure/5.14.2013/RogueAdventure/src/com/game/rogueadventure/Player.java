package com.game.rogueadventure;


public class Player extends Tile 
{
    int x, y;
    World world;
    Game game;
    Tile onTile; //THIS IS THE TILE THE THAT PLAYER IS ON (used when moving to keep the tile types consistent)

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
        super(Tiles.PLAYER);
        this.x = x;
        this.y = y;
    }
    
    protected void setWorld(World world)
    {
        this.world = world;
        onTile = world.tiles[x][y]; //also sets the onTile
    }
    
    protected void setGame(Game game)
    {
        this.game = game;
    }
        
     public void move(int direction)
     {
         
         world.tiles[x][y] = onTile;

            if(direction == NORTH)
            {
                if(!(world.tiles[x][y-1].type == Tiles.WALL))
                {
                    onTile = world.tiles[x][y-1]; //set onTile to the tile about to be moved on to
                    y--;
                }

            }
            if(direction == SOUTH)
            {                
                if(!(world.tiles[x][y+1].type == Tiles.WALL))
                {
                    onTile = world.tiles[x][y+1];
                    y++;
                }

            }
            if(direction == EAST)
            {
                if(!(world.tiles[x+1][y].type == Tiles.WALL))
                {
                    onTile = world.tiles[x+1][y];
                    x++;
                }
            }
            if(direction == WEST)
            {
                if(!(world.tiles[x-1][y].type == Tiles.WALL))
                {
                    onTile = world.tiles[x-1][y];
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
