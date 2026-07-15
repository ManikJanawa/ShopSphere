package view;

import javax.swing.*;
import java.awt.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;
import javax.swing.border.EmptyBorder;


public class Dashboard extends JFrame {


    public Dashboard() {


        setTitle("ShopSphere - Admin Dashboard");

        setSize(1000,650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        JPanel mainPanel = new JPanel(new BorderLayout(20,20));

        mainPanel.setBackground(new Color(245,247,250));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20,20,20,20)
        );



        JLabel title =
                new JLabel("ShopSphere Admin Dashboard");


        title.setFont(
                new Font("Arial", Font.BOLD, 32)
        );

        title.setForeground(new Color(30,60,120));


        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        mainPanel.add(title,BorderLayout.NORTH);





        // Cards Panel

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(new Color(245,247,250));


        cardPanel.setLayout(
                new GridLayout(2,4,20,20)
        );



        cardPanel.add(
                createCard(
                        "Total Products",
                        String.valueOf(getCount("products"))
                )
        );



        cardPanel.add(
                createCard(
                        "Total Customers",
                        String.valueOf(getCount("customers"))
                )
        );



        cardPanel.add(
                createCard(
                        "Total Orders",
                        String.valueOf(getCount("orders"))
                )
        );



        cardPanel.add(
                createCard(
                        "Inventory Items",
                        String.valueOf(getCount("products"))
                )
        );



        cardPanel.add(
                createCard(
                        "Total Vendors",
                        String.valueOf(getCount("vendors"))
                )
        );



        cardPanel.add(
                createCard(
                        "Order Items",
                        String.valueOf(getCount("order_items"))
                )
        );



        cardPanel.add(
                createCard(
                        "Invoices",
                        "0"
                )
        );



        cardPanel.add(
                createCard(
                        "Revenue",
                        "₹" + getRevenue()
                )
        );





        mainPanel.add(
                cardPanel,
                BorderLayout.CENTER
        );







        // Buttons Panel


        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new GridLayout(2,4,15,15));

        buttonPanel.setBorder(
        new EmptyBorder(20,20,10,20)
        );

        buttonPanel.setBackground(new Color(245,247,250));




        JButton productsButton =
                new JButton("Products");
        styleButton(productsButton);


        JButton customersButton =
                new JButton("Customers");
        styleButton(customersButton);


        JButton vendorsButton =
                new JButton("Vendors");
        styleButton(vendorsButton);


        JButton ordersButton =
                new JButton("Orders");
        styleButton(ordersButton);


        JButton orderItemsButton =
                new JButton("Order Items");
        styleButton(orderItemsButton);


        JButton inventoryButton =
                new JButton("Inventory");
        styleButton(inventoryButton);


        JButton suppliersButton =
                new JButton("Suppliers");
        styleButton(suppliersButton);


        JButton billingButton =
                new JButton("Billing");
        styleButton(billingButton);






        buttonPanel.add(productsButton);

        buttonPanel.add(customersButton);

        buttonPanel.add(vendorsButton);

        buttonPanel.add(ordersButton);

        buttonPanel.add(orderItemsButton);

        buttonPanel.add(inventoryButton);

        buttonPanel.add(suppliersButton);

        buttonPanel.add(billingButton);






        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );







        // Button Actions



        productsButton.addActionListener(e -> {

            new Products();

        });




        customersButton.addActionListener(e -> {

            new Customers();

        });





        vendorsButton.addActionListener(e -> {

            new Vendors();

        });





        ordersButton.addActionListener(e -> {

                new AdminOrders();

        });





        orderItemsButton.addActionListener(e -> {

            new OrderItems();

        });





        inventoryButton.addActionListener(e -> {

            new Inventory();

        });





        suppliersButton.addActionListener(e -> {

            new Suppliers();

        });





        billingButton.addActionListener(e -> {

            new Billing();

        });







        add(mainPanel);


        setVisible(true);

    }







    // COUNT DATA FROM DATABASE

    private int getCount(String tableName){


        int count = 0;


        try{


            Connection con =
            DBConnection.getConnection();



            String sql =
            "SELECT COUNT(*) FROM " + tableName;



            PreparedStatement ps =
            con.prepareStatement(sql);



            ResultSet rs =
            ps.executeQuery();



            if(rs.next()){


                count =
                rs.getInt(1);


            }



        }
        catch(Exception e){

            e.printStackTrace();

        }



        return count;

    }







    // TOTAL REVENUE

    private double getRevenue(){


        double total = 0;



        try{


            Connection con =
            DBConnection.getConnection();



            PreparedStatement ps =
            con.prepareStatement(
            "SELECT SUM(total_amount) FROM orders"
            );



            ResultSet rs =
            ps.executeQuery();



            if(rs.next()){


                total =
                rs.getDouble(1);


            }



        }
        catch(Exception e){

            e.printStackTrace();

        }



        return total;

    }








    private JPanel createCard(
            String name,
            String value
    ){


        JPanel panel =
                new JPanel(new BorderLayout());



        panel.setBorder(
                BorderFactory.createLineBorder(
                        Color.GRAY,
                        2
                )
        );



        JLabel heading =
                new JLabel(name);



        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );


        heading.setHorizontalAlignment(
                SwingConstants.CENTER
        );




        JLabel number =
                new JLabel(value);



        number.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );



        number.setHorizontalAlignment(
                SwingConstants.CENTER
        );



        panel.add(
                heading,
                BorderLayout.NORTH
        );



        panel.add(
                number,
                BorderLayout.CENTER
        );



        return panel;

    }
    private void styleButton(JButton button){

        button.setFont(
            new Font("Arial",Font.BOLD,14)
        );

        button.setFocusPainted(false);

        button.setBackground(
            new Color(60,120,220)
        );

        button.setForeground(Color.WHITE);

        button.setPreferredSize(
            new Dimension(130,40)
        );
    }







    public static void main(String[] args){


        new Dashboard();


    }


}