package delft;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeGameTest {

    @Test
    void testInitializeBoard() {
        TicTacToeGame game = new TicTacToeGame();
        char[][] board = game.getBoard();

        for (char[] row : board) {
            for (char cell : row) {
                assertEquals(' ', cell, "All cells should be empty after initialization.");
            }
        }
    }

    @Test
    void testMakeValidMove() {
        TicTacToeGame game = new TicTacToeGame();
        boolean moveResult = game.makeMove(0, 0);

        assertTrue(moveResult, "Making a valid move should return true.");
        assertEquals('X', game.getBoard()[0][0], "The cell should be marked with the current player.");
    }

    @Test
    void testMakeInvalidMoveOutOfBounds() {
        TicTacToeGame game = new TicTacToeGame();
        boolean moveResult = game.makeMove(-1, 0);

        assertFalse(moveResult, "Making a move out of bounds should return false.");
    }

    @Test
    void testMakeInvalidMoveOnOccupiedCell() {
        TicTacToeGame game = new TicTacToeGame();
        game.makeMove(0, 0);

        boolean moveResult = game.makeMove(0, 0);
        assertFalse(moveResult, "Making a move on an occupied cell should return false.");
    }

    @Test
    void testSwitchPlayer() {
        TicTacToeGame game = new TicTacToeGame();
        char currentPlayer = game.getCurrentPlayer();

        game.switchPlayer();
        assertNotEquals(currentPlayer, game.getCurrentPlayer(), "The current player should switch after calling switchPlayer.");
    }

    @Test
    void testIsValidMove() {
        TicTacToeGame game = new TicTacToeGame();
        assertTrue(game.isValidMove(0, 0), "An empty cell should be a valid move.");
        game.makeMove(0, 0);
        assertFalse(game.isValidMove(0, 0), "An occupied cell should not be a valid move.");
    }

    @Test
    void testResetGame() {
        TicTacToeGame game = new TicTacToeGame();
        game.makeMove(0, 0);
        game.switchPlayer();
        game.resetGame();

        char[][] board = game.getBoard();
        for (char[] row : board) {
            for (char cell : row) {
                assertEquals(' ', cell, "All cells should be empty after resetting the game.");
            }
        }
        assertEquals('X', game.getCurrentPlayer(), "The current player should reset to 'X'.");
    }

    @Test
    void testIsWinnerRows() {
        TicTacToeGame game = new TicTacToeGame();
        game.makeMove(0, 0);
        game.makeMove(1, 0);
        game.makeMove(0, 1);
        game.makeMove(1, 1);
        game.makeMove(0, 2);

        assertTrue(game.isWinner('X'), "Player 'X' should be the winner with a complete row.");
    }

    @Test
    void testIsWinnerColumns() {
        TicTacToeGame game = new TicTacToeGame();
        game.makeMove(0, 0);
        game.makeMove(0, 1);
        game.makeMove(1, 0);
        game.makeMove(1, 1);
        game.makeMove(2, 0);

        assertTrue(game.isWinner('X'), "Player 'X' should be the winner with a complete column.");
    }

    @Test
    void testIsWinnerDiagonals() {
        TicTacToeGame game = new TicTacToeGame();
        game.makeMove(0, 0);
        game.makeMove(0, 1);
        game.makeMove(1, 1);
        game.makeMove(1, 2);
        game.makeMove(2, 2);

        assertTrue(game.isWinner('X'), "Player 'X' should be the winner with a complete diagonal.");
    }

    @Test
    void testIsBoardFull() {
        TicTacToeGame game = new TicTacToeGame();
        char[][] moves = {
                {'X', 'O', 'X'},
                {'O', 'X', 'O'},
                {'X', 'O', 'X'}
        };
        fillBoard(game, moves);

        assertTrue(game.isBoardFull(), "The board should be full.");
    }

    @Test
    void testIsBoardNotFull() {
        TicTacToeGame game = new TicTacToeGame();
        game.makeMove(0, 0);

        assertFalse(game.isBoardFull(), "The board should not be full if there are empty spaces.");
    }

    @Test
    void testGetBestMove() {
        TicTacToeGame game = new TicTacToeGame();
        game.switchPlayer(); // Set to 'O'
        game.makeMove(0, 0); // X
        game.makeMove(1, 1); // O
        game.makeMove(0, 1); // X

        int[] bestMove = game.getBestMove();
        assertArrayEquals(new int[]{0, 2}, bestMove, "AI should block the row.");
    }

    @Test
    void testAIThrowsExceptionForInvalidPlayer() {
        TicTacToeGame game = new TicTacToeGame();

        Exception exception = assertThrows(IllegalStateException.class, game::getBestMove);
        assertEquals("AI can only make moves for player 'O'.", exception.getMessage());
    }

    @Test
    void testMinimaxWinningMove() {
        TicTacToeGame game = new TicTacToeGame();
        char[][] moves = {
                {'O', 'X', ' '},
                {'X', 'O', ' '},
                {' ', ' ', 'X'}
        };
        fillBoard(game, moves);
        game.switchPlayer(); // Set to 'O'

        int bestScore = game.getBestMove()[0]; // Simulate AI move
        assertEquals(0, bestScore, "Minimax should find the best winning move.");
    }

    @Test
    void testGetBestMoveForDraw() {
        TicTacToeGame game = new TicTacToeGame();
        char[][] moves = {
                {'X', 'O', 'X'},
                {'X', 'O', 'O'},
                {'O', 'X', ' '}
        };
        fillBoard(game, moves);
        game.switchPlayer(); // Set to 'O'

        int[] bestMove = game.getBestMove();
        assertArrayEquals(new int[]{2, 2}, bestMove, "AI should play the only available move to avoid a loss.");
    }


    @Test
    void testResetAfterWin() {
        TicTacToeGame game = new TicTacToeGame();
        game.makeMove(0, 0);
        game.makeMove(1, 1);
        game.makeMove(0, 1);
        game.makeMove(2, 2);
        game.makeMove(0, 2); // X wins

        assertTrue(game.isWinner('X'), "Player X should win.");
        game.resetGame();

        for (char[] row : game.getBoard()) {
            for (char cell : row) {
                assertEquals(' ', cell, "Board should reset.");
            }
        }
        assertEquals('X', game.getCurrentPlayer(), "Current player should reset to X.");
    }



    @Test
    void testGetBestMoveNoAvailableMoves() {
        TicTacToeGame game = new TicTacToeGame();
        char[][] moves = {
                {'X', 'O', 'X'},
                {'X', 'O', 'O'},
                {'O', 'X', 'X'}
        };
        fillBoard(game, moves);

        Exception exception = assertThrows(IllegalStateException.class, game::getBestMove);
        assertEquals("AI can only make moves for player 'O'.", exception.getMessage());
    }



    @Test
    void testMinimaxTieScenario() {
        TicTacToeGame game = new TicTacToeGame();
        char[][] moves = {
                {'X', 'O', 'X'},
                {'X', 'O', 'O'},
                {'O', 'X', ' '}
        };
        fillBoard(game, moves);
        game.switchPlayer(); // Set to 'O'

        int[] bestMove = game.getBestMove();
        assertArrayEquals(new int[]{2, 2}, bestMove, "AI should choose the move that leads to a tie.");
    }



    private void fillBoard(TicTacToeGame game, char[][] moves) {
        for (int i = 0; i < moves.length; i++) {
            for (int j = 0; j < moves[i].length; j++) {
                if (moves[i][j] != ' ') {
                    game.makeMove(i, j);
                    if (game.getCurrentPlayer() != moves[i][j]) {
                        game.switchPlayer();
                    }
                }
            }
        }
    }
}
