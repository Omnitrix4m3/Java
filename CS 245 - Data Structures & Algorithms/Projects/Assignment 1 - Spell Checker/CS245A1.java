import java.net.*;
import java.io.*;
import java.util.*;

public class CS245A1
{
    public static void main(String[] args) throws IOException
    {
        //Creates both a BST and Trie and sets them to null
		BST tree = null;
        Trie trie = null;

    	//Creates an ArrayList of Strings to hold english.0 dictionary
		ArrayList<String> dictionary = new ArrayList<String>();
		//Creates an ArrayList to hold suggestions
		ArrayList<String> suggestions = new ArrayList<String>();

		//Extra Credit: Uses file found at URL instead of local
        URL githubURL = new URL("https://raw.githubusercontent.com/magsilva/jazzy/master/resource/dict/english.0");
        HttpURLConnection githubConnect = (HttpURLConnection) githubURL.openConnection();
        githubConnect.setRequestMethod("GET");

		//Allows program to read each line
        BufferedReader githubInput = new BufferedReader(new InputStreamReader(githubConnect.getInputStream()));
        
		String line;

        //Adds each word to the ArrayList 'dictionary'
		while ((line = githubInput.readLine()) != null)
        {
            if (!line.isEmpty())
            {
                dictionary.add(line.toLowerCase());
            }
        }

        //Helps prevent resource leaks
		githubInput.close();

        //Reads the properties file to use either a BST or Trie implementation
		BufferedReader propertiesInput = new BufferedReader(new FileReader("a1properties.txt"));
        String type = propertiesInput.readLine();
		propertiesInput.close();

        //Adds each item from the 'dictionary' to the Trie
		if (type.contains("trie"))
        {
            trie = new Trie();

            for (String word : dictionary)
            {
                trie.insert(word.toLowerCase());
            }
        }

        //Adds each item from the 'dictionary' to the BST
		else if (type.contains("tree"))
        {
            tree = new BST();

			//Randomizes the ArrayList as a sorted entry of items into the BST would cause a Degenerate tree
            Collections.shuffle(dictionary);

            for (String word : dictionary)
            {
                tree.insert(word);
            }
        }

		//Allows for the use of command line arguments (i.e. input file)
        BufferedReader fileInput = new BufferedReader(new FileReader(args[0]));

		//Uses an ArrayList to store a list of input data
        ArrayList<String> words = new ArrayList<String>();

        String line1;

		//Adds each word to the ArrayList 'words'
        while ((line1 = fileInput.readLine()) != null)
        {
            if (!line1.isEmpty())
            {
                words.add(line1.toLowerCase());
            }
        }

		//Helps prevent resource leaks
        fileInput.close();

        //Allows for the use of command line arguments (i.e. output file)
		BufferedWriter fileOutput = new BufferedWriter(new FileWriter(args[1]));
        
		//Allows to modify contents of a String without requiring more objects. Allows for a performance gain.
		StringBuilder outputString = new StringBuilder();

        //Iterates throughout the given words and finds suggestions 
		for (String word : words)
        {
            suggestions.clear();

            if (tree != null)
            {
                if (!tree.find(word))
                {
                    tree.clearMatch();
                    tree.getSuggestions(word);
                    suggestions.add(tree.match);
                }

                suggestions.add(word);
            }

            else
            {
                //Prevents false positives by recognizing if a word is spelled correctly
				if (trie.isSpellingCorrect(word))
                {
                    suggestions.add(word);
                }

                else
                {
                    trie.findBestSuggestions(trie.pivotPoint);
                    suggestions = trie.returnSuggestions();
                }
            }

            for (String suggestion : suggestions)
            {
                outputString.append(suggestion).append(" ");
            }

            outputString.append("\n");
        }

        //Writes to the output file
		fileOutput.write(outputString.toString());

		//Helps prevent resource leaks
        fileOutput.close();
    }
}