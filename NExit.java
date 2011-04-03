
/**
 * Write a description of class NExit here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NExit extends NObject
{
    public NObject trigger;
    /**
     * Constructor for objects of class NExit
     */
    public NExit(int xExit, int yExit, int xTrigger, int yTrigger)
    {
        super(xExit, yExit, 11);
        this.trigger = new NObject(xTrigger, yTrigger, 11);
    }
    
    public String toString(){
        return String.valueOf(this.type) + "^" + this.x + "," + this.y + "," + trigger.x + "," + trigger.y + "!";
    }
}
