import java.net.*;
import java.util.*;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Class responsible for parsing through URLs and adding its resultant data to an InvertedIndex model
 *
 * @author Omar Hussain
 * @version v4.0.0
 */
public class WebCrawler
{
    /**
     * Nested class allows for the creation of Runnable objects to support multithreading
     */
    private class Task implements Runnable
    {
        /**
         * The origin url to be used by the WebCrawler
         */
        private URL url;

        /**
         * Single-arg constructor which implements url variables
         * @param link
         */
        public Task(URL link)
        {
            this.url = link;
        }

        /**
         * Overriden run() method allows for custom tasks to be carried out
         */
        @Override
        public void run()
        {
            Stemmer stemmer = new SnowballStemmer(DEFAULT);

            int count = 1;

            String html = HtmlFetcher.fetch(url, 3);

            if (html == null)
            {
				return;
            }
            
            String cleanedHTML = HtmlCleaner.stripHtml(html);
            
            synchronized (index)
            {
                for (String word : TextParser.parse(cleanedHTML))
                {
                    index.add(stemmer.stem(word).toString(), url.toString(), count++);
				}
            }
            
            try
            {
				crawl(url);
            }
            
            catch (MalformedURLException e)
            {
				return;
			}
        }
    }

    /**
     * The default stemmer algorithm used by this class.
     */
    public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

    /**
	 * Initializes a variable to store information from text files that can be globally accessed and modified.
	 */
    private final ThreadSafeInvertedIndex index;

    /**
     * An ArrayList that stores processed URL objects
     */
    private final ArrayList<URL> listOfURLs;
    
    /**
     * Initializes a WorkQueue object to be used by the class
     */
    private final WorkQueue queue;
    
    /**
     * The number of URLs to be processed
     */
    private final int totalURLCount;

    /**
     * Multi-arg constructor that implements index and queue variables as well as an ArrayList of URLs for the class
     * 
     * @param index
     * @param queue
     * @param totalURLCount
     */
    public WebCrawler(ThreadSafeInvertedIndex index, WorkQueue queue, int totalURLCount)
    {
        this.index = index;
        this.queue = queue;
        this.totalURLCount = totalURLCount;
        listOfURLs = new ArrayList<>();
    }

    /**
     * Creates a new Task object that stores data in the InvertedIndex
     * 
     * @param link  the URL to be processed
     */
    public void storeHTML(URL link)
    {
		queue.execute(new Task(link));
    }
    
    /**
     * Adds all HTML content at the URL link to the InvertedIndex
     * 
     * @param linkAsString  URL of the website as a String
     * @param html          the HTML content as a String
     * @param index         the InvertedIndex to be used
     */
    public static void addHTML(String linkAsString, String html, ThreadSafeInvertedIndex index)
    {
        Stemmer stemmer = new SnowballStemmer(DEFAULT);

        int count = 1;
        
        for (String word : TextParser.parse(html))
        {
			index.add(stemmer.stem(word).toString(), linkAsString, count++);
		}
    }

    /**
     * Stores the HTML information in the links found within a URL
     * 
     * @param link                      the URL to be processed
     * @throws MalformedURLException    if an MalformedURLException error occurs
     */
    public void crawl(URL link) throws MalformedURLException
    {
        String html = HtmlFetcher.fetch(link, 3);

        if (html == null)
        {
			return;
        }
        
        for (URL tempURL : LinkParser.listLinks(link, html))
        {
            boolean containsURL = false;

            URL tempCleanedURL = LinkParser.clean(tempURL);
            
            synchronized (listOfURLs)
            {
                if (listOfURLs.contains(tempCleanedURL))
                {
					containsURL = true;
                }
            }

            if (!containsURL && listOfURLs.size() < totalURLCount)
            {
                storeHTML(tempCleanedURL);
                
                listOfURLs.add(tempCleanedURL);
            }
        }
    }

    /**
     * Helper method for 'crawl' which takes in a String
     * 
     * @param linkAsString              the String to be processed
     * @throws MalformedURLException    if an MalformedURLException error occurs
     * @throws InterruptedException     if an InterruptedException error occurs
     * @see WebCrawler#crawl(String)
     */
    public void crawl(String linkAsString) throws MalformedURLException, InterruptedException
    {
        URL cleaned = LinkParser.clean(new URL(linkAsString));
        
		storeHTML(cleaned);
		listOfURLs.add(cleaned);
        queue.finish();
    }
}