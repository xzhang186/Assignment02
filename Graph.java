import java.util.*;


public class Graph
{
    private List<List<Edge>> edgeList;
    private List<Vertex> vertexList;
    private LinkedList<Integer> attractionList;

    //Constructor
    public Graph()
    {
        edgeList = new ArrayList<List<Edge>>();
        vertexList = new ArrayList<Vertex>();
	attractionList = new LinkedList<Integer>();
    }

    //Add edge
    public void addEdge(String cityNamedge1, String cityNamedge2, int distance)
    {
        if(!checkVertex(cityNamedge1))
        {
            Vertex vertex1 = new Vertex(cityNamedge1);
            vertexList.add(vertex1);
            edgeList.add(new ArrayList<Edge>());
        }

        if(!checkVertex(cityNamedge2))
        {
            Vertex vertex2 = new Vertex(cityNamedge2);
            vertexList.add(vertex2);
            edgeList.add(new ArrayList<Edge>());
        }

	int vertex1 = vertexIndex(cityNamedge1);
        int vertex2 = vertexIndex(cityNamedge2);

	//edge1
        Edge edge1 = new Edge();
        edge1.setVertex1(vertex1);
        edge1.setVertex2(vertex2);
        edge1.setDistance(distance);
        
	//add to list
	List<Edge> edgeLists1 = edgeList.get(vertex1);
        edgeLists1.add(edge1);

	//edge2
        Edge edge2 = new Edge();
	edge2.setVertex1(vertex2);
        edge2.setVertex2(vertex1);
        edge2.setDistance(distance);

	//add to list
        List<Edge> edgeLists2 = edgeList.get(vertex2);
        edgeLists2.add(edge2);
    }

    //Check vertex
    public boolean checkVertex(String cityName)
    {
        return vertexIndex(cityName) >= 0;
    }

    //Vertex index
    public int vertexIndex(String cityName)
    {
        for(int index = 0; index < vertexList.size(); index++)
        {
            if(vertexList.get(index).getCityName().equalsIgnoreCase(cityName))
            {
                return index;
            }
        }

        return -1;
    }


    //Add the list of the city name
    public boolean addPlaceToVertex(String cityName, String place)
    {
        if(!checkVertex(cityName))
	{
            return false;
	}

        int vertexIndex = vertexIndex(cityName);
        vertexList.get(vertexIndex).getAttractionList().add(place);

        return true;
    }

    //Add the name and index of the city name to the list
    public boolean placeRoute(String place)
    {
        for(int i = 0; i < vertexList.size(); i++)
        {
            Vertex vertex = vertexList.get(i);

            if(vertex.getAttractionList().contains(place))
            {
                attractionList.add(i);
                return true;
            }
        }

        return false;
    }

    //Dijkstra algorithm
    public void dijkstra(int source, List<Integer> distance, List<Integer> previousious)
    {
        List<Integer> list = new ArrayList<Integer>();

        int ai;
	int bi;
        int alternate;
	int minDistance;
	int currentDistance;
        int a;
	int b;
        
	for(b = 0; b < vertexList.size(); b++)
        {
            distance.add(-1);
            previousious.add(-1);
            list.add(b);
        }
        
	distance.set(source,0);

        while(!list.isEmpty())
        {
            ai = minDistance = -1;
            
	    for(bi = 0; bi < list.size(); bi++)
            {
                b = list.get(bi);
                currentDistance = distance.get(b);

                if(currentDistance >= 0 && (ai < 0 || currentDistance < minDistance))
                {
                    ai = bi;
                    minDistance = currentDistance;
                }
            }

            a = list.get(ai);
            list.remove(ai);


            for(bi = 0; bi < edgeList.get(a).size(); bi++)
            {
                b = edgeList.get(a).get(bi).getVertex2();

                if(list.contains(b))
                {
                    alternate = distance.get(a) + edgeList.get(a).get(bi).getDistance();

                    if(distance.get(b) < 0 || alternate < distance.get(b))
                    {
                        distance.set(b,alternate);
                        previousious.set(b,a);
                    }
                }
            }
        }
    }

    //find route
    public LinkedList<String> findRoute(String sourceCity, String destinationCity)
    {
        int source = vertexIndex(sourceCity);
        int destination = vertexIndex(destinationCity);

	//if there are no places
        boolean places = !attractionList.isEmpty();
        
	if(!places)
	{
            attractionList.add(source);
	}

	List<LinkedList<Integer>> lp = permutation(attractionList);
        int nump = attractionList.size();

        if(places)
	{
            attractionList.add(source);
	}

	attractionList.add(destination);

        int sourceNum = nump;
        int destinationNum = nump + 1;
        
	if(!places)
        {
            sourceNum = 0;
            destinationNum = 1;
        }

	//find all shrtest paths
        List<ArrayList<Integer>> distination1 = new ArrayList<ArrayList<Integer>>();
        List<ArrayList<Integer>> previous1 = new ArrayList<ArrayList<Integer>>();
        
	for(int pi = 0; pi < attractionList.size(); pi++)
        {
            int a = attractionList.get(pi);

            ArrayList<Integer> dist = new ArrayList<Integer>();
            ArrayList<Integer> previous = new ArrayList<Integer>();
            dijkstra(a,dist,previous);
            distination1.add(dist);
            previous1.add(previous);
        }

        int dist = -1;
        int min = -1;

        for(int n = 0; n < lp.size(); n++)
        {
            LinkedList<Integer> permutation = lp.get(n);
            int currentDistance = 0;
	    //the first path
            int destination2 = permutation.get(0);
            currentDistance += distination1.get(sourceNum).get(destination2);

	    //the next paths
            for(int i = 0; i < permutation.size() - 1; i++)
            {
                int p1 = permutation.get(i);
                int p2 = permutation.get(i+1);
                int p1i = attractionList.indexOf(p1);
                int p2i = attractionList.indexOf(p2);

                currentDistance += distination1.get(p1i).get(p2);
                List<Integer> path = getPath(p1,p2,previous1.get(p1i));
            }

	    //the lastest path
            destination2 = permutation.get(permutation.size()-1);
            currentDistance += distination1.get(sourceNum).get(destination2);

            List<Integer> bestpath = getPath(destination,destination2,previous1.get(destinationNum));
            Collections.reverse(bestpath);

            if(n == 0 || dist > currentDistance)
            {
                dist = currentDistance;
                min = n;
            }

        }

	//finally
        LinkedList<String> finalPath = new LinkedList<String>();
        LinkedList<Integer> permutationMin = lp.get(min);
        permutationMin.addFirst(source);
        permutationMin.addLast(destination);

        for(int i = 0; i < permutationMin.size()-1; i++)
        {
            int p1 = permutationMin.get(i);
            int p2 = permutationMin.get(i+1);
            int p1i = attractionList.indexOf(p1);
            int p2i = attractionList.indexOf(p2);
            List<Integer> path = getPath(p1,p2,previous1.get(p1i));

            for(int j = 0; j < path.size()-1; j++)
	    {
                finalPath.addLast(vertexList.get(path.get(j)).getCityName());
	    }
	}

        finalPath.addLast(vertexList.get(destination).getCityName());

        return finalPath;
    }

    //path
    private LinkedList<Integer> getPath(int source, int destination, List<Integer> previousious)
    {
        LinkedList<Integer> list = new LinkedList<Integer>();

        int pos = destination;

        if(pos == source || previousious.get(pos) >= 0)
        {
            while(pos >= 0)
            {
                list.addFirst(pos);
                pos = previousious.get(pos);
            }
        }

        return list;
    }

    //permutation
    private static LinkedList<LinkedList<Integer>> permutation(LinkedList<Integer> l)
    {
        LinkedList<LinkedList<Integer>> list = new LinkedList<LinkedList<Integer>>();

        int s = l.size();


        if(s == 1)
        {
            LinkedList<Integer> l1 = new LinkedList<Integer>();
            l1.addLast(l.get(0));
            list.addLast(l1);
        }
        else
        {
            for(int i = 0; i < s; i++)
            {
                LinkedList<Integer> l1 = new LinkedList<Integer>();

		//add all elements
                for(int j = 0; j < s; j++)
                {
                    if(j != i)
                    {
                        l1.add(l.get(j));
                    }
                }

		//recursively with shorter array
                LinkedList<LinkedList<Integer>> list1 = permutation(l1);

		//add element to the end.
                for(int j = 0; j < list1.size(); j++)
                {
                    LinkedList<Integer> array = list1.get(j);
                    array.add(0,l.get(i));
		    list.add(array);
                }
            }
        }

        return list;
    }


    
}

