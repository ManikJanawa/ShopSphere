package view;

import javax.swing.*;
import java.awt.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import database.DBConnection;


public class Register extends JFrame {


    JTextField nameField;
    JTextField emailField;
    JPasswordField passwordField;

    JComboBox<String> roleBox;



    public Register(){


        setTitle("ShopSphere - Register");

        setSize(700,550);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        setLayout(new GridBagLayout());





        JPanel panel = new JPanel();


        panel.setLayout(
                new GridLayout(5,2,10,10)
        );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        40,40,40,40
                )
        );





        JLabel title =
                new JLabel(
                        "Create Account"
                );


        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );





        nameField =
                new JTextField();



        emailField =
                new JTextField();



        passwordField =
                new JPasswordField();





        roleBox =
                new JComboBox<>(
                        new String[]{
                                "Customer",
                                "Vendor"
                        }
                );






        JButton registerButton =
                new JButton(
                        "REGISTER"
                );







        panel.add(
                new JLabel("Name")
        );


        panel.add(
                nameField
        );





        panel.add(
                new JLabel("Email")
        );


        panel.add(
                emailField
        );





        panel.add(
                new JLabel("Password")
        );


        panel.add(
                passwordField
        );





        panel.add(
                new JLabel("Role")
        );


        panel.add(
                roleBox
        );





        panel.add(
                registerButton
        );







        add(panel);








        registerButton.addActionListener(e -> {



            String name =
                    nameField.getText();



            String email =
                    emailField.getText();



            String password =
                    new String(
                    passwordField.getPassword()
                    );



            String role =
                    roleBox.getSelectedItem()
                    .toString();







            if(name.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty()){


                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all details"
                );


                return;

            }







            try{


                Connection con =
                DBConnection.getConnection();





                String sql =
                "INSERT INTO users(full_name,email,password,role) VALUES(?,?,?,?)";





                PreparedStatement ps =
                con.prepareStatement(sql);





                ps.setString(
                        1,
                        name
                );



                ps.setString(
                        2,
                        email
                );



                ps.setString(
                        3,
                        password
                );



                ps.setString(
                        4,
                        role
                );







                ps.executeUpdate();






                JOptionPane.showMessageDialog(
                        this,
                        "Registration Successful"
                );





                dispose();


                new Login();







            }
            catch(Exception ex){


                ex.printStackTrace();


                JOptionPane.showMessageDialog(
                        this,
                        "Registration Failed"
                );


            }






        });






        setVisible(true);



    }







    public static void main(String[] args){


        new Register();


    }



}