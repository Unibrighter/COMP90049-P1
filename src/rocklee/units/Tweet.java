package rocklee.units;

import org.apache.log4j.Logger;

public class Tweet
{

	// for debug and info
	private static Logger log = Logger.getLogger(Tweet.class);

	//tweet id,since the meaning is no longer useful,use String instead of long
	private String TweetID=null;
	
	//whole content contained in the piece of tweet
	private String fullContent=null;
	
	//separate info 
	private String[] tokens=null;
	
	//length of the tokens array
	int length = -1;

	public Tweet(String raw_str)
	{
		// empty string given
		if (raw_str == null || raw_str.compareTo("") == 0)
		{
			log.debug("Empty string given to set up the tweet!");
			return;
		}

		else
		{
			//first separate the id and content
			String[] tmp=raw_str.split("\t");
			
			//extract tweet id
			this.TweetID=tmp[0];
			this.fullContent = tmp[1];
			
			//extract tokens array
			this.tokens=this.fullContent.split(" ");
			
		}
	}
	
	public String[] getToken()
	{
		return this.tokens;
	}
	
	public String getFullContent()
	{
		return this.fullContent;
	}
	
	
	
	
	
	
}
