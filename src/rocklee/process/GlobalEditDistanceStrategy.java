package rocklee.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

import org.apache.log4j.Logger;

import rocklee.methods.Approach;
import rocklee.units.PlaceName;
import rocklee.units.Tweet;
import sun.security.util.Length;

/***
 * 
 * This Class is a runnable thread which uses global strategy to determine
 * whether there exists a match case in one of the individual string
 * 
 * Note that the value to determine whether if the match is a highly marked one
 * should be well selected, so that the result can be more accurate.
 * 
 * This Class focuses on one PlaceName Query in the tweets. And finally all the
 * process will be done in a ThreadPool
 * 
 * @version 2015-08-29 11:54
 * @author Kunliang WU
 *
 */

public class GlobalEditDistanceStrategy implements Runnable
{

	// for debug and info, since log4j is thread safe, it can also be used to
	// record the result and output
	private static Logger log = Logger
			.getLogger(GlobalEditDistanceStrategy.class);

	// this value is the minimum limit that a half-done-match can be filtered as
	// an approximate match
	public static final double THRESHOLD = 0.9;

	// the path for the resource is the same for every thread
	private static File TWEET_INPUT_FILE = null;

	// this value shows the match of the entire string as the process goes on
	// ranging from 0 to 1
	private double match_rate = -1d;

	private PlaceName placeName = null;

	// input source and its stream for tweets, every thread has an individual
	// scanner
	private Scanner scanner = null;

	public static void setTweetInputFile(File file)
	{
		GlobalEditDistanceStrategy.TWEET_INPUT_FILE = file;
	}

	public void setPlaceName(PlaceName placeName)
	{
		this.placeName = placeName;
	}

	public void run()
	{
		// set up the scanner for tweets input
		try
		{
			this.scanner = new Scanner(
					GlobalEditDistanceStrategy.TWEET_INPUT_FILE);
			// TODO maybe we should use a file map so the IO operation would not
			// become the bottleneck that slows down the program?

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		while(scanner.hasNextLine())//until the end of tweet file
		{

			
			Tweet tmpTweet = new Tweet(scanner.nextLine());
			System.out.println("thread deads with:"+placeName.getFullName() +" and tweet #"+tmpTweet.getTweetID());
			
			// the general idea is to capture the first index that a perfect
			// individual word match

			String[] placeNameTokens = placeName.getTokens();

			String[] tweetTokens = tmpTweet.getTokens();

			for (int i = 0; i <= tweetTokens.length - placeNameTokens.length; i++)
			{

				
				
				// start calculate each match rate
				match_rate = Approach.globalEditDistance(placeNameTokens[0],
						tweetTokens[i]);

				
				// found one which is over threshold
				// TODO here is an assumption to make: the first word for a
				// place name is a must match
				if (match_rate >= GlobalEditDistanceStrategy.THRESHOLD)
				{
					log.info("!!!First Word Match Rate!!! "+match_rate+" for "+placeName.getFullName()+" # "+tmpTweet.getTweetID());
					// look further to see whether the last one also match
					match_rate = Approach.globalEditDistance(
							placeNameTokens[placeNameTokens.length - 1],
							tweetTokens[i + placeNameTokens.length - 1]);
					if (match_rate >= GlobalEditDistanceStrategy.THRESHOLD)
					{// last element also matched,then we need check the words
						// between the head and tail

						log.info("!!!Last Word Match Rate!!! "+match_rate+" for "+placeName.getFullName()+" # "+tmpTweet.getTweetID());
						
						boolean matched = true;

						// from second to the second last
						for (int j = 1; j < placeNameTokens.length - 1; j++)
						{
							match_rate = Approach.globalEditDistance(
									placeNameTokens[j], tweetTokens[i + j]);
							if (match_rate < GlobalEditDistanceStrategy.THRESHOLD)
							{
								// some one in the middle seems to be unhappy
								matched = false;// failed match this round
								break;
							}

						}

						if (!matched)
							continue;

						else
						{// found a successful match in one tweet!

							// out put the result
							String result_output = "@@@Place Name:"
									+ placeName.getFullName()
									+ "\tMatched part in Tweet("
									+ tmpTweet.getTweetID()
									+ ")\tFor "
									+ tmpTweet.getPartOfContent(i,
											tweetTokens.length);

							System.out.println(result_output);
							log.debug(result_output);

						}

					}

				}
				// last one does not match, move to next
				else
					continue;

			}
		}
		

	}

}
