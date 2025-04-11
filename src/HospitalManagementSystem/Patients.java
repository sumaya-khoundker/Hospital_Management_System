package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patients {
    private Connection connection;
    private Scanner scanner;
    public Patients(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    public void addPatients(){
        System.out.println("Enter Patients Name: ");
        String name = scanner.next();
        System.out.println("Enter Patients Age: ");
        int age = scanner.nextInt();
        System.out.println("Enter Patients Gender: ");
        String gender = scanner.next();
        try{
            String query = "Insert into patients(Patient_Name, Age, Gender) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0) {
                System.out.println("Patient Added Successfully.");
            } else{
                System.out.println("Failed to Add Patient.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void viewPatients() {
        String query = "Select * from patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------------+------------------------+-----------+---------------+");
            System.out.println("|  Patient_ID     |  Name                   |  Age      | Gender      |");
            System.out.println("+------------------+------------------------+-----------+---------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("Patient_ID");
                String name = resultSet.getString("Patient_Name");
                int age = resultSet.getInt("Age");
                String gender = resultSet.getString("Gender");
                System.out.printf("|%-17s|%-25s|%-11s|%-13s|\n", id, name, age, gender);
                System.out.println("+------------------+------------------------+-----------+---------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        public boolean getPatientByID(int id){
            String query = "Select * from patients Where Patient_ID = ?";
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return true;
                } else
                    return false;
        } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
    }
}

