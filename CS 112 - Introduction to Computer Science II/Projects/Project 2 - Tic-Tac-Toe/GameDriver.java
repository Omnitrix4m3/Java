import java.util.*;

public class GameDriver 
{
	public static void main(String[] args)
	{
		while (true)
		{
			boolean isRunning = true;
	
			TicTacToe board = new TicTacToe();
			Player player1, player2 = null;
			Scanner userInput = new Scanner(System.in);
	
			System.out.println("=======================");
			System.out.println("LET'S PLAY TIC TAC TOE!");
			System.out.println("=======================");
			System.out.println(board);
	
			
			System.out.println("> Enter the name of Player1:");
			String nameOne = userInput.next();
			player1 = new Player(nameOne);
			System.out.println("Welcome Player 1: " + player1.getName());
			
			System.out.println("> Enter the name of Player2:");
			String nameTwo = userInput.next();
			player2 = new Player(nameTwo);
			System.out.println("Welcome Player 2: " + player2.getName());
		
			while (isRunning == true)
			{
				board.Move();
				System.out.println();
				
				board.hasWon();
				board.isTie();
				System.out.println(board);
				
				if (board.hasWon() == true)
				{
					if (board.playerOneWin() == true)
					{
						player1.setWins();
						player2.setLosses();
						break;
					}
					
					else
					{
						player2.setWins();
						player1.setLosses();
						break;
					}
				}
				
				else if (board.isTie() == true)
				{
					break;
				}
				
			}
					
			System.out.println();
			System.out.println(player1.getName() + " has " + player1.getWins() + " wins and " + player1.getLosses() + " losses!");
			System.out.println(player2.getName() + " has " + player2.getWins() + " wins and " + player2.getLosses() + " losses!");
				
			System.out.println();
			System.out.println("Do you want to play again? Y/N");
			String replay = userInput.next();
			
			System.out.println();
				
			if (isRunning == false)
			{
				if (replay.toLowerCase() == "y")
				{
					isRunning = true;
				}
			
				else
				{
					break;
				}
			}
		}
	}
}
