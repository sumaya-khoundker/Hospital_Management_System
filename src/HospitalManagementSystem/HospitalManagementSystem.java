package HospitalManagementSystem;

import javax.print.Doc;
import javax.swing.text.html.HTMLDocument;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static  final String password = "@siara.1";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patients patients = new Patients(connection, scanner);
            Doctors doctors = new Doctors(connection);
            while(true){
                System.out.println("\u001B[34m==============================\u001B[0m"); // Blue color
                System.out.println("\u001B[32m  HOSPITAL MANAGEMENT SYSTEM  \u001B[0m"); // Green color
                System.out.println("\u001B[34m==============================\u001B[0m"); // Blue color
                System.out.println("\u001B[36m1. Add Patients.\u001B[0m"); // Cyan color
                System.out.println("\u001B[36m2. View Patients.\u001B[0m"); // Cyan color
                System.out.println("\u001B[36m3. View Doctors.\u001B[0m"); // Cyan color
                System.out.println("\u001B[36m4. Book Appointment.\u001B[0m"); // Cyan color
                System.out.println("\u001B[36m5. Exit.\u001B[0m"); // Cyan color
                System.out.print("\n\u001B[36m Enter Your Choice: \u001B[0m");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        patients.addPatients();
                        break ;
                    case 2:
                        patients.viewPatients();
                        break;
                    case 3:
                        doctors.viewDoctors();
                        break;
                    case 4:
                        bookAppointment(patients, doctors, connection, scanner);
                        break;
                    case 5:
                         break;
                    default:
                        System.out.println("Invalid Choice. Enter Valid Choice.");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patients patients, Doctors doctors, Connection connection, Scanner scanner){
        System.out.println("Enter Patient ID: ");
        int patientID = scanner.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorID = scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY-MM-DD:  ");
        String appointmentDate = scanner.next();
        if(patients.getPatientByID(patientID) && doctors.getDoctorByID(doctorID)){
            if(checkDoctorAvailability(doctorID, appointmentDate, connection)){
                String appointmentQuery = "Insert into appointments(Patient_ID, Doctor_ID, App_Date) Values (?, ?, ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientID);
                    preparedStatement.setInt(2, doctorID);
                    preparedStatement.setString(3, appointmentDate);
                    int affectedRows = preparedStatement.executeUpdate();
                    if(affectedRows > 0){
                        System.out.println("Appointment Booked.");
                    } else{
                        System.out.println("Failed To Book Appointment.");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            } else{
                System.out.println("Doctor Not Available.");
            }
        } else {
            System.out.println("Either Patient or Doctor doesn't exist.");
        }
    }
    public static boolean checkDoctorAvailability(int doctorID, String appointmentDate, Connection connection){
        String query = "Select Count(*) from appointments Where Doctor_ID = ? And App_Date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorID);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                } else {
                    return false;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
