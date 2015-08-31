import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import rocklee.methods.Approach;
import rocklee.process.GlobalEditDistanceStrategy;
import rocklee.units.PlaceName;
import rocklee.units.Tweet;

public class Test
{
	
	// for debug and info
	private static Logger log = Logger.getLogger(Test.class);

	public static void main(String[] args)
	{
		// test class
		System.out.println(Approach.globalEditDistance("mothersucker",
				"motherfucker"));

		// initialize the query input

		File query_file = new File(args[0]);
		File tweet_file = new File(args[1]);

		ArrayList<PlaceName> query_list = new ArrayList<PlaceName>(1000);

		try
		{
			Scanner query_scan = new Scanner(query_file);
			long timeStamp_readQueryList = System.currentTimeMillis();

			while (query_scan.hasNextLine())
			{
				query_list.add(new PlaceName(query_scan.nextLine()));
			}
			System.out.println("Query Structure Setup Time: "
					+ (System.currentTimeMillis() - timeStamp_readQueryList));

			log.warn("Query Structure Setup Time: "
					+ (System.currentTimeMillis() - timeStamp_readQueryList));
			
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(10);

		// 设置文件
		GlobalEditDistanceStrategy.setTweetInputFile(tweet_file);

		long timeStamp_matchSearching = System.currentTimeMillis();
		
		
		while (!query_list.isEmpty())
		{
			GlobalEditDistanceStrategy task = new GlobalEditDistanceStrategy();
			task.setPlaceName(query_list.remove(0));

			pool.execute(task);
		}

		// GlobalEditDistanceStrategy task = new GlobalEditDistanceStrategy();
		// task.setPlaceName(new PlaceName("Clear"));
		// pool.execute(task);

		pool.shutdown();

		System.out.println("Query Structure Setup Time: "
				+ (System.currentTimeMillis() - timeStamp_matchSearching));

		log.warn("Query Structure Setup Time: "
				+ (System.currentTimeMillis() - timeStamp_matchSearching));
	}

}
