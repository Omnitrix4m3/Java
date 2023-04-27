
import java.io.*;
import java.nio.file.*;

/**
 * Use of an interface for QueryBuilding allows for queries to be built when the program is run with multiple threads or one
 * 
 * @author Omar Hussain
 * @version v3.0.0
 */
public interface QueryBuilderInterface
{
    /**
	 * Stores query search results in the InvertedIndex.
	 * 
	 * @param query             the query string
	 * @param isPartialSearch   whether or not to perform partial search
	 */
    public void searchQuery(String query, boolean isPartialSearch);
    
    /**
	 * Parses query files using exact and partial search methods.
	 * 
	 * @param path                      the filepath leading to a file with query values
	 * @param isPartialSearch           the type of search to be performed (partial or not)
	 * @throws IOException              if an IO error occurs
	 * @throws FileNotFoundException    if a FileNotFound error occurs
     * @throws InterruptedException     if an Interrupted error occurs
	 */
    public default void build(Path path, boolean isPartialSearch) throws FileNotFoundException, IOException, InterruptedException
    {
        try (BufferedReader reader = Files.newBufferedReader(path))
		{
			String line;
			
			while ((line = reader.readLine()) != null)
			{
				if (!line.isBlank())
				{
					searchQuery(line, isPartialSearch);
				}
			}
		}
    }
    
    /**
	 * Writes the output of a search to a file.
	 * 
	 * @param path  the path where the output file will be saved
	 * @throws IOException if an IO error occurs
	 */
    public void queryWriter(Path path) throws IOException;
}