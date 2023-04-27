import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Organizes search queries to be processed
 * 
 * @author Omar Hussain
 * @version v3.0.0
 */
public class QueryBuilder implements QueryBuilderInterface
{
    /**
	 * Initializes a variable to store information from text files that can be globally accessed and modified.
	 */
	private final InvertedIndex index;
    
    /**
     * Initializes a nested data structure used to hold query values that can be globally accessed and modified.
     */
    private final TreeMap<String, ArrayList<InvertedIndex.Result>> cachedResults;

    /**
     * Multi-arg constructor that defines index and implements a TreeMap of results
     * @param index
     */
    public QueryBuilder(InvertedIndex index)
    {
        this.index = index;
        cachedResults = new TreeMap<>();
    }
    
    /**
	 * Stores query search results in the InvertedIndex.
	 * 
	 * @param query             the query string
	 * @param isPartialSearch   whether or not to perform partial search
	 */
    @Override
    public void searchQuery(String query, boolean isPartialSearch)
    {
        TreeSet<String> setOfStems = TextFileStemmer.uniqueStems(query);

        String mergedStems = String.join(" ", setOfStems);

        if (cachedResults.containsKey(mergedStems) || setOfStems.isEmpty())
        {
            return;
        }

        cachedResults.put(mergedStems, index.searchResultsGenerator(setOfStems, isPartialSearch));
    }

	/**
	 * Writes the output of a search to a file.
	 * 
	 * @param path  the path where the output file will be saved
	 * @throws IOException if an IO error occurs
	 */
    @Override
    public void queryWriter(Path path) throws IOException
    {
        SimpleJsonWriter.asQueryOutput(cachedResults, path);
	}
}