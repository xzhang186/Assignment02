import java.util.*;
import java.io.*;


public class RoadTrip
{
    // distanceList
    private List<Integer> distanceList= new ArrayList();

    //Get distance list
    public List<Integer> getDistanceList()
    {
	return distanceList;
    }
    
    //Route
    public List<String> route(String start, String end, List<String> attractions)
    {
	LinkedList<String> route = new LinkedList<String>();
        Graph graph = new Graph();
	String line = null;
        String roadFileName = "roads.csv";
        String attractionFileName= "attractions.csv";
	BufferedReader reader = null;

        try
	{
	    reader = new BufferedReader(new FileReader(roadFileName));

	    while ((line = reader.readLine()) != null)
            {
                String[] info = line.split(",");
                graph.addEdge(info[0],info[1],Integer.parseInt(info[2]));
                distanceList.add(Integer.parseInt(info[2]));
            }

	    reader.close();
	}
	catch(IOException e)
	{
	    System.out.println("IOException: " + e);
	    return route;
	}

	try
        {
	    reader = new BufferedReader(new FileReader(attractionFileName));

	    while ((line = reader.readLine()) != null)
            {
                String[] data = line.split(",");
                graph.addPlaceToVertex(data[1],data[0]);
            }

	    reader.close();
        }
        catch(IOException e)
        {
	    System.out.println("Exception: " + e);
            return route;
        }

        for(String place: attractions)
	{
            graph.placeRoute(place);
	}

        route = graph.findRoute(start,end);

        return route;
    }


    //Main
    public static void main(String[] args)
    {
	RoadTrip roadTrip = new RoadTrip();
        Scanner scanner = new Scanner(System.in);
        List<String> attractions = new ArrayList<String>();
        
	System.out.print("Please enter the start city name:\n" );
        String start = scanner.nextLine();

        System.out.print("Please enter the end city name: \n");
        String end = scanner.nextLine();

	while(true)
        {
            System.out.println("\nPlease enter an attraction name (Press OK to EOF): ");
            String attraction = scanner.nextLine();

            if("OK".equalsIgnoreCase(attraction))
	    {
		break;
	    }

	    if(attraction != null)
	    {
	        attractions.add(attraction);
	    }
        }

        List<String> route = roadTrip.route(start,end,attractions);

	int distance = 0;

        for(int i = 0; i < route.size(); i++)
	{
            distance += roadTrip.getDistanceList().get(i);
        }

        System.out.printf("Route: " + route +"\nDistance: " + distance + "\nAttractions: ");
        
	for(int j = 0; j < attractions.size(); j++)
	{
            System.out.printf(attractions.get(j)+", ");
        }
	System.out.println("");
    }
}
