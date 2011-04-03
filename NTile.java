/**
 * Write a description of class NTile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.Rectangle;

public class NTile extends Rectangle
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5622187017978705050L;
	public char type;
    public int gridX, gridY;
    
    /**
     * Constructor for objects of class NTile.
     * 
     * @param x The grid-based x-axis orientation of the tile on the map.
     * @param y The grid-based y-axis orientation of the tile on the map.
     * @param type The type of tile it is.
     */
    public NTile(int x, int y, char type)
    {
        super((x + 1) * 24, (y + 1) * 24, 24, 24); // tiles in N are 24 pixels squared
        this.gridX = x;
        this.gridY = y;
        this.type = type;
    }
    
    public boolean contains(NObject object){
        return contains(new NObject[]{object});
    }
    
    public boolean contains(NObject[] objects){
        for(NObject object : objects){
            if(this.contains(object.x, object.y)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns a string representation of this tile.
     */
    public String toString(){
        return String.valueOf(type);
    }
}
