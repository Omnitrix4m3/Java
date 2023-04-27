import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * InvertedIndex data structure used to store a word stem, its filepath, and its location
 *
 * @author Omar Hussain
 * @version v3.0.0
 */

public class InvertedIndex
{
    /**
     * Nested class allows for the creation of Result objects to evaluate matches
     */
    public class Result implements Comparable<Result>
    {
        /**
         * Initializes a variable to keep track of the location of the text file that can be globally accessed and modified.
         */
        private String location;
        
        /**
         * Initializes a variable to track the frequency of queried words appearing in a text file that can be globally accessed and modified.
        */
        private int count;

        /**
		 * Initializes a variable that represents the value of importance for a text file in the context of a search that can be globally accessed and modified.
		 */
        private double priority;
        
        /**
		 * Multi-arg constructor that implements and defines location, count, and priority variables
         * 
         * @param location  location of the text file to be used
		 * @param word      the query word to be used
		 */        
        public Result(String location, String word)
        {
            this.location = location;
            this.update(word);
        }

        /**
         * Returns the directory location of a text file
         * 
         * @return  location
         */
        public String getLocation()
        {
            return this.location;
        }

        /**
         * Returns the number of times a query term appears in a text file
         * 
         * @return  count
         */
        public String getCount()
        {
            return Integer.toString(count);
        }

        /**
		 * Returns the priority of a text file for a search
		 * 
		 * @return  priority
		 */
        public double getPriority()
        {
			return this.priority;
		}
        
        /**
		 * Mutator method that assigns new values for count and priority variables
		 * 
		 * @param word  word to be entered that redefines values for the score at that location
		 */
        private void update(String word)
        {
			this.count += invertedIndexTreeMap.get(word).get(this.location).size();
			this.priority = (double) this.count / dataCountTreeMap.get(location);
		}

        /**
         * Overrides compareTo method so that Result objects can be ranked
         * 
         * @param result    the result object to be compared
         * @return          the priority of that object
         */
        @Override
        public int compareTo(Result result)
        {
            int priorityChecker = Double.compare(this.priority, result.getPriority());

            if (priorityChecker == 0)
            {
				int countChecker = Integer.compare(this.count, Integer.parseInt(result.getCount()));
                
                if (countChecker == 0)
                {
					int locationCheck = this.getLocation().compareToIgnoreCase(result.getLocation());
					return locationCheck;
                }
                
                else
                {
					return countChecker * -1;
                }
                
            }
           
            return (priorityChecker * -1);
        }
        
        /**
         * @return  a visible output of the priority for a text file
         */
        @Override
        public String toString()
        {
            return "Location: " + this.location + ", Count: " + this.getCount() + ", Score: " + this.getPriority();
        }
    }  
    
    /**
	 * Initializes an InvertedIndex model as a TreeMap that can be globally accessed and modified.
	 */
    private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> invertedIndexTreeMap;

    /**
	 * Initializes a data storage and counting model as a TreeMap that can be globally accessed and modified.
	 */
    private final TreeMap<String, Integer> dataCountTreeMap;

    /**
	 * Implements an InvertedIndex as a TreeMap that can be globally accessed and modified
     * as well as a data storage and counting model as a TreeMap that can be globally accessed and modified.
	 */
    public InvertedIndex()
    {
        invertedIndexTreeMap = new TreeMap<>();
        dataCountTreeMap = new TreeMap<>();
    }

    /**
	 * Returns an unmodifiable set of the words in the InvertedIndex

	 * @return  a set with the words (keys) of the InvertedIndex
	 */
    public Set<String> getWords()
    {
		return Collections.unmodifiableSet(invertedIndexTreeMap.keySet());
	}

	/**
     * Returns an unmodifiable set of the locations of a word in the InvertedIndex
     * 
	 * @param word  the word that the locations pertain to
	 * @return      a set with the locations associated with the word
	 */
    public Set<String> getLocations(String word)
    {
        if (invertedIndexTreeMap.containsKey(word)) //Can use '.contains(word)' ?
        {
			return Collections.unmodifiableSet(invertedIndexTreeMap.get(word).keySet());
        }
        
		return Collections.emptySet();
	}

	/**
	 * Returns an unmodifiable set of the positions of a word at a location in the InvertedIndex
	 * 
	 * @param word      the word that the locations pertain to
	 * @param location  the location the set pertains to
	 * @return          a set with the positions associated with the word at a locations
	 */
    public Set<Integer> getPositions(String word, String location)
    {
        if (invertedIndexTreeMap.containsKey(word)) //Can use '.contains(word)' ?
        {
            if (invertedIndexTreeMap.get(word).containsKey(location))
            {
				return Collections.unmodifiableSet(invertedIndexTreeMap.get(word).get(location));
			}
		}
		return Collections.emptySet();
	}

    /**
	 * Counts a specific location for a text file
	 * 
	 * @param location  the location to be counted at
	 * @return          the count at the specified location
	 */
    public Integer getLocationCount(String location)
    {
        if (dataCountTreeMap.containsKey(location))
        {
			return dataCountTreeMap.get(location);
        }
        
		return null;
	}

    /**
	 * Adds a word to the InvertedIndex along with its accompanying attributes
	 *
	 * @param word     the word to add
	 * @param path     the filepath to be added from
	 * @param location the location in the file where the word was found
	 */
    public void add(String word, String path, int location)
    {
        invertedIndexTreeMap.putIfAbsent(word, new TreeMap<>());

		invertedIndexTreeMap.get(word).putIfAbsent(path, new TreeSet<>());
        
        invertedIndexTreeMap.get(word).get(path).add(location);
        
        if (!dataCountTreeMap.containsKey(path) || dataCountTreeMap.get(path) < location)
        {
			dataCountTreeMap.put(path, location);
		} 
    }

    /**
	 * Adds all words to the InvertedIndex from another
	 *
	 * @param originIndex     the index to copy from
	 */
    public void addAll(InvertedIndex originIndex)
    {
        Set<String> setOfWords = originIndex.invertedIndexTreeMap.keySet();
        
        Iterator<String> wordIterator = setOfWords.iterator();

        while (wordIterator.hasNext())
        {
            String word = wordIterator.next();

            if (this.invertedIndexTreeMap.containsKey(word))
            {
                Set<String> setOfLocations = originIndex.invertedIndexTreeMap.get(word).keySet();

                Iterator<String> locationIterator = setOfLocations.iterator();
                
                while (locationIterator.hasNext())
                {
                    String location = locationIterator.next();
                    
                    if (this.invertedIndexTreeMap.get(word).containsKey(location))
                    {
						this.invertedIndexTreeMap.get(word).get(location).addAll(originIndex.invertedIndexTreeMap.get(word).get(location));
                    }
                    
                    else
                    {
						this.invertedIndexTreeMap.get(word).put(location, originIndex.invertedIndexTreeMap.get(word).get(location));
					}
				}
            }

            else
            {
                this.invertedIndexTreeMap.put(word, originIndex.invertedIndexTreeMap.get(word));
            }
        }

        for (String location : originIndex.dataCountTreeMap.keySet())
        {
            if (!dataCountTreeMap.containsKey(location) || dataCountTreeMap.get(location) < originIndex.getLocationCount(location))
            {
				dataCountTreeMap.put(location, originIndex.dataCountTreeMap.get(location));
			}
		}
    }
    
    /**
	 * Checks to see if the InvertedIndex contains a word or not
	 *
	 * @param word     the word to check
	 * @return         whether the word is contained or not
	 */
    public boolean contains(String word)
    {
        return invertedIndexTreeMap.containsKey(word);
    }

    /**
	 * Checks to see if the InvertedIndex contains a word at a specified location or not
	 *
	 * @param word     the word to check
	 * @param location the location to be checked at
	 * @return         whether the word is contained or not
	 */
    public boolean contains(String word, String location)
    {
        return invertedIndexTreeMap.containsKey(word) && invertedIndexTreeMap.get(word).containsKey(location);
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
    public boolean contains(String word, String location, int position)
    {
        return this.contains(word) && this.contains(word, location) && invertedIndexTreeMap.get(word).get(location).contains(position);
	}
  
    /**
	 * Outputs the contents of the InvertedIndex in the JSON format.
	 *
	 * @param path  the filepath of the file to be outputted
	 * @throws IOException if an IO error occurs
	 */
    public void invertedIndexWriter(Path path) throws IOException
    {
        SimpleJsonWriter.asInvertedIndex(invertedIndexTreeMap, path);
    }

    /**
	 * Creates a writer for the -counts flag and outputs to the file passed
	 * 
	 * @param path  the filepath of the file to be outputted
	 * @throws IOException if an IO error occurs
	 */
    public void dataCountWriter(Path path) throws IOException
    {
        SimpleJsonWriter.asObject(dataCountTreeMap, path);
    }
    
    /**
     * 
     * @param query         the word to be searched
     * @param searchResults a list that stores search results
     * @param cachedResults a map that stores search results alongside their original query
     */
    private void search(String query, ArrayList<InvertedIndex.Result> searchResults, HashMap<String, InvertedIndex.Result> cachedResults)
    {
        for (String location : this.invertedIndexTreeMap.get(query).keySet())
        {
            if (cachedResults.containsKey(location))
            {
                cachedResults.get(location).update(query);
            }

            else
            {
                Result searchResult = this.new Result(location, query);
                searchResults.add(searchResult);
                cachedResults.put(location, searchResult);
            }
        }
    }
    
    /**
	 * Searches for any word stem in the InvertedIndex that starts with a query word
	 * 
	 * @param setOfQueries  a set of the queries to be searched for
     * @return              a list containing the contents of the set with any word stem that starts with a query word
	 */
    public ArrayList<InvertedIndex.Result> partialSearch(Set<String> setOfQueries)
    {
        HashMap<String, InvertedIndex.Result> cachedResults = new HashMap<>();
        ArrayList<InvertedIndex.Result> searchResults = new ArrayList<>();

        for (String query : setOfQueries)
        {
            for (String word : invertedIndexTreeMap.tailMap(query).keySet())
            {
                if (word.startsWith(query))
                {
                    search(word, searchResults, cachedResults);
                }

                else
                {
                    break;
                }
            }
        }

        Collections.sort(searchResults);

        return searchResults;
    }
    
    /**
	 * Generates search results depending on the queries and the search configuration
	 * 
	 * @param setOfQueries      a set of the queries to be searched for
     * @return                  a list of search results
	 */
    public ArrayList<InvertedIndex.Result> exactSearch(Set<String> setOfQueries)
    {
        HashMap<String, InvertedIndex.Result> cachedResults = new HashMap<>();
        ArrayList<InvertedIndex.Result> searchResults = new ArrayList<>();

        for (String word : setOfQueries)
        {
            if (invertedIndexTreeMap.containsKey(word))
            {
                search(word, searchResults, cachedResults);
            }
        }

        Collections.sort(searchResults);

        return searchResults;
    }

    /**
	 * Generates search results depending on the queries and the search configuration
	 * 
	 * @param setOfQueries      a set of the queries to be searched for
     * @param isPartialSearch   the type of search to be performed (partial or not)
     * @return                  a list of search results
	 */
    public ArrayList<Result> searchResultsGenerator(Set<String> setOfQueries, boolean isPartialSearch)
    {
        if (isPartialSearch)
        {
            return partialSearch(setOfQueries);
        }

        return exactSearch(setOfQueries);
    }

    /**
     * @return  a visible output of the InvertedIndex
     */
    @Override
    public String toString()
    {
		return invertedIndexTreeMap.toString();
	}
}