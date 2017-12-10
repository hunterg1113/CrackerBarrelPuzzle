package com.arkansascodingacademy;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    private static List<Board> solutions = new ArrayList<>();
    private static Board solutionWithShortestMoves = new Board();

    public static void main(String[] args)
    {
        for (int i = 0; i < 1000; i++)
        {
            PuzzleSolver puzzleSolver = new PuzzleSolver();
            puzzleSolver.solve();
            solutions.add(puzzleSolver.getSolution());
        }

        boolean firstTime = true;
        for(Board solution : solutions)
        {
            if(firstTime)
            {
                solutionWithShortestMoves = solution;
                firstTime = false;
            }
            else
            {
                if(solutionWithShortestMoves.moveCount() > solution.moveCount())
                {
                    solutionWithShortestMoves = solution;
                }
            }
        }

        solutionWithShortestMoves.printBoardAndMoves();
    }
}
