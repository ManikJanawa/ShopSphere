package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import database.DBConnection;
import session.Session;


public class VendorDashboard extends JFrame {


    JLabel productValue;
    JLabel orderValue;
    JLabel revenueValue;


    public VendorDashboard(){


        setTitle("ShopSphere - Vendor Dashboard");
        setSize(1100,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());



        //---------------- SIDEBAR ----------------//


        JPanel sidebar = new JPanel();

        sidebar.setBackground(
                new Color(33,150,243)
        );

        sidebar.setPreferredSize(
                new Dimension(230,650)
        );


        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );



        JLabel logo =
                new JLabel("Vendor Panel");


        logo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );


        logo.setForeground(Color.WHITE);

        logo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );



        sidebar.add(
                Box.createRigidArea(
                        new Dimension(0,40)
                )
        );


        sidebar.add(logo);


        sidebar.add(
                Box.createRigidArea(
                        new Dimension(0,50)
                )
        );




        JButton addProduct =
                new JButton("Add Product");


        JButton manageProduct =
                new JButton("Manage Products");


        JButton orders =
                new JButton("Orders");


        JButton sales =
                new JButton("Sales Report");


        JButton logout =
                new JButton("Logout");




        JButton buttons[] = {

                addProduct,
                manageProduct,
                orders,
                sales,
                logout

        };



        for(JButton btn : buttons){


            btn.setMaximumSize(
                    new Dimension(180,40)
            );


            btn.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );


            sidebar.add(btn);


            sidebar.add(
                    Box.createRigidArea(
                            new Dimension(0,15)
                    )
            );


        }




        //---------------- MAIN AREA ----------------//


        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );



        JLabel heading =
                new JLabel(
                        "Welcome Vendor"
                );


        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        32
                )
        );


        heading.setBorder(
                BorderFactory.createEmptyBorder(
                        30,30,20,20
                )
        );



        mainPanel.add(
                heading,
                BorderLayout.NORTH
        );





        JPanel cards =
                new JPanel(
                        new GridLayout(1,3,20,20)
                );


        cards.setBorder(
                BorderFactory.createEmptyBorder(
                        30,30,30,30
                )
        );



        JPanel productCard =
                createCard("Products","0");

        JPanel orderCard =
                createCard("Orders","0");

        JPanel revenueCard =
                createCard("Revenue","₹0");



        productValue =
                (JLabel)productCard.getComponent(1);

        orderValue =
                (JLabel)orderCard.getComponent(1);

        revenueValue =
                (JLabel)revenueCard.getComponent(1);



        cards.add(productCard);
        cards.add(orderCard);
        cards.add(revenueCard);



        mainPanel.add(
                cards,
                BorderLayout.CENTER
        );





        add(
                sidebar,
                BorderLayout.WEST
        );


        add(
                mainPanel,
                BorderLayout.CENTER
        );



        loadDashboardData();




        addProduct.addActionListener(e -> {

                new AddProduct();

        });



        manageProduct.addActionListener(e -> {

                new ManageProducts();

        });



        orders.addActionListener(e -> {

                new Orders();

        });



        sales.addActionListener(e -> {

                new SalesReport();

        });



        logout.addActionListener(e -> {


            dispose();

            new Login();


        });




        setVisible(true);

    }





    private void loadDashboardData(){


        try{


            Connection con =
                    DBConnection.getConnection();



            int vendorId = Session.userId;



            // Product Count

            PreparedStatement ps1 =
                    con.prepareStatement(
                            "SELECT COUNT(*) FROM products WHERE vendor_id=?"
                    );


            ps1.setInt(1,vendorId);


            ResultSet rs1 =
                    ps1.executeQuery();


            if(rs1.next()){

                productValue.setText(
                        rs1.getString(1)
                );

            }





            // Orders Count

            PreparedStatement ps2 =
                    con.prepareStatement(
                            "SELECT COUNT(*) FROM orders WHERE vendor_id=?"
                    );


            ps2.setInt(1,vendorId);


            ResultSet rs2 =
                    ps2.executeQuery();


            if(rs2.next()){

                orderValue.setText(
                        rs2.getString(1)
                );

            }





            // Revenue

            PreparedStatement ps3 =
                    con.prepareStatement(
                            "SELECT SUM(total_amount) FROM orders WHERE vendor_id=?"
                    );


            ps3.setInt(1,vendorId);


            ResultSet rs3 =
                    ps3.executeQuery();



            if(rs3.next()){


                double revenue =
                        rs3.getDouble(1);


                revenueValue.setText(
                        "₹"+revenue
                );

            }




        }
        catch(Exception e){

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        }


    }






    private JPanel createCard(
            String title,
            String value
    ){


        JPanel card =
                new JPanel(
                        new GridLayout(2,1)
                );


        card.setBorder(
                BorderFactory.createLineBorder(
                        Color.LIGHT_GRAY
                )
        );



        JLabel t =
                new JLabel(title);



        t.setHorizontalAlignment(
                SwingConstants.CENTER
        );



        JLabel v =
                new JLabel(value);



        v.setHorizontalAlignment(
                SwingConstants.CENTER
        );



        t.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );


        v.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );



        card.add(t);

        card.add(v);



        return card;

    }


}