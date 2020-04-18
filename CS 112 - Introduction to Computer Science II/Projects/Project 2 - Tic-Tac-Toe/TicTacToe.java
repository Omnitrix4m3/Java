import java.util.*;

public class TicTacToe
{
	//INSTANCE DATA
	private int[][] board;
	private int moves;
	private int line;
	private boolean playerOneWin;
	private boolean playerTwoWin;

	//CONSTRUCTOR
	public TicTacToe()
	{
		board = new int[3][3];
		
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				board[i][j] = 0;
			}
		}
	}
	
	public void Move()
	{
		int playerToMove;
		Scanner userInput = new Scanner(System.in);
		
		if(moves % 2 == 0)
		{
			playerToMove = 1;
		}
		
		else
		{
			playerToMove = 2;
		}

		System.out.println("Player " + playerToMove + ", where would you like to move? Enter as an ordered pair 'row, column': ");
		
		String stringMovePair = userInput.next();
		String stringMove = stringMovePair.replace(",", "");
		
		int row = Character.getNumericValue(stringMove.charAt(0));
		row--;
		
		int column = Character.getNumericValue(stringMove.charAt(1));
		column--;
		
		if (isLegal(board[row][column]) == true)
		{
			if (playerToMove == 1)
			{
				board[row][column] = 1;
			}
		
			else
			{
				board[row][column] = 2; 
			}
			
			moves++;
		}
		
		else
		{
			System.out.println("Sorry! That space is unavailable!");
		}
	}

	public boolean isLegal(int space)
	{
		if (space == 0)
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	public boolean hasWon()
	{
		for (int index = 0; index < 8; index++)
		{
			switch (index)
			{
				case 0:
					if (board[0][0] == board[0][1] && board[0][1] == board[0][2])
					{
						if (board[0][0] + board[0][1] + board[0][2] == 3)
						{
							System.out.println("Player 1 has won!");
							playerOneWin = true;
							return true;
						}
						
						else if (board[0][0] + board[0][1] + board[0][2] == 6)
						{
							System.out.println("Player 2 has won!");
							playerTwoWin = true;
							return true;
						}
						
						return false;
					}
					
					break;
				case 1:
					if (board[1][0] == board[1][1] && board[1][1] == board[1][2])
					{
						if (board[1][0] + board[1][1] + board[1][2] == 3)
						{
							System.out.println("Player 1 has won!");
							playerOneWin = true;
							return true;
						}
						
						else if (board[1][0] + board[1][1] + board[1][2] == 6)
						{
							System.out.println("Player 2 has won!");
							playerTwoWin = true;
							return true;
						}
						
						return false;
					}
					
					break;
				case 2:
					if (board[2][0] == board[2][1] && board[2][1] == board[2][2])
					{
						if (board[2][0] + board[2][1] + board[2][2] == 3)
						{
							System.out.println("Player 1 has won!");
							playerOneWin = true;
							return true;
						}
						
						else if (board[2][0] + board[2][1] + board[2][2] == 6)
						{
							System.out.println("Player 2 has won!");
							playerTwoWin = true;
							return true;
						}
						
						return false;
					}
					
					break;
				case 3:
					if (board[0][0] == board[1][0] && board[1][0] == board[2][0])
					{
						if (board[0][0] + board[1][0] + board[2][0] == 3)
						{
							System.out.println("Player 1 has won!");
							playerOneWin = true;
							return true;
						}
						
						else if (board[0][0] + board[1][0] + board[2][0] == 6)
						{
							System.out.println("Player 2 has won!");
							playerTwoWin = true;
							return true;
						}
						
						return false;
					}
					
					break;
				case 4:
					if (board[0][2] == board[1][1] && board[1][1] == board[2][1])
					{
						if (board[0][2] + board[1][1] + board[2][1] == 3)
						{
							System.out.println("Player 1 has won!");
							playerOneWin = true;
							return true;
						}
						
						else if (board[0][2] + board[1][1] + board[2][1] == 6)
						{
							System.out.println("Player 2 has won!");
							playerTwoWin = true;
							return true;
						}
						
						return false;
					}
					
					break;
				case 5:
					if (board[0][2] == board[1][2] && board[1][2] == board[2][2])
					{
						if (board[0][2] + board[1][2] + board[2][2] == 3)
						{
							System.out.println("Player 1 has won!");
							playerOneWin = true;
							return true;
						}
						
						else if (board[0][2] + board[1][2] + board[2][2] == 6)
						{
							System.out.println("Player 2 has won!");
							playerTwoWin = true;
							return true;
						}
						
						return false;
					}
					
					break;
				case 6:
					if (board[0][0] == board[1][1] && board[1][1] == board[2][2])
					{
						if (board[0][0] + board[1][1] + board[2][2] == 3)
						{
							System.out.println("Player 1 has won!");
							playerOneWin = true;
							return true;
						}
						
						else if (board[0][0] + board[1][1] + board[2][2] == 6)
						{
							System.out.println("Player 2 has won!");
							playerTwoWin = true;
							return true;
						}
						
						return false;
					}
					
					break;
				case 7:
					if (board[0][2] == board[1][1] && board[1][1] == board[2][0])
					{
						if (board[0][2] + board[1][1] + board[2][0] == 3)
						{
							System.out.println("Player 1 has won!");
							playerOneWin = true;
							return true;
						}
						
						else if (board[0][2] + board[1][1] + board[2][0] == 6)
						{
							System.out.println("Player 2 has won!");
							playerTwoWin = true;
							return true;
						}
						
						return false;
					}
					
					break;
			}
		}
		return false;
	}
	
	public boolean isTie()
	{
		if (moves >= 9 && hasWon() != true)
		{
			System.out.println("It is a tie!");
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	public boolean playerOneWin()
	{
		if (playerOneWin)
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	public boolean playerTwoWin()
	{
		if (playerTwoWin)
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	//METHODS
	public String toString()
	{
		String[][] boardToPrint = new String[3][3];
		
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				if (board[i][j] == 1)
				{
					boardToPrint[i][j] = "X";
				}
				
				else if (board[i][j] == 2)
				{
					boardToPrint[i][j] = "O";
				}
				
				else
				{
					boardToPrint[i][j] = "-";
				}
			}
		}
		
		String output = "";
		output += "| " + boardToPrint[0][0] + " | " + boardToPrint[0][1] + " | " + boardToPrint[0][2] + " |\n";
		output += "| " + boardToPrint[1][0] + " | " + boardToPrint[1][1] + " | " + boardToPrint[1][2] + " |\n";
		output += "| " + boardToPrint[2][0] + " | " + boardToPrint[2][1] + " | " + boardToPrint[2][2] + " |\n";

		return output;
	}
}
