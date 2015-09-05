package rocklee.methods;

import java.util.ArrayList;

import rocklee.units.PlaceName;
import rocklee.units.Tweet;

/***
 * This Class uses some Algorithm to do the comparisons between the String and
 * then outputs some results according to the strategy we use.
 * 
 * 
 * @version 2015-08-29 11:54
 * @author Kunliang WU
 *
 */

public class Approach
{

	/**
	 * Use the global edit distance strategy to compute the match rate
	 * 
	 * And use word 1's length as the measurement for difference degree
	 */

	public static double globalEditDistance(String word1, String word2)
	{
		double result = word1.length();

		int len1 = word1.length();
		int len2 = word2.length();

		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] global_distance_matrix = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++)
		{
			global_distance_matrix[i][0] = i;
		}

		for (int j = 0; j <= len2; j++)
		{
			global_distance_matrix[0][j] = j;
		}

		// iterate though, and check last char
		for (int i = 0; i < len1; i++)
		{
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++)
			{
				char c2 = word2.charAt(j);

				// if last two chars equal
				if (c1 == c2)
				{
					// update dp value for +1 length
					global_distance_matrix[i + 1][j + 1] = global_distance_matrix[i][j];
				} else
				{
					int replace = global_distance_matrix[i][j] + 1;
					int insert = global_distance_matrix[i][j + 1] + 1;
					int delete = global_distance_matrix[i + 1][j] + 1;

					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					global_distance_matrix[i + 1][j + 1] = min;
				}
			}
		}

		// this means it will take global_distance_matrix[i + 1][j + 1] times
		// operation
		// to transform from word 1 to word 2

		// we use the first word's length as measurement in case that the word
		// is too short to give a precise judgement
		result = (len1 - global_distance_matrix[len1][len2]) / result;
		return result;
	}

	/**
	 * Use the local edit distance strategy to compute the match rate
	 * 
	 * Note this is to find whether the query exists in the given aim Text
	 * 
	 * Instead of compare the texts to calculate the result
	 */
	public static double localEditDistance(PlaceName q, Tweet t,
			double threshold, StringBuffer strBuffer)
	{
		String text1 = q.getFullName();
		String text2 = t.getFullContent();

		double result = text1.length();

		int len1 = text1.length();
		int len2 = text2.length();

		int[][] local_distance_matrix = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++)
		{
			local_distance_matrix[i][0] = 0;
		}

		for (int j = 0; j <= len2; j++)
		{
			local_distance_matrix[0][j] = 0;
		}

		// we need three temp var to store the index i,j and max value
		int max_record = -1;
		int index_i = -1;
		int index_j = -1;

		for (int i = 0; i < len1; i++)
		{
			char c1 = text1.charAt(i);
			for (int j = 0; j < len2; j++)
			{
				char c2 = text2.charAt(j);

				int replace = local_distance_matrix[i][j]
						+ ((c1 == c2) ? 1 : -1);
				int insert = local_distance_matrix[i][j + 1] - 1;
				int delete = local_distance_matrix[i + 1][j] - 1;

				int max = replace < insert ? insert : replace;
				max = delete < max ? max : delete;

				// record the best matching index and max value in the temp
				// variables

				if (max > 0)
				{
					local_distance_matrix[i + 1][j + 1] = max;

					// we need to utilize the maximum value of the local
					// distance matrix
					if (max >= max_record)
					{
						index_i = i;
						index_j = j;
						max_record = max;
					}

				} else
					local_distance_matrix[i + 1][j + 1] = 0;
			}
		}

		// return local_distance_matrix;

		// calculate the final score
		result = max_record / result;

		if (result >= threshold)
			strBuffer.append(t.getBestMatchPartOfContent(index_j, len1));// this
																			// part
																			// is
																			// going
																			// to
																			// be
																			// print
																			// a

		return result;

	}

	/**
	 * Use n gram to compute the match rate
	 * 
	 * this would be a little bit slow due the the process and time spent on
	 * string split and store
	 * 
	 * The smaller the n-gram distance, the closer the match.
	 */
	public static double nGramDistance(String s0, String s1, int n)
	{

		// some special rare cases
		final int sl = s0.length();
		final int tl = s1.length();

		if (sl == 0 || tl == 0)
		{
			if (sl == tl)
			{
				return 1;
			} else
			{
				return 0;
			}
		}

		int cost = 0;
		if (sl < n || tl < n)
		{
			for (int i = 0, ni = Math.min(sl, tl); i < ni; i++)
			{
				if (s0.charAt(i) == s1.charAt(i))
				{
					cost++;
				}
			}
			return (double) cost / Math.max(sl, tl);
		}

		// then comes to the common case
		ArrayList<String> collection_1 = new ArrayList<String>();
		ArrayList<String> collection_2 = new ArrayList<String>();

		for (int i = 0; i < sl - n; i++)
		{

			// sub string of s1
			String tmpSub = s0.substring(i, i + n);

			if (collection_1.contains(tmpSub))
				continue;

			collection_1.add(tmpSub);
		}

		for (int i = 0; i < tl - n; i++)
		{
			// sub string of s2
			String tmpSub = s1.substring(i, i + n);

			if (collection_2.contains(tmpSub))
				continue;

			if (collection_1.contains(tmpSub))
				cost++;
		}

		// |A|+|B|-2|A¡ÉB|
		double result = collection_1.size() + collection_2.size();

		result = (result - cost * 2) / result;

		return result;

	}

	/**
	 * 
	 * The Soundex sound-alike matching technique is well-known, widely used,
	 * simple to implement. Transforms a string into a 4-character code that
	 * represents its sound.
	 * 
	 * Then the problem is transformed into string comparison
	 * 
	 * */
	public static double soundexDistance(String word1, String word2)
	{
		String soundex1=digitEncodeWord(word1);
		String soundex2=digitEncodeWord(word2);
		
		return soundex1.equalsIgnoreCase(soundex2)?1:0;
		
		
	}

	private static String digitEncodeWord(String word)
	{
		char[] str_raw = word.toLowerCase().toCharArray();

		for (int i = 1; i < str_raw.length; i++)
		{
			switch (str_raw[i])
			{
			case 'a':
			case 'e':
			case 'h':
			case 'i':
			case 'o':
			case 'u':
			case 'w':
			case 'y':
				str_raw[i] = '0';
				break;

			case 'b':
			case 'f':
			case 'p':
			case 'v':
				str_raw[i] = '1';
				break;

			case 'c':
			case 'g':
			case 'j':
			case 'k':
			case 'q':
			case 's':
			case 'x':
			case 'z':
				str_raw[i] = '2';
				break;

			case 'd':
			case 't':
				str_raw[i] = '3';
				break;

			case 'l':
				str_raw[i] = '4';
				break;
			case 'm':
			case 'n':
				str_raw[i] = '5';
				break;
			case 'r':
				str_raw[i] = '6';
				break;

			default:
				break;
			}
		}
		
		//remove doubles and duplicates
		char tmpChar=str_raw[0];
		StringBuffer strBuf=new StringBuffer("");
		strBuf.append(tmpChar);
		for (int j = 1; j < str_raw.length; j++)
		{
			if(str_raw[j]==tmpChar)//same as the last char
				continue;
			
			else
			{
				tmpChar=str_raw[j];
				strBuf.append(str_raw[j]);
			}
		}
		
		return strBuf.substring(0, 4<=strBuf.length()?4:strBuf.length());
	}

}
