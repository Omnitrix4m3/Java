import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Implements a Thread-Safe version of the InvertedIndex class
 * 
 * @author Omar Hussain
 * @version v3.0.0
 */
public class ThreadSafeInvertedIndex extends InvertedIndex
{
    /**
     * Initializes a SimpleReadWriteLock object to be used by the class
     */
    private final SimpleReadWriteLock lock;

    /**
     * Zero-arg Constructor which implements the lock variable for the class
     */
    public ThreadSafeInvertedIndex()
    {
        super();
        lock = new SimpleReadWriteLock();
    }

    /**
	 * Returns an unmodifiable set of the words in the InvertedIndex

	 * @return  a set with the words (keys) of the InvertedIndex
	 */
    @Override
    public Set<String> getWords()
    {
        lock.readLock().lock();

        try
        {
			return super.getWords();
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
     * Returns an unmodifiable set of the locations of a word in the InvertedIndex
     * 
	 * @param word  the word that the locations pertain to
	 * @return      a set with the locations associated with the word
	 */
    @Override
    public Set<String> getLocations(String word)
    {
        lock.readLock().lock();

        try
        {
			return super.getLocations(word);
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
	 * Returns an unmodifiable set of the positions of a word at a location in the InvertedIndex
	 * 
	 * @param word      the word that the locations pertain to
	 * @param location  the location the set pertains to
	 * @return          a set with the positions associated with the word at a locations
	 */
    @Override
    public Set<Integer> getPositions(String word, String location)
    {
        lock.readLock().lock();

        try
        {
			return super.getPositions(word, location);
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
	 * Counts a specific location for a text file
	 * 
	 * @param location  the location to be counted at
	 * @return          the count at the specified location
	 */
    @Override
    public Integer getLocationCount(String location)
    {
        lock.readLock().lock();
        
        try
        {
			return super.getLocationCount(location);
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
	 * Adds a word to the InvertedIndex along with its accompanying attributes
	 *
	 * @param word     the word to add
	 * @param path     the filepath to be added from
	 * @param location the location in the file where the word was found
	 */
    @Override
    public void add(String word, String path, int location)
    {
        lock.writeLock().lock();

        try
        {
            super.add(word, path, location);
        }

        finally
        {
            lock.writeLock().unlock();
        }
    }

    /**
	 * Adds all words to the InvertedIndex from another
	 *
	 * @param originIndex     the index to copy from
	 */
    @Override
    public void addAll(InvertedIndex originIndex)
    {
        lock.writeLock().lock();

        try
        {
			super.addAll(originIndex);
        }
        
        finally
        {
            lock.writeLock().unlock();
        }
    }

    /**
	 * Checks to see if the InvertedIndex contains a word or not
	 *
	 * @param word     the word to check
	 * @return         whether the word is contained or not
	 */
    @Override
    public boolean contains(String word)
    {
        lock.readLock().lock();

        try
        {
			return super.contains(word);
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
	 * Checks to see if the InvertedIndex contains a word at a specified location or not
	 *
	 * @param word     the word to check
	 * @param location the location to be checked at
	 * @return         whether the word is contained or not
	 */
    @Override
    public boolean contains(String word, String location)
    {
        lock.readLock().lock();

        try
        {
			return super.contains(word, location);
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
	 * Checks to see if the InvertedIndex contains a word at a specified location and position or not
	 * 
	 * @param word     the word to check
	 * @param location the location to be checked at
     * @param position the position to be checked at
	 * @return         whether the word is contained or not
     * @see InvertedIndex#contains(String)
     * @see InvertedIndex#contains(String, String)
	 */
    @Override
    public boolean contains(String word, String location, int position)
    {
        lock.readLock().lock();

        try
        {
			return super.contains(word, location, position);
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
	 * Outputs the contents of the InvertedIndex in the JSON format.
	 *
	 * @param path  the filepath of the file to be outputted
	 * @throws IOException if an IO error occurs
	 */
    @Override
    public void invertedIndexWriter(Path path) throws IOException
    {
        lock.readLock().lock();
        
        try
        {
			super.invertedIndexWriter(path);
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
	 * Creates a writer for the -counts flag and outputs to the file passed
	 * 
	 * @param path  the filepath of the file to be outputted
	 * @throws IOException if an IO error occurs
	 */
    public void dataCountWriter(Path path) throws IOException
    {
        lock.readLock().lock();

        try
        {
			super.dataCountWriter(path);
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
	 * Searches for any word stem in the InvertedIndex that starts with a query word
	 * 
	 * @param setOfQueries  a set of the queries to be searched for
     * @return              a list containing the contents of the set with any word stem that starts with a query word
	 */
    @Override
    public ArrayList<Result> partialSearch(Set<String> setOfQueries)
    {
        lock.readLock().lock();

        try
        {
			return super.partialSearch(setOfQueries);
        }
        
        finally
        {
            lock.readLock().unlock();
        }
    }

    /**
	 * Generates search results depending on the queries and the search configuration
	 * 
	 * @param setOfQueries      a set of the queries to be searched for
     * @return                  a list of search results
	 */
    public ArrayList<Result> exactSearch(Set<String> setOfQueries)
    {
        lock.readLock().lock();
        
        try
        {
			return super.exactSearch(setOfQueries);
        }
        
        finally
        {
			lock.readLock().unlock();
		}
    }

    /**
     * @return  a visible output of the InvertedIndex
     */
    @Override
    public String toString()
    {
		lock.readLock().lock();
        
        try
        {
			return super.toString();
        }
        
        finally
        {
			lock.readLock().unlock();
		}
	}
}