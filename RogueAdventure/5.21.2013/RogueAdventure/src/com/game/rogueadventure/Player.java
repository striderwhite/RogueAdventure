package com.game.rogueadventure;

public class Player extends Entity 
{
    int level;
    int strength;
    int intelligence;
    int dexterity;
    int maxHealth = 50;
    int expBeforeNextLevel = 25;
    

    //Inventory inventory
    

    Player(int x, int y, World world, Tile_Type type)
    {
        super(x, y, world, type);
        level = 1;
        health = 125;
        maxHealth = 125;
    }


    /*
     * returns true if leveled up
     */
    public boolean setExperience(int amount)
    {
        experience = amount;
        if(experience >= expBeforeNextLevel)
        {
            levelUp();
            return true;
        }
        return false;
    }

    public void levelUp()
    {
        level++;
        expBeforeNextLevel += 25;
        experience = 0;
        
    }


    public void setHealth(int amount) 
    {
        health = amount;
    }

}
