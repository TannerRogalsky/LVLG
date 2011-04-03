import java.io.*;

/**
 * Write a description of class Main here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Main
{    
    public static void main(String[] argv){ 
        
    	try{
	        FileWriter f = new FileWriter("userlevels.txt");
	        f.write("&userdata= \n\n");
	        	        
	        for(int i = 0; i <= 100; i++){
	            System.out.print(i + ". ");
	            NMap map = new NMap("11100011111111111000001110000011110000000000001000000000000000000000000001000000000100000001000000000000011100000111000000011000111000011111000000110000010000111111000000000000000000111111000000000000000011111111000010000000000111111111000111000000011111111111001111100001111111011110011111100011111100011000111111100011111000000001111111100011111000000111111111100011110000001111111111100011100000011111111111100110000000011111111111111100000000000001111100111000000000000001110000110001000000100000000001100111100001000000000010001111100000000000000100011111100000011100001000011111111111111000011000011111111111111100111100111111001111111100111100111000000111110001111100000000000000000011111100011110000000001");
	            //NMap map = new NMap();
	            map.checkAndFix();  
	            
	            String output = map.toString();
	            
	            if (i < 10){
	                f.write("$00" + i + "#rennaT#none#" + output + "\n");
	            }else if(i < 100){
	                f.write("$0" + i + "#rennaT#none#" + output + "\n");
	            }else{
	                f.write("$" + i + "#rennaT#none#" + output + "\n");
	            }
	            
	        }
	        f.close();
    	} catch (IOException e){
    		System.err.println(e.getStackTrace());
    	}
    }
}
