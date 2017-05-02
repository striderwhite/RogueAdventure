package com.game.rogueadventure;

public class Player extends Entity 
{
    int strength;
    int intelligence;
    int dexterity;
    int health_threshold = 50;
    int mana_threshold = 50; 
    int experience_threshold = 25;

    

    //Inventory inventory
    
    Player(int x, int y, World world, Tile_Type type)
    {
        super(x, y, world, type);
        level = 1;
        health = 100;
        health_threshold = 100;
        strength = 5;
        intelligence = 5;
        dexterity = 5;
    }
    
    Player(int x, int y, World world, Tile_Type type, Player player)
    {
        super(x, y, world, type);
        level = player.level;
        health = player.health;
        health_threshold = player.health_threshold;
        strength = player.strength;
        intelligence = player.intelligence;
        dexterity = player.dexterity;
    }


    /*
     * returns true if leveled up
     */
    public boolean setExperience(int amount)
    {
        experience = amount;
        if(experience >= experience_threshold)
        {
            levelUp();
            return true;
        }
        return false;
    }

    public void levelUp()
    {
        level++;
        experience_threshold += 25;
        strength++;
        dexterity++;
        intelligence++;
        experience = 0;
        attack_strength += 2;
    }


    public void setHealth(int amount) 
    {
        if(amount > health_threshold)
        {
            health = health_threshold;
        }
        else
        {
            health = amount;
        }
    }

    public void setMana(int amount) 
    {
        if(amount > mana_threshold)
        {
            mana = mana_threshold;
        }
        else
        {
            mana = amount;
        }
    }
    
    
    public boolean isOnStaircase()
	{
        if(typeBeforeEntityMoved == Tile_Type.UPSTAIRS || typeBeforeEntityMoved == Tile_Type.DOWNSTAIRS )
        {
            return true;
        }
        else
        {
            return false;
        }
    }




}
