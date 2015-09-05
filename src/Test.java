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
import rocklee.process.TokenProcessStrategy;
import rocklee.units.PlaceName;
import net.java.frej.*;
import net.java.frej.fuzzy.*;
public class Test
{

	// for debug and info
	private static Logger log = Logger.getLogger(Test.class);

	public static void main(String[] args)
	{
		Regex testReg=new Regex("[(spring,valley)]");
//		testReg.setThreshold(0.85);
		System.out.println(testReg.presentInSequence("i just got fucked in spring valleeeey super fun"));
		
		System.out.println(testReg.getMatchStart());
		System.out.println(testReg.getMatchEnd());

		System.out.println(testReg.getThreshold());
		System.out.println(testReg.matchedTokenCount());
		
		System.out.println(testReg.getMatchResult());
		System.out.println(testReg.getThreshold());
		
		System.out.println(Approach.nGramDistance("micheel","michael", 2,0.6));
//		
//		
//		// ================ initialize the query input begin ==================
//
//		File query_file = new File(args[0]);
//
//		ArrayList<PlaceName> query_list = new ArrayList<PlaceName>(1000);
//
//		try
//		{
//			Scanner query_scan = new Scanner(query_file);
//			long timeStamp_readQueryList = System.currentTimeMillis();
//
//			while (query_scan.hasNextLine())
//			{
//				query_list.add(new PlaceName(query_scan.nextLine()));
//			}
//			System.out.println("Query Structure Setup Time: "
//					+ (System.currentTimeMillis() - timeStamp_readQueryList));
//
//			log.warn("Query Structure Setup Time: "
//					+ (System.currentTimeMillis() - timeStamp_readQueryList));
//
//		} catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//
//		// ================ initialize the query input end ==================
//
//		File tweet_file = new File(args[1]);
//
//		// build the thread pool
//		ExecutorService pool = Executors.newFixedThreadPool(8);
//
//		// set the tweet input source
//		TokenProcessStrategy.setTweetInputFile(tweet_file);
//
//		// do the memory map
//		FileChannel fc = null;
//		MappedByteBuffer byteBuffer = null;
//		try
//		{
//			fc = new FileInputStream(tweet_file).getChannel();
//			byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//			TokenProcessStrategy.setMappedByteBuffer(byteBuffer);
//
//		} catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//
//		// assign the task to the thread pool
//		while (!query_list.isEmpty())
//		{
//			TokenProcessStrategy task = new TokenProcessStrategy();
//
//			task.setPlaceName(query_list.remove(0));
//
//			pool.execute(task);
//		}
//
//		if (fc != null)
//			try
//			{
//				fc.close();
//			} catch (IOException e)
//			{
//
//				e.printStackTrace();
//			}
//		pool.shutdown();

	}

	// TODO дһ���Աȷ���ȷ��һ��map תnextLine��scanner io nextLine��Ч��
	// see the README.md for notes
	public static void testMap(String filePath)
	{
		Long startTime = System.currentTimeMillis();
		try
		{
			FileChannel fc = new FileInputStream(filePath).getChannel();
			MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY,
					0, fc.size());
			Charset charset = Charset.forName("US-ASCII");
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer charBuffer = decoder.decode(byteBuffer);
			Scanner sc = new Scanner(charBuffer).useDelimiter(System
					.getProperty("line.separator"));
			while (sc.hasNext())
			{
				// read str as dumb spin
				String tmpStr = sc.next();
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
		System.out.printf("map time used:" + estimatedTime);
	}

	public static void testScannerIOStream(String filePath)
	{
		Long startTime = System.currentTimeMillis();
		Scanner sc = null;
		try
		{
			sc = new Scanner(new File(filePath));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (sc.hasNext())
		{
			// read str as dumb spin
			String tmpStr = sc.next();
		}
		Long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.printf("\npure io time used:" + estimatedTime);
	}

}
