import java.util.ArrayList;
import java.util.List;


public class Vertex
{
    private String cityName;
    private List<String> attractionList;

    //Constructor
    public Vertex(String cityName)
    {
        this.cityName = cityName;
        attractionList = new ArrayList<String>();
    }

    //Get city name
    public String getCityName()
    {
	return cityName;
    }

    //Get attraction list
    public List<String> getAttractionList()
    {
	return attractionList;
    }
}

