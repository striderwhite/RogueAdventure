package com.game.rogueadventure;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class EventLog extends TextView 
{
    ArrayList<String> log;
    final int LOG_SIZE = 10;
	
    public EventLog(Context context) 
    {
        super(context);
        init();
    }

    public EventLog(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public EventLog(Context context, AttributeSet attrs, int defStyle) 
    {
        super(context, attrs, defStyle);
        init();
    }
    
    public void init()
    {
        setTextColor(Color.rgb(0, 196, 255));
        setTextSize(11);
        log = new ArrayList<String>(LOG_SIZE);
    }
    
    public void logText(String tag, String message)
    {
        log.add( 0 , new String(tag + ": " + message));
        
    	if(log.size() > LOG_SIZE)
    	{
    	    log.remove(log.size() - 1);
        }
        
        setText(assembleMessage());   
    }

	public String assembleMessage() 
	{
	    String message = new String("");
	    for(String s : log)
	    {
	        message += s + "\n";
	    }
	    return message;
    }

    public int countLines(String text)
    {
        Scanner textReader = new Scanner(text);
        int count = 0;
        while(textReader.hasNextLine())
        {
            count++;
            textReader.nextLine();
        }
        return count;
    }

}
