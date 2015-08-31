package rocklee.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

import org.apache.log4j.Logger;

import rocklee.units.PlaceName;
import rocklee.units.Tweet;

/***
 * 
 * This Class is a runnable thread which uses global strategy to determine
 * whether there exists a match case in one of the individual string
 * 
 * Note that the value to determine whether if the match is a highly marked one
 * should be well selected, so that the result can be more accurate.
 * 
 * This Class focuses on one PlaceName Query in the tweets.
 * And finally all the process will be done in a ThreadPool
 * 
 * @version 2015-08-29 11:54
 * @author Kunliang WU
 *
 */

public class GlobalEditDistanceStrategy implements Runnable
{

	// for debug and info, since log4j is thread safe, it can also be used to record the result and output
	private static Logger log = Logger
			.getLogger(GlobalEditDistanceStrategy.class);

	// this value is the minimum limit that a half-done-match can be filtered as
	// an approximate match
	public static final double THREHOLD = 0.8;

	// this value is used to decide the start value, taking first two letters
	// into consideration
	public static final int NUM_PRE_CONSIDER = 2;
	
	//the path for the resource is the same for every thread
	private static  File TWEET_INPUT_FILE = null;

	// this value shows the match of the entire string as the process goes on
	//ranging from 0 to 1
	private double match_rate = -1d;

	
	private PlaceName placeName=null;
	
	// input source and its stream for tweets, every thread has an individual scanner
	private Scanner scanner = null;


	public static void setTweetInputFile(File file)
	{
		GlobalEditDistanceStrategy.TWEET_INPUT_FILE=file;
	}
	
	public void setPlaceName(PlaceName placeName)
	{
		this.placeName=placeName;
	}
	
	
	
	public void run()
	{
		//set up the scanner for tweets input
		try
		{
			this.scanner=new Scanner(GlobalEditDistanceStrategy.TWEET_INPUT_FILE);
			//TODO maybe we should use a file map so the IO operation would not become the bottleneck that slows down the program?
			
			Tweet tmpTweet=new Tweet(scanner.nextLine());
			
			
			
			
			
			
			
			
			
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
			

	}



}
