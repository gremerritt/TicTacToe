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
// 			   X  O 
// 			  O   O   X
//
//				X X O
//			   O     O
//			  X   O   X
//
//			    X   X
//			   O  O  
//			  X   O   X
// 			then then array would be:
// 				{{{0,1,2},{1,2,0},{2,2,1}},{{1,1,0},{2,0,2},{1,2,1}},{{1,0,1},{2,2,0},{1,2,1}}}
//
//		input (or any variable you use for the square to be played - eg. return value of computerTurn):
//			This is a number in the range 0 to (dim * dim)-1.
//			So for a traditional 3x3 board, this would be 0-8, numbered as follows:
//				0  1  2
//			   3   4   5
//			  6    7    8
//
//			    9 10 11
//			   12 13  14
//			  15  16   17
//
//			   18 19 20
//			  21  22  23
//			 24   25   26

public class TicTacToe3D {
	private static final int dim = 3;
	private static int[][][] board = new int[dim][dim][dim];
	
	public static void main(String args[]) {
		
		String rawInput;
		int input;
		final int maxVal = (dim * dim * dim) - 1;
		Scanner sc = new Scanner(System.in);
		
		// main game loop
		while (true) {
			printBoard();

			// get input and check it ------------------------
			System.out.println("Enter the next \"X\" location (0 - " + maxVal + "): ");
			rawInput = sc.nextLine();	
			input = Integer.parseInt(rawInput);
			if ( !checkInput(input) )
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
			
			if ( isWin(0) )
			{
				System.out.println("Congratulations! You win!");
				break;
			}

			makeMove(computerTurn(), 2);
			
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
		int location[] = new int[ 3 ];
		boxNumToArray(square, location);
		board[ location[ 0 ] ][ location[ 1 ] ][ location[ 2 ] ] = player;
	}
    
	//-----------------------------------------------------------
	// This function checks if the player given by 'player' input
	// won the game.
	//
	// Input: 
	//		board  - gameboard array. see board variable in main.
	//		player - the player to check for a win:
	//					0 - user
	//					1 - computer
	//
	// Returns:
	//		True  - 'player' won
	//		False - 'player' didn't win
	//-----------------------------------------------------------
    public static boolean isWin(int player)
    {
    	player++;
    	
    	// for each board check row, column, and diagonals
    	for (int i=0; i<dim; i++)
    	{
			// check horizontals
			for (int j=0; j<dim; j++)
			{
				if (board[ i ][ j ][ 0 ] == player)
				{
					for (int k=1; k<dim; k++)
					{
						if ( board[ i ][ j ][ k ] != player )
							break;
						
						if ( k == ( dim - 1 ) )
							return true;
					}
				}
			}
		
			// check verticals
			for (int k=0; k<dim; k++)
			{
				if (board[ i ][ 0 ][ k ] == player)
				{
					for (int j=1; j<dim; j++)
					{
						if ( board[ i ][ j ][ k ] != player )
							break;
						
						if ( j == ( dim - 1 ) )
							return true;
					}
				}
			}
		
			// check diagonals
			for (int j=0; j<dim; j++)
			{
				if ( board[ i ][ j ][ j ] != player )
					break;
			
				if ( j == ( dim - 1 ) )
					return true;
			}
		
			for (int j=0; j<dim; j++)
			{
				if ( board[ i ][ dim - 1 -j ][ j ] != player )
					break;
			
				if ( j == ( dim - 1 ) )
					return true;
			}
		}
		
		// then check the pillars
		for (int j=0; j<dim; j++)
		{
			for (int k=0; k<dim; k++)
			{
				if ( board[ 0 ][ j ][ k ] == player )
				{
					for (int i=1; i<dim; i++)
					{
						if ( board[ i ][ j ][ k ] != player )
							break;
						
						if ( i == ( dim - 1 ) )
							return true;
					}
				}
			}
		}

    	return false;
    }
    
	//-----------------------------------------------------------
	// This is a helper function for isTie() to record the board
	// row/column/diagonals/pillars that have certain values in
	// them.
	//-----------------------------------------------------------
	public static void setTakenValue(boolean[][][] closedBoards, boolean[][] closedPillars, int i, int j, int k, int player)
	{	
		closedBoards[ i ][ j ][ player ] = true;
		closedBoards[ i ][ k + dim ][ player ] = true;
		closedPillars[ ( j * dim ) + k ][ player ] = true;
		if ( j == k )
			closedBoards[i][ dim * 2 ][ player ] = true;
		if ( ( j + k ) == ( dim - 1) )
			closedBoards[i][ ( dim * 2 ) + 1 ][ player ] = true;
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
    	boolean[][][] closedBoards = new boolean[ dim ][ ( dim * 2 ) + 2 ][ 2 ];
    	boolean[][] closedPillars = new boolean[ ( dim * dim ) ][ 2 ];
    	
    	for (int i=0; i<dim; i++)
    	{
			for (int j=0; j<dim; j++)
			{
				for (int k=0; k<dim; k++)
				{
					switch (board[i][j][k]) {
						case 1:
							setTakenValue(closedBoards, closedPillars, i, j, k, 0);
							break;
						case 2:
							setTakenValue(closedBoards, closedPillars, i, j, k, 1);
							break;
						default:
							break;
					}
				}
			}
    	}
    	
    	// check each board for a tie
    	for (int i=0; i<dim; i++)
    	{
			for (int j=0; j<(( dim * 2 ) + 2); j++)
			{
				if ( ( !closedBoards[ i ][ j ][ 0 ] ) || ( !closedBoards[ i ][ j ][ 1 ] ) )
					return false;
			}
		}
    
    	// check the pillars for a tie
		for (int i=0; i<( dim * dim ); i++)
		{
			if ( ( !closedPillars[ i ][ 0 ] ) || ( !closedPillars[ i ][ 1 ] ) )
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
	// Input: 
	//		board - gameboard array. see board variable in main.
	//
	// Returns:
	//		 1 - computer won
	//		 0 - computer didn't win
	//		-1 - tie game
	//-----------------------------------------------------------
	public static int computerTurn()
	{
		int[] coord = {0, 0, 0};
	
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
	//		coord - gameboard coordinate to play in.
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
		boolean[][] closedBoards = new boolean[ dim ][ ( dim * 2 ) + 2 ];		// defaults to false
		boolean[] closedPillars = new boolean[ dim * dim ];					// defaults to false
		int count = 0;											// count of empty spots in a given row/col/diagonal
		int min = dim;											// the current lowest number of free spaces
		int[][] tmpBestSpots = new int[ ( dim * dim * dim ) ][3];
		int[][] bestSpots = new int[ ( dim * dim * dim ) ][3];
		int bestSpotsLength = 0;
		int[][] availableSpots = new int[ ( dim * dim * dim ) ][3];
		int availableSpotsLength = 0;
		
		for (int i=0; i<dim; i++)
		{
			for (int j=0; j<dim; j++)
			{
				for (int k=0; k<dim; k++)
				{
					if ( board[ i ][ j ][ k ] == 2 )
					{
						// first see how close we can get in a vertical
						for (int l=0; l<dim; l++)
						{
							if ( l == j )
								continue;
						
							if ( ( board[ i ][ l ][ k ] == 1 ) || ( closedBoards[ i ][ dim + k ] ) )
							{
								count = 0;
								break;
							}
							else if ( board[ i ][ l ][ k ] == 0 )
							{
								tmpBestSpots[ count ][ 0 ] = i;
								tmpBestSpots[ count ][ 1 ] = l;
								tmpBestSpots[ count ][ 2 ] = k;
								count++;
							}
						}
						if ( ( count < min ) && ( count > 0 ) )
						{
							min = count;
							bestSpotsLength = count;
							for (int l=0; l<count; l++)
							{
								bestSpots[ l ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ l ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ l ][ 2 ] = tmpBestSpots[ l ][ 2 ];
							}
							closedBoards[ i ][ dim + k ] = true;
						}
						else if ( count == min )
						{
							for (int l=0; l<count; l++)
							{
								bestSpots[ bestSpotsLength ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ bestSpotsLength ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ bestSpotsLength ][ 2 ] = tmpBestSpots[ l ][ 2 ];
								bestSpotsLength++;
							}
							closedBoards[ i ][ dim + k ] = true;
						}
					
						// then check how close we can get in a horizontal
						count = 0;
						for (int l=0; l<dim; l++)
						{
							if ( l == k )
								continue;
							
							if ( ( board[ i ][ j ][ l ] == 1 ) || ( closedBoards[ i ][ j ] ) )
							{
								count = 0;
								break;
							}
							else if ( board[ i ][ j ][ l ] == 0 )
							{
								tmpBestSpots[ count ][ 0 ] = i;
								tmpBestSpots[ count ][ 1 ] = j;
								tmpBestSpots[ count ][ 2 ] = l;
								count++;
							}
						}
						if ( ( count < min ) && ( count > 0 ) )
						{
							min = count;
							bestSpotsLength = count;
							for (int l=0; l<count; l++)
							{
								bestSpots[ l ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ l ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ l ][ 2 ] = tmpBestSpots[ l ][ 2 ];
							}
							closedBoards[ i ][ j ] = true;
						}
						else if ( count == min )
						{
							for (int l=0; l<count; l++)
							{
								bestSpots[ bestSpotsLength ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ bestSpotsLength ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ bestSpotsLength ][ 2 ] = tmpBestSpots[ l ][ 2 ];
								bestSpotsLength++;
							}
							closedBoards[ i ][ j ] = true;
						}
					
						// then check how close we can get in a diagonal
						// upper-left to bottom-right diagonal
						count = 0;
						if ( j == k )
						{
							for (int l=0; l<dim; l++)
							{
								if ( l == j )
									continue;
								
								if ( ( board[ i ][ l ][ l ] == 1 ) || ( closedBoards[ i ][ ( dim * 2 ) ] ) )
								{
									count = 0;
									break;
								}
								else if ( board[ i ][ l ][ l ] == 0 )
								{
									tmpBestSpots[ count ][ 0 ] = i;
									tmpBestSpots[ count ][ 1 ] = l;
									tmpBestSpots[ count ][ 2 ] = l;
									count++;
								}
							}
						}
						if ( ( count < min ) && ( count > 0 ) )
						{
							min = count;
							bestSpotsLength = count;
							for (int l=0; l<count; l++)
							{
								bestSpots[ l ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ l ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ l ][ 2 ] = tmpBestSpots[ l ][ 2 ];
							}
							closedBoards[ i ][ ( dim * 2 ) ] = true;
						}
						else if ( count == min )
						{
							for (int l=0; l<count; l++)
							{
								bestSpots[ bestSpotsLength ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ bestSpotsLength ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ bestSpotsLength ][ 2 ] = tmpBestSpots[ l ][ 2 ];
								bestSpotsLength++;
							}
							closedBoards[ i ][ ( dim * 2 ) ] = true;
						}
	
						// bottom-left to upper-right diagonal
						count = 0;
						if ( ( j + k ) == ( dim - 1) )
						{
							for (int l=0; l<dim; l++)
							{
								if ( l == k )
									continue;
								
								if ( ( board[ i ][ dim - 1 - l ][ l ] == 1 ) || ( closedBoards[ i ][ ( dim * 2 ) + 1 ] ) )
								{
									count = 0;
									break;
								}
								else if ( board[ i ][ dim - 1 - l ][ l ] == 0 )
								{
									tmpBestSpots[ count ][ 0 ] = i;
									tmpBestSpots[ count ][ 1 ] = dim - 1 - l;
									tmpBestSpots[ count ][ 2 ] = l;
									count++;
								}
							}
						}
						if ( ( count < min ) && ( count > 0 ) )
						{
							min = count;
							bestSpotsLength = count;
							for (int l=0; l<count; l++)
							{
								bestSpots[ l ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ l ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ l ][ 2 ] = tmpBestSpots[ l ][ 2 ];
							}
							closedBoards[ i ][ ( dim * 2 ) + 1 ] = true;
						}
						else if ( count == min )
						{
							for (int l=0; l<count; l++)
							{
								bestSpots[ bestSpotsLength ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ bestSpotsLength ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ bestSpotsLength ][ 2 ] = tmpBestSpots[ l ][ 2 ];
								bestSpotsLength++;
							}
							closedBoards[ i ][ ( dim * 2 ) + 1 ] = true;
						}
						
						// check the pillars
						for (int l=0; l<dim; l++)
						{
							if ( l == i )
								continue;
						
							if ( ( board[ l ][ j ][ k ] == 1 ) || ( closedPillars[ ( dim * j ) + k ] ) )
							{
								count = 0;
								break;
							}
							else if ( board[ l ][ j ][ k ] == 0 )
							{
								tmpBestSpots[ count ][ 0 ] = l;
								tmpBestSpots[ count ][ 1 ] = j;
								tmpBestSpots[ count ][ 2 ] = k;
								count++;
							}
						}
						if ( ( count < min ) && ( count > 0 ) )
						{
							min = count;
							bestSpotsLength = count;
							for (int l=0; l<count; l++)
							{
								bestSpots[ l ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ l ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ l ][ 2 ] = tmpBestSpots[ l ][ 2 ];
							}
							closedPillars[ ( dim * j ) + k ] = true;
						}
						else if ( count == min )
						{
							for (int l=0; l<count; l++)
							{
								bestSpots[ bestSpotsLength ][ 0 ] = tmpBestSpots[ l ][ 0 ];
								bestSpots[ bestSpotsLength ][ 1 ] = tmpBestSpots[ l ][ 1 ];
								bestSpots[ bestSpotsLength ][ 2 ] = tmpBestSpots[ l ][ 2 ];
								bestSpotsLength++;
							}
							closedPillars[ ( dim * j ) + k ] = true;
						}
						
					}
					else if ( board[ i ][ j ][ k ] == 0 )
					{
						availableSpots[ availableSpotsLength ][ 0 ] = i;
						availableSpots[ availableSpotsLength ][ 1 ] = j;
						availableSpots[ availableSpotsLength ][ 2 ] = k;
						availableSpotsLength++;
					}
				}
			}
    	}
    	
    	Random rand = new Random();
    	int randomNum;
    	if ( bestSpotsLength == 0 )
    	{
    		randomNum = rand.nextInt( availableSpotsLength );		// this gets a random number between 0 and availableSpotsLength
    		coord[ 0 ] = availableSpots[ randomNum ][ 0 ];
    		coord[ 1 ] = availableSpots[ randomNum ][ 1 ];
    		coord[ 2 ] = availableSpots[ randomNum ][ 2 ];
    	}
    	else
    	{
			randomNum = rand.nextInt(bestSpotsLength);			// this gets a random number between 0 and bestSpotsLength
			coord[ 0 ] = availableSpots[ randomNum ][ 0 ];
    		coord[ 1 ] = availableSpots[ randomNum ][ 1 ];
    		coord[ 2 ] = availableSpots[ randomNum ][ 2 ];
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
	//-----------------------------------------------------------
	public static boolean isOneAway(int[] coord, int player)
	{
		int opponent;
		
		if ( player == 1 )
			opponent = 2;
		else
			opponent = 1;
		
		boolean canWin = false;
		
		// first check each board
		for (int i=0; i<dim; i++)
		{
			for (int j=0; j<dim; j++)
			{
				for (int k=0; k<dim; k++)
				{
					if ( board[ i ][ j ][ k ] == player )
					{
						// check the verticals
						for (int l=0; l<dim; l++)
						{
							if ( l == j )
								continue;
						
							if ( board[ i ][ l ][ k ] == opponent )
							{
								canWin = false;
								break;
							}
							else if ( board[ i ][ l ][ k ] == 0 )
							{
								if ( canWin )
								{
									canWin = false;
									break;
								}
								canWin = true;
								coord[ 0 ] = i;
								coord[ 1 ] = l;
								coord[ 2 ] = k;
							}
						}
						if ( canWin )
							break;
					
						// check the horizontals
						for (int l=0; l<dim; l++)
						{
							if ( l == k )
								continue;
							
							if ( board[ i ][ j ][ l ] == opponent )
							{
								canWin = false;
								break;
							}
							else if ( board[ i ][ j ][ l ] == 0 )
							{
								if ( canWin )
								{
									canWin = false;
									break;
								}
								canWin = true;
								coord[ 0 ] = i;
								coord[ 1 ] = j;
								coord[ 2 ] = l;
							}
						}
						if ( canWin )
							break;
					
						// if the square is on a diagonal, check it
						if ( j == k )
						{
							// upper-left to bottom-right diagonal
							for (int l=0; l<dim; l++)
							{
								if ( l == j )
									continue;
								
								if ( board[ i ][ l ][ l ] == opponent )
								{
									canWin = false;
									break;
								}
								else if ( board[ i ][ l ][ l ] == 0 )
								{
									if ( canWin )
									{
										canWin = false;
										break;
									}
									canWin = true;
									coord[ 0 ] = i;
									coord[ 1 ] = l;
									coord[ 2 ] = l;
								}
							}
						}
						if ( canWin )
							break;
	
						// bottom-left to upper-right diagonal
						if ( ( j + k ) == ( dim - 1) )
						{
							for (int l=0; l<dim; l++)
							{
								if ( l == k )
								{
									continue;
								}
								
								if ( board[ i ][ dim - 1 - l ][ l ] == opponent )
								{
									canWin = false;
									break;
								}
								else if ( board[ i ][ dim - 1 - l ][ l ] == 0 )
								{
									if ( canWin )
									{
										canWin = false;
										break;
									}
									canWin = true;
									coord[ 0 ] = i;
									coord[ 1 ] = dim - 1 - l;
									coord[ 2 ] = l;
								}
							}
						}
					}
				}
				if ( canWin )
					break;
			}
			if ( canWin )
					break;
		}
		
		// then check the pillars
		if ( !canWin )
		{
			for (int j=0; j<dim; j++)
			{
				for (int k=0; k<dim; k++)
				{
					if ( board[ 0 ][ j ][ k ] == player )
					{
						for (int i=1; i<dim; i++)
						{
							if ( board[ i ][ j ][ k ] == opponent )
							{
								canWin = false;
								break;
							}
							if ( board[ i ][ j ][ k ] == 0 )
							{
								if ( canWin )
								{
									canWin = false;
									break;
								}
								canWin = true;
								coord[ 0 ] = i;
								coord[ 1 ] = j;
								coord[ 2 ] = k;
							}
						}
					}
				}
			}
		}
				
		if ( canWin )
			board[ coord[ 0 ] ][ coord[ 1 ] ][ coord[ 2 ] ] = 2;
			
    	return canWin;
	}

	//-----------------------------------------------------------
	// This function checks if the space is available to play
	//
	// Input: 
	//		input - input array where:
	//					input[0] = row
	//					input[1] = col
	//
	// Returns:
	//		True  - space can be played
	//		False - space is taken and cannot be played
	//
	//-----------------------------------------------------------
	public static boolean isOpen(int input)
	{
		int[] location = new int[ 3 ];
		boxNumToArray(input, location);
		
		if ( board[ location[0] ][ location[ 1 ] ][ location[ 2 ] ] != 0 )
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
			if ( n < ( dim * dim * i ) )
			{
				location[ 0 ] = ( i - 1 );
				break;
			}
		}
		
		while ( n >= ( dim * dim ) )
			n -= ( dim * dim );
			
		for (int i=1; i<=dim; i++)
		{
			if ( n < ( dim * i ) )
			{
				location[ 1 ] = ( i - 1 );
				break;
			}
		}
		location[ 2 ] = n % dim;
	}
	
	//-----------------------------------------------------------
	// This function converts the array value of a square to the
	// number value for that square.
	//-----------------------------------------------------------
	public static int boxArrayToNum(int[] location)
	{
		return ( ( dim * dim * location[ 0 ] ) + ( dim * location[ 1 ] ) + ( location[ 2 ] ) );
	}

	//-----------------------------------------------------------
	// This function checks the user input.
	//
	// Input:
	//		input	 - user input
	//
	// Returns:
	//		True  - input is okay
	//		False - input is bad
	//-----------------------------------------------------------
	public static boolean checkInput(int input)
	{
		if ( ( input < 0 ) || ( input >= ( dim * dim * dim ) ) )
			return false;
			
		return true;
	}

	//-----------------------------------------------------------
	// This function prints the current game board
	//-----------------------------------------------------------
    public static void printBoard()
    {
    	String display = "";
    	
    	for (int i=0; i<dim; i++)
    	{
			for (int j=0; j<dim; j++)
				display += " _____";
			System.out.println(display);
			display = "";
			
			for (int j=0; j<dim; j++)
			{
				for (int k=0; k<dim; k++)
					display += "|     ";
				display += "|";
				System.out.println(display);
				display = "";
			
				for (int k=0; k<dim; k++)
				{
					switch (board[i][j][k]) {
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
}