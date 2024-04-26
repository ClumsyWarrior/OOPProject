package Part2;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JTextArea;

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
    private JTextArea raceArea;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance, JTextArea raceArea)
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

    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber == -1 || laneNumber > horses.size()){
            horses.add(theHorse);
            System.out.println(horses);
            
        }
        else {
            horses.set(laneNumber - 1, theHorse); // Replace the None value with the horse object
            System.out.println(horses);
        }
    }
    
    public void removeHorse(int laneNumber)
    {
        if (laneNumber == 1 && horses.size() == 1) {
            this.horses = new ArrayList<>();
        }
        else if (laneNumber > 1 && laneNumber <= horses.size()) {
            this.horses.remove(laneNumber - 1);
        }
        else {
            this.horses = new ArrayList<>();
        }
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
                TimeUnit.MILLISECONDS.sleep(10);
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

    private void printRace() {
        StringBuilder raceState = new StringBuilder();
    
        multiplePrint(raceState, '=', raceLength + 3); // top edge of track
        raceState.append('\n');
    
        for (Horse horse : horses) {
            if (horse != null) {
                printLane(raceState, horse);
                raceState.append('\n');
            } else {
                raceState.append('|');
                multiplePrint(raceState, ' ', raceLength + 1);
                raceState.append("|\n");
            }
        }
    
        multiplePrint(raceState, '=', raceLength + 3); // bottom edge of track
        raceState.append('\n');
    
        raceArea.setText(raceState.toString());
    }
    
    private void printLane(StringBuilder sb, Horse theHorse) {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
    
        //append a | for the beginning of the lane
        sb.append('|');
    
        //append the spaces before the horse
        multiplePrint(sb, ' ', spacesBefore);
    
        //if the horse has fallen then append dead
        //else append the horse's symbol
        if(theHorse.hasFallen()) {
            sb.append('\u2322');
        } else {
            sb.append(theHorse.getSymbol());
        }
    
        //append the spaces after the horse
        multiplePrint(sb, ' ', spacesAfter);
    
        //append the | for the end of the track
        sb.append("| " + theHorse.getName() + " (Current confidence " + theHorse.getConfidence() + ")");
    }
    
    /***
     * append a character a given number of times.
     * e.g. multiplePrint(sb, 'x', 5) will append: xxxxx
     * 
     * @param sb the StringBuilder to append to
     * @param aChar the character to append
     * @param times the number of times to append the character
     */
    private void multiplePrint(StringBuilder sb, char aChar, int times) {
        for (int i = 0; i < times; i++) {
            sb.append(aChar);
        }
    }
}