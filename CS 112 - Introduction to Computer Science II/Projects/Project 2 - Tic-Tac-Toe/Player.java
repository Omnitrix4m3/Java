public class Player
{
	// INSTANCE DATA: name, wins, losses
	private String playerName;
	private int wins;
	private int losses;
	
	//CONSTRUCTOR
	public Player(String playerName)
	{
		this.playerName = playerName;
	}
	
	// METHODS
	// getName(), setName(), getWins(), getLosses(), toString(), etc
	public String getName()
	{
		return playerName;
	}
	
	public void setName(String newName)
	{
		playerName = newName;
	}
	
	public int getWins()
	{
		return wins;
	}
	
	public void setWins()
	{
		wins++;
	}
	
	public int getLosses()
	{
		return this.losses;
	}
	
	public void setLosses()
	{
		losses++;
	}
	
	public String toString()
	{
		return ("Name: " + playerName + ", Wins: " + wins + ", Losses: " + losses);
	}
}
