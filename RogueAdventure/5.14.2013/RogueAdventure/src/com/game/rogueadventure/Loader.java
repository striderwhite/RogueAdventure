/*  *   *   *   *   *   *   *   *   *   *   *   *   *   *   *
 *  Loader is the implementation of a save / load mechanism.
 *  
 *  Loading is done by reconstructing the world object (with values found in a structured list that is parsed for values).
 * 	Saving is the process of creates this list.
 *   
 *  Saving member variables is done by creating a list of strings which represent member variable's type, name and value with the following format:
 *  TYPE NAME VALUE
 *  Note the following:
 *  (1) Each new entry must be on a new line
 *  (2) Member variables are represented in the file members.sav
 *  
 *  Saving the two dimensional array of Tiles is in a different format (located in the save file tiles.sav).
 *  Instead of String type they are of int type.
 *  The format is a list of ints relative to the width and height of the world. 
 *  For example if the world was a grid of 3x3 tiles, the format would be this:
 *  
 *  000
 *  000
 *  000
 *  
 * 	Note that the value of the a given integer corresponds to a tile type, not the implemented enumeration called Tiles; 
 * 	this mechanism is isolated from the 'Tiles' enumeration.
 * 
 * 	Instead of using enumeration type Tiles, constants with the name of the types are assigned a particular value. 
 * 	For example the value 0 will cause the parser to assign the type 'NONE' to the new world of tiles.
 *  
 *  
 *  Loading is done by reconstructing the world by parsing this list for values 
 *  The order in which entries added does not matter; the load parser takes care of that.
 *  
 *  The class needs a reference to the world it is saving.
 */

package com.game.rogueadventure;

public class Loader
{
    World world;
    
    Loader(World world)
    {
        this.world = world;
    }
    
    public void save()
    {
        
    }
    
    public void load()
    {
        
    }
}
