import java.util.*;

public class Trie
{
    //Nested 'Node' class allows for initialization of Node objects 
	public class TrieNode
    {
        private Character character;
        TrieNode[] list;
        TrieNode parent;

		//TrieNode constructor which allows for the TrieNode object to be modeled after the alphabet
        TrieNode()
        {
            this.list = new TrieNode[29];
        }

        //Overloaded constructor
        TrieNode(Character character)
        {
            this.character = character;
            this.list = new TrieNode [29];
        }

        //Allows for a link to a parent TrieNode to be made
		void setParent(TrieNode parent)
        {
            this.parent = parent;
        }

        //Access a specific Character
        Character getVal()
        {
            return this.character;
        }

		//Uses arithmetic to give correct index values 
        int convert(Character letter)
        {
            // If the letter is a \ insert to position 27 of list
            if (letter.equals('\''))
            {
                return 27;
            }

            return ((int) letter - 97);
        }

		//Allows for the insertion of a Character into the list of TrieNodes 
        TrieNode insertToList(Character letter)
        {
            TrieNode temp = this.list[convert(letter)];

            if (temp == null)
            {
                TrieNode temp2 = new TrieNode(letter);
                this.list[convert(letter)] = temp2;
                return temp2;
            }

            return temp;
        }
    }

    //Trie instance data
	public TrieNode root, pivotPoint;
    private String failedWord;
    private ArrayList<String> possibleSuggestions;

	//Constructor for a Trie
    public Trie()
    {
        this.root = new TrieNode();
        this.pivotPoint = null;
        this.possibleSuggestions = new ArrayList<>();
    }

	//Public insert method allows for the insertion of a word into a Trie
    public void insert(String word)
    {
        insert(word, root);
    }

    //Private insert method allows for the iteration of TrieNodes
	private void insert(String word, TrieNode node)
    {
        int length = word.length();

        TrieNode prev;

        for (int i = 0; i < length; i++)
        {
            prev = node;
            node = node.insertToList(word.charAt(i));
            node.setParent(prev);
        }

        node.list[28] = new TrieNode('\0');
    }

	//Public boolean method which eliminates false positives by checking to see if the word is spelled correctly already
    public boolean isSpellingCorrect(String word)
    {
        return isSpellingCorrect(word, root);
    }

    //Private boolean method which allows checking to see if the word is spelled correctly already by using a pivot
	private boolean isSpellingCorrect(String word, TrieNode node)
    {
        for (int i = 0; i < word.length(); i++)
        {
            if (node.list[(node.convert(word.charAt(i)))] != null)
            {
                node = node.list[(node.convert(word.charAt(i)))];
            }

            else
            {
                this.pivotPoint = node;
                this.failedWord = word;
                return false;
            }
        }

        if (node.list[28] != null)
        {
            return true;
        }

        this.pivotPoint = node;
        return false;
    }

 	//Public boolean method which eliminates false positives by checking to see if the word is spelled correctly already
    boolean checkSpelling(String word)
    {
        return checkSpelling(word, this.root);
    }

    //Private boolean method which allows checking to see if the word is spelled correctly already by not using a pivot
	private boolean checkSpelling(String word, TrieNode node)
    {
        for (int i = 0; i < word.length(); i++)
        {
            if (node.list[(node.convert(word.charAt(i)))] != null)
            {
                node = node.list[(node.convert(word.charAt(i)))];
            }

            else
            {
                return false;
            }
        }

        return node.list[28] != null;
    }

	//Private reverse string method that allows for 'backtracking' up the Trie by reversing the word
    private String reverseString(String word)
    {
        char[] tempArray = word.toCharArray();

        String temp = "";
        for (int i = tempArray.length - 1; i >= 0; i--)
            temp += tempArray[i];

        return temp;
    }

    //Private method which returns any non null character which exists first
    private TrieNode returnFirst(TrieNode node)
    {
        for (int i = 0; i < node.list.length; i++)
        {
            if (node.list[i] != null)
            {
                return node.list[i];
            }
        }

        return null;
    }

	//Private method which allows for the initial word suggestion
    private void makeFirstSuggestion(String word)
    {
        for (int i = 0; i < word.length(); i++)
        {
            for (int j = 97; j <= 122; j++)
            {
                StringBuilder checker = new StringBuilder(word);
                checker.setCharAt(i, (char) j);

                if (checkSpelling(checker.toString()))
                {
                    possibleSuggestions.add(checker.toString());
                }
            }
        }
    }

	//Private method which allows for the secpndary word suggestion
    private void makeSecondSuggestion(String word)
    {
        for (int i = 0; i < word.length() - 1; i++)
        {
            StringBuilder s = new StringBuilder(word);
            s.setCharAt(i, word.charAt(i+1));
            s.setCharAt(i+1, word.charAt(i));

            if (checkSpelling(s.toString()))
            {
                possibleSuggestions.add(s.toString());
            }
        }
    }

	//Private method which allows for the best word suggestion by combining outputs from the primary and secondary suggestions
    public void findBestSuggestions(TrieNode pivot)
    {
        String prefix = "";
        TrieNode temp = pivot;

        makeSecondSuggestion(failedWord);
        makeFirstSuggestion(failedWord);

        while (temp.parent != null)
        {
            prefix += temp.getVal();
            temp = temp.parent;
        }

        prefix = reverseString(prefix);
        temp = pivot;

        if (temp.list[28] != null)
        {
            possibleSuggestions.add(prefix);
        }

        while (temp.getVal() != '\0')
        {
            temp = returnFirst(temp);
            assert temp != null;
            prefix += temp.getVal();
        }

        possibleSuggestions.add(prefix);
    }

    //Public method which returns an ArrayList of suggestions
    public ArrayList<String> returnSuggestions()
    {
        return possibleSuggestions;
    }
}