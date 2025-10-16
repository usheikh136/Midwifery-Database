import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.* ;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GoBabbyApp {


    public static void main(String[] args) throws SQLException {

        boolean notDone = true;

        // Unique table names.  Either the user supplies a unique identifier as a command line argument, or the program makes one up.
        String tableName = "";
        int sqlCode = 0;      // Variable to hold SQLCODE
        String sqlState = "00000";  // Variable to hold SQLSTATE

        if (args.length > 0)
            tableName += args[0];
        else
            tableName += "exampletbl";

        // Register the driver.  You must register the driver before you can use it.
        try {
            DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());
        } catch (Exception cnfe) {
            System.out.println("Class not found");
        }

        // This is the url you must use for DB2.
        //Note: This url may not valid now ! Check for the correct year and semester and server name.
        String url = "jdbc:db2://winter2022-comp421.cs.mcgill.ca:50000/cs421";

        //REMEMBER to remove your user id and password before submitting your code!!
        String your_userid = "uhamee1";
        String your_password = "gOt_T<*O";
        //AS AN ALTERNATIVE, you can just set your password in the shell environment in the Unix (as shown below) and read it from there.
        //$  export SOCSPASSWD=yoursocspasswd
        if (your_userid == null && (your_userid = System.getenv("SOCSUSER")) == null) {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        if (your_password == null && (your_password = System.getenv("SOCSPASSWD")) == null) {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        Connection con = DriverManager.getConnection(url, your_userid, your_password);
        Statement statement = con.createStatement();

        boolean mainmenu = true;
        while (mainmenu) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Please enter your practitioner id [E] to exit: ");
            String input = sc.nextLine();
            if (input.compareTo("E") == 0) {
                statement.close();
                con.close();
                break;
            }

            int pracid = Integer.parseInt(input);

            try {
                String query1 = "SELECT * FROM MIDWIFE WHERE PRACTITIONERID = " + pracid;
                java.sql.ResultSet rs = statement.executeQuery(query1);

                if (!rs.next()) {
                    System.out.println("ID Entered is not valid.");
                    continue;
                }
            } catch (SQLException e) {
                sqlCode = e.getErrorCode(); // Get SQLCODE
                sqlState = e.getSQLState(); // Get SQLSTATE

                // Your code to handle errors comes here;
                // something more meaningful than a print would be good
                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                System.out.println(e);
            }

            ArrayList<String> times = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> hcns = new ArrayList<>();
            ArrayList<String> pbs = new ArrayList<>();
            ArrayList<String> pids = new ArrayList<>();
            ArrayList<String> appids = new ArrayList<>();

            boolean practID = true;
            while (practID) {

                System.out.println("Please enter the date for appointment list [E] to exit: ");
                String input2 = sc.nextLine();
                if (input2.compareTo("E") == 0) {
                    statement.close();
                    con.close();
                    System.exit(0);
                }

                boolean samedate = true;

                while (samedate) {
                    try {
                        String query2 = "SELECT Appointment.Time, Mother.Name, Mother.HealthCardNum, 'P' as PB, Pregnancy.PregnancyID, " +
                                "Appointment.AppointmentID FROM APPOINTMENT, APPOINTMENTSCHEDULES, PREGNANCY, MOTHER, PARENTS " +
                                "WHERE APPOINTMENT.APPOINTMENTID = APPOINTMENTSCHEDULES.APPOINTMENTID AND APPOINTMENTSCHEDULES.PRACTITIONERID = "
                                + pracid + " AND DATE = " + input2 + " AND APPOINTMENT.PREGNANCYID = PREGNANCY.PREGNANCYID AND PREGNANCY.PARENTSID = " +
                                "PARENTS.PARENTSID AND PARENTS.MOTHERID = MOTHER.PATIENTID ORDER BY TIME";
                        java.sql.ResultSet rs2 = statement.executeQuery(query2);
                        int count = 0;

                        if (!rs2.next()) {
                            System.out.println("No appointments found under the entered date.");
                            break;
                        } else {
                            do {
                                count++;
                                String time = rs2.getString(1);
                                String name = rs2.getString(2);
                                String hcn = rs2.getString(3);
                                String p = rs2.getString(4);
                                String pregid = rs2.getString(5);
                                String appid = rs2.getString(6);
                                times.add(time);
                                names.add(name);
                                hcns.add(hcn);
                                pbs.add(p);
                                pids.add(pregid);
                                appids.add(appid);

                                System.out.println(count + ":\t" + time + "\t" + p + "\t" + name + "\t" + hcn);
                            } while (rs2.next());
                        }
                    } catch (SQLException e) {
                        sqlCode = e.getErrorCode(); // Get SQLCODE
                        sqlState = e.getSQLState(); // Get SQLSTATE

                        // Your code to handle errors comes here;
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                        System.out.println(e);
                    }

                    System.out.println("Enter the appointment number you would like to work on. [E] to exit [D] to go back to another date: ");
                    String inputappnum = sc.nextLine();

                    if (inputappnum.compareTo("E") == 0) {
                        statement.close();
                        con.close();
                    } else if (inputappnum.compareTo("D") == 0) {
                        break;
                    }

                    boolean sameapp = true;
                    while (sameapp) {
                        System.out.println("For " + names.get(Integer.parseInt(inputappnum) - 1) + " " + hcns.get(Integer.parseInt(inputappnum) - 1) + "\n");
                        System.out.println("1.\tReview notes\n2.\tReview tests\n3.\tAdd a note\n4.\tPrescribe a test\n5.\tGo back to the appointments.\n\nEnter your choice: ");
                        int menuoption = sc.nextInt();
                        if (menuoption == 1) {
                            try {

                                String query3 = "SELECT NOTE.DATE, NOTE.TIME, NOTE.DESCRIPTION FROM NOTE, APPOINTMENT WHERE NOTE.APPOINTMENTID = APPOINTMENT.APPOINTMENTID AND " +
                                        "APPOINTMENT.PREGNANCYID = " + pids.get(Integer.parseInt(inputappnum) - 1) + " ORDER BY NOTE.DATE DESC, NOTE.TIME";
                                java.sql.ResultSet rs3 = statement.executeQuery(query3);

                                while (rs3.next()) {
                                    String nDate = rs3.getString(1);
                                    String nTime = rs3.getString(2);
                                    String nDescription = rs3.getString(3);
                                    if (nDescription.length() > 50) nDescription = nDescription.substring(0, 50);
                                    System.out.println(nDate + "\t" + nTime + "\t" + nDescription);
                                }

                            } catch (SQLException e) {
                                sqlCode = e.getErrorCode(); // Get SQLCODE
                                sqlState = e.getSQLState(); // Get SQLSTATE

                                // Your code to handle errors comes here;
                                // something more meaningful than a print would be good
                                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                                System.out.println(e);
                            }
                        } else if (menuoption == 2) {

                            try {

                                String healthcn = hcns.get(Integer.parseInt(inputappnum) - 1);

                                String query4 = "SELECT TEST.PRESCRIBED, TEST.TYPE, TEST.RESULT FROM TEST, TESTCOMPLETEDFOR, MOTHER WHERE TEST.TESTID = " +
                                        "TESTCOMPLETEDFOR.TESTID AND TESTCOMPLETEDFOR.PATIENTID = MOTHER.PATIENTID AND MOTHER.HEALTHCARDNUM = \'" + healthcn +"\'";
                                ResultSet rs4 = statement.executeQuery(query4);

                                while (rs4.next()) {
                                    String tDate = rs4.getString(1);
                                    String tType = rs4.getString(2);
                                    String tResult = rs4.getString(3);
                                    if (tResult.length() == 0) tResult = "PENDING";
                                    if (tResult.length() > 50) tResult = tResult.substring(0, 50);
                                    System.out.println(tDate + " [" + tType + "] " + tResult);
                                }

                            } catch (SQLException e) {
                                sqlCode = e.getErrorCode(); // Get SQLCODE
                                sqlState = e.getSQLState(); // Get SQLSTATE

                                // Your code to handle errors comes here;
                                // something more meaningful than a print would be good
                                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                                System.out.println(e);
                            }
                        } else if (menuoption == 3) {
                            sc.nextLine();
                            System.out.println("Please type your observation: ");
                            String newnote = sc.nextLine();

                            long cur = System.currentTimeMillis();
                            java.sql.Date cDate = new java.sql.Date(cur);
                            java.sql.Time cTime = new java.sql.Time(cur);
                            int min = 1000000; int max = 9999999;
                            int randid = (int) Math.floor(Math.random()*(max-min+1)+min);
                            try {
                                String query5 = "INSERT INTO Note(NoteID, AppointmentID, Date, Time, Description) " +
                                        "VALUES (" + randid + "," + appids.get(Integer.parseInt(inputappnum)-1) + ", '" + cDate +
                                        "', '" + cTime + "', '" + newnote + "')";
                                statement.executeUpdate(query5);
                            } catch (SQLException e) {
                                sqlCode = e.getErrorCode(); // Get SQLCODE
                                sqlState = e.getSQLState(); // Get SQLSTATE

                                // Your code to handle errors comes here;
                                // something more meaningful than a print would be good
                                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                                System.out.println(e);
                            }

                        } else if (menuoption == 4) {
                            sc.nextLine();
                            System.out.println("Please enter the type of test: ");
                            String newtest = sc.nextLine();

                            long cur = System.currentTimeMillis();
                            java.sql.Date cDate = new java.sql.Date(cur);
                            int min = 1000000; int max = 9999999;
                            int randid = (int) Math.floor(Math.random()*(max-min+1)+min);

                            try {
                                String query6 = "INSERT INTO TEST(TestID, Type, Prescribed, SampleTaken, LabDone, Result, PractitionerID) VALUES ("
                                        + randid +",'" + newtest + "','" + cDate + "', '" + cDate + "','" + cDate + "', 'Pending'," + pracid + ")";
                                statement.executeUpdate(query6);
                                String query7 = "INSERT INTO TESTCOMPLETEDFOR(TestID, PatientID) VALUES (" + randid + "," + "(SELECT PATIENTID FROM MOTHER WHERE MOTHER.HEALTHCARDNUM = " + hcns.get(Integer.parseInt(inputappnum)-1) + ")";
                                statement.executeUpdate(query7);

                            } catch (SQLException e) {
                                sqlCode = e.getErrorCode(); // Get SQLCODE
                                sqlState = e.getSQLState(); // Get SQLSTATE

                                // Your code to handle errors comes here;
                                // something more meaningful than a print would be good
                                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                                System.out.println(e);
                            }
                        } else if (menuoption == 5) {
                            sc.nextLine();
                            break;
                        }
                    }continue;
                }

            }
        }

        statement.close();
        con.close();
    }
}