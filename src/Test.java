import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import rocklee.methods.Approach;
import rocklee.process.GlobalEditDistanceStrategy;
import rocklee.units.PlaceName;

public class Test
{
	
	// for debug and info
	private static Logger log = Logger.getLogger(Test.class);

	public static void main(String[] args)
	{
//		Test.testMap(args[1]);
//		Test.testScannerIOStream(args[1]);
		
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
		ExecutorService pool = Executors.newFixedThreadPool(8);

		// 设置文件
		GlobalEditDistanceStrategy.setTweetInputFile(tweet_file);

		long timeStamp_matchSearching = System.currentTimeMillis();
		
		
		while (!query_list.isEmpty())
		{
			GlobalEditDistanceStrategy task = new GlobalEditDistanceStrategy();
			

			
			task.setPlaceName(query_list.remove(0));
			
			pool.execute(task);
		}

		pool.shutdown();


	}
	
	
	
	//TODO 写一个对比方法确认一下map 转nextLine和scanner io nextLine的效率
	//see the README.md for notes
	public static void testMap(String filePath)
	{
		Long startTime = System.currentTimeMillis();
		try
		{
			FileChannel fc = new FileInputStream(filePath).getChannel();
			MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY,0, fc.size());
			Charset charset = Charset.forName("US-ASCII");
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer charBuffer = decoder.decode(byteBuffer);
			Scanner sc = new Scanner(charBuffer).useDelimiter(System.getProperty("line.separator"));
			while(sc.hasNext())
			{
				//read str as dumb spin
				String tmpStr=sc.next();
			}
			fc.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CharacterCodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.printf("map time used:"+estimatedTime);
	}

	public static void testScannerIOStream(String filePath) 
	{
		Long startTime = System.currentTimeMillis();
		Scanner sc=null;
		try
		{
			sc = new Scanner(new File(filePath));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(sc.hasNext())
		{
			//read str as dumb spin
			String tmpStr=sc.next();
		}
		Long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.printf("\npure io time used:"+estimatedTime);	
		
	}
	
	
}
