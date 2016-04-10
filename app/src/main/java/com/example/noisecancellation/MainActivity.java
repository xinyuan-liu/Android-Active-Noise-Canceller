package com.example.noisecancellation;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ToggleButton;

import com.example.noisecancellation.MainProcess.MainProcess;

public class MainActivity extends Activity
{
	private MainProcess work_process;
    Thread thread;
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        work_process = new MainProcess();
        thread=new Thread( work_process, "work" );
        thread.start();
        
        if( ( savedInstanceState != null ) && ( savedInstanceState.containsKey( "ButtonState" ) ) )
        {
            restoreButtonState( savedInstanceState.getBoolean( "ButtonState" ) );
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return( true );
        
    }

    public void onToggleClicked(View view) 
    {        

        boolean on = ((ToggleButton) view).isChecked();
        
        if( on ) 
        {
            work_process.resume();
        } 
        else 
        {
        	work_process.pause();
        }
        
    }
    
    @Override
    public void onDestroy()
    {
        work_process.stopProcessing();
        try
        {
            thread.join();
        }
        catch( InterruptedException ie )
        {
            Log.i( "MainActivity--onToggleClicked()", "Failed to clean up after myself." );
            throw new RuntimeException( "Unable to clean up after myself" );
        }
        super.onDestroy();
        
    }
        
    @Override
    public void onSaveInstanceState( Bundle b )
    {
        super.onSaveInstanceState( b );
        ToggleButton btn = (ToggleButton)findViewById( R.id.togglebutton );
        b.putBoolean( "ButtonState", btn.isChecked() );
    }
    
    @Override
    public void onRestoreInstanceState( Bundle b )
    {
        super.onRestoreInstanceState( b );
        restoreButtonState( b.getBoolean( "ButtonState" ) ); 
    }
    
    private void restoreButtonState( boolean prev_state )
    {
        ToggleButton btn = (ToggleButton)findViewById( R.id.togglebutton );
        btn.setChecked( prev_state );
        if( prev_state )
        {
            work_process.resume();
        }
    }

}