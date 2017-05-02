/* This class contains the 2d array of tiles and the random generation code
 */

package com.game.rogueadventure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.graphics.Rect;


public class World
{
    protected int width = 30;
	protected int height = 30;
	protected int tileSize = 25;
    protected int roomsToMake;
	protected Tile[][] tiles;
	protected Room[] rooms;
	protected ArrayList<Enemy> enemies;
	protected Player player;
	
	//Adaptive variables
	int maxEnemies, minEnemies;

	//first time the world is built
	World()
	{
	    minEnemies = 2;
	    maxEnemies = 5;
		buildWorld(); //creates the array of tiles and adds some boundaries
		createRooms(); //generates the rooms 
		addRoomsToTiles(); //converts rooms to the tiles array
		connectRooms();
	    placeStairCase();
		populateWorld(); //adds things to the world
	}
	
	World(World w)
	{
	    width = w.width + 1;
	    height = w.height +1;
        minEnemies += w.minEnemies++;
        maxEnemies += w.maxEnemies++;
        buildWorld(); //creates the array of tiles and adds some boundaries
        createRooms(); //generates the rooms 
        addRoomsToTiles(); //converts rooms to the tiles array
        connectRooms();
        placeStairCase();
        populateWorldWithMoreDifficultEnemies(w); //adds things to the world	    
	}
	


    public World(Tile[][] tiles) 
	{
		this.tiles = tiles;
	}

	private void buildWorld()
	{
		tiles = new Tile[width][height];
		for(int x = 0 ; x < width ; x++)
		{
			for(int y = 0 ; y < height ; y++)
			{
				tiles[x][y] = new Tile(x,y,Tile_Type.WALL);
			}
		}
		for(int x = 0 ; x < width ; x++ ) //creates walls around room
		{
			for(int y = 0 ; y < height ; y++)
			{
				if( y == 0 || y == height-1 ) //if we are on the top or bottom of the grid make it a wall
				{
					tiles[x][y].type = Tile_Type.WALL;
				}
				else
				{
					if( x == 0 || x == width-1 )
					{
						tiles[x][y].type = Tile_Type.WALL;
					}
				}
			}
		}
		
	}
	
	private void createRooms()
	{
		//figure out how many rooms to make and create an array of that size
		Random gen = new Random(); //number generator
	    roomsToMake = gen.nextInt(7) + 3; //make between 4 and 7 rooms  
		rooms = new Room[roomsToMake]; //create the array
		
		//##################Create first room  ###################
		//we need at least one room defined before making the other ones 
		//add the first room to the array
		rooms[0] = new Room(this);
		
		//############### create the rest of the rooms ###################
		
		//these variables are fail safes in case the rooms being generated could not fit within the game area (to prevent infinite loop)
		int attempt = 0; //amount of times tried
		int maxAttempts = 30000; //max amount of times before not trying to make any more rooms
		
		makingRooms: // (the fail safe)
		for(int roomsMade = 1 ; roomsMade < roomsToMake ; roomsMade++)
		{
			//create a new room check and if it overlaps with any rooms in the array, make a new room until it doesnt
		    Room newRoom = new Room(this);	      	
		    
		    //note: potential infinite loop
		    while(isOverlaping(newRoom))
		    {
		    		newRoom = new Room(this); //make new room
		    		if(attempt > maxAttempts){ break makingRooms; } //if reached max attempts break out of making rooms
		    		else{ attempt++;} //
		    }
		    
		    rooms[roomsMade] = newRoom;  //if the room wasnt overlaping then add it to the rooms
		}

	}
	
	//reads the room tiles and puts them into the world tiles
	private void addRoomsToTiles()
	{
		for(int currentRoom = 0 ; currentRoom < getRoomCount() ; currentRoom++)
		{
			Room room = rooms[currentRoom];
			int roomOriginX = room.startPointX;
			int roomOriginY = room.startPointY;
			int roomMaxX =  room.width;
			int roomMaxY =  room.height;
			int roomXIndex = 0;//index of of room tile being accessed from room object
			int roomYIndex = 0;
			
			int worldX = roomOriginX;
			int worldY = roomOriginY;
			
			while(roomXIndex < roomMaxX)
			{
			    roomYIndex = 0;
			    while(roomYIndex < roomMaxY)
			    {
			        worldX = roomOriginX + roomXIndex;
			        worldY = roomOriginY + roomYIndex;
			        tiles[worldX][worldY] = room.roomTiles[roomXIndex][roomYIndex];
			        roomYIndex++;
			    }
	             roomXIndex++;
			}	
		}
	}
	
	//this creates "hallways" by finding all the tiles between two rooms and setting those tiles to be traversable
	private void connectRooms()
	{
		for(int room = 0 ; room < getRoomCount() - 1 ; room++) //-1 because i look ahead one cell in the array when comparing
		{
			int diffx = rooms[room+1].centerX - rooms[room].centerX; 
			int diffy = rooms[room+1].centerY - rooms[room].centerY;
			int xTile, yTile;
			
			for(int x = 0 ; x < Math.abs(diffx) ; x++)
			{
				if(diffx > 0)
				{
					xTile = rooms[room].centerX + x;
					yTile = rooms[room].centerY;
				}
				else
				{
					xTile = rooms[room].centerX - x;
					yTile = rooms[room].centerY;
				}
				
				tiles[xTile][yTile] = new Tile(xTile,yTile);
			 }

			for(int y = 0 ; y < Math.abs(diffy) ; y++ )
			{
			    if(diffy > 0)
			    {
			    	xTile = rooms[room+1].centerX;
			    	yTile = rooms[room].centerY + y;
			    }
			    else
			    {
			    	xTile = rooms[room+1].centerX;
			    	yTile = rooms[room].centerY - y;
			    }
			    
				tiles[xTile][yTile] = new Tile(xTile,yTile);
			}	
		} 
	}
	
	private boolean isOverlaping(Room room)
	{
		for(int x = 0 ; x < getRoomCount() ; x++)
		{
			if(Rect.intersects(room.rectangle , rooms[x].rectangle))
			{
				return true;
			}
		}
		return false;
	}
	
	//returns the amount of not null rooms in the rooms array
	protected int getRoomCount()
	{
		int amount=0;
		for(int x = 0 ; x < rooms.length ; x++)
		{
			if(rooms[x] != null)
			{
				amount++;
			}
		}
		return amount;
	}

	private void populateWorld()
	{
	    Random gen = new Random();
	    int enemiesToMake = gen.nextInt(maxEnemies) + minEnemies;
	    int enemiesMade = 0;
	    enemies = new ArrayList<Enemy>(enemiesToMake);
	    
	    while(enemiesMade < enemiesToMake)
	    {
	        int randX = gen.nextInt(width-1) + 1;
	        int randY = gen.nextInt(height-1) + 1;
	        while(!tiles[randX][randY].isTraversableToEnemy())
	        {
	            randX = gen.nextInt(width-1) + 1;
	            randY = gen.nextInt(height-1) + 1; 
	        }
            enemies.add(new Enemy(randX,randY,this,Tile_Type.ENEMY));
            enemiesMade++;     
	    }
	    
	    for(Enemy e : enemies)
	    {
	        tiles[e.x][e.y].setType(e.type);
	    }
	    
	}
	
    private void populateWorldWithMoreDifficultEnemies(World w)
    {
        Random gen = new Random();
        int enemiesToMake = gen.nextInt(maxEnemies) + minEnemies;
        int enemiesMade = 0;
        ArrayList<Enemy> newEnemies = new ArrayList<Enemy>(enemiesToMake);
        
        while(enemiesMade < enemiesToMake)
        {
            int randX = gen.nextInt(width-1) + 1;
            int randY = gen.nextInt(height-1) + 1;
            while(!tiles[randX][randY].isTraversableToEnemy())
            {
                randX = gen.nextInt(width-1) + 1;
                randY = gen.nextInt(height-1) + 1; 
            }
            newEnemies.add(new Enemy(w.enemies.get(0), randX, randY));
            enemiesMade++;     
        }
        
        enemies = newEnemies;
        
        for(Enemy e : enemies)
        {
            tiles[e.x][e.y].setType(e.type);
        } 
    }
	
   private void placeStairCase()
   {
       tiles[rooms[getRoomCount()-1].centerX][rooms[getRoomCount()-1].centerY].setType(Tile_Type.STAIRS);
   }

	public Tile getTileFacing(int x, int y, Direction direction) 
	{
		 switch(direction)
         {
			 case NORTH:
			 {
				 return tiles[x][y-1];
			 }
			 case SOUTH:
			 {
				 return tiles[x][y+1]; 
			 }
			 case EAST:
			 {
				 return tiles[x+1][y];
			 }
			 case WEST:
			 {
				 return tiles[x-1][y];
			 }
			case NORTHEAST:
			{
	            return tiles[x+1][y-1];
			}
			case NORTHWEST:
			{
                return tiles[x-1][y-1];
			}
			case SOUTHEAST:
			{
                return tiles[x+1][y+1];
			}
			case SOUTHWEST:
			{
                return tiles[x-1][y+1];
			}
			default:
			{
				break;
			}
         }
		 return null;
	}

	public Tile getTile(int x, int y) 
	{
		return tiles[x][y];
	}

    public void remove(Enemy e) 
    {
        enemies.remove(e);
        getTile(e.getX(),e.getY()).setType(e.typeBeforeEntityMoved);  
    }
    
    public void setPlayer(Player p)
    {
        player = p;
    }
    
    
    //THIS IS THE A* SEARCH ALGORITHIM THAT RETURNS A PATHNODE TO A LIST WHICH CONTAINS A PATH TO FROM START TO FINISH
    public ArrayList<Tile> aStar (Tile start, Tile goal)
    {
        ArrayList<Tile> closed = new ArrayList<Tile>(1); //set of notes evaluated
        ArrayList<Tile> open = new ArrayList<Tile>(); //set of notes to be evaluated
        HashMap<Tile,Tile> came_from = new HashMap<Tile,Tile>(1); // map of navigated nodes (each node accociated with a parent)
        HashMap<Tile,Integer> g_score = new HashMap<Tile,Integer>(1);//associates a tile with a g score
        HashMap<Tile,Integer> f_score = new HashMap<Tile,Integer>(1);//assocaites a tile with an f score (f = g + h), h being cost estimate
        
        open.add(start);
        
        g_score.put(start, 0);
        f_score.put(start, g_score.get(start) + heuristic_cost_estimate(start, goal) );
        
        while(!open.isEmpty())
        {
            Tile current = lowest_f_score(f_score);
            if(current == goal)
            {
                return reconstruct_path(came_from,goal);
            }
            open.remove(current);
            closed.add(current);
            for(Tile neighbor : getSurroundingTiles(current))
            {
                int tentative_g_score = g_score.get(current) + ( dist_between(current,neighbor) ); //10 being lateral_movement_cost
                /*if(closed.contains(neighbor) && tentative_g_score >= g_score.get(neighbor))
                {
                    continue;
                }*/
                if(closed.contains(neighbor) || !neighbor.isTraversableToEnemyNotIncludingEnemy())
                {
                    continue;
                }
                if(!closed.contains(neighbor) || tentative_g_score < g_score.get(neighbor))
                {
                    came_from.put(neighbor, current);
                    g_score.put(neighbor, tentative_g_score);
                    f_score.put(neighbor, g_score.get(neighbor) + heuristic_cost_estimate(neighbor, goal)); //f = g + h
                    if(!open.contains(neighbor))
                    {
                        open.add(neighbor);
                    }
                }
            }
        }
        return null; //if failed
        
        
    }
    
    private Integer heuristic_cost_estimate(Tile start, Tile goal)
    {
        return new Integer( (Math.abs(start.x - goal.x) + Math.abs(start.y - goal.y) )); 
    }
    
    private Tile lowest_f_score(HashMap<Tile,Integer> f_score)
    {
        Tile lowest = null;
        for(Tile t : f_score.keySet())
        {
            if(lowest == null) { lowest = t; }
            if(f_score.get(t) < f_score.get(lowest)){ lowest = t;}
        }
        return lowest;
    }
    
    private ArrayList<Tile> reconstruct_path(HashMap<Tile,Tile> came_from, Tile current_node)
    {
        ArrayList<Tile> path = new ArrayList<Tile>(1);
        while(came_from.containsKey(current_node))
        {
            path.add(current_node);
            current_node = came_from.get(current_node);
        }
        return path;
    }
    
    private int dist_between(Tile current, Tile neighbor)
    {
        return Math.abs(current.x - neighbor.x) + Math.abs(current.y - neighbor.y);
    }
    
    private Tile[] getSurroundingTiles(Tile location) 
    {
        int x = location.x;
        int y = location.y;
        int counter = 0;
        Tile[] surroundingTiles = new Tile[8];
        
        //add those tiles to the new array
        for(Direction d : Direction.values())
        {
            surroundingTiles[counter] = getTileFacing(x,y,d);
            counter++;
        }
        return surroundingTiles;
    }
}
