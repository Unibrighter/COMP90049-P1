package rocklee.methods;

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

	/*
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

	/*
	 * Use the local edit distance strategy to compute the match rate
	 * 
	 * Note this is to find whether the query exists in the given aim Text
	 * 
	 * Instead of compare the texts to calculate the result
	 */
	public static int[][] localEditDistance(String text1, String text2)
	{
		double result = text1.length();

		int len1 = text1.length();
		int len2 = text2.length();

		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] local_distance_matrix = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++)
		{
			local_distance_matrix[i][0] = 0;
		}

		for (int j = 0; j <= len2; j++)
		{
			local_distance_matrix[0][j] = 0;
		}

		// iterate though, and check last char
		for (int i = 0; i < len1; i++)
		{
			char c1 = text1.charAt(i);
			for (int j = 0; j < len2; j++)
			{
				char c2 = text2.charAt(j);

				// if last two chars equal

					// update dp value for +1 length
					int replace=local_distance_matrix[i][j]+((c1 == c2)?1:-1);
					int insert = local_distance_matrix[i][j + 1] - 1;
					int delete = local_distance_matrix[i + 1][j] - 1;

					int max = replace < insert ? insert : replace;
					max = delete < max ? max : delete;
					
					local_distance_matrix[i + 1][j + 1] = max>0?max:0;
				
			}
		}
		
		// this means it will take global_distance_matrix[i + 1][j + 1] times
		// operation
		// to transform from word 1 to word 2

		// we use the first word's length as measurement in case that the word
		// is too short to give a precise judgement
//		result = (len1 - local_distance_matrix[len1][len2]) / result;
//		return result;
		
		return local_distance_matrix;
	}
	
	public static void printLocalDistanceMatrix(String str1,String str2)
	{
		System.out.println(" "+str2);
		int [][] matrix=localEditDistance(str1, str2);
		
		for (int i = 0; i < str1.length(); i++)
		{
			System.out.print(str1.charAt(i));
			for (int j = 0; j < str2.length(); j++)
			{
				System.out.print(matrix[i+1][j+1]);
			}
			System.out.println();
			
		}
	}
	

}
