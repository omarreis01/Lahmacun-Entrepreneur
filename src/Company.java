import java.io.FileWriter;
import java.io.IOException;
/**
 * The Company class represents a company and manages its employees, branches, and operations.
 */
public class Company {
    // Cities
    public static myDict<String, City> cities = new myDict<>();
    // how many times monthly score should be zero
    public static int timeCompany=0;
    /**
     * Default constructor for the Company class.
     */
    Company(){}
    /**
     * Adds a new member to the company with the specified details and updates the output file.
     *
     * @param name   The name of the new member.
     * @param city   The city where the member works.
     * @param branch The branch where the member works.
     * @param job    The job position of the member.
     * @param output The FileWriter used for writing output to a file.
     * @throws IOException If an I/O error occurs during file writing.
     */

    public void addNewMember(String name,String city,String branch,String job,FileWriter output) throws IOException {
        if(cities.contains(city)){
            if((cities.get(city).branches.contains(branch))){
                // if there is already such people
                if(cities.get(city).branches.get(branch).people.contains(name)){
                    output.write("Existing employee cannot be added again."+"\n");
                    output.flush();
                }
                else {
                    //add the new person
                    Person name1 = new Person(name, city, branch, job);
                    cities.get(city).branches.get(branch).people.put(name, name1);
                    //if it is manager set it to manager
                    if(job.equals("MANAGER")) {
                        cities.get(city).branches.get(branch).manager = name;
                    }
                    // if it is cook check whether there is one cook which should be dismissed or want to promote, if it is, make the arrangements
                    else if(job.equals("COOK")){
                        if(!cities.get(city).branches.get(branch).toBeManager.isEmpty()&& cities.get(city).branches.get(branch).numberCook==1){
                            if(cities.get(city).branches.get(branch).changeManager) {
                                String newName = cities.get(city).branches.get(branch).toBeManager.poll();
                                if(cities.get(city).branches.get(branch).people.get(newName).promotion>=10) {
                                    cities.get(city).branches.get(branch).people.get(newName).promote(output);
                                }
                            }
                        }
                        else if(cities.get(city).branches.get(branch).numberCook==1){
                            if(!cities.get(city).branches.get(branch).toKickCook.isEmpty()){
                                output.write(cities.get(city).branches.get(branch).toKickCook+" is dismissed from branch: " +branch +"." +"\n");
                                output.flush();
                                cities.get(city).branches.get(branch).people.remove(cities.get(city).branches.get(branch).toKickCook);
                                cities.get(city).branches.get(branch).numberCook--;
                                cities.get(city).branches.get(branch).toKickCook="";
                            }
                        }
                    }
                    // if it is cashier check whether there is one cashier which should be dismissed or want to promote, if it is, make the arrangements
                    else if(job.equals(("CASHIER"))){
                        if (cities.get(city).branches.get(branch).numberCashier==1 ){
                            if(!cities.get(city).branches.get(branch).toBeCook.isEmpty()){
                                cities.get(city).branches.get(branch).people.get(cities.get(city).branches.get(branch).toBeCook).promote(output);
                            }
                            else if(!cities.get(city).branches.get(branch).toKickCashier.isEmpty()){
                                output.write(cities.get(city).branches.get(branch).toKickCashier+" is dismissed from branch: " +branch +"."+"\n");
                                output.flush();
                                cities.get(city).branches.get(branch).people.remove(cities.get(city).branches.get(branch).toKickCashier);
                                cities.get(city).branches.get(branch).numberCashier--;
                                cities.get(city).branches.get(branch).toKickCashier="";
                            }
                        }
                    }
                    // if it is cashier check whether the is one courier should be dismissed,if it is, kick the other
                    else if(job.equals("COURIER")){
                        if (cities.get(city).branches.get(branch).numberCourier==1 ){
                            if(!cities.get(city).branches.get(branch).toKickCourier.isEmpty()){
                                output.write(cities.get(city).branches.get(branch).toKickCourier+" is dismissed from branch: " +branch +"."+"\n");
                                output.flush();
                                cities.get(city).branches.get(branch).people.remove(cities.get(city).branches.get(branch).toKickCourier);
                                cities.get(city).branches.get(branch).numberCourier--;
                                cities.get(city).branches.get(branch).toKickCourier="";
                            }
                        }
                    }
                    // increase the number of job in the branch
                    addNumber(name,city,branch,job);
                }
            }
            else{
                // if it is new branch create branch
                Branch branch1 = new Branch(branch,city);
                Person name1 = new Person(name,city,branch,job);
                branch1.people.put(name,name1);
                cities.get(city).branches.put(branch,branch1);
                if(job.equals("MANAGER")){
                    cities.get(city).branches.get(branch).manager=name;
                }
                addNumber(name,city,branch,job);
            }
        }
        else{
            //if it is new city create new city and branch
            City city1 = new City(city);
            Branch branch1 = new Branch(branch,city);
            Person name1 = new Person(name,city,branch,job);
            branch1.people.put(name,name1);
            city1.branches.put(branch,branch1);
            cities.put(city,city1);
            if(job.equals("MANAGER")){
                cities.get(city).branches.get(branch).manager=name;
            }
            addNumber(name,city,branch,job);
        }
    }
    /**
     * Updates the bonus and promotion of a member in the company and writes the result to the output file.
     *
     * @param name        The name of the member whose performance is updated.
     * @param city        The city where the member works.
     * @param branch      The branch where the member works.
     * @param totalBonus  The total bonus amount for the performance update.
     * @param output      The FileWriter used for writing output to a file.
     * @throws IOException If an I/O error occurs during file writing.
     */
    public void performanceUpdate(String name,String city,String branch,int totalBonus,FileWriter output) throws IOException {
        if(cities.contains(city)) {
            if ((cities.get(city).branches.contains(branch))) {
                if (cities.get(city).branches.get(branch).people.contains(name)) {
                    cities.get(city).branches.get(branch).people.get(name).processMonthlyUpdate(totalBonus,output);
                    // after update if its promotion point is lower from -5, dismiss the employee
                    if(cities.get(city).branches.get(branch).people.get(name).checkDismis()){
                        deleteNewMember(name,city,branch,1,output);
                    }
                }
                else{
                    output.write("There is no such employee."+"\n");
                    output.flush();

                }
            }
            else{
                output.write("There is no such employee."+"\n");
                output.flush();

            }
        }
        else{
            output.write("There is no such employee."+"\n");
            output.flush();

        }

    }
    /**
     * Deletes a member from the company and updates the output file.
     *
     * @param name   The name of the member to be deleted.
     * @param city   The city where the member works.
     * @param branch The branch where the member works.
     * @param number An indicator for dismissal or voluntary leave (0 for leave, 1 for dismissal).
     * @param output The FileWriter used for writing output to a file.
     * @throws IOException If an I/O error occurs during file writing.
     */

    public void deleteNewMember(String name,String city,String branch,int number,FileWriter output) throws IOException {
        if(cities.contains(city)){
            if((cities.get(city).branches.contains(branch))){
                if(cities.get(city).branches.get(branch).people.contains(name)){
                    //check if it is manager
                    if(cities.get(city).branches.get(branch).people.get(name).job.equals("MANAGER")) {
                        //if there is no one to replace him, if it wants to leave give  bonus,
                        if (cities.get(city).branches.get(branch).toBeManager.isEmpty()) {
                            //if it dismissed changeManager variable set to true so if new member added it can replace it
                            if(number==1){
                                cities.get(city).branches.get(branch).changeManager=true;
                            }
                            //if it wants to leave give bonus
                            else if(number==0) {
                                if(cities.get(city).branches.get(branch).people.get(name).promotion>-5){
                                    if(Company.cities.get(city).branches.get(branch).monthBranch!=Company.timeCompany) {
                                        Company.cities.get(city).branches.get(branch).monthly = 0;
                                        Company.cities.get(city).branches.get(branch).monthBranch = Company.timeCompany;
                                    }
                                    cities.get(city).branches.get(branch).people.get(name).bonus += 200;
                                    cities.get(city).branches.get(branch).overall += 200;
                                    cities.get(city).branches.get(branch).monthly += 200;

                                }
                            }
                        }
                        //if there is at least one people who can replace it
                        else{
                            //check the numberCook if it is bigger than 1, take the cook and promote it, then dismiss manager
                            if(cities.get(city).branches.get(branch).numberCook>1){
                                String newName =cities.get(city).branches.get(branch).toBeManager.peek();
                                if(Company.cities.get(city).branches.get(branch).people.contains(newName)&&Company.cities.get(city).branches.get(branch).people.get(newName).promotion>=10){
                                    newName= cities.get(city).branches.get(branch).toBeManager.poll();
                                    if(number ==0) {
                                        output.write(name + " is leaving from branch: " + branch + "."+"\n");
                                        output.flush();
                                    }
                                    else{
                                        output.write(name + " is dismissed from branch: " + branch + "."+"\n");
                                        output.flush();
                                    }
                                    cities.get(city).branches.get(branch).people.get(newName).promote(output);
                                    cities.get(city).branches.get(branch).people.remove(name);
                                }
                                //if toBeManager list's first person is not suitable to be manager(leave or decrease in promotion)
                                else{
                                    // itarete until finding a suitable one to be manager
                                    boolean m=false;
                                    while (!Company.cities.get(city).branches.get(branch).people.contains(newName)||Company.cities.get(city).branches.get(branch).people.get(newName).promotion<10) {
                                        if(Company.cities.get(city).branches.get(branch).toBeManager.isEmpty()){
                                            m=true;
                                            break;
                                        }
                                        newName = cities.get(city).branches.get(branch).toBeManager.poll();
                                    }
                                    // if there is no one,
                                    if(m){
                                        //if it dismissed changeManager variable set to true so if new member added it can replace it
                                        if(number==1){
                                            cities.get(city).branches.get(branch).changeManager=true;
                                        }
                                        //if it wants to leave, give bonus
                                        else if(number==0) {
                                            if(cities.get(city).branches.get(branch).people.get(name).promotion>-5){
                                                if(Company.cities.get(city).branches.get(branch).monthBranch!=Company.timeCompany) {
                                                    Company.cities.get(city).branches.get(branch).monthly = 0;
                                                    Company.cities.get(city).branches.get(branch).monthBranch = Company.timeCompany;
                                                }
                                                cities.get(city).branches.get(branch).people.get(name).bonus += 200;
                                                cities.get(city).branches.get(branch).overall += 200;
                                                cities.get(city).branches.get(branch).monthly += 200;
                                            }
                                        }
                                    }
                                    // if we find the suitable one,promote it and dismiss manager
                                    else {
                                        if(number ==0) {
                                            output.write(name + " is leaving from branch: " + branch + "."+"\n");
                                            output.flush();
                                        }
                                        else{
                                            output.write(name + " is dismissed from branch: " + branch + "."+"\n");
                                            output.flush();
                                        }
                                        cities.get(city).branches.get(branch).people.get(newName).promote(output);
                                        cities.get(city).branches.get(branch).people.remove(name);
                                    }
                                }

                            }
                            //if number cook is 1
                            else{
                                //if it dismissed changeManager variable set to true so if new member added it can replace it
                                if(number==1){
                                    cities.get(city).branches.get(branch).changeManager=true;
                                }
                                //if it wants to leave, give bonus
                                else if(number==0) {
                                    if(cities.get(city).branches.get(branch).people.get(name).promotion>-5){
                                        if(Company.cities.get(city).branches.get(branch).monthBranch!=Company.timeCompany) {
                                            Company.cities.get(city).branches.get(branch).monthly = 0;
                                            Company.cities.get(city).branches.get(branch).monthBranch = Company.timeCompany;
                                        }
                                        cities.get(city).branches.get(branch).people.get(name).bonus += 200;
                                        cities.get(city).branches.get(branch).overall += 200;
                                        cities.get(city).branches.get(branch).monthly += 200;
                                    }
                                }
                            }
                        }
                    }
                    // check if it is cook
                    else if(cities.get(city).branches.get(branch).people.get(name).job.equals("COOK")){
                        //if number of cook is bigger than 1,dismiss cook
                        if(cities.get(city).branches.get(branch).numberCook>1){
                            if(number ==0) {
                                output.write(name + " is leaving from branch: " + branch + "."+"\n");
                                output.flush();
                            }
                            else{
                                output.write(name + " is dismissed from branch: " + branch + "."+"\n");
                                output.flush();
                            }
                            deleteNumber(name,city,branch);
                            cities.get(city).branches.get(branch).people.remove(name);
                            if(number==0) {
                                cities.get(city).branches.get(branch).toBeManager.remove(name);
                            }
                        }
                        //if number of cook is 1
                        else{
                            //if it wants to leave give bonus
                            if(number==0) {
                                if(cities.get(city).branches.get(branch).people.get(name).promotion>-5){
                                    if(Company.cities.get(city).branches.get(branch).monthBranch!=Company.timeCompany) {
                                        Company.cities.get(city).branches.get(branch).monthly = 0;
                                        Company.cities.get(city).branches.get(branch).monthBranch = Company.timeCompany;
                                    }
                                    cities.get(city).branches.get(branch).people.get(name).bonus += 200;
                                    cities.get(city).branches.get(branch).overall += 200;
                                    cities.get(city).branches.get(branch).monthly += 200;
                                }
                                }
                            // if it is dismissed, name it to kick him after
                            else if(number==1){
                                cities.get(city).branches.get(branch).toKickCook=name;
                            }
                        }
                    }
                    // if it is cashier
                    else if(cities.get(city).branches.get(branch).people.get(name).job.equals("CASHIER")){
                        //if cashier number is bigger than 1, leave or kick it
                        if(cities.get(city).branches.get(branch).numberCashier>1){
                            deleteNumber(name,city,branch);
                            cities.get(city).branches.get(branch).people.remove(name);
                            if(number ==0) {
                                output.write(name + " is leaving from branch: " + branch + "."+"\n");
                                output.flush();
                            }
                            else{
                                output.write(name + " is dismissed from branch: " + branch + "."+"\n");
                                output.flush();
                            }
                        }
                        //if it is not , give bonus, or name it to kick him after
                        else{
                            if(number==0) {
                                if(cities.get(city).branches.get(branch).people.get(name).promotion>-5){
                                    if(Company.cities.get(city).branches.get(branch).monthBranch!=Company.timeCompany) {
                                        Company.cities.get(city).branches.get(branch).monthly = 0;
                                        Company.cities.get(city).branches.get(branch).monthBranch = Company.timeCompany;
                                    }
                                    cities.get(city).branches.get(branch).people.get(name).bonus += 200;
                                    cities.get(city).branches.get(branch).overall += 200;
                                    cities.get(city).branches.get(branch).monthly += 200;
                                }
                            }
                            else if(number==1){
                                cities.get(city).branches.get(branch).toKickCashier=name;
                            }

                        }
                    }
                    //if it is courier same procedure as cashier
                    else {
                        if (cities.get(city).branches.get(branch).numberCourier>1){
                            deleteNumber(name,city,branch);
                            cities.get(city).branches.get(branch).people.remove(name);
                            if(number ==0) {
                                output.write(name + " is leaving from branch: " + branch + "."+"\n");
                                output.flush();
                            }
                            else{
                                output.write(name + " is dismissed from branch: " + branch + "."+"\n");
                                output.flush();
                            }
                        }
                        else{
                            if(number==0) {
                                if(cities.get(city).branches.get(branch).people.get(name).promotion>-5){
                                    if(Company.cities.get(city).branches.get(branch).monthBranch!=Company.timeCompany) {
                                        Company.cities.get(city).branches.get(branch).monthly = 0;
                                        Company.cities.get(city).branches.get(branch).monthBranch = Company.timeCompany;
                                    }
                                    cities.get(city).branches.get(branch).people.get(name).bonus += 200;
                                    cities.get(city).branches.get(branch).overall += 200;
                                    cities.get(city).branches.get(branch).monthly += 200;
                                }
                            }
                            if(number==1){
                                cities.get(city).branches.get(branch).toKickCourier=name;

                            }
                        }
                    }
                }
                else {
                    output.write("There is no such employee."+"\n");
                    output.flush();
                }
            }
            else{
                output.write("There is no such employee."+"\n");
                output.flush();
            }
        }
        else{
            output.write("There is no such employee."+"\n");
            output.flush();
        }
    }
    /**
     * Increments the number of employees for a specific job position in a branch.
     *
     * @param name   The name of the member.
     * @param city   The city where the member works.
     * @param branch The branch where the member works.
     * @param job    The job position of the member.
     * @throws IOException If an I/O error occurs during file writing.
     */
    public void addNumber(String name,String city,String branch,String job) throws IOException {
        if(job.equals("COOK")){
            cities.get(city).branches.get(branch).numberCook+=1;
        }
        else if(job.equals("CASHIER")){
            cities.get(city).branches.get(branch).numberCashier+=1;
        }
        else if(job.equals("COURIER")){
            cities.get(city).branches.get(branch).numberCourier+=1;
        }
    }
    /**
     * Decrements the number of employees for a specific job position in a branch.
     *
     * @param name   The name of the member.
     * @param city   The city where the member works.
     * @param branch The branch where the member works.
     * @throws IOException If an I/O error occurs during file writing.
     */
    public void deleteNumber(String name,String city,String branch) throws IOException {
        if(cities.get(city).branches.get(branch).people.get(name).job.equals("COOK")){
            cities.get(city).branches.get(branch).numberCook-=1;
        }
        else if(cities.get(city).branches.get(branch).people.get(name).job.equals("CASHIER")) {
            cities.get(city).branches.get(branch).numberCashier -= 1;
        }
        else if(cities.get(city).branches.get(branch).people.get(name).job.equals("COURIER")){
            cities.get(city).branches.get(branch).numberCourier -= 1;
        }
    }
}


