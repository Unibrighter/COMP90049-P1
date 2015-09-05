package rocklee.process;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Scanner;

import org.apache.log4j.Logger;

import rocklee.methods.Approach;
import rocklee.units.Tweet;

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

public class GlobalEditDistanceStrategy extends AbstractStrategy
{

	// this value is the minimum limit that a half-done-match can be filtered as
	// an approximate match
	public static final double THRESHOLD = 0.85;
	
	
	// for debug and info, since log4j is thread safe, it can also be used to
	// record the result and output
	private static Logger log = Logger
			.getLogger(GlobalEditDistanceStrategy.class);

	public void run()
	{

		// set up the scanner for tweets input
		try
		{

			if (!mapMemory)
			{// direct disk IO stream
				this.scanner = new Scanner(
						GlobalEditDistanceStrategy.TWEET_INPUT_FILE);
			}

			else
			{// memory map is used

				Charset charset = Charset.forName("US-ASCII");
				CharsetDecoder decoder = charset.newDecoder();
				CharBuffer charBuffer = decoder
						.decode(GlobalEditDistanceStrategy.mappedByteBuffer
								.asReadOnlyBuffer());
				this.scanner = new Scanner(charBuffer).useDelimiter(System
						.getProperty("line.separator"));
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		while (scanner.hasNextLine())
		{

			Tweet tmpTweet = new Tweet(scanner.nextLine());

			this.dealWithOneTweet(tmpTweet);

		}
		scanner.close();

	}

	@Override
	public void dealWithOneTweet(Tweet tmpTweet)
	{
		// the general idea is to capture the first index that a perfect
		// individual word match

		String[] placeNameTokens = placeName.getTokens();

		String[] tweetTokens = tmpTweet.getTokens();

		// within one piece of tweet
		for (int i = 0; i <= tweetTokens.length - placeNameTokens.length; i++)
		{

			// start calculate each match rate
			match_rate = Approach.globalEditDistance(placeNameTokens[0],
					tweetTokens[i]);

			// found one which is over threshold
			// TODO here is an assumption to make: the first word for a
			// place name is almost a perfect match
			if (match_rate >= GlobalEditDistanceStrategy.THRESHOLD)
			{
				// look further to see whether the last one also match
				match_rate = Approach.globalEditDistance(
						placeNameTokens[placeNameTokens.length - 1],
						tweetTokens[i + placeNameTokens.length - 1]);
				if (match_rate >= GlobalEditDistanceStrategy.THRESHOLD)
				{// last element also matched,then we need check the words
					// between the head and tail

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
								+ tmpTweet.getBestMatchSequenceOfContent(i,
										placeNameTokens.length) + "\n";

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
