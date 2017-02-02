/*Jason Ivey 
 * PROGRAMMING FUNDAMENTALS II (F)
 * This lab three!
 * COSC 1437 – Lab 3
 * MW - 5.30-7pm
 * 
 */
import java.util.*;
import java.io.*; // Here we import what we need. \
import java.lang.*;
public class Engine{
  public static void main(String [] args) throws FileNotFoundException{ //engine main, updates game and takes inputs
    Scanner input = new Scanner(System.in);
    Map map = new Map();
    while(map.getTries() < 50 || map.getHits() < map.getShips()){
      try{
        System.out.print("\n To exit press [enter]. To play, input coordinates such as [ a,1 ] and press [enter]."
                           +"\n Type your coordinates digit here: "); 
        String line = input.nextLine();
        if (0 >= line.trim().length())
          break;
        String[] coordinates = line.trim().split(",");
        coordinates[1] += " ";
        coordinates[0] += " ";
        int testValue1 = java.lang.Character.getNumericValue(coordinates[0].trim().charAt(0));
        int testValue2 = Integer.parseInt(coordinates[1].trim());
        if( testValue1 > 19 ||testValue2 > 10 || testValue1 < 0 || testValue2 < 0 ) 
          throw new InvalidateCoordinatesException();
        System.out.println("x: "+coordinates[0]);
        System.out.println("y: "+coordinates[1]);
        
        int[] shootingPos = toDigit(coordinates);
        map.shoot(shootingPos[1],shootingPos[0]);
        map.printMap(1);
        map.scoreBoard(1);
      }
      catch (InvalidateCoordinatesException e){ //catches valid input but out of bounds of grid. 
        continue;
      }
      catch (ArrayIndexOutOfBoundsException e ){ // catches invalid inputs
        System.out.println("Invalid input, don't try and hack me please.");
        continue;
      }
      catch (NumberFormatException v){ //catches valid input but out of bounds of grid. 
        System.out.println("\nAlmost broke me, you HACKER! but" +
                           "\nalas, I have many exceptions and you shall not destory me.");
        continue;
      }
      
    }
    map.scoreBoard(0);
  }
  public static boolean isADigit(String toCheck) throws NumberFormatException{//checks if value is a digit
    try {
      int result = Integer.parseInt(toCheck); 
    }
    catch (NumberFormatException e){
      return false;
    }
    return true;  
  }
  public static int[] toDigit(String[] translate){//turns letters to digits
    int charValue;
    int[] result = new int[translate.length];
    for (int x = 0; x < translate.length; ++x){
      try {
        result[x] = (Integer.parseInt(translate[x].trim())-1); 
      }
      catch( NumberFormatException e){
        translate[x].toUpperCase();
        charValue = java.lang.Character.getNumericValue(translate[x].charAt(0));
        switch (charValue){
          case 10: result[x] = 0;
          break;
          case 11: result[x] = 1;
          break;
          case 12: result[x] = 2;
          break;
          case 13: result[x] = 3;
          break;
          case 14: result[x] = 4;
          break;
          case 15: result[x] = 5;
          break;
          case 16: result[x] = 6;
          break;
          case 17: result[x] = 7;
          break;
          case 18: result[x] = 8;
          break;
          case 19: result[x] = 9;
          break;
        }
      }
    }
    return result;
  }
}
class Map{//map object
  private int shipCount;
  private String fileName;
  private int hits;
  private int tries;
  private char[][] recordGrid;
  public Map() throws FileNotFoundException{//constructor
    makeTempMap();
    hits = 0; 
    tries = 0;
  }
  public void makeTempMap() throws FileNotFoundException{//loads and fills record gird from map file
    try {
      String buffer;
      char[][] mapFile = new char[10][10];
      Scanner nameInput = new Scanner(System.in);
      System.out.println("Enter file name: "); 
      String name = nameInput.nextLine();
      File myFile = new File(name);
      if(!myFile.exists())
        throw new MapNotFoundException();
      Scanner fileReader = new Scanner(myFile);
      shipCount = Integer.parseInt(fileReader.nextLine());
      for (int x = 0; x < mapFile.length; x++){
        try{
          buffer = fileReader.nextLine();
        } catch (NoSuchElementException e){
          buffer = "          ";
        }
        for (int y = 0; y < mapFile[x].length; y++){
          if(buffer.length() == 0){
            mapFile[x][y] = ' ';
          }
          else
            mapFile[x][y] = buffer.charAt(y);
        }
      }
      recordGrid = copy(mapFile);
    }
    catch(MapNotFoundException ex) {
      makeTempMap();
    }
    
  }
  public void printMap(){//prints map at end of game
    char[][] printable = copy(recordGrid);
    int legend = 1;
    System.out.println("    A-B-C-D-E-F-G-H-I-J");
    for (int x = 0; x < 10; x++){
      if(legend < 10)
        System.out.print(" " + legend + ": ");
      else
        System.out.print((legend)+ ": ");
      legend++;
      for (int y = 0; y < 10; y++){
        System.out.print(printable[x][y]+" ");
      }
      System.out.print("\n");
    }
  }
  public void printMap(int signal){//prints map during game
    char[][] printable = copy(recordGrid);
    int legend = 1;
    System.out.println("    A-B-C-D-E-F-G-H-I-J");
    for (int x = 0; x < 10; x++){
      if(legend < 10)
        System.out.print(" " + legend + ": ");
      else
        System.out.print((legend)+ ": ");
      legend++;
      for (int y = 0; y < 10; y++){
        if (printable[x][y] == 'o')
          System.out.print("  ");
        else
          System.out.print(printable[x][y]+" ");
      }
      System.out.print("\n");
    }
  }
  public char[][] copy(char[][] temp){//copy method
    char[][] result = new char[10][10];
    for (int x = 0; x < 10; x++){
      for (int y = 0; y < 10; y++){
        result[x][y] = temp[x][y];
      }
    }
    return result;
  }
  public char[][] getMap(){
    return copy(recordGrid);
  }
  public int getShips(){// getters
    return shipCount; 
  }
  public int getTries(){
    return tries; 
  }
  public int getHits(){
    return hits; 
  }
  
  public void setMap(char[][] map){//can change map
    recordGrid = copy(map);
  }
  public void shoot(int x, int y){//shoots missiles
    ++tries;
    if(recordGrid[x][y] == 'o'){
      System.out.println("EXPLOSIVE HIT");
      recordGrid[x][y] = 'X'; 
      hits++;
    }
    else if(recordGrid[x][y] == 'X'){
      System.out.println("HOW DARE YOU SHOOT A SUNK SHIP!");//if they shoot the same ship
    }
    else if (recordGrid[x][y] == ' ')
    {
      System.out.println("TRAGIC MISS!");//miss
      recordGrid[x][y] = '-';
    }
  }
  public void scoreBoard(int inGame){//Scoreboard, has different versions depending on scenario
    if(inGame == 0){
      if (hits == shipCount){
        System.out.printf("SUCCES!\nSailor! You shot %d missles,  only %d hit the enemy fleet. YOU HAVE WON!!!", tries, hits);//prints if you win for winning
      }
      else if (tries == 50){
        System.out.printf("FAILED MISSION!\nSailor! You shot %d missles,  only %d hit the enemy fleet.", tries, hits);//prints if you run out of tries 
        printMap();
      }
      else{
        System.out.printf("GOODBYE!\nSailor! You shot %d missles,  only %d hit the enemy fleet.\nOur spies"+ //Prints of exits mid game
                          "have uncovered this map from the enemy admiral.\n\n", tries, hits);  
        printMap();
      }
    }
    else{
      System.out.printf("SCORE BOARD:\nSailor! You have shot %d missles so far and of those\n%d"+
                        "have hit the enemy fleet.\n", tries, hits);  
    }
  }
}
class MapNotFoundException extends Exception{ //exception for bad file name
  public MapNotFoundException(){
    super();
    System.out.println("Map not found exception!");
  }
}
class InvalidateCoordinatesException extends Exception{ //exception for bad inputs
  public InvalidateCoordinatesException(){
    System.out.println("Invalid coordinate exception, please use A-J and 1 - 10. "); 
  }
}
