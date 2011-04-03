/**
 * Write a description of class NMap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Arrays;
import java.util.Random;
import org.newdawn.slick.util.pathfinding.*;
import java.util.ArrayList;

public class NMap implements TileBasedMap
{
    private NTile[][] tiles;                // all the tiles data
    private ArrayList<NObject> objects;     // all the objects on the map
    private boolean[][] visited;            // whether the tile has been visited while pathfinding
    
    private static final char[][] TILEVALUES = {{'2','3','4','5'}, {'6','7','8','9'}, {':',';','<','='}, 
            {'>','?','@','A'}, {'B','C','D','E'}, {'F','G','H','I'}, {'J','K','L','M'}, {'N','O','P','Q'}};
    private static final int TILESWIDTH = 31;
    private static final int TILESHEIGHT = 23;
    
    private static final int NINJA = 5;      // 1 set of coords
    private static final int EXIT = 11;      // 2 set of coords
    private static final int GAUSS = 3;      // 1 set of coords
    private static final int ROCKET = 10;    // 1 set of coords
    private static final int MINE = 12;      // 1 set of coords
    private static final int FLOORGUARD = 4; // 1 set of coords + direction (always "1")
    private static final int GOLD = 0;       // 1 set of coords
    private static final int BOUNCEBLOCK = 1;// 1 set of coords
    private static final int THUMP = 8;      // 1 set of coords + direction
    private static final int DRONE = 6;      // a lot of data
    private static final int LAUNCHPAD = 2;  // 1 set of coords + vector info (1 or -1 (x2))
    private static final int ONEWAY = 7;     // 1 set of coords + direction
    private static final int DOOR = 9;       // a lot of data
    
    private static final int[][][] PATTERNS = {{{6,6},{12,6},{18,6},{6,12},{12,12},{18,12},{6,18},{12,18},{18,18}},
    	{{6,6},{6,12},{6,18},{12,12},{18,6},{18,12},{18,18}},
    	{{6,6},{12,12},{18,18},{18,6},{6,18}},
    	{{6,6},{6,12},{6,18},{12,18},{18,18},{18,12},{18,6},{12,6}},
    	{{12,6},{6,12},{12,18},{18,12}},
    	{{6,12},{12,12},{18,12},{12,6},{12,18}},
    	{{6,12},{12,12},{18,12}},
    	{{12,6},{12,12},{12,18}},
    	{{6,6},{18,6},{18,18},{6,18}},
    	{{6,6},{6,12},{6,18},{18,6},{18,12},{18,18}},
    	{{6,6},{12,6},{18,6},{6,18},{12,18},{18,18}},
    	{{6,6},{12,6},{18,6},{12,12},{6,18},{12,18},{18,18}},
    	{{6,12},{12,6},{18,12}},
    	{{6,12},{12,18},{18,12}},
    	{{12,6},{6,12},{12,18}},
    	{{12,6},{18,12},{12,18}},
    	{{12,6},{12,18}},
    	{{6,12},{18,12}},
    	{{6,18},{18,6}},
    	{{6,6},{18,18}},
    	{{6,6},{18,6},{12,18}},
    	{{6,12},{18,18},{18,6}},
    	{{6,18},{18,18},{12,6}},
    	{{6,18},{6,6},{18,12}}};
    
    /**
     * Constructor for objects of class NMap.
     * Creates and generates a new NMap using all default values.
     */
    public NMap()
    {
        tiles = new NTile[TILESWIDTH][TILESHEIGHT];
        visited = new boolean[TILESWIDTH][TILESHEIGHT];
        generateTiles();
        generateObjects();
    }
    
    /**
     * Constructor for objects of class NMap.
     * Creates and generates a new NMap using predefined tiles.
     * 
     * @param rawTilesData The string of data associated with map tiles.
     */
    public NMap(String rawTilesData){
        tiles = new NTile[TILESWIDTH][TILESHEIGHT];
        visited = new boolean[TILESWIDTH][TILESHEIGHT];
        int x = 0;
        int y = 0;
        for(char c : rawTilesData.toCharArray()){
            tiles[x][y] = new NTile(x, y, c);
            y++;
            if(y >= TILESHEIGHT){
                x++;
                y = 0;
            }
        }
        generateObjects();
    }

    /**
     * Method required by the TileBasedMap interface.
     * Returns the width of the map in tiles.
     */
    public int getWidthInTiles(){
        return TILESWIDTH;
    }
    
    /**
     * Method required by the TileBasedMap interface.
     * Returns the height of the map in tiles.
     */
    public int getHeightInTiles(){
        return TILESHEIGHT;
    }
    
    /**
     * Method required by the TileBasedMap interface.
     * Returns whether the mover can move to the tile specified in the args.
     */
    public boolean blocked(Mover mover, int x, int y){
        if (tiles[x][y].contains(getObjectsOfType(MINE))){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Method required by the TileBasedMap interface.
     * Returns the "cost" of moving from (sx, sy) to (tx, ty).
     */
    public float getCost(Mover mover, int sx, int sy, int tx, int ty){
        if (tiles[tx][ty].type == '0'){
            return 0;
        } else {
            return 100;
        }
    }
    
    /**
     * Method required by the TileBasedMap interface.
     * Marks places in the tile map that have been tried while pathfinding.
     */
    public void pathFinderVisited(int x, int y){
        visited[x][y] = true;
    }
    
    /**
     * Gets all objects of the given type.
     * 
     * @param type The type of object to return.
     * @return An array with all the objects of the given type.
     */
    private NObject[] getObjectsOfType(int type){
        ArrayList<NObject> temp = new ArrayList<NObject>();
        for(NObject object : objects){
            if(object.type == type){
                temp.add(object);
            }
        }
        return temp.toArray(new NObject[temp.size()]);
    }
    
    /**
     * Generates the objects for the map.
     * 
     * When generating new objects, you must only use less than or equal to and specify a range of random numbers with one greater than your maximum desired objects.
     * This ensures that maps can generated without the specified object.
     * If you wish to make it so that there is a minimum of the specified object generated, add to the random number.
     */
    private void generateObjects(){
        objects = new ArrayList<NObject>();
        Random r = new Random();
        
        objects.add(generateObject(NINJA));
        objects.add(generateObject(EXIT));
        objects.add(((NExit)objects.get(objects.size() - 1)).trigger);
        
        for(int i = 1; i <= r.nextInt(6) + 2; i++){
            objects.add(generateObject(MINE));
        }
        objects.add(generateObject(GAUSS));
        objects.add(generateObject(ROCKET));
        for(int i = 1; i <= r.nextInt(12) + 4; i++){
            objects.addAll(Arrays.asList(generatePattern(GOLD)));
        }
        for(int i = 1; i <= r.nextInt(3); i++){
            NObject floorguard = generateObject(FLOORGUARD);
            if (floorguard != null){
                objects.add(floorguard);
            }
        }
    }
    
    /**
     * Generates a random object pattern.
     * 
     * @param type The type of object it is.
     * @return A randomly generated object pattern.
     */
    public NObject[] generatePattern(int type){
    	int coords[] = generateCoords();
    	int index = new Random().nextInt(PATTERNS.length);
    	NObject[] pattern = new NObject[PATTERNS[index].length];
    	
		int x = coords[2] * 24;
		int y = coords[3] * 24;
		for (int i = 0; i < pattern.length; i++){
			pattern[i] = new NObject(x + PATTERNS[index][i][0], y  + PATTERNS[index][i][1], type);
		}
		return pattern;
    }
    
    /**
     * Generates a random object.
     * 
     * @param type The type of object it is.
     * @return A randomly generated object.
     */
    public NObject generateObject(int type){
        int coords[] = generateCoords();
        if(type == EXIT){
            int triggerCoords[] = generateCoords();
            return new NExit(coords[0], coords[1], triggerCoords[0], triggerCoords[1]);
        }else if (type == NINJA){
            return new Ninja(coords[0], coords[1]);
        }else if (type == FLOORGUARD){
            int timesLooped = 0;
            while(true) { // Checks to make sure that there's a full tile below the floorguard. Has to convert the coords back to gridspace first.
                timesLooped++;
                if (coords[3] + 1 == getHeightInTiles()){
                    break;
                } else if ((coords[3] + 1 < getHeightInTiles()) && (coords[3] - 1 >= 0)){ //
                    if ((tiles[coords[2]][coords[3] + 1].type == '1') && (!tiles[coords[2]][coords[3] - 1].contains(objects.get(0)))){
                        break;
                    }
                } else if (timesLooped >= 1000){
                    return null;
                }
                coords = generateCoords();
            }
            return new Floorguard(coords[0], coords[1]);
        }else if (type == MINE){
            int timesLooped = 0;
            while(true) {
                timesLooped++;
                if (coords[3] - 1 == 0){
                    break;
                } else if ((coords[3] + 1 < getHeightInTiles()) && (coords[3] - 1 >= 0)){ //
                    if (!tiles[coords[2]][coords[3] - 1].contains(objects.get(0))){
                        break;
                    }
                } else if (timesLooped >= 1000){
                    return null;
                }
                coords = generateCoords();
            }
        } else if (type == BOUNCEBLOCK){
        	
        } else if (type == THUMP){
        	
        } else if (type == DRONE){
        	
        } else if (type == LAUNCHPAD){
        	
        } else if (type == ONEWAY){
        	
        } else if (type == DOOR){
        	
        }
        return new NObject(coords[0], coords[1], type);
    }
    
    /**
     * Generates a random set of coordinates and makes sure the tile is empty.
     * 
     * @return A size 4 int array the first 2 entries being the pixel coordinates and the latter 2 being the grid coordinates.
     */
    public int[] generateCoords(){
        int x,y;
        Random r = new Random();
        // makes sure we aren't putting it inside a tile
        do{
            x = r.nextInt(getWidthInTiles());
            y = r.nextInt(getHeightInTiles());
        }while((tiles[x][y].type != '0') || (tiles[x][y].contains(objects.toArray(new NObject[objects.size()]))));
        return new int[]{(x + 1) * 24 + 12,(y + 1) * 24 + 12, x, y};
    }
    
    /**
     * Checks and fixes the tiles using an A* search to determine the lease costly path from the ninja to the trigger to the exit.
     */
    public void checkAndFix(){
        AStarPathFinder pathFinder = new AStarPathFinder(this, 713, false);
        Ninja ninja = (Ninja)objects.get(0);
        NExit exit = (NExit)objects.get(1);;
        
        try{
            IterablePath path = new IterablePath(pathFinder.findPath(ninja, ninja.gridX, ninja.gridY, exit.trigger.gridX, exit.trigger.gridY));
            path.add(new IterablePath(pathFinder.findPath(ninja, exit.trigger.gridX, exit.trigger.gridY, exit.gridX, exit.gridY)));
       
            for(Path.Step step: path){
                System.out.print(step.getX() + ", " + step.getY() + "; ");
                tiles[step.getX()][step.getY()].type = '0';
            }
            System.out.println("");  
        } catch (NullPointerException e){
            System.out.println("Could not find path.");
//             for (int y = 0; y < getHeightInTiles(); y++){
//                 for (int x = 0; x < getWidthInTiles(); x++){
//                     System.out.print(visited[x][y] + " ");
//                     if (visited[x][y]){
//                         System.out.print(" ");
//                     }
//                 }
//                 System.out.println("");
//             }
        }        
    }
    
    /**
     * Generates the tiles.
     */
    private void generateTiles(){
        emptyTiles();
        Random r = new Random();
        // how many tile forms to put in
        int openness = r.nextInt(12)+5;
        for(int h = 0; h < openness; h++){
            //generates the position and size of each form
            int formWidth, formHeight;
            formWidth = (int)Math.ceil(Math.random()*Math.random()*getWidthInTiles());
            formHeight = (int)Math.ceil(Math.random()*Math.random()*getHeightInTiles());
            int formX = r.nextInt(getWidthInTiles());
            int formY = r.nextInt(getHeightInTiles());
            //int tileType = r.nextInt(8);
            // places the form in the tile data
            for(int i = formX; i < formX + formWidth; i++){
                for(int j = formY; j < formY + formHeight; j++){
                    try{
                        // puts decorations on the corners
                        if((i == formX  + formWidth -1) && (j == formY)){
                            tiles[i][j] = new NTile(i, j, TILEVALUES[r.nextInt(8)][0]);
                        }else if((i == formX) && (j == formY)){
                            tiles[i][j] = new NTile(i, j, TILEVALUES[r.nextInt(8)][1]);
                        }else if((i == formX) && (j == formY + formHeight - 1)){
                            tiles[i][j] = new NTile(i, j, TILEVALUES[r.nextInt(8)][2]);
                        }else if((i == formX + formWidth - 1) && (j == formY + formHeight - 1)){
                            tiles[i][j] = new NTile(i, j, TILEVALUES[r.nextInt(8)][3]);
                        }else{ 
                            tiles[i][j] = new NTile(i, j, '1'); // fills in the form
                        }
                    }catch (Exception e){}
                }
            }
        }       
    }
    
    /**
     * Makes every tile in the map into an empty tile.
     */
    private void emptyTiles(){
        for(int x = 0; x < getWidthInTiles(); x++){
            for(int y = 0; y < getHeightInTiles(); y++){
                tiles[x][y] = new NTile(x, y, '0');
            }
        }
    }
    
    /**
     * Returns a string based representation of the map.
     */
    public String toString(){
        String temp = new String("");
        for(int i = 0; i < getWidthInTiles(); i++){
            for(int j = 0; j < getHeightInTiles(); j++){
                temp += tiles[i][j];
            }
        }
        temp += "|";
        for(NObject object: objects){
            temp += object.toString();
        }
        return temp;
    } 
}
