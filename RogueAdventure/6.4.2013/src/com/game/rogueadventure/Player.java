package com.game.rogueadventure;

import android.graphics.Rect;

public class Player extends Entity 
{
    int strength;
    int intelligence;
    int dexterity;
    int maxHealth = 50;
    int maxMana = 50;
    int expBeforeNextLevel = 25;

    

    //Inventory inventory
    
    Player(int x, int y, World world, Tile_Type type)
    {
        super(x, y, world, type);
        level = 1;
        health = 100;
        maxHealth = 100;
        strength = 5;
        intelligence = 5;
        dexterity = 5;
    }
    
    Player(int x, int y, World world, Tile_Type type, Player player)
    {
        super(x, y, world, type);
        level = player.level;
        health = player.health;
        maxHealth = player.maxHealth;
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
        strength++;
        dexterity++;
        intelligence++;
        experience = 0;
        attackStrength += 2;
    }


    public void setHealth(int amount) 
    {
        if(amount > maxHealth)
        {
            health = maxHealth;
        }
        else
        {
            health = amount;
        }
    }




}
