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
    
    private String name;
    private float x;
    private float y;
    private int grid;
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
    
    public City(String name, float x, float y, int grid) {
        landNeighbors = new ArrayList<String>();
        seaNeighbors = new ArrayList<String>();
        this.name = name;
        this.x = x;
        this.y = y;
        this.grid = grid;
    }
    
    //Getters
    public String getName() {
        return name;
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public int getGrid() {
        return grid;
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
}
