package view;

import session.Session;
import javax.swing.*;
import java.awt.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;


public class Login extends JFrame {


    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton registerButton;



    public Login() {


        setTitle("ShopSphere - Multi Vendor E-Commerce Platform");

        setSize(1100,650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());





        // LEFT PANEL


        JPanel leftPanel = new JPanel();


        leftPanel.setBackground(
                new Color(33,150,243)
        );


        leftPanel.setPreferredSize(
                new Dimension(430,650)
        );


        leftPanel.setLayout(
                new BoxLayout(
                        leftPanel,
                        BoxLayout.Y_AXIS
                )
        );



        leftPanel.add(
                Box.createVerticalGlue()
        );



        JLabel title =
                new JLabel("ShopSphere");


        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        34
                )
        );


        title.setForeground(Color.WHITE);





        JLabel subTitle =
                new JLabel(
                        "Multi-Vendor E-Commerce Platform"
                );


        subTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        subTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        18
                )
        );


        subTitle.setForeground(Color.WHITE);




        JLabel fast =
                new JLabel("Fast");

        JLabel secure =
                new JLabel("Secure");

        JLabel reliable =
                new JLabel("Reliable");



        JLabel arr[] =
        {
                fast,
                secure,
                reliable
        };



        for(JLabel l:arr){

            l.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            l.setForeground(
                    Color.WHITE
            );

            l.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            16
                    )
            );

        }





        leftPanel.add(title);

        leftPanel.add(
                Box.createRigidArea(
                        new Dimension(0,15)
                )
        );


        leftPanel.add(subTitle);


        leftPanel.add(
                Box.createRigidArea(
                        new Dimension(0,40)
                )
        );


        leftPanel.add(fast);

        leftPanel.add(
                Box.createRigidArea(
                        new Dimension(0,8)
                )
        );


        leftPanel.add(secure);

        leftPanel.add(
                Box.createRigidArea(
                        new Dimension(0,8)
                )
        );


        leftPanel.add(reliable);


        leftPanel.add(
                Box.createVerticalGlue()
        );







        // RIGHT PANEL


        JPanel rightPanel =
                new JPanel(
                        new GridBagLayout()
                );


        rightPanel.setBackground(
                Color.WHITE
        );



        GridBagConstraints gbc =
                new GridBagConstraints();



        gbc.insets =
                new Insets(
                        12,12,12,12
                );


        gbc.fill =
                GridBagConstraints.HORIZONTAL;





        JLabel welcome =
                new JLabel(
                        "Welcome Back"
                );


        welcome.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );



        JLabel text =
                new JLabel(
                        "Login to continue"
                );



        JLabel emailLabel =
                new JLabel("Email");



        emailField =
                new JTextField();



        emailField.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );



        JLabel passwordLabel =
                new JLabel("Password");



        passwordField =
                new JPasswordField();



        passwordField.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );




        loginButton =
                new JButton(
                        "LOGIN"
                );


        loginButton.setBackground(
                new Color(
                        33,
                        150,
                        243
                )
        );


        loginButton.setForeground(
                Color.WHITE
        );




        registerButton =
                new JButton(
                        "Create New Account"
                );







        gbc.gridx=0;


        gbc.gridy=0;
        rightPanel.add(welcome,gbc);


        gbc.gridy++;
        rightPanel.add(text,gbc);


        gbc.gridy++;
        rightPanel.add(emailLabel,gbc);


        gbc.gridy++;
        rightPanel.add(emailField,gbc);


        gbc.gridy++;
        rightPanel.add(passwordLabel,gbc);


        gbc.gridy++;
        rightPanel.add(passwordField,gbc);


        gbc.gridy++;
        rightPanel.add(loginButton,gbc);


        gbc.gridy++;
        rightPanel.add(registerButton,gbc);






        add(
                leftPanel,
                BorderLayout.WEST
        );


        add(
                rightPanel,
                BorderLayout.CENTER
        );








        // LOGIN BUTTON WITH ROLE CHECK


        loginButton.addActionListener(e -> {


            String email =
                    emailField.getText();



            String password =
                    new String(
                            passwordField.getPassword()
                    );



            if(email.isEmpty() ||
                    password.isEmpty()){


                JOptionPane.showMessageDialog(
                        this,
                        "Please enter email and password"
                );


                return;

            }




            try{


                Connection con =
                        DBConnection.getConnection();



                String sql =
                        "SELECT * FROM users WHERE email=? AND password=?";



                PreparedStatement ps =
                        con.prepareStatement(sql);



                ps.setString(
                        1,
                        email
                );


                ps.setString(
                        2,
                        password
                );



                ResultSet rs =
                        ps.executeQuery();





                if(rs.next()){

                    Session.userId = rs.getInt("user_id");
                    Session.fullName = rs.getString("full_name");
                    Session.email = rs.getString("email");
                    Session.role = rs.getString("role");

                    JOptionPane.showMessageDialog(
                    this,
                    "Login Successful as " + Session.role
                    );

                    dispose();

                    if(Session.role.equalsIgnoreCase("Vendor")){

                        new VendorDashboard();

                    }
                    else if(Session.role.equalsIgnoreCase("Customer")){

                        new CustomerDashboard();

                    }
                    else if(Session.role.equalsIgnoreCase("Admin")){

                        new Dashboard();

                    }

                }
                else{


                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid Email or Password"
                    );


                }




            }
            catch(Exception ex){


                ex.printStackTrace();


            }




        });








        registerButton.addActionListener(e -> {


            dispose();

            new Register();


        });






        setVisible(true);


    }







    public static void main(String[] args){


        new Login();


    }


}