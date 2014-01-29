package com.CornelCocioaba.Pixti.Core;


public class Time 
{
	public static final int MONTHS_PER_YEAR = 12;

    public static final int DAYS_PER_WEEK = 7;

    public static final int DAYS_PER_MONTH = 30;

    public static final int HOURS_PER_DAY = 24;

    public static final int MINUTES_PER_HOUR = 60;

    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int MICROSECONDS_PER_SECOND = 1000 * 1000;
    public static final long NANOSECONDS_PER_SECOND = 1000 * 1000 * 1000;

    public static final long MICROSECONDS_PER_MILLISECOND = MICROSECONDS_PER_SECOND / MILLISECONDS_PER_SECOND;

    public static final long NANOSECONDS_PER_MICROSECOND = NANOSECONDS_PER_SECOND / MICROSECONDS_PER_SECOND;
    public static final long NANOSECONDS_PER_MILLISECOND = NANOSECONDS_PER_SECOND / MILLISECONDS_PER_SECOND;

    public static final float SECONDS_PER_NANOSECOND = 1f / NANOSECONDS_PER_SECOND;
    public static final float MICROSECONDS_PER_NANOSECOND = 1f / NANOSECONDS_PER_MICROSECOND;
    public static final float MILLISECONDS_PER_NANOSECOND = 1f / NANOSECONDS_PER_MILLISECOND;

    public static final float SECONDS_PER_MICROSECOND = 1f / MICROSECONDS_PER_SECOND;
    public static final float MILLISECONDS_PER_MICROSECOND = 1f / MICROSECONDS_PER_MILLISECOND;

    public static final float SECONDS_PER_MILLISECOND = 1f / MILLISECONDS_PER_SECOND;

    public static final int SECONDS_PER_MINUTE = 60;
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
    public static final int SECONDS_PER_WEEK = SECONDS_PER_DAY * DAYS_PER_WEEK;
    public static final int SECONDS_PER_MONTH = SECONDS_PER_DAY * DAYS_PER_MONTH;
    public static final int SECONDS_PER_YEAR = SECONDS_PER_MONTH * MONTHS_PER_YEAR;
	
	
	/**
	 * deltaTime in seconds
	 */
	public static float deltaTime;
	
	/**
	 * time in seconds
	 */
	public static float time;
	
	/**
	 * scale factor 1 - normal, 0 - stop
	 */
	public static float timeScale;
	
	public static void Init(){
		time = System.nanoTime() * Time.SECONDS_PER_NANOSECOND;
	}
	/*
	 * this should only be called in onDraw
	 */
	public static void Update()
	{
		final float newTime = System.nanoTime() * Time.SECONDS_PER_NANOSECOND;
		
        deltaTime = (newTime - time) * timeScale;

        time += deltaTime;
	}
}
