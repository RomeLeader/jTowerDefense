package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MapFile {
	
	FileInputStream file;
	InputStreamReader reader;
	
	Scanner scanner;
	
	Map map = new Map();
	
public Map getMap(String fileName) {
	
	try {
		file = new FileInputStream("map/" + fileName + ".map");
		reader = new InputStreamReader(file);
		
		scanner = new Scanner(reader);
		
		map.arena = new int[17][10];
		
		int x = 0;
		int y = 0;
		
		while(scanner.hasNext()){
			map.arena[x][y] = scanner.nextInt();
			if (x < (17-1)) {
				x++;
			}
			
			else {
				y++;
				x = 0;
			}
			
		}
		
		return map;
	 }catch(FileNotFoundException e) {
		e.printStackTrace();
	}
	
	return null;
	
}

}
