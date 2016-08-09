
/**
 * Ghost should have an x and y coordinate
 * Ghost should start in their box
 * 1 ghost should be released every time score decreases by 100
 * Ghosts should know whether their "head" square is above food or not.
 * If 1 square over food, then 2 square should be over food next round, and third square round after that
 * Ghosts should take up 2 squares
 * By changing dir, ghost will turn in that direction next.
 */

public class Ghost
{
    private int x;//starting position of pacman
    private int y;
    private int startX;
    private int startY;
    private int lastx;
    private int lasty;
    private int on = 0;
    private boolean eaten = false;

    public int turn = 0;
    private String dir = "up";

    /**
     * Constructor for objects of class Pacman
     * x and y are the starting coordinates of pacman
     */
    public Ghost(int x, int y)
    {
        this.x=x;
        this.y=y;
        this.startX=x;
        this.startY=y;
        this.lasty = y;
        this.lastx = x;
    }

    /**
     * Moves the pacman coordinates in the correct direction
     * Note. doesnt change the board array, just changes the coordinates of pacman.
     */
    public void move()
    {
        if (dir.equals("right") && PacmanGame.board[y][x+1]!=2 && PacmanGame.board[y][x+1]!=7){
            lastx = x;
            lasty = y;
            x=x+1;
            if(x>PacmanGame.XboardDots-2){
                x=x-1;
            }
        }
        else if (dir.equals("left") && PacmanGame.board[y][x-1]!=2 && PacmanGame.board[y][x-1]!=7){
            lastx = x;
            lasty = y;
            x=x-1;
            if(x<1){
                x=x+1;
            }
        }
        else if (dir.equals("up") && PacmanGame.board[y-1][x]!=2 && PacmanGame.board[y-1][x]!=7){
            lasty = y;
            lastx = x;
            y=y-1;
            if(y<1){
                y=y+1;
            }
        }
        else if (dir.equals("down") && PacmanGame.board[y+1][x]!=2 && PacmanGame.board[y+1][x]!=7 && PacmanGame.board[y+1][x]!=5){
            lasty = y;
            lastx = x;
            y=y+1;
            if(y>PacmanGame.YboardDots-2){
                y=y-1;
            }
        }
        else {
            this.changeDir();
            return;
        }
        if(x==startX && y==startY){
            eaten = false;
        }
    }

    /**
     * This method will turn ghost.
     * 
     * Turn field
     * 0 = no direction change
     * 1 = up
     * 2 = left
     * 3 = down
     * 4 = right
     */
    public void changeDir (){
        this.bestDir(PacmanGame.getPac());
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

    public void changeon(int num){
        this.on = num;
    }

    public int getOn(){
        return this.on;
    }
    
    public void eaten (){
        this.eaten =true;
    }

    /**
     * This method is given a pacman and checks where the pacman is, checks where the ghost is and sets turn to the best direction the ghost should turn in
     * Ghost can only turn backwards if it goes into a dead end
     * Ghost atm is too fucking good, it will hunt you down without mercy too well.
     */
    public void bestDir (Pacman p){
        int px = p.xCoord();
        int py = p.yCoord();
        if(!eaten){
            px = p.xCoord();
            py = p.yCoord();
        }
        else if (eaten){
            px = this.startX;
            py = this.startY;
        }
        int xdif = px-x;
        int ydif = py-y;
        int prio1 = 0;
        int prio2 = 0;
        int prio3 = 0;
        if(Math.abs(xdif)>Math.abs(ydif)){
            if(xdif>0){prio1 = 4;}
            else {prio1 = 2;}
        }
        else {
            if(ydif>0){prio1 = 3;}
            else {prio1 = 1;}
        }
        if(Math.abs(xdif)>Math.abs(ydif)){
            if(ydif>0){prio2 = 3;}
            else {prio2 = 1;}
        }
        else {
            if(xdif>0){prio2 = 4;}
            else {prio2 = 2;}
        }
        if(Math.abs(xdif)>Math.abs(ydif)){
            if(ydif>0){prio3 = 1;}
            else {prio3 = 3;}
        }
        else {
            if(xdif>0){prio3 = 2;}
            else {prio3 = 4;}
        }

        if(prio1==1 && PacmanGame.board[y-1][x]!=2 && !dir.equals("down")){
            turn = 1;
            return;
        }
        else if(prio1==2 && PacmanGame.board[y][x-1]!=2 && !dir.equals("right")){
            turn = 2;
            return;
        }
        else if(prio1==3 && PacmanGame.board[y+1][x]!=2 && PacmanGame.board[y+1][x]!=5  && !dir.equals("up")){
            turn = 3;
            return;
        }
        else if(prio1==4 && PacmanGame.board[y][x+1]!=2 && !dir.equals("left")){
            turn = 4;
            return;
        }
        else if(prio2==1 && PacmanGame.board[y-1][x]!=2 && !dir.equals("down")){
            turn = 1;
            return;
        }
        else if(prio2==2 && PacmanGame.board[y][x-1]!=2  && !dir.equals("right")){
            turn = 2;
            return;
        }
        else if(prio2==3 && PacmanGame.board[y+1][x]!=2 && PacmanGame.board[y+1][x]!=5 && !dir.equals("up")){
            turn = 3;
            return;
        }
        else if(prio2==4 && PacmanGame.board[y][x+1]!=2 && !dir.equals("left")){
            turn = 4;
            return;
        }
        else if(prio3==1 && PacmanGame.board[y-1][x]!=2 && !dir.equals("down")){
            turn = 1;
            return;
        }
        else if(prio3==2 && PacmanGame.board[y][x-1]!=2 && !dir.equals("right")){
            turn = 2;
            return;
        }
        else if(prio3==3 && PacmanGame.board[y+1][x]!=2 && PacmanGame.board[y+1][x]!=5 && !dir.equals("up")){
            turn = 3;
            return;
        }
        else if(prio3==4 && PacmanGame.board[y][x+1]!=2 && !dir.equals("left")){
            turn = 4;
            return;
        }
        else if(dir.equals("down")){
            turn = 1;
            return;
        }
        else if(dir.equals("up")){
            turn = 3;
            return;
        }
        else if(dir.equals("left")){
            turn = 4;
            return;
        }
        else if(dir.equals("right")){
            turn = 2;
            return;
        }
    }
}
