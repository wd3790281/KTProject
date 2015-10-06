package au.edu.unimelb.dingw2.kt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class GlobalEditDistance {

	public static void GlobaleEditDistance(String queryFile, String tweetsFile,  int threshold) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Date date = new Date();
		System.out.println(dateFormat.format(date));
		Scanner inputStream = null;
		ArrayList<String> tweets = new ArrayList<String>();
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<String> originTweets = new ArrayList<String>();

		try {
			inputStream = new Scanner(new File(queryFile));
			while (inputStream.hasNextLine()) {
				query.add(inputStream.nextLine());
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		try {
			inputStream = new Scanner(new File(tweetsFile));
			while (inputStream.hasNextLine()) {
				String getString;
				getString = inputStream.nextLine();
				String[] array = getString.split("\t");
				tweets.add(array[1]);
				originTweets.add(getString);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < query.size(); i++) {
			for (int j = 0; j < tweets.size(); j++) {

				String[] splitTweetString = tweets.get(j).split(" ");
				String[] splitQueryString = query.get(i).split(" ");
				int splitQueryStringLength = splitQueryString.length;
				int range = splitTweetString.length - splitQueryStringLength;
				for (int k = 0; k < range + 1; k++) {
					String tweetString = "";
					double dissimilarity = 0.0;
					for (int x = 0; x < (splitQueryString.length - 1); x++) {
						tweetString = tweetString + splitTweetString[k + x]
								+ " ";
					}
					tweetString = tweetString
							+ splitTweetString[k + splitQueryStringLength - 1];
					dissimilarity = ComputeEditDistance(query.get(i),
							tweetString) * 100 / query.get(i).length();
					if (dissimilarity <= (100-threshold)) {

						PrintWriter processedString = null;
						try {
							processedString = new PrintWriter(
									new FileOutputStream("globalEditDistanceResult.txt", true));
						} catch (FileNotFoundException e) {

						}
						processedString.println(originTweets.get(j));
						processedString.println("The query is: " + query.get(i) + "     "
								+ "The found string is: " + tweetString + "     " + "dissimilarity is:" +
                                dissimilarity);
						processedString.close();
						continue;
					}

				}

			}
		}
		Date date2 = new Date();
		System.out.println(dateFormat.format(date2));

	}

	public static int ComputeEditDistance(CharSequence lhs, CharSequence rhs) {
		int len0 = lhs.length() + 1;
		int len1 = rhs.length() + 1;

		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];

		// initial cost of skipping prefix in String s0
		for (int i = 0; i < len0; i++)
			cost[i] = i;

		// dynamically computing the array of distances

		// transformation cost for each letter in s1
		for (int j = 1; j < len1; j++) {
			// initial cost of skipping prefix in String s1
			newcost[0] = j;

			// transformation cost for each letter in s0
			for (int i = 1; i < len0; i++) {
				// matching current letters in both strings
				int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

				// computing cost for each transformation
				int cost_replace = cost[i - 1] + match;
				int cost_insert = cost[i] + 1;
				int cost_delete = newcost[i - 1] + 1;

				// keep minimum cost
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
						cost_replace);
			}

			// swap cost/newcost arrays
			int[] swap = cost;
			cost = newcost;
			newcost = swap;
		}

		// the distance is the cost for transforming all letters in both strings
		return cost[len0 - 1];
	}
}
