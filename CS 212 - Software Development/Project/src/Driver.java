import java.nio.file.*;
import java.time.*;
import java.io.*;
import java.net.*;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author Omar Hussain
 * @version v4.0.0
 */
public class Driver
{

	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 */
	public static void main(String[] args)
	{
		// store initial start time
		Instant start = Instant.now();

		// DONE Fill in and modify this method as necessary.
		int threadCount;
		int totalURLCount;
		
		InvertedIndex index = new InvertedIndex();
		ThreadSafeInvertedIndex threadedIndex = new ThreadSafeInvertedIndex();

		InvertedIndexBuilder indexBuilder = new InvertedIndexBuilder(index);
		ThreadSafeInvertedIndexBuilder threadedIndexBuilder;

		QueryBuilder queryBuilder = new QueryBuilder(index);
		ThreadSafeQueryBuilder threadedQueryBuilder = null;

		WebCrawler threadedWebCrawler = null;
		
		WorkQueue queue = null;

		ArgumentParser parser = new ArgumentParser();
		parser.parse(args);
		
		try
		{
			totalURLCount = Integer.parseInt(parser.getString("-limit", "50"));
		}

		catch (NumberFormatException e)
		{
			return;
		}

		if (parser.hasFlag("-url"))
		{
			if (!parser.hasFlag("-threads"))
			{
				String[] arg = {"-threads"};
				
				parser.parse(arg);
			}
		}

		try
		{
			threadCount = Integer.parseInt(parser.getString("-threads", "5"));

		}
		
		catch (NumberFormatException e)
		{
			return;
		}

		if (threadCount < 1)
		{
			return;
		}

		else
		{
			queue = new WorkQueue(threadCount);
			
			if (parser.hasFlag("-url"))
			{
				threadedWebCrawler = new WebCrawler(threadedIndex, queue, totalURLCount);
			}
		}

		if (parser.hasFlag("-url"))
		{
			try
			{
				try
				{
					threadedWebCrawler.crawl(parser.getString("-url"));
				}

				catch (InterruptedException e)
				{
					System.out.println("Invalid URL! Unable to use String URL with WebCrawler");
				}
			}

			catch (MalformedURLException e)
			{
				System.out.println("Malformed URL Exception!");
			}
		}

		if (parser.hasFlag("-path"))
		{
			Path path = parser.getPath("-path");

			if (parser.hasFlag("-threads"))
			{
				threadedIndexBuilder = new ThreadSafeInvertedIndexBuilder(threadedIndex, queue);

				try
				{
					threadedIndexBuilder.directoryTraverseAndParse(path);
				}

				catch (Exception e)
				{
					System.out.println("Invalid path! Unable to add '" + path + "' to InvertedIndex!");
				}
			}

			else
			{
				try
				{
					indexBuilder.directoryTraverseAndParse(path);
				}
				
				catch (Exception e)
				{
					System.out.println("Invalid path! Unable to add '" + path + "' to InvertedIndex!");
				}
			}
		}

		if (parser.hasFlag("-index"))
		{
			Path indexPath = parser.getPath("-index", Path.of("index.json"));
			
			if (parser.hasFlag("-threads"))
			{
				try
				{
					threadedIndex.invertedIndexWriter(indexPath);
				}
				
				catch (Exception e)
				{
					System.out.println("Invalid output file! Cannot send '" + indexPath + "' to InvertedIndexWriter!");
				}
			}
			
			else
			{
				try
				{
					index.invertedIndexWriter(indexPath);
				}
				
				catch (Exception e)
				{
					System.out.println("Invalid output file! Cannot send '" + indexPath + "' to InvertedIndexWriter!");
				}
			}
		}

		if (parser.hasFlag("-counts"))
		{
			Path countsPath = parser.getPath("-counts", Path.of("counts.json"));

			if (parser.hasFlag("-threads"))
			{
				try
				{
					threadedIndex.dataCountWriter(countsPath);
				}

				catch (Exception e)
				{
					System.out.println("Unable to write data count! Invalid output path at '" + countsPath + "'!");
				}
			}

			else
			{
				try
				{
					index.dataCountWriter(countsPath);
				}
				
				catch (Exception e)
				{
					System.out.println("Unable to write data count! Invalid output path at '" + countsPath + "'!");
				}
			}
		}

		if (parser.hasFlag("-query"))
		{
			Path queryPath = parser.getPath("-query");
			
			if (queryPath != null)
			{
				boolean isPartialSearch = true;

				if (parser.hasFlag("-exact"))
				{
					isPartialSearch = false;
				}

				if (parser.hasFlag("-threads"))
				{
					threadedQueryBuilder = new ThreadSafeQueryBuilder(threadedIndex, queue);

					try
					{
						threadedQueryBuilder.build(queryPath, isPartialSearch);
					}

					catch (Exception e)
					{
						System.out.println("Invalid query file!: " + queryPath);
					}
				}
				
				else
				{
					try
					{
						queryBuilder.build(queryPath, isPartialSearch);
					}
					
					catch (Exception e)
					{
						System.out.println("Invalid query file!: " + queryPath);
					}
				}
			}
		}

		if (parser.hasFlag("-results"))
		{
			Path resultsPath = parser.getPath("-results", Path.of("results.json"));

			if (parser.hasFlag("-threads"))
			{
				try
				{
					threadedQueryBuilder.queryWriter(resultsPath);
				}

				catch (IOException e)
				{
					System.out.println("Unable to write results! Invalid output path at '" + resultsPath + "!");
				}
			}

			else
			{
				try
				{
					queryBuilder.queryWriter(resultsPath);
				}
			
				catch (IOException e)
				{
					System.out.println("Unable to write results! Invalid output path at '" + resultsPath + "!");
				}
			}
		}

		if (queue != null)
		{
			queue.shutdown();
		}

		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}