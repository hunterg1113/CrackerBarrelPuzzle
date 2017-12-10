package com.arkansascodingacademy;

class PuzzleSolver
{
    Board solution = new Board();

    PuzzleSolver()
    {
    }

    void solve()
    {
        Board board = new Board();
        board.removeRandomPeg();

        while (board.moveExists())
        {
            board.makeMove();
        }

        if (board.countPegsRemaining() > 1)
        {
            solve();
        }
        else
        {
            solution = board;
        }
    }

    public Board getSolution()
    {
        return solution;
    }
}

