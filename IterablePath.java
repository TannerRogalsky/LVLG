import org.newdawn.slick.util.pathfinding.Path;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Write a description of class IterablePath here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IterablePath extends Path implements Iterable<Path.Step>
{
    private ArrayList<Path.Step> steps;
    /**
     * Constructor for objects of class IterablePath
     */
    public IterablePath(Path path)
    {
        super();
        steps = new ArrayList<Path.Step>();
        for(int x = 0; x < path.getLength(); x++){
            Path.Step current = path.getStep(x);
            this.appendStep(current.getX(), current.getY());
            steps.add(current);
        }
    }
    
    public Iterator<Path.Step> iterator() {        
        return steps.iterator(); 
    }
    
    public void add(IterablePath path){
        for(int x = 0; x < path.getLength(); x++){
            Path.Step current = path.getStep(x);
            this.appendStep(current.getX(), current.getY());
            steps.add(current);
        }
    }
}
