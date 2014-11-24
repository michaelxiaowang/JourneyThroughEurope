/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jte.data;

import java.util.ArrayList;
import javafx.scene.image.ImageView;

/**
 *
 * @author Michael
 */
public class Player {
    public static ArrayList<Player> players = new ArrayList<Player>();
    
    private String name;
    private String color;
    private boolean computer;
    private City startCity;
    private City currentCity;
    private ArrayList<String> cards;
    private ImageView playerPiece;
    private ImageView playerFlag;
    
    public Player() {
        
    }
    
    public Player(String name, boolean computer) {
        this.name = name;
        this.computer = computer;
        cards = new ArrayList();
    }
    
    //Getters
    public String getName() {
        return name;
    }
    
    public boolean isComputer() {
        return computer;
    }
    
    public City getStartCity() {
        return startCity;
    }
    
    public City getCurrentCity() {
        return currentCity;
    }
    
    public ImageView getPiece() {
        return playerPiece;
    }
    
    public ImageView getFlag() {
        return playerFlag;
    }
    
    public ArrayList<String> getCards() {
        return cards;
    }
    
    public String getColor() {
        return color;
    }
    
    //Setters
    public void setStartCity(City startCity) {
        this.startCity = startCity;
    }
    
    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }
    
    public void setPiece(ImageView playerPiece) {
        this.playerPiece = playerPiece;
    }
    
    public void setFlag(ImageView playerFlag) {
        this.playerFlag = playerFlag;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
