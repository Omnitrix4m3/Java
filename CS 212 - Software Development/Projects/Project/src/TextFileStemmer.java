import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Utility class for parsing and stemming text and text files into collections
 * of stemmed words.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2020
 *
 * @see TextParser
 */
public class TextFileStemmer
{

	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Returns a list of cleaned and stemmed words parsed from the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return a list of cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static ArrayList<String> listStems(String line, Stemmer stemmer)
	{
		// DONE Fill in listStems(String, Stemmer)
		ArrayList<String> parsedStrings = new ArrayList<>();

		String[] tokenizedStrings = TextParser.parse(line);

		for (int i = 0; i < tokenizedStrings.length; i++)
		{
			parsedStrings.add(stemmer.stem(tokenizedStrings[i]).toString());
		}

		return parsedStrings;
	}

	/**
	 * Returns a list of cleaned and stemmed words parsed from the provided line.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @return a list of cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see #DEFAULT
	 * @see #listStems(String, Stemmer)
	 */
	public static ArrayList<String> listStems(String line)
	{
		// THIS IS PROVIDED FOR YOU; NO NEED TO MODIFY
		return listStems(line, new SnowballStemmer(DEFAULT));
	}

	/**
	 * Returns a set of unique (no duplicates) cleaned and stemmed words parsed from
	 * the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return a sorted set of unique cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static TreeSet<String> uniqueStems(String line, Stemmer stemmer)
	{
		// DONE Fill in uniqueStems(String, Stemmer)
		TreeSet<String> setOfProcessedStrings = new TreeSet<>();
		
		setOfProcessedStrings.addAll(listStems(line, stemmer));

		return setOfProcessedStrings;
	}

	/**
	 * Returns a set of unique (no duplicates) cleaned and stemmed words parsed from
	 * the provided line.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @return a sorted set of unique cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see #DEFAULT
	 * @see #uniqueStems(String, Stemmer)
	 */
	public static TreeSet<String> uniqueStems(String line)
	{
		// THIS IS PROVIDED FOR YOU; NO NEED TO MODIFY
		return uniqueStems(line, new SnowballStemmer(DEFAULT));
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then adds those words to a set.
	 *
	 * @param inputFile the input file to parse
	 * @return a sorted set of stems from file
	 * @throws IOException if unable to read or parse file
	 *
	 * @see #uniqueStems(String)
	 * @see TextParser#parse(String)
	 */
	public static TreeSet<String> uniqueStems(Path inputFile) throws IOException
	{
		// DONE Fill in uniqueStems(Path)

		BufferedReader br = new BufferedReader(new FileReader(inputFile.toString()));
		TreeSet<String> setOfProcessedStrings = new TreeSet<>();

		String line;

		while ((line = br.readLine()) != null)
		{
			setOfProcessedStrings.addAll(listStems(line, new SnowballStemmer(DEFAULT)));
		}

		br.close();

		return setOfProcessedStrings;
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then adds those words to a set.
	 *
	 * @param inputFile the input file to parse
	 * @return a sorted set of stems from file
	 * @throws IOException if unable to read or parse file
	 *
	 * @see #uniqueStems(String)
	 * @see TextParser#parse(String)
	 */
	public static ArrayList<String> listStems(Path inputFile) throws IOException
	{
		// DONE Fill in uniqueStems(Path)

		BufferedReader br = new BufferedReader(new FileReader(inputFile.toString()));
		Stemmer stemmer = new SnowballStemmer(DEFAULT);
		ArrayList<String> arrayOfProcessedStrings = new ArrayList<>();

		String line;

		while ((line = br.readLine()) != null)
		{
			arrayOfProcessedStrings.addAll(listStems(line, stemmer));
		}

		br.close();

		return arrayOfProcessedStrings;
	}

	/**
	 * Returns the stem of a single word passed as an argument.
	 *
	 * @param word    the word to stem
	 * @param stemmer the stemmer to use
	 * @return the stemmed form of the word
	 */
	public static String singleStemmer(String word)
	{
		return new SnowballStemmer(DEFAULT).stem(word).toString();
	}
}