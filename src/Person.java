import java.io.FileWriter;
import java.io.IOException;
/**
 * The Person class represents an individual working in a company, with specific attributes and operations.
 */
public class Person {
    public String city;
    public String branch;
    public String name;
    public int promotion;
    public int bonus;
    public String job;
    /**
     * Default constructor for the Person class.
     */
    public Person() {
    }
    /**
     * Constructs a Person object with the specified name, city, branch, and job role.
     *
     * @param name   The name of the person.
     * @param city   The city in which the person works.
     * @param branch The branch in which the person works.
     * @param job    The job role of the person.
     */
    public Person(String name, String city, String branch,String job) {
        this.name = name;
        this.city = city;
        this.branch = branch;
        this.job =job;
    }
    /**
     * Processes the monthly update for the person based on the provided monthly score.
     *
     * @param monthlyScore The monthly score used to calculate promotions and bonuses.
     * @param output       The FileWriter to log output messages.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    public void processMonthlyUpdate(int monthlyScore, FileWriter output) throws IOException {
        if (monthlyScore > 0) {
            int promotionPointsToAdd = monthlyScore / 200;
            promotion += promotionPointsToAdd;
            int bonus = monthlyScore % 200;
            //if the time between company and branch is not same(it should be zero every end of the month),first set it zero
            if(Company.cities.get(city).branches.get(branch).monthBranch!=Company.timeCompany) {
                Company.cities.get(city).branches.get(branch).monthly=0;
                Company.cities.get(city).branches.get(branch).monthBranch=Company.timeCompany;
            }
            this.bonus += bonus;
            //add bonus
            Company.cities.get(city).branches.get(branch).monthly += bonus;
            Company.cities.get(city).branches.get(branch).overall+= bonus;
            // if it is manager
            if(job.equals("MANAGER")){
                // if it should be dismissed and not, then with new updates, it increased his promote bigger than -5, so do not dismiss anymore(remove from variable)
                if(promotion-promotionPointsToAdd<=-5){
                    if(promotion>-5){
                        Company.cities.get(city).branches.get(branch).changeManager=false;
                    }
                }
            }
            // if it is cook
            if(job.equals("COOK")){
                // if it should be dismissed and not, then with new updates, it increased his promote bigger than -5, so do not dismiss anymore(remove from variable)
                if(promotion-promotionPointsToAdd<=-5){
                    if(promotion>-5){
                        Company.cities.get(city).branches.get(branch).toKickCook="";
                    }
                }
                // if it updates his promotion bigger than 10, if manager should be dismissed or want to leave, kick it and make this cook new manager
                if(promotion>=10){
                    if((promotion-promotionPointsToAdd)<10){
                        if(Company.cities.get(city).branches.get(branch).changeManager&&Company.cities.get(city).branches.get(branch).toBeManager.isEmpty()){
                            if(Company.cities.get(city).branches.get(branch).people.get(Company.cities.get(city).branches.get(branch).manager).promotion<=-5){
                                output.write(Company.cities.get(city).branches.get(branch).manager +" is dismissed from branch: "+ branch +"."+"\n");
                            output.flush();
                            }
                            else {
                                output.write(Company.cities.get(city).branches.get(branch).manager + " is leaving from branch: " + branch + "." + "\n");
                                output.flush();
                            }
                            String tempName=Company.cities.get(city).branches.get(branch).manager;
                            promote(output);
                            Company.cities.get(city).branches.get(branch).people.remove(tempName);

                        }
                        // if manager doesnt want to leave or dismissed, add it to list to promote later
                        else{
                            Company.cities.get(city).branches.get(branch).toBeManager.add(name);
                        }
                    }
                }
            }
            // if it is cashier
            else if(job.equals("CASHIER")){
                // if it should be dismissed and not, then with new updates, it increased his promote bigger than -5, so do not dismiss anymore(remove from variable)
                if(promotion-promotionPointsToAdd<=-5){
                    if(promotion>-5){
                        Company.cities.get(city).branches.get(branch).toKickCashier="";
                    }
                }
                // if it updates his promotion bigger than 3, and numbercashier is bigger than 1 make it cook, or add tobeCook variable to promote later
                if(promotion>=3){
                    if(Company.cities.get(city).branches.get(branch).numberCashier>1){
                        Company.cities.get(city).branches.get(branch).people.get(name).promote(output);
                        if(Company.cities.get(city).branches.get(branch).numberCook==2){
                            if(Company.cities.get(city).branches.get(branch).changeManager){
                                if(Company.cities.get(city).branches.get(branch).toBeManager.size()==1){
                                    if(Company.cities.get(city).branches.get(branch).people.get(Company.cities.get(city).branches.get(branch).toBeManager.peek()).promotion>=-5){
                                        Company.cities.get(city).branches.get(branch).people.get(Company.cities.get(city).branches.get(branch).toBeManager.poll()).promote(output);
                                    }
                                }
                            }
                        }
                    }
                    else{
                        Company.cities.get(city).branches.get(branch).toBeCook=name;
                    }
                }
            }
            //if it is cashier
            else if(job.equals("COURIER")){
                // if it should be dismissed and not, then with new updates, it increased his promote bigger than -5, so do not dismiss anymore(remove from variable)
                if(promotion-promotionPointsToAdd<=-5){
                    if(promotion>-5){
                        Company.cities.get(city).branches.get(branch).toKickCourier="";
                    }
                }
            }


        } else if (monthlyScore < 0) {
            int promotionPointsToSubtract = Math.abs(monthlyScore) / 200;
            promotion -= promotionPointsToSubtract;
            //if it is cook and in the toBeManager list, but with new update it should be removed, remove it
            if(job.equals("COOK")) {
                if (promotion < 10&& promotion +promotionPointsToSubtract >=10) {
                    Company.cities.get(city).branches.get(branch).toBeManager.remove(name);
                }
            }
            // if it is cashier same procedure as cook
            if(job.equals("CASHIER")){
                if(promotion<3&&promotion+promotionPointsToSubtract>=3&&Company.cities.get(city).branches.get(branch).toBeCook.equals(name)){
                    Company.cities.get(city).branches.get(branch).toBeCook="";
                }
            }

            // Negative monthly score means no bonus
        }

        // Reset monthly score after processing
    }
    /**
     * Promotes the person to the next job role and updates relevant attributes.
     *
     * @param output The FileWriter to log output messages.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    public void promote(FileWriter output) throws IOException {
        // if it is cook make the manager,
        if (job.equals("COOK")){
            job="MANAGER";
            promotion-=10;
            Company.cities.get(city).branches.get(branch).numberCook-=1;
            Company.cities.get(city).branches.get(branch).manager=name;
            Company.cities.get(city).branches.get(branch).changeManager=false;
            output.write(name+" is promoted from Cook to Manager."+"\n");
            output.flush();
        }
        // if it is cashier make the cook,
        else if(job.equals("CASHIER")){
            job="COOK";
            promotion-=3;
            if(promotion>10){
                Company.cities.get(city).branches.get(branch).toBeManager.add(name);
            }
            Company.cities.get(city).branches.get(branch).numberCashier-=1;
            Company.cities.get(city).branches.get(branch).numberCook+=1;
            output.write(name+" is promoted from Cashier to Cook."+"\n");
            Company.cities.get(city).branches.get(branch).toBeCook="";
            output.flush();
        }
    }
    /**
     * Checks if the person should be dismissed based on their promotion points.
     *
     * @return True if the person should be dismissed, otherwise false.
     */
    public boolean checkDismis(){
        return promotion<=-5;
    }

}
