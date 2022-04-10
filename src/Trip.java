public class Trip {
    double weight;
    int start;
    Stop destination;

    Trip(double distance,int start, Stop destination)
    {
        this.weight=distance;
        this.start=start;
        this.destination=destination;
    }
}