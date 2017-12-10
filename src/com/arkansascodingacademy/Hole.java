package com.arkansascodingacademy;

import java.util.ArrayList;
import java.util.List;

class Hole
{
    private List<Integer> coordinates = new ArrayList<>();
    private boolean available = false;
    private boolean pegSet = false;
    private List<Hole> possibleMoves = new ArrayList<>();

    Hole()
    {}

    boolean isAvailable()
    {
        return available;
    }

    void setAvailable(boolean available)
    {
        this.available = available;
    }

    void setPegSet(boolean pegSet)
    {
        this.pegSet = pegSet;
    }

    boolean inPlay()
    {
        return available && pegSet;
    }

    boolean opening()
    {
        return available && !pegSet;
    }

    List<Hole> getPossibleMoves()
    {
        return possibleMoves;
    }

    List<Integer> getCoordinates()
    {
        return coordinates;
    }

    void setCoordinates(List<Integer> coordinates)
    {
        this.coordinates = coordinates;
    }

    boolean isPegSet()
    {
        return pegSet;
    }

    void setPossibleMoves(List<Hole> possibleMoves)
    {
        this.possibleMoves = possibleMoves;
    }
}
