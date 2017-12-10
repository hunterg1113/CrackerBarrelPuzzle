package com.arkansascodingacademy;

import java.util.*;

class Board
{
    private final static int ROW_LENGTH = 9;
    private final static int COL_LENGTH = 5;

    private Hole[][] board = new Hole[COL_LENGTH][ROW_LENGTH];

    private List<Board> previousStates = new ArrayList<>();
    private List<List<Integer>> moveToBeMade = new ArrayList<>();

    Board()
    {
        for (int i = 0; i < COL_LENGTH; i++)
        {
            for (int j = 0; j < ROW_LENGTH; j++)
            {
                board[i][j] = new Hole();
                board[i][j].getCoordinates().add(i);
                board[i][j].getCoordinates().add(j);
            }
        }

        setPegs();
    }

    private Board copy()
    {
        Board copy = new Board();

        for (int i = 0; i < COL_LENGTH; i++)
        {
            for (int j = 0; j < ROW_LENGTH; j++)
            {
                copy.getBoard()[i][j].setPegSet(board[i][j].isPegSet());
                copy.getBoard()[i][j].setAvailable(board[i][j].isAvailable());
                copy.getBoard()[i][j].setCoordinates(new ArrayList<>(board[i][j].getCoordinates()));
                copy.getBoard()[i][j].setPossibleMoves(new ArrayList<>(board[i][j].getPossibleMoves()));
            }
        }

        return copy;
    }

    List<Board> getPreviousStates()
    {
        return previousStates;
    }

    List<List<Integer>> getMoveToBeMade()
    {
        return moveToBeMade;
    }

    private void setPegs()
    {
        for (int j = 0; j < ROW_LENGTH; j++)
        {
            if (j == 4)
            {
                board[0][j].setPegSet(true);
                board[0][j].setAvailable(true);
            }
        }
        for (int j = 0; j < ROW_LENGTH; j++)
        {
            if (j == 3 || j == 5)
            {
                board[1][j].setPegSet(true);
                board[1][j].setAvailable(true);
            }
        }
        for (int j = 0; j < ROW_LENGTH; j++)
        {
            if (j == 2 || j == 4 || j == 6)
            {
                board[2][j].setPegSet(true);
                board[2][j].setAvailable(true);
            }
        }
        for (int j = 0; j < ROW_LENGTH; j++)
        {
            if (j == 1 || j == 3 || j == 5 || j == 7)
            {
                board[3][j].setPegSet(true);
                board[3][j].setAvailable(true);
            }
        }
        for (int j = 0; j < ROW_LENGTH; j++)
        {
            if (j == 0 || j == 2 || j == 4 || j == 6 || j == 8)
            {
                board[4][j].setPegSet(true);
                board[4][j].setAvailable(true);
            }
        }

        setPegPossibleMoves();
    }

    private void setPegPossibleMoves()
    {
        for (int i = 0; i < COL_LENGTH; i++)
        {
            for (int j = 0; j < ROW_LENGTH; j++)
            {
                if (board[i][j].inPlay())
                {
                    setPegPossibles(i, j);
                }
            }
        }
    }

    private void setPegPossibles(int i, int j)
    {
        if (i - 2 >= 0 && j - 2 >= 0 && board[i - 2][j - 2].isAvailable())
        {
            board[i][j].getPossibleMoves().add(board[i - 2][j - 2]);
        }
        if (i - 2 >= 0 && j + 2 <= 8 && board[i - 2][j + 2].isAvailable())
        {
            board[i][j].getPossibleMoves().add(board[i - 2][j + 2]);
        }
        if (i + 2 <= 4 && j - 2 >= 0 && board[i + 2][j - 2].isAvailable())
        {
            board[i][j].getPossibleMoves().add(board[i + 2][j - 2]);
        }
        if (i + 2 <= 4 && j + 2 <= 8 && board[i + 2][j + 2].isAvailable())
        {
            board[i][j].getPossibleMoves().add(board[i + 2][j + 2]);
        }
        if (j - 4 >= 0 && board[i][j - 4].isAvailable())
        {
            board[i][j].getPossibleMoves().add(board[i][j - 4]);
        }
        if (j + 4 <= 8 && board[i][j + 4].isAvailable())
        {
            board[i][j].getPossibleMoves().add(board[i][j + 4]);
        }
    }

    void removeCenterPeg()
    {
        board[2][4].setPegSet(false);
    }

    void removeRandomPeg()
    {
        Random random = new Random();
        boolean keepSearching = true;

        while (keepSearching)
        {
            int randomRow = random.nextInt(5);
            int randomCol = random.nextInt(9);
            if (board[randomRow][randomCol].inPlay())
            {
                board[randomRow][randomCol].setPegSet(false);
                keepSearching = false;
            }
        }
        previousStates.add(copy());
    }

    void makeMove()
    {
        Random random = new Random();
        List<Hole> possibleMoves = new ArrayList<>();

        for (int i = 0; i < COL_LENGTH; i++)
        {
            for (int j = 0; j < ROW_LENGTH; j++)
            {
                if (board[i][j].opening())
                {
                    possibleMoves.add(board[i][j]);
                }
            }
        }

        boolean keepSearching = true;
        while (keepSearching)
        {
            int randomMoveSelect = random.nextInt(possibleMoves.size());
            int randomPegSelect = random.nextInt(possibleMoves.get(randomMoveSelect).getPossibleMoves().size());

            Hole selectedMove = possibleMoves.get(randomMoveSelect);
            Hole pegToMove = selectedMove.getPossibleMoves().get(randomPegSelect);
            Hole pegInMiddle = getPegInMiddle(selectedMove, pegToMove);

            if (pegToMove.inPlay() && pegInMiddle.inPlay())
            {

                selectedMove.setPegSet(true);
                pegInMiddle.setPegSet(false);
                pegToMove.setPegSet(false);

                Board currentState = copy();
                currentState.getMoveToBeMade().add(selectedMove.getCoordinates());
                currentState.getMoveToBeMade().add(pegInMiddle.getCoordinates());
                currentState.getMoveToBeMade().add(pegToMove.getCoordinates());
                previousStates.add(currentState);

                keepSearching = false;
            }
        }
    }

    int moveCount()
    {
        return previousStates.size() - 1;
    }

    private Hole getPegInMiddle(Hole selectedMove, Hole pegToMove)
    {
        int rowCoordinateOfPegToRemove;

        if (selectedMove.getCoordinates().get(0) > pegToMove.getCoordinates().get(0))
        {
            rowCoordinateOfPegToRemove = pegToMove.getCoordinates().get(0) + 1;
        }
        else if (selectedMove.getCoordinates().get(0) < pegToMove.getCoordinates().get(0))
        {
            rowCoordinateOfPegToRemove = selectedMove.getCoordinates().get(0) + 1;
        }
        else
        {
            rowCoordinateOfPegToRemove = selectedMove.getCoordinates().get(0);
        }

        return board[rowCoordinateOfPegToRemove][getColCoordOfPegToRemove(selectedMove, pegToMove)];
    }

    private int getColCoordOfPegToRemove(Hole selectedMove, Hole pegToMove)
    {
        int colCoordinateOfPegToRemove;

        if (selectedMove.getCoordinates().get(0) == pegToMove.getCoordinates().get(0))
        {
            if (selectedMove.getCoordinates().get(1) > pegToMove.getCoordinates().get(1))
            {
                colCoordinateOfPegToRemove = pegToMove.getCoordinates().get(1) + 2;
            }
            else
            {
                colCoordinateOfPegToRemove = selectedMove.getCoordinates().get(1) + 2;
            }
        }
        else
        {
            if (selectedMove.getCoordinates().get(1) > pegToMove.getCoordinates().get(1))
            {
                colCoordinateOfPegToRemove = pegToMove.getCoordinates().get(1) + 1;
            }
            else
            {
                colCoordinateOfPegToRemove = selectedMove.getCoordinates().get(1) + 1;
            }
        }


        return colCoordinateOfPegToRemove;
    }

    int countPegsRemaining()
    {
        int pegCount = 0;

        for (int i = 0; i < COL_LENGTH; i++)
        {
            for (int j = 0; j < ROW_LENGTH; j++)
            {
                if (board[i][j].inPlay())
                {
                    pegCount++;
                }
            }
        }

        return pegCount;
    }

    boolean moveExists()
    {
        List<Hole> possibleMoves = new ArrayList<>();

        for (int i = 0; i < COL_LENGTH; i++)
        {
            for (int j = 0; j < ROW_LENGTH; j++)
            {
                if (board[i][j].opening())
                {
                    possibleMoves.add(board[i][j]);
                }
            }
        }

        for (Hole possibleOpening : possibleMoves)
        {
            for (Hole possiblePegToMove : possibleOpening.getPossibleMoves())
            {
                if (possiblePegToMove.inPlay() && getPegInMiddle(possibleOpening, possiblePegToMove).inPlay())
                {
                    return true;
                }
            }
        }
        return false;
    }

    void printBoard()
    {
        for (int i = 0; i < COL_LENGTH; i++)
        {
            for (int j = 0; j < ROW_LENGTH; j++)
            {
                if (board[i][j].inPlay())
                {
                    System.out.print("X");
                }
                else if (board[i][j].opening())
                {
                    System.out.print(" ");
                }
                else
                {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    void printBoardAndMoves()
    {
        for (Board previousState : previousStates)
        {
            for (List<Integer> coord : previousState.getMoveToBeMade())
            {
                System.out.println(coord);
            }
            previousState.printBoard();

        }
        System.out.println("Moves to win : " + moveCount());
    }

    private Hole[][] getBoard()
    {
        return board;
    }
}
