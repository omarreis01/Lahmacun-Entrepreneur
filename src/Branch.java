import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
/**
 * The Branch class represents a branch of a company with specific attributes and operations.
 */
public class Branch {
    public String name;
    public String city;
    public String manager;
    public int monthBranch=1;
    public boolean changeManager=false;
    public int overall;
    public int monthly;
    public int numberCook;
    public int numberCashier;
    public int numberCourier;
    public String toBeCook="";
    public String toKickCashier ="";
    public String toKickCook="";
    public String toKickCourier ="";
    // the list that stores cook who can be manager
    public Queue<String> toBeManager = new LinkedList<>();
    // it stores the people who belong to this branch
    public myDict<String, Person> people = new myDict<>();
    /**
     * Constructs a Branch object with the specified name and city.
     *
     * @param name The name of the branch.
     * @param city The city in which the branch is located.
     */
    Branch(String name,String city){
        this.name = name;
        this.city = city;
    }



}
