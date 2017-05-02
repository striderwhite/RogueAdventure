/*  *   *   *   *   *   *   *   *   *   *   *   *   *   *   *
 *  State is the implementation of a save / load mechanism.
 *  
 *  Saving is done by writing state variables and object to a save file (GAME_SAVE_<slot>.SAV)
 *	Loading the inverse.
 *	
 *  The class needs a reference to the world it is saving.
 */

package com.game.rogueadventure;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class State
{
    //World world;
    //Player player;
	
	Block[][] blocks;
	int playerX, playerY;
	
    Scanner stateScanner;
    ObjectInputStream objectIn;
    ObjectOutputStream objectOut;
    FileInputStream fileIn;
    FileOutputStream fileOut;
    
    File sdCard;
    File saveDir;
    File saveFile;
    
    State(int slot)
    {
    	initPointers(slot);
    }
    
    State(Block[][] tiles, int x, int y, int slot)
    {
        this.blocks = tiles;
        playerX = x;
        playerY = y;
        initPointers(slot);
    }
    
    public void save()
    {
        try 
        {           
            fileOut = new FileOutputStream(saveFile);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(blocks);
            objectOut.writeInt(playerX);
            objectOut.writeInt(playerY);
            objectOut.close();
        } 
        
        catch (Exception e) 
        {
            Log.e("ROGUE ADVENTURE",Log.getStackTraceString(e));         
        } 

    }
    
    public void load()
    {
        try
        {
            fileIn = new FileInputStream(saveFile);
            objectIn = new ObjectInputStream(fileIn);
            blocks = (Block[][]) objectIn.readObject();
            playerX = objectIn.readInt();
            playerY = objectIn.readInt();
            objectIn.close();
        
        }
        catch(Exception e)
        {
            Log.e("ROGUE ADVENTURE",Log.getStackTraceString(e));  
        }
    }
    
    private void initPointers(int slot)
    {
        sdCard = Environment.getExternalStorageDirectory();
        saveDir = new File(sdCard.getAbsolutePath(), "/RougeAdventure/save");
        saveDir.mkdirs();
        saveFile = new File(saveDir, "GAME_SAVE_"+slot+".sav");
    }
}
