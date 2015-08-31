package rocklee.units;

import org.apache.log4j.Logger;

/***
 * This class is used to support the gazetteers After the set up process,every
 * line read from the file give as query will be warped as an entity of
 * PlaceName, to make it easier for further process
 * 
 * @version 2015-08-29 11:54
 * @author Kunliang WU
 *
 */

public class PlaceName
{
	// for debug and info
	private static Logger log = Logger.getLogger(PlaceName.class);

	private String[] tokens = null;
	private String fullName = null;


	public PlaceName(String raw_str)
	{
		// empty string given
		if (raw_str == null || raw_str.compareTo("") == 0)
		{
			log.debug("Empty string given to set up the place name!");
			return;
		}

		else
		{
			this.fullName = raw_str;

			this.tokens = raw_str.split(" ");
		}
	}
	
	public String[] getTokens()
	{
		return this.tokens;
	}
	
	public String getFullName()
	{
		return this.fullName;
	}

	
	
	

}
