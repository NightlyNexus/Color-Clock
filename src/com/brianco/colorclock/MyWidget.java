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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

public class MyWidget extends AppWidgetProvider{
	
	@Override
    public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
        	int appWidgetId = appWidgetIds[i];
        	Log.d("deleting", "" + appWidgetId);
        	context.getSharedPreferences("" + appWidgetId, Context.MODE_PRIVATE).edit().clear().apply();
        }
    }
	
	@Override
	public void  onAppWidgetOptionsChanged (Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions){
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        int color = context.getSharedPreferences("" + appWidgetId, Context.MODE_PRIVATE).getInt("color", Color.RED);
        updateAppWidget(context, appWidgetManager, appWidgetId, color, newOptions);
	}
	
	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, int color, Bundle options) {
		int nwidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int nheight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
		Log.d("nheight", "" + nheight);
		Log.d("nwidth", "" + nwidth);
        // Construct the RemoteViews object.  It takes the package name (in our case, it's our
        // package, but it needs this because on the other side it's the widget host inflating
        // the layout from our package).
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_main);
        
        //set color
        views.setTextColor(R.id.textClock, color);
        
        //set size
        //views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_SP, dpToSp(context, nwidth-16));
        /*if (nwidth >= 60 || nwidth==0){
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_SP, 48);
        } else if(nwidth >= 40){
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_SP, 26);
        } else{
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_SP, 12);
        }*/
        /*if(nwidth==0&&nheight==0){
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_SP, 
        			context.getResources().getDimension(R.dimen.lock_screen_size));
		} else if(nwidth >= 300){
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_DIP, 62);
        }else if(nwidth >= 210){
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_DIP, 48);
        } else if(nwidth >= 100){
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_DIP, 36);
        } else if(nwidth >= 50){
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_DIP, 24);
        } else{
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_DIP, 14);
        }*/
        /*float fontSize = context.getResources().getDimension(R.dimen.widget_big_font_size);
        views.setTextViewTextSize(
                R.id.textClock, TypedValue.COMPLEX_UNIT_DIP, nwidth * 48 / 272);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(fontSize * scale / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        Log.d("size in dip", "" + dp);*/
        // Get the value of OPTION_APPWIDGET_HOST_CATEGORY
        int category = options.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY, -1);
        // If the value is WIDGET_CATEGORY_KEYGUARD, it's a lockscreen widget
        boolean isKeyguard = category == AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD;
        if(isKeyguard || (nwidth==0&&nheight==0)){
        	views.setTextViewTextSize(R.id.textClock, TypedValue.COMPLEX_UNIT_DIP, 
        			context.getResources().getDimension(R.dimen.lock_screen_size));
        } else{
	        //int wdp = nwidth * 48 / 272;
        	int wdp = nwidth * 48 / 240;
	        int hdp = nheight * 64 / 71;
	        int dp = (wdp <= hdp) ? wdp : hdp;
	        views.setTextViewTextSize(
	                R.id.textClock, TypedValue.COMPLEX_UNIT_DIP, dp);
	        Log.d("wdp:", "" + wdp);
	        Log.d("hdp:", "" + hdp);
	        Log.d("dp:", "" + dp);
        }
        
        //set alarm clock click
        PackageManager packageManager = context.getPackageManager();
    	Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
    	// Verify clock implementation
    	String clockImpls[][] = {
    	        {"HTC Alarm Clock", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" },
    	        {"Standard Alarm Clock", "com.android.deskclock", "com.android.deskclock.AlarmClock"},
    	        {"Froyo Nexus Alarm Clock", "com.google.android.deskclock", "com.android.deskclock.DeskClock"},
    	        {"Moto Blur Alarm Clock", "com.motorola.blur.alarmclock",  "com.motorola.blur.alarmclock.AlarmClock"},
    	        {"Samsung Galaxy Clock", "com.sec.android.app.clockpackage","com.sec.android.app.clockpackage.ClockPackage"}
    	};
    	boolean foundClockImpl = false;
    	for(int i=0; i<clockImpls.length; i++) {
    	    String vendor = clockImpls[i][0];
    	    String packageName = clockImpls[i][1];
    	    String className = clockImpls[i][2];
    	    try {
    	        ComponentName cn = new ComponentName(packageName, className);
    	        ActivityInfo aInfo = packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
    	        alarmClockIntent.setComponent(cn);
    	        Log.d("Clock", "Found " + vendor + " --> " + packageName + "/" + className + ".");
    	        foundClockImpl = true;
    	    } catch (NameNotFoundException e) {
    	        Log.d("Clock", vendor + " does not exists.");
    	    }
    	}
    	if (foundClockImpl) {
    	    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmClockIntent, 0);
    	        // add pending intent to your component
    	        // ....
    		views.setOnClickPendingIntent(R.id.textClock, pendingIntent);
    	}

        // Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetIds[]){
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		final int N = appWidgetIds.length;
		for (int i=0; i<N; i++) {
	    	int appWidgetId = appWidgetIds[i];
	    	Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
	    	if(options!=null){
	      		onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, options);
	      	}
		}
	}
}
