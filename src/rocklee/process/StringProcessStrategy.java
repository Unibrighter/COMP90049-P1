package rocklee.process;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Scanner;

import org.apache.log4j.Logger;

import rocklee.methods.Approach;
import rocklee.units.PlaceName;
import rocklee.units.Tweet;


/***
 * When it's compared to Global edit distance, there are two main factors you need to concern 
 * 
 * @version 2015-08-29 11:54
 * @author Kunliang WU
 *
 */

public class StringProcessStrategy extends AbstractStrategy
{

	public static void main(String[] args)
	{
		StringProcessStrategy task=new StringProcessStrategy();
		task.setPlaceName(new PlaceName("Jack Lee"));
		
		task.dealWithOneTweet(new Tweet("8975748880\tI just fucking love u Jack Lee u sweet little honey"));
	}
	
	
	// this value is the minimum limit that a half-done-match can be filtered as
	// an approximate match
	public static final double THRESHOLD = 0.85;
	

	// for debug and info, since log4j is thread safe, it can also be used to
	// record the result and output
	private static Logger log = Logger
			.getLogger(StringProcessStrategy.class);

	public void run()
	{

		// set up the scanner for tweets input
		try
		{

			if (!mapMemory)
			{// direct disk IO stream
				this.scanner = new Scanner(
						TokenProcessStrategy.TWEET_INPUT_FILE);
			}

			else
			{// memory map is used

				Charset charset = Charset.forName("US-ASCII");
				CharsetDecoder decoder = charset.newDecoder();
				CharBuffer charBuffer = decoder
						.decode(TokenProcessStrategy.mappedByteBuffer
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
		StringBuffer strBuffer=new StringBuffer("");
		
		double match_rate=Approach.localEditDistance(this.placeName, tmpTweet, strBuffer);
		
		if(match_rate>=StringProcessStrategy.THRESHOLD)
		{//print the relating info to the log file
			String result_output = "@@@Place Name:"
					+ placeName.getFullName()
					+ "\tMatched part in Tweet("
					+ tmpTweet.getTweetID()
					+ ")\tFor "
					+ strBuffer;
//			System.out.println(result_output);
			log.debug(result_output);
		}

	}
	
}
