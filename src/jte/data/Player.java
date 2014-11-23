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
    private boolean computer;
    private int startX;
    private int startY;
    private int currentX;
    private int currentY;
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
    
    public int getStartX() {
        return startX;
    }
    
    public int getStartY() {
        return startX;
    }
    
    public int getCurrentX() {
        return currentX;
    }
    
    public int getCurrentY() {
        return currentY;
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
    
    //Setters
    public void setStartX(int startX) {
        this.startX = startX;
    }
    
    public void setStartY(int startY) {
        this.startY = startY;
    }
    
    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }
    
    public void setCurrentY(int startY) {
        this.currentY = currentY;
    }
    
    public void setPiece(ImageView playerPiece) {
        this.playerPiece = playerPiece;
    }
    
    public void setFlag(ImageView playerFlag) {
        this.playerFlag = playerFlag;
    }
}
