import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private ArrayList<Horse> horses;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        horses = new ArrayList<Horse>();
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */

    public void addHorse(Horse theHorse)
    {
        horses.add(theHorse);
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        for (Horse horse : horses) {
            if (horse != null) {
                horse.goBackToStart();
            }
        }
        
        // Create an ArrayList to keep track of the winners
        ArrayList<Horse> winners = new ArrayList<>();
                      
        while (!finished)
        {
            //move each horse
            for (Horse horse : horses) {
                if (horse != null) {
                    moveHorse(horse);
                }
            }
                            
            //print the race positions
            printRace();
            
            // Check if each horse has won and add them to the winners list
            for (Horse horse : horses) {
                if (horse != null && raceWonBy(horse)) {
                    horse.setConfidence(horse.getConfidence() + 0.1);
                    if (horse.getConfidence() > 1) {
                        horse.setConfidence(1);
                    }
                    winners.add(horse);
                }
            }
            
            // If any horse has won, the race is finished
            if (!winners.isEmpty())
            {
                // If there's more than one winner, it's a tie
                if (winners.size() > 1)
                {
                    System.out.println("It's a tie between the following horses:");
                    for (Horse winner : winners)
                    {
                        System.out.println(winner.getName());
                    }
                }
                else
                {
                    System.out.println("The winner is " + winners.get(0).getName());
                }
                finished = true;
            }
            //if all horses have fallen
            else if (allHorsesHaveFallen()) {
                System.out.println("No Winner! All horses have fallen");
                System.out.println();
                finished = true;
            }
            try{ 
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){}
        }
    }
    
    private boolean allHorsesHaveFallen() {
        for (Horse horse : horses) {
            if (horse != null && !horse.hasFallen()) {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but also depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
                theHorse.setConfidence(theHorse.getConfidence() - 0.1);
                if (theHorse.getConfidence() <0) {
                    theHorse.setConfidence(0);
                }

            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
        
            // Windows
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } 
    
            // Unix or Linux or Mac
            else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                new ProcessBuilder("/bin/bash", "-c", "clear").inheritIO().start().waitFor();
            }
    
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();
        
        for (Horse horse : horses) {
            if (horse != null) {
                printLane(horse);
                System.out.println();
            }
            
            else {
                System.out.print('|');
                multiplePrint(' ', raceLength+1);
                System.out.println('|');
            }
        }
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('\u2322');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print("| " + theHorse.getName() + " (Current confidence " +theHorse.getConfidence() + ")");
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
