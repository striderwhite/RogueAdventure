//an entity can change it position and contains information about the type of tile that it was on before it moved.

package com.game.rogueadventure;

public class Entity
{
    int x, y;
    World world;
    Game game;
    Tiles type;
    Tiles oldType; //this is the type of tile before the player was on it

    protected final int NORTH          = 0;
    protected final int SOUTH          = 1;
    protected final int EAST           = 2;
    protected final int WEST           = 3;
    protected final int SOUTH_EAST     = 4;
    protected final int SOUTH_WEST     = 5;
    protected final int NORTH_EAST     = 6;
    protected final int NORTH_WEST     = 7;
    
    Entity(int x, int y, World world, Game game, Tiles type)
    {
        this.x = x;
        this.y = y;
        this.world = world;
        this.game = game;
        this.type = type;
        oldType = world.tiles[x][y].type;
        world.tiles[x][y].type = type;
    }
        
     public void move(int direction)
     {
         
         world.tiles[x][y].type = oldType;

            if(direction == NORTH)
            {
                if(!(world.tiles[x][y-1].type == Tiles.WALL))
                {
                    oldType = world.tiles[x][y-1].type; //set onTile to the tile about to be moved on to
                    y--;
                }

            }
            if(direction == SOUTH)
            {                
                if(!(world.tiles[x][y+1].type == Tiles.WALL))
                {
                    oldType = world.tiles[x][y+1].type;
                    y++;
                }

            }
            if(direction == EAST)
            {
                if(!(world.tiles[x+1][y].type == Tiles.WALL))
                {
                    oldType = world.tiles[x+1][y].type;
                    x++;
                }
            }
            if(direction == WEST)
            {
                if(!(world.tiles[x-1][y].type == Tiles.WALL))
                {
                    oldType = world.tiles[x-1][y].type;
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
            
        world.tiles[x][y].type = type;
        game.graphics.invalidate();
            
     }
}
