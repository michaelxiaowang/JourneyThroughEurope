/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jte.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Michael
 */
public class City {
    public static HashMap<String, City> cities = new HashMap<String, City>();
    public static ArrayList<String> redcities = new ArrayList<String>();
    public static ArrayList<String> greencities = new ArrayList<String>();
    public static ArrayList<String> yellowcities = new ArrayList<String>();
    
    private String name;
    private int x;
    private int y;
    private int grid;
    private int flight;
    private String color;
    private ArrayList<String> landNeighbors;
    private ArrayList<String> seaNeighbors;
    
    public City() {
        landNeighbors = new ArrayList<String>();
        seaNeighbors = new ArrayList<String>();
    }
    
    public City(String name) {
        landNeighbors = new ArrayList<String>();
        seaNeighbors = new ArrayList<String>();
        this.name = name;
    }
    
    //Getters
    public String getName() {
        return name;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getGrid() {
        return grid;
    }
    
    public int getFlight() {
        return flight;
    }
    
    public String getColor() {
        return color;
    }
    
    public ArrayList<String> getLandNeighbors() {
        return landNeighbors;
    }
    
    public ArrayList<String> getSeaNeighbors() {
        return seaNeighbors;
    }
    
    //Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void setGrid(int grid) {
        this.grid = grid;
    }
    
    public void setFlight(int flight) {
        this.flight = flight;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
