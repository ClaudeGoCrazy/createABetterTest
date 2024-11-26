package delft;

public class TicTacToeGame {
	private char[][] board;
	private char currentPlayer;
	private static final int SIZE = 3;

	public TicTacToeGame() {
		board = new char[SIZE][SIZE];
		currentPlayer = 'X';
		initializeBoard();
	}

	// Initialize the game board with empty spaces
	private void initializeBoard() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				board[i][j] = ' ';
			}
		}
	}

	// Reset the game for a new round
	public void resetGame() {
		initializeBoard();
		currentPlayer = 'X';
	}

	// Get the current state of the board
	public char[][] getBoard() {
		char[][] copy = new char[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			System.arraycopy(board[i], 0, copy[i], 0, SIZE);
		}
		return copy;
	}

	// Get the current player
	public char getCurrentPlayer() {
		return currentPlayer;
	}

	// Check if a move is valid
	public boolean isValidMove(int row, int col) {
		return row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == ' ';
	}

	// Make a move on the board
	public boolean makeMove(int row, int col) {
		if (isValidMove(row, col)) {
			board[row][col] = currentPlayer;
			return true;
		}
		return false;
	}

	// Switch the current player
	public void switchPlayer() {
		currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
	}

	// Check if the game has a winner
	public boolean isWinner(char player) {
		// Check rows and columns
		for (int i = 0; i < SIZE; i++) {
			if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
					(board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
				return true;
			}
		}
		// Check diagonals
		return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
				(board[0][2] == player && board[1][1] == player && board[2][0] == player);
	}

	// Check if the board is full
	public boolean isBoardFull() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j] == ' ') return false;
			}
		}
		return true;
	}

	// Get the best move for the AI using the Minimax algorithm
	public int[] getBestMove() {
		if (currentPlayer != 'O') {
			throw new IllegalStateException("AI can only make moves for player 'O'.");
		}

		int bestScore = Integer.MIN_VALUE;
		int[] bestMove = new int[2];

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j] == ' ') {
					board[i][j] = currentPlayer;
					int score = minimax(false, 0);
					board[i][j] = ' ';
					if (score > bestScore) {
						bestScore = score;
						bestMove[0] = i;
						bestMove[1] = j;
					}
				}
			}
		}
		return bestMove;
	}

	// Minimax algorithm for AI decision-making
	private int minimax(boolean isMaximizing, int depth) {
		if (isWinner('X')) return -10 + depth;
		if (isWinner('O')) return 10 - depth;
		if (isBoardFull()) return 0;

		int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j] == ' ') {
					board[i][j] = isMaximizing ? 'O' : 'X';
					int score = minimax(!isMaximizing, depth + 1);
					board[i][j] = ' ';
					bestScore = isMaximizing ? Math.max(bestScore, score) : Math.min(bestScore, score);
				}
			}
		}
		return bestScore;
	}

	// Utility method to print the board (for debugging)
	public void printBoard() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				System.out.print(" " + board[i][j]);
				if (j < SIZE - 1) System.out.print(" |");
			}
			System.out.println();
			if (i < SIZE - 1) {
				System.out.println("---+---+---");
			}
		}
	}
}
