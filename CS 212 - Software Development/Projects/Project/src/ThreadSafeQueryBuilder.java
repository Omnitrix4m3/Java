import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Implements a Thread-Safe version of the QueryBuilderInterface interface
 * 
 * @author Omar Hussain
 * @version v3.0.0
 */
public class ThreadSafeQueryBuilder implements QueryBuilderInterface
{
    /**
     * Nested class allows for the creation of Runnable objects to support multithreading
     */
    public class Task implements Runnable
    {
        /**
         * The queried text the task is running
         */
        private String query;

        /**
         * The type of search to be performed (partial or not)
         */
        private boolean isPartialSearch;

        /**
         * Multi-arg constructor which implements both query and search type variables for the class
         * 
         * @param query             the queried text the task is running
         * @param isPartialSearch   the type of search to be performed (partial or not)
         */
        public Task(String query, boolean isPartialSearch)
        {
            this.query = query;
            this.isPartialSearch = isPartialSearch;
        }

        /**
         * Overriden run() method allows for custom tasks to be carried out
         */
        @Override
        public void run()
        {
            TreeSet<String> setOfStems = TextFileStemmer.uniqueStems(query);
            
            String mergedStems = String.join(" ", setOfStems);
            
            boolean contains;

            synchronized (cachedResults)
            {
                contains = cachedResults.containsKey(mergedStems);
            }

            if (contains || setOfStems.isEmpty())
            {
                return;
            }

            ArrayList<InvertedIndex.Result> searchResults = index.searchResultsGenerator(setOfStems, isPartialSearch);

            synchronized (cachedResults)
            {
                cachedResults.put(mergedStems, searchResults);
            }
        }
    }
    
    /**
	 * Initializes a variable to store information from text files that can be globally accessed and modified.
	 */
	private final ThreadSafeInvertedIndex index;
    
    /**
     * Initializes a nested data structure used to hold query values that can be globally accessed and modified.
     */
    private final TreeMap<String, ArrayList<InvertedIndex.Result>> cachedResults;

    /**
     * Initializes a WorkQueue object to be used by the class
     */
    private final WorkQueue queue;

    /**
     * Multi-arg constructor that implements index and queue variables as well as TreeMap of results for the class
     * @param index     the ThreadSafeInvertedIndex to be used by the class
     * @param queue     the WorkQueue to be used by the class
     */
    public ThreadSafeQueryBuilder(ThreadSafeInvertedIndex index, WorkQueue queue)
    {
        this.index = index;
        this.queue = queue;
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
        queue.execute(new Task(query, isPartialSearch));
    }

    /**
	 * Parses query files using exact and partial search methods.
	 * 
	 * @param path                      the filepath leading to a file with query values
	 * @param isPartialSearch           the type of search to be performed (partial or not)
	 * @throws IOException              if an IO error occurs
	 * @throws FileNotFoundException    if a FileNotFound error occurs
     * @throws InterruptedException     if an Interrupted error occurs
	 */
    @Override
    public void build(Path path, boolean isPartialSearch) throws FileNotFoundException, IOException, InterruptedException
    {
        QueryBuilderInterface.super.build(path, isPartialSearch);
		queue.finish();
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
        synchronized (cachedResults)
        {
            SimpleJsonWriter.asQueryOutput(cachedResults, path);
        }
	}
}