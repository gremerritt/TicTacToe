import java.util.Scanner;
import java.util.Random;

public class TicTacToe3D {
	static Scanner sc = new Scanner(System.in);
	public static final int dim = 3;
	
	public static void main(String args[]) {
		// the board variable is an NxN (given by the 'dim' parameter)
		// multidimensional array of the current game board.
		// 
		// Values: 	0 - Empty space
		//			1 - Users space ("X")
		//			2 - Computers space ("O")
		//
		// For example, if the board was:
		//		e X O
		//		X O e
		//		O O X
		// then then array would be:
		//		{{0,1,2},{1,2,0},{2,2,1}}
		//
		int[][][] board = new int[dim][dim][dim];
		
		String rawInput;
		int[] input = {0, 0, 0};
		int computerTurn;
		boolean quit = false;
		
		// main game loop
		while (true) {
			printBoard(board);

			// get input and check it ------------------------
			System.out.println("Enter the next \"X\" location (<row 1-" + dim + "> <col 1-" + dim + ">): ");
			rawInput = sc.nextLine();	
			if ( !checkInput(rawInput, input) )
				continue;
			// -----------------------------------------------
			
			// check if this space is available
			if ( !isOpen(board, input) )
				continue;
			
			board[input[0]][input[1]][input[2]] = 1;
			
			if ( isTie(board) )
			{
				System.out.println("It's a tie!");
				break;
			}
			
			if ( isWin(board, 0) )
			{
				System.out.println("Congratulations! You win!");
				break;
			}

			computerTurn = computerTurn(board);
			
			switch (computerTurn) {
				case -1:
					System.out.println("It's a tie!");
					quit = true;
					break;
				case 1:
					System.out.println("Uh oh! You lost.");
					quit = true;
					break;
				default:
					break;
			}
			if ( quit )
				break;
		}
		System.out.println("\nFinal board:");
		printBoard(board);
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
    public static boolean isWin(int[][][] board, int player)
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
					for (int i=0; i<dim; i++)
					{
						if ( board[ i ][ j ][ j ] != player )
							break;
						
						if ( i == ( dim - 1 ) )
							return true;
					}
				}
			}
		}

    	return false;
    }
    
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
    public static boolean isTie(int[][][] board)
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
	public static int computerTurn(int[][][] board)
	{
		// first check if computer can win
		if ( isOneAway(board, 2) )
			return 1;
		
		// then check if computer can block user from winning
		if ( !(isOneAway(board, 1)) )
			computerMove(board);
		
		if ( isTie(board) )
			return -1; 
		else
			return 0;
	}
	
	//-----------------------------------------------------------
	// This function makes a move for the computer. It finds the 
	// row/column/diagonal that is the closest to winning and
	// makes a move there.
	//
	// Input: 
	//		board - gameboard array. see board variable in main.
	//-----------------------------------------------------------
	public static void computerMove(int[][][] board)
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
    		board[ availableSpots[ randomNum ][ 0 ] ][ availableSpots[ randomNum ][ 1 ] ][ availableSpots[ randomNum ][ 2 ] ] = 2;
    	}
    	else
    	{
			randomNum = rand.nextInt(bestSpotsLength);			// this gets a random number between 0 and bestSpotsLength
			board[ bestSpots[ randomNum ][ 0 ] ][ bestSpots[ randomNum ][ 1 ] ][ bestSpots[ randomNum ][ 2 ] ] = 2;
		}
	}
	
	//-----------------------------------------------------------
	// This function determines if the computer can either win
	// the game, or block the user from winning the game.
	//
	// Input: 
	//		board - gameboard array. see board variable in main.
	//		player - 1: check if computer can block
	//				 2: check if computer can win
	//
	// Returns:
	//		True  - computer can win/block
	//		False - computer cannot win/block
	//-----------------------------------------------------------
	public static boolean isOneAway(int[][][] board, int player)
	{
		int[] coord = {0, 0, 0};
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
	//		board - gameboard array. see board variable in main.
	//		input - input array where:
	//					input[0] = row
	//					input[1] = col
	//
	// Returns:
	//		True  - space can be played
	//		False - space is taken and cannot be played
	//
	//-----------------------------------------------------------
	public static boolean isOpen(int[][][] board, int[] input)
	{
		if ( board[input[0]][input[1]][input[2]] != 0 )
		{
			System.out.println("Already played!");
			return false;
		}
		return true;
	}

	//-----------------------------------------------------------
	// This function checks the user input.
	//
	// Input:
	//		rawInput - full user input string
	//		input	 - input array where
	//						input[0] = row
	//						input[1] = col
	//
	// Returns:
	//		True  - input is good
	//		False - input is bad
	//
	// If the input is good, the 'input' array will also be set.
	// 		input[0] is the row value 0 - <dim-1>
	// 		input[1] is the column value 0 - <dim-1>
	//
	//-----------------------------------------------------------
	public static boolean checkInput(String rawInput, int[] input)
	{
		String[] split = rawInput.split("\\s+");
	
		if ( split.length != 3  )
		{
			System.out.println("  Incorrect number of inputs!\n");
			return false;
		}
		
		input[0] = (Integer.parseInt(split[0])) - 1;
		input[1] = (Integer.parseInt(split[1])) - 1;
		input[2] = (Integer.parseInt(split[2])) - 1;
		
		if ( (input[0] < 0) || (input[0] > (dim - 1)) || 
			 (input[1] < 0) || (input[1] > (dim - 1)) ||
			 (input[2] < 0) || (input[2] > (dim - 1)) )
		{
			System.out.println("  Bad row or column value!\n");
			return false;
		}
		
		return true;
	}

	//-----------------------------------------------------------
	// This function prints the current game board
	//
	//-----------------------------------------------------------
    public static void printBoard(int[][][] board)
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