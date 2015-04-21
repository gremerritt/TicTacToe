import java.util.Scanner;
import java.util.Random;


//	This implements a text-based version of the classic tic-tac-toe game
//	It should be easy to use functions from this game to create an Android version
//		Most relevant functions (see the functions for more detailed information):
//			isOpen			-	(boolean)	checks if a square can be played in
//			makeMove		-	(void)		make a play in a specific box. this is the function that should
// 											likely be updated so that it updates the UI.
//			isTie			-	(boolean)	checks if the board is in a tied state.
//			isWin			-	(boolean)	checks if the provided player has won.
//			computerTurn	-	(int)		returns the square that the computer will play in.
//
//	A few notes on variables:
//		board:
//			The board variable is an NxN (given by the 'dim' parameter)
// 			multidimensional array of the current game board.
//				Values: 	0 - Empty space
// 							1 - Users space ("X")
// 							2 - Computers space ("O")
//
// 			For example, if the board was:
// 				  X O
// 				X O 
// 				O O X
// 			then then array would be:
// 				{{0,1,2},{1,2,0},{2,2,1}}
//
//		input (or any variable you use for the square to be played - eg. return value of computerTurn):
//			This is a number in the range 0 to (dim * dim)-1.
//			So for a traditional 3x3 board, this would be 0-8, numbered as follows:
//				0 1 2
//				3 4 5
//				6 7 8

public class TicTacToe {
	private static final int dim = 3;
	private static int[][] board = new int[dim][dim];
	
	public static void main(String args[]) {
		
		String rawInput;
		int input;
		int computerTurn;
		final int maxVal = (dim * dim) - 1;
		Scanner sc = new Scanner(System.in);
		
		// main game loop
		while (true) {
			printBoard();
			
			// get input and check it ------------------------
			System.out.println("Enter the next \"X\" location (0 - " + maxVal + "): ");
			rawInput = sc.nextLine();
			input = Integer.parseInt(rawInput);
			if ( !checkInput( input ) )
			{
				System.out.println("Bad input value!");
				continue;
			}
			// -----------------------------------------------
			
			// check if this space is available
			if ( !isOpen(input) )
			{
				System.out.println("Already played!");
				continue;
			}
			
			makeMove(input, 1);
			
			if ( isTie() )
			{
				System.out.println("It's a tie!");
				break;
			}
			
			if ( isWin(1) )
			{
				System.out.println("Congratulations! You win!");
				break;
			}
			
			computerTurn = computerTurn();
			makeMove(computerTurn, 2);
			
			if ( isTie() )
			{
				System.out.println("It's a tie!");
				break;
			}
			
			if ( isWin(2) )
			{
				System.out.println("Uh oh! You lost.");
				break;
			}
		}
		System.out.println("\nFinal board:");
		printBoard();
	}
	
	//-----------------------------------------------------------
	// This function makes a move for the given 'player' in the
	// given 'square'.
	//-----------------------------------------------------------
	public static void makeMove(int square, int player)
	{
		int location[] = new int[ 2 ];
		boxNumToArray(square, location);
		board[ location[ 0 ] ][ location[ 1 ] ] = player;
	}
    
	//-----------------------------------------------------------
	// This function checks if the player given by 'player' input
	// won the game.
	//
	// Input: 
	//		player - the player to check for a win:
	//					1 - user
	//					2 - computer
	//
	// Returns:
	//		True  - 'player' won
	//		False - 'player' didn't win
	//-----------------------------------------------------------
    public static boolean isWin(int player)
    {
    	// check horizontals
    	for (int i=0; i<dim; i++)
    	{
    		if (board[i][0] == player)
    		{
    			for (int j=1; j<dim; j++)
    			{
    				if ( board[i][j] != player )
    					break;
    					
    				if ( j == (dim - 1) )
    					return true;
    			}
    		}
    	}
    	
    	// check verticals
    	for (int j=0; j<dim; j++)
    	{
    		if (board[0][j] == player)
    		{
    			for (int i=1; i<dim; i++)
    			{
    				if ( board[i][j] != player )
    					break;
    					
    				if ( i == (dim - 1) )
    					return true;
    			}
    		}
    	}
    	
    	// check diagonals
    	for (int i=0; i<dim; i++)
    	{
    		if ( board[i][i] != player )
    			break;
    		
    		if ( i == (dim - 1) )
    			return true;
    	}
    	
    	for (int i=0; i<dim; i++)
    	{
    		if ( board[dim - 1 -i][i] != player )
    			break;
    		
    		if ( i == (dim - 1) )
    			return true;
    	}

    	return false;
    }
    
	//-----------------------------------------------------------
	// This function determines if the game is a tie. This occurs
	// when each row, column, and diagonal has both an X and an 0
	//
	// Input: 
	//		board - gameboard array. see board variable in main.
	//
	// Returns:
	//		True  - tie
	//		False - not a tie
	//-----------------------------------------------------------
    public static boolean isTie()
    {
    	boolean[][] taken = new boolean[ ( dim * 2 ) + 2 ][ 2 ];
    	
    	for (int i=0; i<dim; i++)
    	{
    		for (int j=0; j<dim; j++)
    		{
    			switch (board[i][j]) {
    				case 1:
    					taken[i][0] = true;
    					taken[j+dim][0] = true;
    					if ( i == j )
    						taken[ dim * 2 ][0] = true;
    					if ( ( i + j ) == ( dim - 1) )
    						taken[ (dim * 2) + 1 ][0] = true;
    					break;
    				case 2:
    					taken[i][1] = true;
    					taken[j+dim][1] = true;
    					if ( i == j )
    						taken[ dim * 2 ][1] = true;
    					if ( ( i + j ) == ( dim - 1) )
    						taken[ (dim * 2) + 1 ][1] = true;
    					break;
    				default:
    					break;
    			}
    		}
    	}
    	
    	for (int i=0; i<(( dim * 2 ) + 2); i++)
    	{
    		if ( ( !taken[i][0] ) || ( !taken[i][1] ) )
    			return false;
    	}
    
    	return true;
    }
    
	//-----------------------------------------------------------
	// This function generates the computers move, then checks if
	// the computer has won (via the isWin function).
	//
	// The basic strategy is:
	//		- If the user is about to win, the computer will block
	//		- If the computer can win, it will
	//		- Else, the computer will pick a move
	//
	// Returns:
	//		square that the computer will play
	//-----------------------------------------------------------
	public static int computerTurn()
	{
		int[] coord = {0, 0};
		
		// check if computer can win or block user from winning
		if ( isOneAway(coord, 2) )
			return boxArrayToNum(coord);
			
		if ( isOneAway(coord, 1) )
			return boxArrayToNum(coord);
		
		computerMove(coord);
		return boxArrayToNum(coord);
	}
	
	//-----------------------------------------------------------
	// This function makes a move for the computer. It finds the 
	// row/column/diagonal that is the closest to winning and
	// makes a move there.
	//
	// Input: 
	//		coord - the space that the computer will play.
	//-----------------------------------------------------------
	public static void computerMove(int[] coord)
	{
		// this will be very similar to the isOneAway function
		// except instead of stopping if we find more than one
		// empty space, we'll keep the count, and check if it's
		// greater than the max.
		
		// the 'which' variable will tell us which row/column/diagonal have already been assessed
		// 		the indexes are as follows:
		//			0 - [dim-1] 		= rows 0 through [dim-1]
		//			dim - [(dim*2) - 1]	= columns dim through [(dim*2) - 1]
		//			dim*2				= upper-left to bottom right diagonal
		//			(dim*2) + 1			= bottom-left to upper-right diagonal
		boolean[] which = new boolean[ ( dim * 2 ) + 2 ];		// defaults to false
		int count = 0;											// count of empty spots in a given row/col/diagonal
		int min = dim;											// the current lowest number of free spaces
		int[][] tmpBestSpots = new int[ (dim * dim) ][2];
		int[][] bestSpots = new int[ (dim * dim) ][2];
		int bestSpotsLength = 0;
		int[][] availableSpots = new int[ (dim * dim) ][2];
		int availableSpotsLength = 0;
		
		for (int i=0; i<dim; i++)
    	{
    		for (int j=0; j<dim; j++)
    		{
    			if ( board[i][j] == 2 )
    			{
    				// first see how close we can get in a vertical
    				for (int k=0; k<dim; k++)
    				{
    					if ( k == i )
    						continue;
						
						if ( ( board[k][j] == 1 ) || ( which[dim + j] ) )
						{
							count = 0;
							break;
						}
    					else if ( board[k][j] == 0 )
    					{
    						tmpBestSpots[count][0] = k;
    						tmpBestSpots[count][1] = j;
    						count++;
    					}
    				}
    				if ( ( count < min ) && ( count > 0 ) )
    				{
    					min = count;
    					bestSpotsLength = count;
    					for (int k=0; k<count; k++)
    					{
    						bestSpots[k][0] = tmpBestSpots[k][0];
    						bestSpots[k][1] = tmpBestSpots[k][1];
    					}
    					which[dim + j] = true;
    				}
    				else if ( count == min )
    				{
    					for (int k=0; k<count; k++)
    					{
    						bestSpots[bestSpotsLength][0] = tmpBestSpots[k][0];
    						bestSpots[bestSpotsLength][1] = tmpBestSpots[k][1];
    						bestSpotsLength++;
    					}
    					which[dim + j] = true;
    				}
    				
    				// then check how close we can get in a horizontal
    				count = 0;
    				for (int k=0; k<dim; k++)
    				{
    					if ( k == j )
    						continue;
    						
    					if ( ( board[i][k] == 1 ) || ( which[i] ) )
						{
							count = 0;
							break;
						}
    					else if ( board[i][k] == 0 )
    					{
    						tmpBestSpots[count][0] = i;
    						tmpBestSpots[count][1] = k;
    						count++;
    					}
    				}
    				if ( ( count < min ) && ( count > 0 ) )
    				{
    					min = count;
    					bestSpotsLength = count;
    					for (int k=0; k<count; k++)
    					{
    						bestSpots[k][0] = tmpBestSpots[k][0];
    						bestSpots[k][1] = tmpBestSpots[k][1];
    					}
    					which[i] = true;
    				}
    				else if ( count == min )
    				{
    					for (int k=0; k<count; k++)
    					{
    						bestSpots[bestSpotsLength][0] = tmpBestSpots[k][0];
    						bestSpots[bestSpotsLength][1] = tmpBestSpots[k][1];
    						bestSpotsLength++;
    					}
    					which[i] = true;
    				}
    				
    				// then check how close we can get in a diagonal
    				// upper-left to bottom-right diagonal
    				count = 0;
    				if ( i == j )
    				{
						for (int k=0; k<dim; k++)
						{
							if ( k == i )
								continue;
								
							if ( ( board[k][k] == 1 ) || ( which[ ( dim * 2 ) ] ) )
							{
								count = 0;
								break;
							}
							else if ( board[k][k] == 0 )
							{
								tmpBestSpots[count][0] = k;
								tmpBestSpots[count][1] = k;
								count++;
							}
						}
					}
					if ( ( count < min ) && ( count > 0 ) )
    				{
    					min = count;
    					bestSpotsLength = count;
    					for (int k=0; k<count; k++)
    					{
    						bestSpots[k][0] = tmpBestSpots[k][0];
    						bestSpots[k][1] = tmpBestSpots[k][1];
    					}
    					which[ ( dim * 2 ) ] = true;
    				}
    				else if ( count == min )
    				{
    					for (int k=0; k<count; k++)
    					{
    						bestSpots[bestSpotsLength][0] = tmpBestSpots[k][0];
    						bestSpots[bestSpotsLength][1] = tmpBestSpots[k][1];
    						bestSpotsLength++;
    					}
    					which[ ( dim * 2 ) ] = true;
    				}
	
					// bottom-left to upper-right diagonal
					count = 0;
					if ( ( i + j ) == ( dim - 1) )
					{
						for (int k=0; k<dim; k++)
						{
							if ( k == j )
								continue;
								
							if ( ( board[dim - 1 - k][k] == 1 ) || ( which[ ( dim * 2 ) + 1 ] ) )
							{
								count = 0;
								break;
							}
							else if ( board[dim - 1 - k][k] == 0 )
							{
								tmpBestSpots[count][0] = dim - 1 - k;
								tmpBestSpots[count][1] = k;
								count++;
							}
						}
    				}
    				if ( ( count < min ) && ( count > 0 ) )
    				{
    					min = count;
    					bestSpotsLength = count;
    					for (int k=0; k<count; k++)
    					{
    						bestSpots[k][0] = tmpBestSpots[k][0];
    						bestSpots[k][1] = tmpBestSpots[k][1];
    					}
    					which[ ( dim * 2 ) + 1 ] = true;
    				}
    				else if ( count == min )
    				{
    					for (int k=0; k<count; k++)
    					{
    						bestSpots[bestSpotsLength][0] = tmpBestSpots[k][0];
    						bestSpots[bestSpotsLength][1] = tmpBestSpots[k][1];
    						bestSpotsLength++;
    					}
    					which[ ( dim * 2 ) + 1 ] = true;
    				}
    			}
    			else if ( board[i][j] == 0 )
    			{
    				availableSpots[availableSpotsLength][0] = i;
    				availableSpots[availableSpotsLength][1] = j;
    				availableSpotsLength++;
    			}
    		}
    	}
    	
    	Random rand = new Random();
    	int randomNum;
    	if ( bestSpotsLength == 0 )
    	{
    		randomNum = rand.nextInt(availableSpotsLength);		// this gets a random number between 0 and availableSpotsLength
    		coord[ 0 ] = availableSpots[ randomNum ][ 0 ];
    		coord[ 1 ] = availableSpots[ randomNum ][ 1 ];
    	}
    	else
    	{
			randomNum = rand.nextInt(bestSpotsLength);			// this gets a random number between 0 and bestSpotsLength
			coord[ 0 ] = bestSpots[ randomNum ][ 0 ];
    		coord[ 1 ] = bestSpots[ randomNum ][ 1 ];
		}
	}
	
	
	//-----------------------------------------------------------
	// This function determines if the computer can either win
	// the game, or block the user from winning the game.
	//
	// Input: 
	//		coord - the space that either wins or blocks.
	//		player - 1: check if computer can block
	//				 2: check if computer can win
	//
	// Returns:
	//		True  - computer can win/block
	//		False - computer cannot win/block
	//
	//-----------------------------------------------------------
	public static boolean isOneAway(int[] coord, int player)
	{
		int opponent;
		
		if ( player == 1 )
			opponent = 2;
		else
			opponent = 1;
		
		boolean canWin = false;
		
		for (int i=0; i<dim; i++)
    	{
    		for (int j=0; j<dim; j++)
    		{
    			if ( board[i][j] == player )
    			{
    				// check the verticals
    				for (int k=0; k<dim; k++)
    				{
    					if ( k == i )
    						continue;
						
						if ( board[k][j] == opponent )
						{
							canWin = false;
							break;
						}
    					else if ( board[k][j] == 0 )
    					{
    						if ( canWin )
    						{
    							canWin = false;
    							break;
    						}
    						canWin = true;
    						coord[0] = k;
    						coord[1] = j;
    					}
    				}
    				if ( canWin )
    					break;
    				
    				// check the horizontals
    				for (int k=0; k<dim; k++)
    				{
    					if ( k == j )
    						continue;
    						
    					if ( board[i][k] == opponent )
						{
							canWin = false;
							break;
						}
    					else if ( board[i][k] == 0 )
    					{
    						if ( canWin )
    						{
    							canWin = false;
    							break;
    						}
    						canWin = true;
    						coord[0] = i;
    						coord[1] = k;
    					}
    				}
    				if ( canWin )
    					break;
    				
    				// if the square is on a diagonal, check it
    				if ( i == j )
    				{
    					// upper-left to bottom-right diagonal
						for (int k=0; k<dim; k++)
						{
							if ( k == i )
								continue;
								
							if ( board[k][k] == opponent )
							{
								canWin = false;
								break;
							}
							else if ( board[k][k] == 0 )
							{
								if ( canWin )
								{
									canWin = false;
									break;
								}
								canWin = true;
								coord[0] = k;
								coord[1] = k;
							}
						}
    				}
    				if ( canWin )
    					break;
	
					// bottom-left to upper-right diagonal
					if ( ( i + j ) == ( dim - 1) )
					{
						for (int k=0; k<dim; k++)
						{
							if ( k == j )
							{
								continue;
							}
								
							if ( board[dim - 1 - k][k] == opponent )
							{
								canWin = false;
								break;
							}
							else if ( board[dim - 1 - k][k] == 0 )
							{
								if ( canWin )
								{
									canWin = false;
									break;
								}
								canWin = true;
								coord[0] = dim - 1 - k;
								coord[1] = k;
							}
						}
    				}
    			}
    		}
    		if ( canWin )
    			break;
    	}
    	
    	return canWin;
	}

	//-----------------------------------------------------------
	// This function checks if the space is available to play.
	// This is specifically designed for a 3x3 board and will
	// not work otherwise
	//
	// Input: 
	//		input - user input
	//
	// Returns:
	//		true  - space can be played
	//		false - space is taken and cannot be played
	//-----------------------------------------------------------
	public static boolean isOpen(int input)
	{		
		int[] location = new int[ 2 ];
		boxNumToArray(input, location);
		
		if ( board[ location[ 0 ] ][ location[ 1 ] ] != 0 )
			return false;
		
		return true;
	}

	//-----------------------------------------------------------
	// This function converts the number value of a square to the
	// array value for that square.
	//-----------------------------------------------------------
	public static void boxNumToArray(int n, int[] location)
	{
		for (int i=1; i<=dim; i++)
		{
			if ( n < ( dim * i ) )
			{
				location[ 0 ] = ( i - 1 );
				break;
			}
		}
		location[ 1 ] = n % dim;
	}
	
	//-----------------------------------------------------------
	// This function converts the array value of a square to the
	// number value for that square.
	//-----------------------------------------------------------
	public static int boxArrayToNum(int[] location)
	{
		return ( ( dim * location[ 0 ] ) + location[ 1 ] );
	}

	//-----------------------------------------------------------
	// This function checks the user input.
	//
	// Input:
	//		input - user input
	//
	// Returns:
	//		True  - input is good
	//		False - input is bad
	//-----------------------------------------------------------
	public static boolean checkInput(int input)
	{
		if ( ( input < 0 ) || ( input >= ( dim * dim ) ) )
			return false;
			
		return true;
	}

	//-----------------------------------------------------------
	// This function prints the current game board
	//
	//-----------------------------------------------------------
    public static void printBoard()
    {
    	String display = "";
    	
    	for (int i=0; i<dim; i++)
    		display += " _____";
    	System.out.println(display);
    	display = "";
    		
    	for (int i=0; i<dim; i++)
    	{
    		for (int k=0; k<dim; k++)
    			display += "|     ";
    		display += "|";
    		System.out.println(display);
    		display = "";
    		
    		for (int j=0; j<dim; j++)
    		{
    			switch (board[i][j]) {
    				case 0:
    					display += "|     ";
    					break;
    				case 1:
    					display += "|  X  ";
    					break;
    				case 2:
    					display += "|  O  ";
    					break;
    				default:
    					break;
    			}
    		}
    		display += "|";
    		System.out.println(display);
    		display = "";
    		
    		for (int k=0; k<dim; k++)
    			display += "|_____";
    		display += "|";
    		System.out.println(display);
    		display = "";
    	}
    }
}