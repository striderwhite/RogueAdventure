package com.game.rogueadventure;

public class Player extends Entity 
{
    int experience;
    int strength;
    int intelligence;
    int dexterity;
    

    //Inventory inventory
    

    Player(int x, int y, World world, Game game, Tile_Type type)
    {
        super(x, y, world, game, type);
    }

}
