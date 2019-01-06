package com.teamtreehouse.publicdata;

import com.teamtreehouse.publicdata.model.Country;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();


    private static SessionFactory buildSessionFactory(){
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        run();
    }

    //Print a list of formatted data for all countries
    private static void printWorldBank(List<Country> countries){
        //Print Data for all Countries
        System.out.printf("%-32s%20s%20s%n", "Country", "Internet Users", "Literacy");
        System.out.printf("--------------------------------" + "--------------------" + "--------------------%n");
        countries.stream().forEach(System.out::println);
        System.out.printf("%n%n");
    }

    private static void printStatistics(List<Country> countries){
        System.out.println("Country Statistics");
        System.out.printf("--------------------------------" + "--------------------" + "--------------------%n");

        //Print Min Internet Users
        Country c = countries.stream().filter(c2 -> c2.getInternetUsers() != null).min(Comparator.comparing(Country::getInternetUsers)).get();
        System.out.println("Lowest Internet Users:       " + c.getName() + String.format(" = %.2f", c.getInternetUsers()));

        //Print Max Internet Users
        c = countries.stream().filter(c2 -> c2.getInternetUsers() != null).max(Comparator.comparing(Country::getInternetUsers)).get();
        System.out.println("Highest Internet Users:      " + c.getName() + String.format(" = %.2f",c.getInternetUsers()));

        //Print Min Literacy
        c = countries.stream().filter(c2 -> c2.getAdultLiteracyRate() != null).min(Comparator.comparing(Country::getAdultLiteracyRate)).get();
        System.out.println("Lowest Adult Literacy Rate:  " + c.getName() + String.format(" = %.2f", c.getAdultLiteracyRate()));

        //Print Max Literacy
        c = countries.stream().filter(c2 -> c2.getAdultLiteracyRate() != null).max(Comparator.comparing(Country::getAdultLiteracyRate)).get();
        System.out.println("Highest Adult Literacy Rate: " + c.getName() + String.format(" = %.2f",c.getAdultLiteracyRate()));

        //Print correlation coefficient
        correlationCoefficient(countries);
        System.out.printf("%n%n");
    }

    private static void correlationCoefficient(List<Country> countries){
        List<Country> corCofCountries = countries.stream().filter(c2 -> (c2.getAdultLiteracyRate() != null && c2.getInternetUsers() !=null)).collect(Collectors.toList());
        double[] x = new double[corCofCountries.size()];
        double[] y = new double[corCofCountries.size()];

        int n = 0;
        for(Country c2 : corCofCountries){
            x[n] = c2.getInternetUsers();
            y[n] = c2.getAdultLiteracyRate();
            n++;
        }

        //Calculate -- Edited code from https://www.easycalculation.com/code-java-program-correlation-co-efficient.html
        double r,nr=0,dr_1=0,dr_2=0,dr_3=0,dr=0;
        double xx[],yy[];
        xx =new double[n];
        yy =new double[n];
        double sum_y=0,sum_yy=0,sum_xy=0,sum_x=0,sum_xx=0;
        int i;
        for(i=0;i<n;i++){
            xx[i]=x[i]*x[i];
            yy[i]=y[i]*y[i];
        }
        for(i=0;i<n;i++){
            sum_x+=x[i];
            sum_y+=y[i];
            sum_xx+= xx[i];
            sum_yy+=yy[i];
            sum_xy+= x[i]*y[i];
        }
        nr=(n*sum_xy)-(sum_x*sum_y);
        double sum_x2=sum_x*sum_x;
        double sum_y2=sum_y*sum_y;
        dr_1=(n*sum_xx)-sum_x2;
        dr_2=(n*sum_yy)-sum_y2;
        dr_3=dr_1*dr_2;
        dr=Math.sqrt(dr_3);
        r=(nr/dr);
        System.out.println("Correlation Coefficient:     " + String.format("%.2f",r));
    }

    @SuppressWarnings("unchecked")
    private static List<Country> fetchAllCountries(){
        //Open a session
        Session session = sessionFactory.openSession();

        //Create the criteria
        Criteria criteria = session.createCriteria(Country.class);

        // Get a list of Contact objects according to the Criteria object
        List<Country> countries = criteria.list();

        //Close the session
        session.close();

        countries = countries.stream().sorted(Comparator.comparing(Country::getName)).collect(Collectors.toList());

        return countries;
    }

    private static Country findCountryByCode(String code){
        //Open a session
        Session session = sessionFactory.openSession();

        //Retrieve the persistant object (or null if not found)
        Country country = session.get(Country.class,code);

        //Close the session
        session.close();

        //Return the object
        return country;
    }

    private static void update(Country country){
        //Open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Use the session to update the contact
        session.update(country);

        //Commit the transaction
        session.getTransaction().commit();

        //Close the session
        session.close();
    }

    private static void save(Country country) {
        // Open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Use the session to save the contact
        session.save(country);

        //Commit the transaction
        session.getTransaction().commit();

        //Close the session
        session.close();
    }

    private static void delete(Country country){
        //Open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Use the session to update the contact
        session.delete(country);

        //Commit the transaction
        session.getTransaction().commit();

        //Close the session
        session.close();
    }


    private static Map<String, String> getMenu() {
        Map<String, String> menu;
        menu = new HashMap<String, String>();
        menu.put("   view", "View a list of data for all countries");
        menu.put("   stats", "View a list of statistics for all countries");
        menu.put("   update", "Edit a country's data");
        menu.put("   save", "Add a country's data");
        menu.put("   delete", "Delete a country's data");
        menu.put("   quit", "Give up. Exit the program");
        return menu;
    }

    private static String promptAction() throws IOException {
        Map<String, String> menu = getMenu();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Menu: ");
        for (Map.Entry<String, String> option : menu.entrySet()) {
            System.out.printf("%s - %s %n",
                    option.getKey(),
                    option.getValue());
        }
        System.out.print("What do you want to do: ");
        String choice = reader.readLine();
        System.out.println();
        return choice.trim().toLowerCase();
    }

    private static Country promptForCode() throws IOException {
        String code = "";
        Country country = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        do{
            System.out.print("Enter the 3 digit code of the country: ");
            code = reader.readLine().trim().toUpperCase();
            country = findCountryByCode(code);
        } while (country == null);
        return country;
    }

    private static Country promptForEdit(Country country) throws IOException {
        String choice = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Which do you want to edit? (name, internetUsers, adultLiteracy): ");
            choice = reader.readLine().trim();
            switch (choice) {
                case "name":
                    System.out.print("Enter name: ");
                    country.setName(reader.readLine().trim());
                    break;
                case "internetUsers":
                    System.out.print("Enter internet users: ");
                    country.setInternetUsers(Double.parseDouble(reader.readLine().trim()));
                    break;
                case "adultLiteracy":
                    System.out.print("Enter adult literacy rate: ");
                    country.setAdultLiteracyRate(Double.parseDouble(reader.readLine().trim()));
                    break;
                default:
                    System.out.printf("Unknown choice: '%s'. Try again. %n%n%n", choice);
            }
        }catch (NumberFormatException e){
            System.out.println("Problem with input");
            e.printStackTrace();
        }
        return country;
    }

    private static Country promptForSave() throws IOException {
        Country.CountryBuilder countryBuilder = null;
        String name = "";
        String code = "";
        String bool = "";
        Double internetUsers = null;
        Double adultLiteracyRate = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //Get name
        System.out.print("Enter country's name: ");
        name = reader.readLine().trim();

        //Get code
        do{
            System.out.print("Enter country's 3 digit code: ");
            code = reader.readLine().trim().toUpperCase();
        } while(code.length() != 3 );

        //Set name and code
        countryBuilder = new Country.CountryBuilder(code, name);

        //Set (optional) internet users
        System.out.print("Would you like to enter a value for internet users? (y/n) ");
        bool = reader.readLine().trim().toLowerCase();
        if(bool.equals("y")){
            System.out.print("Enter country's internet user value: (0.0) ");
            internetUsers = Double.parseDouble(reader.readLine().trim());
            countryBuilder.withInternetUsers(internetUsers);
        }

        //Set (optional) adult literacy
        System.out.print("Would you like to enter a value for adult literacy? (y/n) ");
        bool = reader.readLine().trim().toLowerCase();
        if(bool.equals("y")){
            System.out.print("Enter country's adult literacy value: (0.0) ");
            adultLiteracyRate = Double.parseDouble(reader.readLine().trim());
            countryBuilder.withAdultLiteracyRate(adultLiteracyRate);
        }

        return countryBuilder.build();
    }

    private static void run() {
        String choice = "";
        Country c = null;
        List<Country> countries = null;
        do {
            try {
                choice = promptAction();


                switch (choice) {
                    case "view":
                        countries = fetchAllCountries();
                        printWorldBank(countries);
                        break;
                    case "stats":
                        countries = fetchAllCountries();
                        printStatistics(countries);
                        break;
                    case "update":
                        c = promptForCode();
                        c = promptForEdit(c);
                        System.out.printf("Updating... %s%n%n", c.getName());
                        update(c);
                        break;
                    case "save":
                        c = promptForSave();
                        System.out.printf("Saving... %s%n%n", c.getName());
                        save(c);
//                        System.out.println("Saved country with code: " + code);
                        break;
                    case "delete":
                        c = promptForCode();
                        System.out.printf("Deleting... %s%n%n", c.getName());
                        delete(c);
                        break;
                    case "quit":
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.printf("Unknown choice: '%s'. Try again. %n%n%n", choice);
                }
            } catch (IOException ioe) {
                System.out.println("Problem with input");
                ioe.printStackTrace();
            }
        } while (!choice.equals("quit"));
    }

}
