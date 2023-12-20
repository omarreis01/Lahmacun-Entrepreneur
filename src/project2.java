import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * The Main class is the entry point of the program that processes company data
 * and performs various operations based on input from external files.
 * It utilizes the Company class to manage company information and perform updates.
 */
public class project2 {
    /**
     * The main method reads input files, processes commands, and writes output to a file.
     * It uses the Company class to manage company data and operations.
     *
     * @param args The command line arguments (not used in this implementation).
     * @throws IOException If an I/O error occurs during file handling.
     */
    public static void main(String[] args) throws IOException {
        // Initialize the Company object
        Company c = new Company();
        Long t1=System.currentTimeMillis();
        // take the initial file
        File first = new File(args[0]);
        Scanner myReader = new Scanner(first);
        // open the file to write output
        FileWriter output= new FileWriter(args[2]);
        // reading the initial file line by line
        while(myReader.hasNextLine()){
            if(!myReader.hasNext()){
                break;
            }
            //take the line split it and add initialize the Company with adding member
            String[] line = myReader.nextLine().split(",");
            String city = line[0].strip();
            String branch = line[1].strip();
            String name = line[2].strip();
            String job = line[3].strip();
            c.addNewMember(name,city,branch,job,output);
        }

        String[] months = {"January:", "February:", "March:", "April:", "May:", "June:", "July:", "August:", "September:", "October:", "November:", "December:"};
        ArrayList<String> monthsArray= new ArrayList<>();
        for(String i:months){
            monthsArray.add(i);
        }
        //take the other input file
        File second = new File(args[1]);
        Scanner reader = new Scanner(second);
        //read the input file
        while(reader.hasNextLine()){
            if(!reader.hasNext()){
                break;
            }
            // take the operation to execute
            String operation =reader.next();
            // take the line
            String[] line = reader.nextLine().split(",");
            // execute performance update operation
            if(operation.equals("PERFORMANCE_UPDATE:")){
                String city = line[0].strip();
                String branch = line[1].strip();
                String name = line[2].strip();
                int bonus = Integer.parseInt(line[3].strip());
                c.performanceUpdate(name,city,branch,bonus,output);
                output.flush();
            }
            // execute print monthly bonuses update operation
            else if(operation.equals("PRINT_MONTHLY_BONUSES:")){
                String city = line[0].strip();
                String branch = line[1].strip();
                if(Company.cities.get(city).branches.get(branch).monthBranch!=Company.timeCompany){
                    Company.cities.get(city).branches.get(branch).monthBranch=Company.timeCompany;
                    Company.cities.get(city).branches.get(branch).monthly=0;
                }
                output.write("Total bonuses for the "+branch+" branch this month are: "+Company.cities.get(city).branches.get(branch).monthly+"\n");
                output.flush();
            }
            // execute add operation
            else if(operation.equals("ADD:")){
                String city = line[0].strip();
                String branch = line[1].strip();
                String name = line[2].strip();
                String job = line[3].strip();
                c.addNewMember(name,city,branch,job,output);
                output.flush();
            }
            // execute leave operation
            else if(operation.equals("LEAVE:")){
                String city = line[0].strip();
                String branch = line[1].strip();
                String name = line[2].strip();
                c.deleteNewMember(name,city,branch,0,output);
                output.flush();
            }
            // execute print manager operation
            else if(operation.equals("PRINT_MANAGER:")){
                String city = line[0].strip();
                String branch = line[1].strip();
                output.write("Manager of the "+branch+" branch is "+Company.cities.get(city).branches.get(branch).manager+"."+"\n");
                output.flush();
            }
            // execute print overall bonuses operation

            else if(operation.equals("PRINT_OVERALL_BONUSES:")){
                String city = line[0].strip();
                String branch = line[1].strip();
                output.write("Total bonuses for the "+branch+" branch are: "+Company.cities.get(city).branches.get(branch).overall+"\n");
                output.flush();
            }
            //if it is end of the month add timeCompany by 1
            else if(monthsArray.contains(operation)){
                Company.timeCompany++;
            }
        }
    }


}