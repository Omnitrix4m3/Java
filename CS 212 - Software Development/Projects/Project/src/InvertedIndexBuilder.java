import java.io.*;
import java.nio.file.*;
import java.util.*;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;


/**
 * Class containing auxiliary methods to configure an InvertedIndex with data
 *
 * @author Omar Hussain
 * @version v3.0.0
 */
public class InvertedIndexBuilder
{
    /**
     * Stemmer used to generate stems of words for the InvertedIndex
     */
    public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

    /**
     * Initializes an InvertedIndex object to be used by the class methods
     */
    private InvertedIndex index;

    /**
     * Single-arg constructor which implements the InvertedIndex object to be used by the class
     * @param index
     */
    public InvertedIndexBuilder(InvertedIndex index)
    {
        this.index = index;
    }

    /**
     * Method that indicates if a file is a text file or not
     * 
     * @param path  filepath to use
     * @return      if a file ends in 'txt' or 'text'
     */
    public static boolean isTextFile(Path path)
    {
        return path.toString().toLowerCase().endsWith(".txt") || path.toString().toLowerCase().endsWith(".text");
    }

    /**
	 * Parses through a file at the specified filepath and adds desired elements to the InvertedIndex.
	 *
	 * @param path     the filepath to search at
     * @param index    the InvertedIndex to store data in
     * @throws FileNotFoundException if a FileNotFound error occurs
	 * @throws IOException if an IO error occurs
	 */
    public static void fileParse(Path path, InvertedIndex index) throws FileNotFoundException, IOException
    {
        try (BufferedReader br = Files.newBufferedReader(path))
        {
            Stemmer stemmer = new SnowballStemmer(DEFAULT);
            
            int count = 1;
            String line;
            String pathName = path.toString();
            
            while ((line = br.readLine()) != null)
            {
                String[] wordsInLine = TextParser.parse(line);

                for (String word : wordsInLine)
                {
                    index.add(stemmer.stem(word).toString(), pathName, count);
                    count++;
                }
            }
        }
    }

    /**
     * fileParse helper method which takes in only a filepath
     * 
     * @param path  the filepath to search at
     * @throws FileNotFoundException
     * @throws IOException
     * @see #fileParse(Path, InvertedIndex)
     */
    public void fileParse(Path path) throws FileNotFoundException, IOException
    {
        fileParse(path, this.index);
    }

    /**
	 * Recursively searches a filepath for files to fileParse and add to the InvertedIndex
	 *
	 * @param path     the origin filepath to search at
	 * @throws IOException if an IO error occurs
     * @throws FileNotFoundException if a FileNotFound error occurs
	 */
    public void directoryTraverseAndParse(Path path) throws IOException, FileNotFoundException
    {
        if (path != null)
        {
            List<Path> listing = TextFileFinder.list(path);

            for (Path currentPath : listing)
            {
                if (Files.isRegularFile(currentPath))
                {
                    fileParse(currentPath);
                }
            }
		}
    }
}