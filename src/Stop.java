import java.util.ArrayList;

public class Stop
{
    int number;
    ArrayList<Trip> trips = new ArrayList<Trip>();
    boolean used;

    Stop(int number)
    {
        this.number = number;
        used=false;
    }

    void createTrip(double distance, Stop destination)
    {
        Trip newTrip = new Trip(distance, number, destination);
        trips.add(newTrip);
    }

    int adj() {
        return trips.size();
    }
}