/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jte.data;

/**
 *
 * @author Michael
 */
public class City {
    private String name;
    private float x;
    private float y;
    private int grid;
    
    public City() {
        
    }
    
    public City(String name, float x, float y, int grid) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.grid = grid;
    }
    
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
}
