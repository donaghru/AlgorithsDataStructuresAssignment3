import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class mainLine
{
    public static void main(String[] args)
    {
        Stop[] stops=setup();
        boolean exitFlag=false;
        Scanner input = new Scanner(System.in);
        while(!exitFlag)
        {
            System.out.print("Welcome to Vancouver Bus Service");
            String newInput = input.next();
            if (newInput.equalsIgnoreCase("quit")) exitFlag = true;
            else if(newInput.equalsIgnoreCase("play"))
            {}
        }
    }

    static Stop[] setup()
    {
        try
        {
            FileReader fileReaderStops = new FileReader("stops.txt");   //reads in file
            BufferedReader bufferedReaderStops = new BufferedReader(fileReaderStops);
            int stopNumber=12479;
            boolean endOfFile = false;  //tracks end of file
            Stop[] stops= new Stop[stopNumber];
            for(int i=0;i<stopNumber;i++)
            {
                Stop newStop=new Stop(i);
                stops[i]=newStop;
            }
            fileReaderStops.close();
            bufferedReaderStops.close();
            FileReader fileReaderTrips = new FileReader("stop_times.txt");   //reads in file
            BufferedReader bufferedReaderTrips = new BufferedReader(fileReaderTrips);
            bufferedReaderTrips.readLine();
            Stop prevStop=null;
            int prevTripID=-1;
            while(!endOfFile)   //loop persists until end of file
            {
                String inputDetails = bufferedReaderTrips.readLine();   //read street information
                if (inputDetails != null)
                {
                    String[] tripData = inputDetails.trim().split(",");   //separates information into array of three pieces of data
                    Stop currentStop = stops[Integer.parseInt(tripData[3])];   //creates vertex at start of edge from array info
                    int tripID= Integer.parseInt(tripData[0]);
                    if((tripID==prevTripID)&&(prevStop!=null))
                    {
                        boolean accountedFor=false;
                        for(int i=0;i<prevStop.adj();i++)
                        {
                            if(prevStop.trips.get(i).destination==currentStop)accountedFor=true;
                        }
                        if(!accountedFor)prevStop.createTrip(1,currentStop);
                        prevStop.used=true;
                    }     //creates new edge with weight and endpoint data, and stores it in the vertex at the start of the edge
                    prevStop = currentStop;
                    prevTripID = tripID;
                }
                else
                {
                    endOfFile = true;   //ends loop once file is empty
                }
            }
            FileReader fileReaderTransfers = new FileReader("transfers.txt");   //reads in file
            BufferedReader bufferedReaderTransfers = new BufferedReader(fileReaderTransfers);
            bufferedReaderTransfers.readLine();
            endOfFile = false;
            while(!endOfFile)   //loop persists until end of file
            {
                String inputDetails = bufferedReaderTransfers.readLine();   //read street information
                if (inputDetails != null)
                {
                    String[] transferData = inputDetails.trim().split(",");   //separates information into array of three pieces of data
                    Stop currentStop = stops[Integer.parseInt(transferData[0])];   //creates vertex at start of edge from array info
                    Stop currentDestination = stops[Integer.parseInt(transferData[1])];    //creates vertex at end of edge from array info
                    boolean accountedFor=false;
                    for (int i = 0; i < currentStop.adj(); i++)
                    {
                        if (currentStop.trips.get(i).destination == currentDestination) accountedFor = true;
                    }
                    if (!accountedFor)
                    {
                        if(Integer.parseInt(transferData[2])==0)currentStop.createTrip(2, currentDestination);
                        else currentStop.createTrip(Double.parseDouble(transferData[3])/100, currentDestination);
                    }
                }     //creates new edge with weight and endpoint data, and stores it in the vertex at the start of the edge
                else
                {
                    endOfFile = true;   //ends loop once file is empty
                }
            }
            fileReaderTransfers.close();
            bufferedReaderTransfers.close();
            return stops;
        } // End try
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param stops: the list of all vertices to find the distances between
     * @param start: vertex to start all computing most efficient paths from from
     * @param dest: destination to find path to
     * @return double: returns distance to furthest vertex from start vertex, using the most efficient journey
     */
    double maxDijkstra(Stop[] stops,int start,int dest)
    {
        double[] distTo = new double[stops.length];  //array stores distances from start to every other vertex
        Queue pq = new Queue();
        for (int v = 0; v < stops.length; v++) distTo[v] = Double.POSITIVE_INFINITY;    //initialises paths to infinity
        distTo[start] = 0.0;    //initialises distance from vertex to itself as 0
        pq.insert(start, 0.0);  //priority queue of vertexes to find paths for
        while (!pq.isEmpty())   //finds journeys to all other vertexes according to priority
        {
            int v = pq.delMin();
            for (int i=0; i< stops[v].adj();i++)
                relax(stops[v],stops[v].trips.get(i),distTo,pq);
        }
        return distTo[dest];
    }

    /**
     * @param v: vertex to start path from
     * @param e: edge to between vertexes
     * @param distTo: current distances of edges to edit
     * @param pq: priority queue of vertexes to check
     */
    private void relax(Stop v, Trip e, double[] distTo, Queue pq)    //adjusts journey distance if more efficient path found
    {
        int w = e.destination.number;
        int y= v.number;
        if (distTo[w] > distTo[y] + e.weight) //checks if current path is longer than current path and remaining distance
        {
            distTo[w] = distTo[y] + e.weight;
            boolean contained=pq.decreaseKey(w, distTo[w]); //makes vertex less immediate if closer to start
            if(!contained) pq.insert (w, distTo[w]);    //adds vertex to queue if not already
        }
    }
}