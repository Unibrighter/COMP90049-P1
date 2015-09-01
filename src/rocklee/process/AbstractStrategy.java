package rocklee.process;

import java.io.File;
import java.nio.MappedByteBuffer;
import java.util.Scanner;

import rocklee.units.PlaceName;
import rocklee.units.Tweet;

/**
 * This is the basic and general case of all strategy, every strategy need to
 * extends this class and implement a method to deal with a line of tweet
 * 
 * 
 * @author Kunliang WU
 * @version 2015-09-01
 * 
 * */

public abstract class AbstractStrategy implements Runnable
{
	// this value is the minimum limit that a half-done-match can be filtered as
	// an approximate match
	public static final double THRESHOLD = 0.9;

	// the path for the resource is the same for every thread
	protected static File TWEET_INPUT_FILE = null;

	// this value shows the match of the entire string as the process goes on
	// ranging from 0 to 1
	protected double match_rate = -1d;

	protected PlaceName placeName = null;

	// input source and its stream for tweets, every thread has an individual
	// scanner
	protected Scanner scanner = null;

	// this is an indicator that shows if we need to use mapMemory or not
	protected boolean mapMemory = false;

	protected static MappedByteBuffer mappedByteBuffer = null;

	public static void setTweetInputFile(File file)
	{
		AbstractStrategy.TWEET_INPUT_FILE = file;
	}

	public void setPlaceName(PlaceName placeName)
	{
		this.placeName = placeName;
	}

	public static void setMappedByteBuffer(MappedByteBuffer mappedByteBuffer)
	{
		AbstractStrategy.mappedByteBuffer = mappedByteBuffer;
	}

	abstract public void run();

	abstract public void dealWithOneTweet(Tweet tmpTweet);

}
