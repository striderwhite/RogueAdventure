package com.game.rogueadventure;

public class Player extends Entity 
{
    int level;
    int experience;
    int strength;
    int intelligence;
    int dexterity;
    int health;
    int mana;
    //Inventory inventory
    

    Player(int x, int y, World world, Game game, Tiles type)
    {
        super(x, y, world, game, type);
    }

}
