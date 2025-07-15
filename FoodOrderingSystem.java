import java.io.*;
import java.sql.*;
import java.util.*;
public class FoodOrderingSystem
{
    static Scanner sc;
    public static void main(String[] args) throws Exception
    {
        String dburl="jdbc:mysql://localhost:3306/food_ordering_database";
        String dbuser="root";
        String dbpass="";
        String driver="com.mysql.cj.jdbc.Driver";

        Class.forName(driver);
        Connection c=DriverManager.getConnection(dburl,dbuser,dbpass);
        System.out.println(c!=null?"Connected":"Not Connected");

        sc=new Scanner(System.in);
//        System.out.println("Enter your id:");
//        int id=sc.nextInt();
//        System.out.println("Enter you name:");
//        String name=sc.next();
//        System.out.println("Enter pass:");
//        String pass=sc.next();
        System.out.println("Enter your id:");
        int id1=sc.nextInt();
        System.out.println("Enter you name:");
        String name1=sc.next();
        System.out.println("Enter pass:");
        String pass1=sc.next();
        OwnerManagement um=new OwnerManagement(id1,name1,pass1);
        um.checkingDetails(id1,name1,pass1);
        
    }
}
class OwnerManagement
{
    Scanner sc=new Scanner(System.in);

    private int ownerId;
    private String ownerName;
    private String ownerPass;

    public OwnerManagement(int ownerId, String ownerName, String ownerPass) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerPass = ownerPass;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPass() {
        return ownerPass;
    }
//    void enteringDetails() throws Exception
//    {
//        FileWriter fileWriter=new FileWriter("ownerData.txt");
//        BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
//        bufferedWriter.write(getOwnerId()+",");
//        bufferedWriter.write(getOwnerName()+",");
//        bufferedWriter.write(getOwnerPass());
//
//        bufferedWriter.flush();
//        bufferedWriter.close();
//    }

    void checkingDetails(int id,String name,String pass) throws Exception
    {
        File ownerData=new File("ownerData.txt");
        ownerData.createNewFile();
        FileReader fileReader=new FileReader(ownerData);
        BufferedReader bufferedReader=new BufferedReader(fileReader);
        String lineReader;
        boolean found=false;

        while((lineReader=bufferedReader.readLine())!=null)
        {
            String parts[]= lineReader.split(",");
            if (parts.length==3)
            {
                int oId=Integer.parseInt(parts[0].trim());
                String oName=parts[1];
                String oPass=parts[2];
                if(oId==id && oName.equals(name) && oPass.equals(pass))
                {
                    found=true;
                    break;
                }
            }
        }
        bufferedReader.close();
        if (found)
        {
            System.out.println("Value matched");
            updatePass();
        }
        else
        {
            System.out.println("No");
        }

    }

    void updatePass() throws Exception
    {
        int choice;
        System.out.println("Do you want to update password ?, If yes");
        System.out.println("then please select from below options");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Enter Your Choice : ");
        choice = sc.nextInt();
        switch(choice) {
            case 1:
                System.out.print("Enter new password : ");
                String password = sc.next();
                FileWriter fw1 = new FileWriter("ownerData");
                fw1.write("");
                fw1.close();
                OwnerManagement om1 = new OwnerManagement(12,"FoodO",password);
                BufferedWriter bw1 = new BufferedWriter(new FileWriter("ownerData.txt"));
                bw1.write("12,FoodO,"+password+"");
                bw1.close();
                System.out.println("Password Updated Successfully !");
                break;
            case 2:
                break;
        }
    }
}