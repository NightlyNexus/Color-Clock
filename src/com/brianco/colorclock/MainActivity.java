/*
 * Copyright (C) 2013 Eric Cochran <ericphysics@gatech.edu>
 *
 * This file is part of Color Clock.
 * 
 * Color Clock is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Color Clock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * For a copy of the GNU General Public License, see 
 * <http://www.gnu.org/licenses/>.
 *
 */

package com.brianco.colorclock;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerDialog.OnColorChangedListener;
import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class MainActivity extends Activity {
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;;
	ColorPickerDialog cpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.main_activity);
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        
        //final TextClock textClock = (TextClock) findViewById(R.id.textClock);
        //textClock.setTextColor(PreferenceManager.getDefaultSharedPreferences(this).getInt("color", Color.RED));
        
        //PreferenceManager.getDefaultSharedPreferences(this).getInt("color", Color.RED);
        
        
        
        
        //setting the alarm clock click
        
        
        
        
        OnCancelListener cancelListener = new OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                 MainActivity.this.finish();
            }
        };
        
        OnColorChangedListener ccListener = new ColorPickerDialog.OnColorChangedListener(){

			@Override
			public void onColorChanged(int color) {
				// TODO Auto-generated method stub
				PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("color", color).apply();
				getSharedPreferences("" + mAppWidgetId, MODE_PRIVATE).edit().putInt("color", color).apply();
				//textClock.setTextColor(color);
				
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
				Bundle options = appWidgetManager.getAppWidgetOptions(mAppWidgetId);
				//int nwidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
				//int nheight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
                /*RemoteViews views = new RemoteViews(getPackageName(),
                		R.layout.activity_main);
                views.setTextColor(R.id.textClock, color);
                appWidgetManager.updateAppWidget(mAppWidgetId, views);*/
				//MyWidget.updateAppWidget(MainActivity.this, appWidgetManager, mAppWidgetId, color);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                MyWidget.updateAppWidget(MainActivity.this, appWidgetManager, mAppWidgetId, color, options);
                finish();
			}
        	
        };
        
        int initialColor = PreferenceManager.getDefaultSharedPreferences(this).getInt("color", Color.RED);
        
        cpd = new ColorPickerDialog(this, initialColor);
        cpd.setHexValueEnabled(true);
        cpd.setAlphaSliderVisible(true);
        cpd.setOnColorChangedListener(ccListener);
        cpd.setOnCancelListener(cancelListener);
        cpd.show();
        
        /*OnColorChangedListener ccListener = new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                //you change your color when the color is changed in the dialog
            	PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("color", color).apply();
            	textClock.setTextColor(color);
            	
            	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
                RemoteViews views = new RemoteViews(getPackageName(),
                		R.layout.activity_main);
                views.setTextColor(R.id.textClock, color);
                appWidgetManager.updateAppWidget(mAppWidgetId, views);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        };
        new ColorPickerDialog(this, ccListener, PreferenceManager.getDefaultSharedPreferences(this).getInt("color", 256)).show();*/
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	if(cpd!=null){
    		cpd.cancel();
    	}
    	finish();
    }
    
}
