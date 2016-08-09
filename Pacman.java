
/**
 * Write a description of class Pacman here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;
import java.awt.Color;
import java.io.*;
import ecs100.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Pacman
{
    private int x;//starting position of pacman
    private int y;
    private int lastx;
    private int lasty;

    public int turn = 0;
    private String dir = "up";

    /**
     * Constructor for objects of class Pacman
     * x and y are the starting coordinates of pacman
     */
    public Pacman(int x, int y)
    {
        this.x=x;
        this.y=y;
        this.lasty = y;
        this.lastx = x;
    }

    /**
     * Moves the pacman coordinates in the correct direction
     * Note. doesnt change the board array, just changes the coordinates of pacman.
     */
    public void move()
    {
        if (dir.equals("right") && PacmanGame.board[y][x+1]!=2 && PacmanGame.board[y][x+1]!=5){
            lastx = x;
            lasty = y;
            if(PacmanGame.board[y][x+1]==7){//does the portal movement
                x = x-(PacmanGame.board[0].length-3);
                return;
            }
            x=x+1;
            if(x>PacmanGame.XboardDots-2){x=x-1;}
        }
        else if (dir.equals("left") && PacmanGame.board[y][x-1]!=2 && PacmanGame.board[y][x-1]!=5){
            lastx = x;
            lasty = y;
            if(PacmanGame.board[y][x-1]==7){//does the portal movement
                x = x+PacmanGame.board[0].length-3;
                return;
            }
            x=x-1;
            if(x<1){x=x+1;}
        }
        else if (dir.equals("up") && PacmanGame.board[y-1][x]!=2 && PacmanGame.board[y-1][x]!=5){
            lasty = y;
            lastx = x;
            y=y-1;
            if(y<1){y=y+1;}
        }
        else if (dir.equals("down") && PacmanGame.board[y+1][x]!=2 && PacmanGame.board[y+1][x]!=5){
            lasty = y;
            lastx = x;
            y=y+1;
            if(y>PacmanGame.YboardDots-2){y=y-1;}
        }
        else {return;}
    }

    /**
     * This method will turn pacman 	
     * 
     * Turn field
     * 0 = no direction change
     * 1 = up
     * 2 = left
     * 3 = down
     * 4 = right
     */
    public void changeDir (){
        if(turn!=0){
            if(turn==1){
                if(PacmanGame.board[y-1][x]!=2){
                    dir = "up";
                    turn = 0;
                }
            }
            else if (turn==2){
                if(PacmanGame.board[y][x-1]!=2){
                    dir = "left";
                    turn = 0;
                }
            }
            else if (turn==3){
                if(PacmanGame.board[y+1][x]!=2 && PacmanGame.board[y+1][x]!=5){
                    dir = "down";
                    turn = 0;
                }
            }
            else if (turn==4){
                if(PacmanGame.board[y][x+1]!=2){
                    dir = "right";
                    turn = 0;
                }
            }
        }
        else {
            return;
        }
    }

    public int xCoord (){
        return this.x;
    }

    public int yCoord (){
        return this.y;
    } 

    public int ylast (){
        return this.lasty;
    }

    public int xlast (){
        return this.lastx;
    }
    
}
