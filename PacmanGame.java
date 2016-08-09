
/**
 * Pacman
 * 
 * 1 class to control the game.
 * 1 class to control Pacman.
 * 1 class to control Ghost.
 * 
 * 1. Draw board.
 * 2. Get a pacman working on the board
 * 3. Get the ghosts working on the board.
 * Use an array for the board
 * 
 * Current problems
 * The ghost when overlapping a pacman often leaves a useless pacman, which does nothing.
 * The ghost will run over big circles and then turn an adjacent little circle into a big circle
 * 
 * @author Thomas Edwards 
 * @version (a version number or a date)
 */

import ecs100.*;
import java.util.*;
import java.awt.Color;
import java.io.*;
import ecs100.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class PacmanGame
{
    public static final int XboardDots = 30;//number of dots along the horizontal
    public static final int YboardDots = 30;//number of dots along the Vertical
    
    public static final double DIFFICULTY = 0.7;//HOW GOOD GHOSTS ARE AT CHASING YOU 1 is best, 0 is worst
    
    public static int [][] board;
    private boolean gg = false;
    private static Pacman p1;
    private Ghost g1;
    private int score = 0;
    private int count =0;
    private boolean flip = true;

    /**
     * Constructor for objects of class Pacman
     * 
     * Array
     * 0 = food
     * 1 = empty/black
     * 2 = wall
     * 3 = pacman
     * 4 = ghost
     * 5 = gate
     * 6 = big circle
     * 7 = portal
     */
    public static void main (String [] args){
        PacmanGame p = new PacmanGame();
    }
    
    public PacmanGame()
    {
        UI.initialise();
        UI.setKeyListener(this::doKey);
        this.startGame();
    }

    public void startGame (){
        p1 = new Pacman (15,18);//starting position of pacman
        g1 = new Ghost (1,3);
        this.setBoard();
        gg=false;
        UI.printMessage("Press spacebar to end");
        UI.setWindowSize(950,800);
        count=0;
        while (!gg){
            count++;
            p1.changeDir();
            p1.move();
            if(this.touching() && count >=0){
                gg=true;
            }
            else if(this.touching() && count <0){
                g1.eaten();
            }
            if(Math.random()<DIFFICULTY){//how accurate the ghost is decreasing this number will make the ghost less accurate at finding pacman
                g1.changeDir();
            }
            g1.move();
            if(this.touching() && count >=0){//ends the game if you touch a ghost
                gg=true;
            }
            else if(this.touching() && count <0){//if the ghost is vulnerable sends the ghost back to its starting point and doesnt end the game
                g1.eaten();
            }
            this.updateArray();
            this.redraw();
            UI.sleep(100);
        }
    }

    /**
     * 
     */
    public void doKey(String key){
        if(key.equals("w")){p1.turn=1;}
        else if(key.equals("a")){p1.turn = 2;}
        else if(key.equals("s")){p1.turn = 3;}
        else if(key.equals("d")){p1.turn = 4;}
        else if (key.equals("Space")){
            gg=true;
            new PacmanGame();
        }
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void redraw()
    {
        UI.clearPanes();
        this.drawBoard();
    }

    public void gameEnd(int count){
        UI.clearGraphics();
        UI.setFontSize(60);
        if(score==1){
            UI.drawString("You Win",200,300);
        }
        else{
            UI.drawString("You Lose",200,300);
        }
    }

    /**
     * Draws the board in its current state based of the board array
     */
    public void drawBoard(){
        UI.setColor(Color.black);
        UI.fillRect(0,0,1200,800);
        int left = 20;
        int top = 20;
        int size = 20;//size of game
        for (int i = 0; i<board.length; i++){
            for(int j = 0; j<board[0].length;j++){
                int num = board[i][j];
                if(num==2){
                    UI.setColor(Color.blue);
                    UI.fillRect(left,top,size,size);
                }
                else if(num==0){
                    UI.setColor(Color.white);
                    UI.fillOval(left+size/4,top+size/4,size/2,size/2);
                }
                else if(num==5){
                    UI.setColor(Color.white);
                    UI.fillRect(left,top+size/4,size,size/2);
                }
                else if(num==3){
                    UI.setColor(Color.yellow);
                    UI.fillRect(left,top,size,size);
                }
                else if(num==7){
                    UI.setColor(Color.green);
                    UI.fillRect(left,top,size,size);
                }
                else if (num==6){
                    UI.setColor(Color.white);
                    UI.fillOval(left+size/8,top+size/8,size*3/4,size*3/4);
                }
                else if(num==4 && count >=0){//draw normal ghost
                    UI.setColor(Color.red);
                    UI.fillRect(left,top,size,size);
                }
                else if(num==4 && count <0){//draws vulnerable ghost
                    if (count>-21){flip = !flip;}
                    if(flip){
                        UI.setColor(Color.cyan);
                        UI.fillRect(left,top,size,size);
                    }
                }
                left = left+size;
            }
            top = top+size;
            left = 20;
        }
    }

    /**
     * This sets the board to the original state
     */
    public void setBoard(){
        board = new int[YboardDots][XboardDots];
        this.score = 0;
        //draws outline wall
        for (int j = 0; j<board.length;j++){
            for (int i =0; i<board[0].length;i++){
                if(i==0 || j==0 || j==board.length-1 || i==board[0].length-1){board[j][i]=2;}
            }
        }
        this.template1();

        board[board.length/2-1][0] = 7;//portals
        board[board.length/2-1][board[0].length-1] = 7;
        //ghosts starting box and gate
        for (int j = board.length/2-3; j<board.length/2+3;j++){
            for (int i =board[0].length/2-4; i<board[0].length/2+4;i++){
                if(j==board.length/2-3 || i==board[0].length/2-4 || i==board[0].length/2+3 || j==board.length/2+2){
                    board[j][i]=2;
                }
                else{
                    board[j][i]=1;
                }
                if(j==board.length/2-3 && (i==board[0].length/2 || i==board[0].length/2-1)){
                    board[j][i]=5;
                    board[j-1][i]=0;
                }
            }
        }

        //should set the score to be number of food pixels
        for (int j =0; j<board.length;j++){
            for (int i =0; i<board[0].length;i++){
                if(board[j][i]==0){score++;}
            }
        }
        //adds the big circles
        board[1][1] = 6;
        board[28][28] = 6;
        board[28][1] = 6;
        board[1][28] =6;
    }

    /**
     * Adds walls at position x,y of size hori,vert.
     */
    public void addWall(int x, int y, int hori, int vert){
        int startX = x;
        int startY = y;
        while (y<startY+vert){
            while(x<startX+hori){
                board[y][x]=2;
                x++;
            }
            y++;
            x=startX;
        }
    }

    /**
     * Updates positions in array based on position of pacman
     */
    public void updateArray(){
        if(board[p1.yCoord()][p1.xCoord()]==0 || board[p1.yCoord()][p1.xCoord()]==6){//reduces score by one, if food is eaten
            score--;
            if(score==0){gg=true;}//ends the game if all the food has been eaten (Note. this might need a new endGame method ()
        }
        board[p1.ylast()][p1.xlast()] = 1;
        if(board[p1.yCoord()][p1.xCoord()]==6){//if the coordinates are the same as a big circle, makes ghosts vulnerable for 120 moves
            g1.eaten();
            count = -120;
        }
        board[p1.yCoord()][p1.xCoord()]=3;

        //Ghost 1 updates
        board[g1.ylast()][g1.xlast()] = g1.getOn();//makes the board at position ylast x last equal to what the ghost was on.
        if(board[g1.yCoord()][g1.xCoord()]!=4){//if the ghost is not on top of itself, then make it on whatever is at x,y
            g1.changeon(board[g1.yCoord()][g1.xCoord()]);
        }
        if(board[g1.yCoord()][g1.xCoord()]==3){// if it is over a pacman, make it 1
            g1.changeon(1);
        }
        
        board[g1.yCoord()][g1.xCoord()]=4;
    }

    public boolean touching (){
        if(g1.xCoord()==p1.xCoord() && g1.yCoord()==p1.yCoord()){
            return true;
        }
        else {
            return false;}
    }

    /**
     * First arrangement of walls, based around a 30/30 grid
     */
    public void template1(){
        //rect wall in each corner
        this.addWall(2,2,4,2);
        this.addWall(24,2,4,2);
        this.addWall(2,26,4,2);
        this.addWall(24,26,4,2);

        //walls around channel
        this.addWall(1,10,5,4);
        this.addWall(1,15,5,5);
        this.addWall(24,8,5,6);
        this.addWall(24,15,5,5);

        //bottom horizontal wall section
        this.addWall(7,26,5,2);
        this.addWall(13,26,4,2);
        this.addWall(18,26,5,2);

        //bot left design
        this.addWall(7,22,2,4);
        this.addWall(10,22,2,3);
        this.addWall(7,19,5,2);
        this.addWall(2,21,2,2);
        this.addWall(5,21,1,4);
        this.addWall(2,24,3,1);

        //bot middle
        this.addWall(13,19,6,3);
        this.addWall(13,23,2,2);
        this.addWall(16,23,2,2);

        //bot left
        this.addWall(18,23,10,2);
        this.addWall(20,19,3,3);
        this.addWall(23,21,5,1);

        //middle right
        this.addWall(22,16,1,3);
        this.addWall(20,16,1,3);
        this.addWall(20,10,1,5);
        this.addWall(22,8,1,7);

        //top middle(/rightish)
        this.addWall(20,3,1,6);
        this.addWall(17,3,1,5);
        this.addWall(16,3,1,5);
        this.addWall(15,3,1,8);

        this.addWall(13,3,1,8);
        this.addWall(14,3,1,9);
        this.addWall(16,7,1,4);
        this.addWall(17,8,1,3);
        this.addWall(18,3,1,8);
        this.addWall(7,1,16,1);  
        this.addWall(22,5,6,2);  
        this.addWall(22,2,1,2);  

        //top left
        this.addWall(10,3,2,8);
        this.addWall(7,3,3,4);
        this.addWall(2,5,4,4);
        this.addWall(7,8,2,10);
        this.addWall(10,12,2,6);

    }

    /**
     * Adds a gate, mainly used for testing purposes 
     */
    public void addGate(int x, int y, int hori, int vert){
        int startX = x;
        int startY = y;
        while (y<startY+vert){
            while(x<startX+hori){
                board[y][x]=5;
                x++;
            }
            y++;
            x=startX;
        }
    }

    public static Pacman getPac(){
        return p1;
    }
}
