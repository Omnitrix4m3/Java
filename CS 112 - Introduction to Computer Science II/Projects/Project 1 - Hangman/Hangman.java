import java.util.*;
import java.lang.*;

public class Hangman
{
    //Initializes an array of movie titles that does not change
    static String movieList[] = {"Star Wars: The Last Jedi", "The Matrix", "Avatar", "Tron: Legacy", "Interstellar", "The Dark Knight", "Inception", "Doctor Strange", "Avengers", "Black Panther", "Iron Man", "Justice League", "Solo", "Rogue One", "Batman Begins"};
    
    public static void main(String[] args)
    {
        //Creates Random and Scanner objects that are later invoked in the code
        Random randomGenerator = new Random();
        Scanner userInput = new Scanner(System.in);
        
        //Allows for the creation of a while loop that runs according to a singular boolean case
        boolean isRunning = true;

        //Picks a random integer to be chosen from the array of movies and sets the initial amount of wrong guesses to zero
        int randomSelection = randomGenerator.nextInt(movieList.length);
        int wrongGuesses = 0;
        
        //Initializes a String variables with the value of a random movie from the array and one in lowercase
        String randomMovie = movieList[randomSelection];
        String randomMovieLower = randomMovie.toLowerCase();
        
        //Creates a StringBuilder object with the value of the random movie in lowercase so that the characters of the movie can be replaced with asterisks 
        StringBuilder currentGuess = new StringBuilder(randomMovieLower.length());
        currentGuess.append(randomMovieLower);
        
        //Sets the individual letters of the movie name to asterisks
        for (int i = 0; i < currentGuess.length(); i++)
        {
            char c = currentGuess.charAt(i);
            if (Character.isLetter(c) == true)
            {
                currentGuess.setCharAt(i, '*');
            }
        }

        //Prints out a statement greeting the user and explains the rules
        System.out.println("Welcome to Hangman, you will have to guess the name of the following movie one letter at a time. If you get them all you win, if you guess incorrectly seven times, you lose!");
        System.out.println();
        System.out.println(currentGuess);

        //The main body of the code that repeats after every letter guess
        while (isRunning == true)
        {
            //Prompts the user for a guess
            System.out.println("Guess a letter of the word: ");
            
            //Initializes a String variable with the lowercase value of the user's guess 
            String s = userInput.nextLine().toLowerCase();
            
            //Initializes a character variable with the value of the first letter of the user's guess
            char c = s.charAt(0);
            
            //Logic which checks to see if the movie contains the letter guess and replaces all asterisks with instances of it and capitalizes the letter if it was originally capitalized
            if (randomMovieLower.contains(s) == true)
            {
                for (int i = 0; i < currentGuess.length(); i++)
                {
                    if (randomMovieLower.charAt(i) == c)
                    {
                        if (Character.isUpperCase(randomMovie.charAt(i)) == true)
                        {
                            currentGuess.setCharAt(i, Character.toUpperCase(c));
                        }
                        else
                        {
                            currentGuess.setCharAt(i, c);
                        }
                        
                        System.out.println(currentGuess);
                    }
                }
            }
            
            //Logic which prints an error if the movie does not contain the letter guess and also increments the amount of incorrect guesses
            else if (randomMovieLower.contains(s) == false)
            {
                System.out.println("Sorry, try again!");
                wrongGuesses += 1;
            }
            
            //Logic which exits the loop and prints a message after the movie name is discovered
            if (randomMovie.equals(currentGuess.toString()))
            {
                System.out.println("Congratulations, you win!");
                break;
            }
            
            //Logic which exits the loop and prints a message after the seven incorrect guesses have been exhausted
            if (wrongGuesses >= 7)
            {
                System.out.println("Game over!");
                break;
            }
        }
    }
}