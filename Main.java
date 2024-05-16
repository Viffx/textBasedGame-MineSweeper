// This is a text based mine sweeper game
// Requires
	// In.java	 (Has several input methods)
	// Out.java	 (Has several printing methods)
	// Validate.java (Has input valiation methods)
public class Main {
	public static void main(String args[]) {
		// prompt the user for if they want to have detailed prompting for moves
		boolean playerFriendly = Validate.input("Player friendly mode?");

		int dem = 10; // set the demention of the game board
		int mines = (int)(dem*dem * 0.2); // 20 percent of the board will be mines

		// declare variables
		int[][] offsets = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,0},{0,1},{1,-1},{1,0},{1,1}}; // Stores offsets for checking around a cell in an array
		int[][] board = new int[dem][dem]; // stores the board state that the player will see
		int[][] hidden = new int[dem][dem]; // stores the completed board state for game logic
		
		
		// initialise hidden board with mines
		for (int i = mines; i > 0; i--) {
			int x = (int)(Math.random() * dem);
			int y = (int)(Math.random() * dem);
			do {
				x = (int)(Math.random() * dem);
				y = (int)(Math.random() * dem);
			} while (hidden[x][y] == 9);
			hidden[x][y] = 9; // 9 is the index for the 💥 symbol
		}
		
		// initilise the hidden board with the completed solution
		for (int i = 0; i < dem; i++) {
			for (int j = 0; j < dem; j++) {
				if(hidden[i][j] == 9) { // skip the index if its a mine
					continue;
				}
				int count = 0; // its not a mine so count the number of non mine squares around the index
				for (int k = 0; k < 9; k++) { // loop through offsets to check the idexes of the squares around the index
					if (!(i + offsets[k][0] < 0 ||i + offsets[k][0] > dem - 1||j + offsets[k][1] < 0 ||j + offsets[k][1] > dem - 1)) {
						if (hidden[i + offsets[k][0]][j + offsets[k][1]] == 9) {
							count++;
							if (count > 8) { // cap the max value of bomb squares to 8
								count = 8;
							}
						}
					}
				}
				if (count == 0) { 
					hidden[i][j] = 11; // set the index to the solved tile ⬜
					continue;
				}
				hidden[i][j] = count; // set the index to the number of surounding bombs
			}
		}
		
		// get the starting tile
		int startIndx[] = new int[2];
		boolean selected = false;
		while (!selected) { // loop until you find a random ⬜ square
			int x = (int)(Math.random() * dem);
			int y = (int)(Math.random() * dem);
			int actual = 0; // 11 : ⬜ is the same as zero in code
			if (hidden[x][y] != 11) {
				actual = hidden[x][y];
			}
			if (actual == 0) {
				startIndx[0] = x;
				startIndx[1] = y;
				selected = true;
			}
		}
		
		// add a starting pattern
		board[startIndx[0]][startIndx[1]] = 11;
		board = revealBlock(dem,board,hidden,offsets); // revealBlock is a path finding algorythm that shows all the conecting white squares and a border of 1

		// start game
		boolean lost = false;
		boolean gameover = false;
		while(!gameover) {
			// clear screen and print board
			Out.clear();
			printBoard(board);

			// user input and input validation
			String input = "C";
			String[] valid = {"","f","flag","c","change tile"};
			int[] move = new int[2];
			while (input.equalsIgnoreCase("C")) {
				move = getMove(board); // prompy user, validate the move, return the move
				if (board[move[0]][move[1]] != 10) {
					if (playerFriendly) {
						// prompt the user with detailed input
						input = Validate.input("enter to reveal\nf or flag to flag\nc or change tile to enter select mode\ninput:",valid);
					} else {
						// prompt the user for input
						input = Validate.input("input:",valid);
					}
				} else {
					// the move selcted was a flah so simulate a input of F
					input = "F";
				}
			}

			// game logic
			// handle adding mines / flags / blanks to the board
			// set gameover to true if the user input is a mine
			if (input.equals("")) { // reveal a tile
				board[move[0]][move[1]] = hidden[move[0]][move[1]];
				if (hidden[move[0]][move[1]] == 9) { // it it was a bomb the player lost
					gameover = true;
					Out.clear("You lose");
					lost = true;
				} else if (hidden[move[0]][move[1]] == 11) { // if it was a blank tile reveal the whole block and its peremeter
					board = revealBlock(dem,board,hidden,offsets);
				}
			} else if (input.equalsIgnoreCase("F")) { // toggle the flag on the tile
				if (board[move[0]][move[1]] == 10) {
					board[move[0]][move[1]] = 0;
				} else {
					board[move[0]][move[1]] = 10;
				}
			}

			// detect a completed board (win condition)
			boolean completed = true;
			for (int i = 0; i < dem; i++) {
				for (int j = 0; j < dem; j++) {
					if (board[i][j] != 0) {
						completed = false;
						break;
					}
				}
				if (!completed) {
					break;
				}
			}
			if (completed) {
				break;
			}
		}

		// if they lost do a mines blowing up animation
		if (lost) {
			mines = (int)(dem*dem * 0.2);
			do {
				int x = (int)(Math.random() * dem);
				int y = (int)(Math.random() * dem);
				if ((hidden[x][y] == 9 && board[x][y] != 9) || board[x][y] == 10) {
					board[x][y] = 9;
					Out.clear("You lose");
					printBoard(board);
					utils.sleep(70);
					mines--;
				}
				if (mines <= 0) {
					break;
				}
			} while (mines > 0);
		}

		Out.clear();
		Out.println("You win");
		printBoard(board);
		
		// prompt user
		In.getString("play again? any key to continue");

		// restart
		main(args);
	}

	// reveal all the conecting white blocks 11: ⬜ and a peremiter
	public static int[][] revealBlock (int dem, int[][] board, int[][] hidden, int offsets[][]) {
		boolean completed = false;
		while (!completed) {
			completed = true;
			// loop through the array
			for (int i = 0; i < dem; i++) {
				for (int j = 0; j < dem; j++) {
					// the only tiles to be considerd in hidden is the 11 tiles skip all other cases
					if (hidden[i][j] != 11) {
						continue;
					}
					// only consider 11 tiles on board for path finding algorythim
					if (board[i][j] == 11) {
						int[] temp = new int[8];
						// loop through offsets
						for (int k = 0; k < 9; k++) {
							// check if the offset would be in bounds
							if (!(i + offsets[k][0] < 0 ||i + offsets[k][0] > dem - 1||j + offsets[k][1] < 0 ||j + offsets[k][1] > dem - 1)) {
								// if the tile at the index of the offset position is empty then
								if (board[i + offsets[k][0]][j + offsets[k][1]] == 0) {
									// reveal the tile
									board[i + offsets[k][0]][j + offsets[k][1]] = hidden[i + offsets[k][0]][j + offsets[k][1]];
									// tell the program that it needs to do another path finding run
									completed = false;
								}
							}
						}
					}
				}
			}
		}
		return board;
	}

	// get the move from the player
	public static int[] getMove(int[][] board) {
		// declare variable
		String input = "";
		String[] valid = getValidInputArray(board);

		// user input, input validation
		int move[] = getRowCol(board,valid);
		if (board[move[0]][move[1]] != 0) {
			do {
				if (board[move[0]][move[1]] == 10) {
					break;
				}
				move = getRowCol(board,valid);
			} while (board[move[0]][move[1]] != 0);
		}
		
		// output
		return move;
	}

	// get the row and collom of the move
	public static int[] getRowCol(int board[][],String[] valid) {
		// get the index of the move in the valid array
		int indx = locate.indxInArray(Validate.input("Which tile do you want to select?",valid),valid);
		/* the first perameter of locate.indxInArray is the search item, the second is the array to search
  			int indx = locate.indxInArray(
	 			Validate.input("Which tile do you want to select?",valid), // the user is prompted for the search item
	 			valid // this is the array to search
	 		);
  		*/
		
		// get the row and collom from the index
		int row = indx/board.length;
		int col = indx%board.length;
		if (row >= board.length) {
			row = board.length -1;
		}
		if (col >= board.length) {
			col = board.length - 1;
		}
		return new int[] {row,col};
	}

	// print the board
	public static void printBoard(int board[][]) {
		int leftGap = String.valueOf(board.length).length() + 1;
		String Symbols[] = {"☐ ","1️⃣ ","2️⃣ ","3️⃣ ","4️⃣ ","5️⃣ ","6️⃣ ","7️⃣ ","8️⃣ ","💥","🚩","⬜"};
		int rowNum = 1;

		// print the letter row at the top
		Out.print(repeatString(" ",leftGap));
		for (int i = 1; i < board.length + 1; i++) {
			String indx = convertToBase26(i) + " ";
			Out.print(indx);
		}
		Out.ln();

		// print the rows
		for (int[] array : board) {
			Out.print(repeatString(" ",leftGap - 1 - String.valueOf(rowNum).length()) + rowNum + " "); // right allign the number label
			rowNum++;
			int j = 1;
			for (int i : array) {
				int offset = convertToBase26(j).length()-1; // get the number label
				j++;
				Out.println(Symbols[i] + repeatString(" ",offset)); // print the row
			}
		}
	}
	public static String convertToBase26(int decimalNumber) {
		StringBuilder base26 = new StringBuilder();
		
		while (decimalNumber > 0) {
			int remainder = (decimalNumber - 1) % 26; // -1 to map 1 to A, 2 to B, ..., 26 to Z
			char digit = (char) ('A' + remainder);
			base26.insert(0, digit); // Inserting each character at the beginning of the string
			decimalNumber = (decimalNumber - remainder) / 26;
		}
		
		return base26.toString();
	    }
	// return a sting of str repeated count times
	public static String repeatString(String str, int count) {
		if (count == 0) {
			return "";
		}
		StringBuilder repeatedString = new StringBuilder();
		for (int i = 0; i < count; i++) {
			repeatedString.append(str);
		}
		return repeatedString.toString();
	}
	// generate all the valid moves
	public static String[] getValidInputArray(int[][] board) {
		String[][] proccessing = new String[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				proccessing[j][i] = convertToBase26(i + 1) + String.valueOf(j + 1);
			}
		}
		String[] valid = new String[board.length*board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				int indx = i * board.length + j;
				valid[indx] = proccessing[i][j];
			}
		}
		return valid;
	}
}
