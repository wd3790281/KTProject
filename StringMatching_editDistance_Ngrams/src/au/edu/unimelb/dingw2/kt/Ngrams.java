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

public class Ngrams {

	public static void Ngram(String queryFile, String tweetsFile, int n, int threshold) {

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
				String[] splitQuery = query.get(i).split(" ");
				String[] splitTweets = tweets.get(j).split(" ");
				int splitQueryLength = splitQuery.length;
				int splitTweetsLength = splitTweets.length;

				for (int k = 0; k < splitTweetsLength - splitQueryLength + 1; k++) {
					String gram = "";
					int similarity = 0;

					for (int x = 0; x < splitQueryLength; x++) {
						gram = gram + splitTweets[k + x];
					}
					String inputQuery = query.get(i).replaceAll(" ", "");
					similarity = computeNgram(inputQuery, gram, n);

					if (similarity > threshold) {
						PrintWriter output = null;
						try {
							output= new PrintWriter(
									new FileOutputStream("ngramResult.txt",
											true));
						} catch (FileNotFoundException e) {

						}

						output.println(originTweets.get(j));
						output.println("Query is: " + query.get(i) + "    " + "the found match is: " + gram
                                + "    " + "similarity is: " + similarity);
						output.close();
						break;
					}
				}

			}
		}
		Date date2 = new Date();
		System.out.println(dateFormat.format(date2));

	}

	public static int computeNgram(String query, String tweets, int n) {
		int simi;
		String[] splitQuery = query.split("");
		int splitQueryLength = splitQuery.length;
		String[] splitTweets = tweets.split("");
		int splitTweetsLength = splitTweets.length;
		ArrayList<String> queryGram = new ArrayList<String>();
		ArrayList<String> tweetsGram = new ArrayList<String>();
		if (n >= splitQueryLength) {
			queryGram.add(query);
		} else {
			for (int k = 0; k < splitQueryLength - n + 1; k++) {
				String gram = "";
				for (int x = 0; x < n; x++) {
					gram = gram + splitQuery[k + x];
				}
				if (!queryGram.contains(gram)) {
					queryGram.add(gram);
				}
			}
		}
		if (tweets.length() <= n) {
			tweetsGram.add(tweets);
		} else {
			for (int k = 0; k < splitTweetsLength - n + 1; k++) {
				String gram = "";
				for (int x = 0; x < n; x++) {
					gram = gram + splitTweets[k + x];
				}
				if (!tweetsGram.contains(gram)) {
					tweetsGram.add(gram);
				}
			}
		}
		int count = 0;
		for (int a = 0; a < queryGram.size(); a++) {
			for (int b = 0; b < tweetsGram.size(); b++) {
				if (queryGram.get(a).equals(tweetsGram.get(b))) {
					count++;
				}
			}
		}
		simi = count * n * 100 / (queryGram.size() + tweetsGram.size());
		return simi;
	}

}
