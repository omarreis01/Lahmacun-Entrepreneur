/**
 * The City class represents a city within the context of a company, containing branches and specific attributes.
 */
public class City {
    public String name;
    public myDict<String, Branch> branches = new myDict<>();
    /**
     * Constructs a City object with the specified name.
     *
     * @param name The name of the city.
     */
    City(String name){
        this.name = name;
    }

}
