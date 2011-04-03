/**
 * NObject.java
 *
 *
 * @author Tanner Rogalsky
 * @version 1.00 2008/5/17
 */


public class NObject{
    public int type;
    public int x, y;
    public int gridX, gridY;
    
    public NObject(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
        gridX = x / 24 - 1;
        gridY = y / 24 - 1;
    }
    
    public String toString(){
        if((type == 11) && !(this instanceof NExit)){
            return "";
        }
        return String.valueOf(type) + "^" + x + "," + y + "!";
    }
}