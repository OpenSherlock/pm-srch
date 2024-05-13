/*
 * Copyright 2021 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.research.carrot2;
import java.util.*;

import org.topicquests.os.asr.Environment;

import java.text.SimpleDateFormat;
/**
 * @author park
 *
 */
public class HarvestTimer {
	private Environment environment;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat otherFormat;
	//this acts as a toggle
	private boolean isStarted = false;
	private final long delay = 60000; // 1 minute
	private Object synchObject = new Object();
	private int hour = 0;
	private final int stopHour = 2; //19; // for test 2;
	private final int startHour = 18;

	/**
	 * 
	 */
	public HarvestTimer(Environment env) {
		environment = env;
		environment.logDebug("HarvestTimer Booting");
		environment = env;
		//put hour in 24 hour format
		dateFormat = new SimpleDateFormat("HH");
		otherFormat = new SimpleDateFormat("HH:mm");
		System.out.println("0");
		doStart();
	}
	
	private void doStart() {
		System.out.println("1");
		int now = getHour();
		if (now < startHour) {
			System.out.println("2");
			//calculate time
			Calendar c = new GregorianCalendar();
			c.set(Calendar.HOUR_OF_DAY, startHour);
			c.set(Calendar.MINUTE, 0);
			long then = c.getTimeInMillis();
			Date j = new Date();
			long delta = then -j.getTime();
			Timer t = new Timer();
			t.schedule(new StartTask(), delta);
			System.out.println("Waiting "+delta);
		} else {
			System.out.println("3");
			//start now, but schedule a stop task
			String x = otherFormat.format(new Date());
			String []y = x.split(":");
			int h = Integer.parseInt(y[0]);
			int m = Integer.parseInt(y[1]);
			int dh = 0;
			if (stopHour > startHour)
				dh = h+(stopHour-startHour); // now + difference = total hours
			else
				dh = 24-h+stopHour; // end of day - now + stophour = total hours
			long f = 1000*60*60*dh;
			long df = 1000*(60-m); //milliseconds after hour
			f = f - df;
			Timer t = new Timer();
			t.schedule(new StopTask(), f);
			environment.startHarvest();
		}
		
	}

	/**
	 * Return hour in 24-hour format
	 * @return
	 */
	private int getHour() {
		int result = 0;
		String x = dateFormat.format(new Date());
		result = Integer.parseInt(x);
		return result;
	}

	private class StopTask extends TimerTask {
		@Override
		public void run() {
			environment.logDebug("HarvestTimer Stopping");
			System.out.println("Pausing");
			environment.pauseHarvest();
		}
	}
	
	private class StartTask extends TimerTask {
		
		@Override
		public void run() {
			
			System.out.println("Starting");
			//schedule the stop task
			//7 hour delay
//			long delay = 1000 * (60*60*7);
			String x = otherFormat.format(new Date());
			String []y = x.split(":");
			int h = Integer.parseInt(y[0]);
			int m = Integer.parseInt(y[1]);
			int dh = 0;
			if (stopHour > startHour)
				dh = h+(stopHour-startHour); // now + difference = total hours
			else
				dh = 24-h+stopHour; // end of day - now + stophour = total hours
			long f = 1000*60*60*dh;
			long df = 1000*(60-m); //milliseconds after hour
			f = f - df;
			Timer t = new Timer();
			t.schedule(new StopTask(), f); // delay);

			environment.logDebug("HarvestTimer Starting");
			environment.startHarvest();
		}
	} 
}
