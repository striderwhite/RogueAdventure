package com.game.rogueadventure;

public class PathNode 
{
	PathNode parent;
	int x, y; //locs on grid
	int f; //called "f cost" representing the efficiency of a given path.
	int g; //cost to move from parent plus parent g
	int h; //estimated distance to the goal from here
	PathNode(PathNode parent, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.parent = parent;
	}	
	PathNode(int x, int y)
	{
		this.x = x;
		this.y = y;
	}	
	public PathNode() 
	{
		
	}
	public void add(PathNode n)
	{
		parent = n;
	}
	
	public int getG()
	{
		while(parent != null)
		{
			return parent.g += g;
		}
		return g;
	}
	
	public void calculateCost()
	{
		
	}
	
	//if the x matches up and y diffrent return true
	//if y matches up and x diffrent true true
	//else return false
	public boolean isParallelTo(PathNode other)
	{
		if(y == other.y && x > other.x || x < other.x)
		{
			return true;
		}
		if(x == other.x && y > other.y || y < other.y)
		{
			return true;
		}
		return false;
	}
	public void setG(int i)
	{
		PathNode n = this;
		g = i;
		while(n.parent != null) //goes down the nodes and collects the g values
		{
			g = g + n.parent.g;
			n = n.parent;
		}
	}
	public void calculateScore(PathNode goal)
	{

		if(this.isParallelTo(parent))
		{
			setG(10);
		}
		else
		{
			setG(14);
		}

    	//calculate the h value for all nodes
    	h = (Math.abs((goal.x - x) + (goal.y - y)) * 10); //h value is amount of tiles away times 10
    	
    	//calculate f value for all nodes{
    	f = g + h;
		
	}
}
