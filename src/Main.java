import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import rocklee.process.AbstractStrategy;
import rocklee.process.StringProcessStrategy;
import rocklee.process.TokenProcessStrategy;
import rocklee.units.PlaceName;

public class Main
{
	// query collection
	private static ArrayList<PlaceName> query_list = null;

	// thread pool
	private static ExecutorService pool = null;

	// FileChannel fc used to manipulate the file
	private static FileChannel fc = null;

	// for debug and info
	private static Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args)
	{

		Main.initializeQuery(args[0]);
		// build the thread pool
		pool = Executors.newFixedThreadPool(8);

		Main.initializeTweetSetting(args[1]);

		// determine the process strategy we use
		boolean strStrategy = false;
		for (int i = 0; i < args.length; i++)
		{
			if ("-str".equalsIgnoreCase(args[i]))
			{
				strStrategy = true;
				break;
			}

		}

		if (strStrategy)
		{
			System.out
					.println("StringProcessStrategy/local edit distance approach");
		}

		else
		{
			System.out.println("TokenProcessStrategy.\nNow Give a specific approach:...");
			System.out.println("1 for global edit distance");
			System.out.println("2 for two gram ");
			System.out.println("3 for soundex");
			
			Scanner cmd=new Scanner(System.in);
			
			int cmd_index=cmd.nextInt();
			TokenProcessStrategy.setApproach(cmd_index);
			
		}

		// assign the task to the thread pool
		while (!query_list.isEmpty())
		{
			AbstractStrategy task = null;
			if (strStrategy)
			{

				task = new StringProcessStrategy();
				
			}

			else
			{

				task = new TokenProcessStrategy();

			}

			task.setPlaceName(query_list.remove(0));
			

			pool.execute(task);
		}

		// clean up
		if (fc != null)
			try
			{
				fc.close();
			} catch (IOException e)
			{

				e.printStackTrace();
			}
		pool.shutdown();

	}

	/**
	 * Read the file via a scanner and build the collection of query
	 * 
	 * */
	private static void initializeQuery(String filePath)
	{
		File query_file = new File(filePath);

		query_list = new ArrayList<PlaceName>(1000);

		try
		{
			Scanner query_scan = new Scanner(query_file);
			long timeStamp_readQueryList = System.currentTimeMillis();

			while (query_scan.hasNextLine())
			{
				query_list.add(new PlaceName(query_scan.nextLine()));
			}

			log.warn("Query Structure Setup Time: "
					+ (System.currentTimeMillis() - timeStamp_readQueryList));
			query_scan.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Set up the config for the input tweet source,such as memory map and
	 * stream
	 * 
	 * */
	private static void initializeTweetSetting(String filePath)
	{
		File tweet_file = new File(filePath);

		// set the tweet input source
		// for both two main strategies
		TokenProcessStrategy.setTweetInputFile(tweet_file);
		StringProcessStrategy.setTweetInputFile(tweet_file);

		// do the memory map
		fc = null;
		MappedByteBuffer byteBuffer = null;
		try
		{
			fc = new FileInputStream(tweet_file).getChannel();
			byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			// for both two main strategies
			TokenProcessStrategy.setMappedByteBuffer(byteBuffer);
			StringProcessStrategy.setMappedByteBuffer(byteBuffer);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
