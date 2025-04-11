package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
        private Connection connection;
        public Doctors(Connection connection){
            this.connection = connection;
        }
        public void viewDoctors() {
            String query = "Select * from doctors";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("Doctors: ");
                System.out.println("+-------------------+-----------------------------+--------------------------------+");
                System.out.println("| Doctor_ID         | Doctor_Name                 | Specialization                 |");
                System.out.println("+-------------------+-----------------------------+--------------------------------+");
                while (resultSet.next()) {
                    int id = resultSet.getInt("Doctor_ID");
                    String name = resultSet.getString("Doctor_Name");
                    String specialization = resultSet.getString("Specialization");
                    System.out.printf("|%-19s|%-29s|%-32s|\n", id, name, specialization);
                    System.out.println("+-------------------+-----------------------------+--------------------------------+");;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public boolean getDoctorByID(int id){
            String query = "Select * from doctors Where Doctor_ID = ?";
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
