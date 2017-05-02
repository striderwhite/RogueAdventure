package com.game.rogueadventure;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Scanner;

public class EventLog extends TextView 
{
	
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
    }
    
    public void logText(String tag, String message)
    {
    	if(countLines((String) getText()) >= 6)
        {
    		setText("");
        }
        
        setText(getText()+ "\n" + tag + ":\t" + message);   
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
