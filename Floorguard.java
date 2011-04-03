
/**
 * Write a description of class Floorguard here.
 * 
 * @author Tanner Rogalsky
 * @version 1.0
 */
public class Floorguard extends NObject
{

    /**
     * Constructor for objects of class Floorguard
     */
    public Floorguard(int x, int y)
    {
        super(x, y, 4);
    }

	/**
     * Overrides the NObject toString method 
     * 
     * @return     A string representation of the Floorguard.
     */
	public String toString(){
        return String.valueOf(type) + "^" + x + "," + y + "," + 1 + "!";
    }
}
