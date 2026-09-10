package view;

import database.DBConnection;
import session.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Orders extends JFrame {


    JTable table;
    DefaultTableModel model;


    public Orders(){


        setTitle("Vendor Orders");
        setSize(1000,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());



        JLabel heading =
                new JLabel(
                        "Customer Orders",
                        SwingConstants.CENTER
                );


        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );


        add(heading,BorderLayout.NORTH);




        model =
                new DefaultTableModel();


        model.setColumnIdentifiers(
                new String[]{

                        "Order ID",
                        "Product Name",
                        "Quantity",
                        "Price",
                        "Customer ID",
                        "Order Date",
                        "Status"

                }
        );



        table =
                new JTable(model);



        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );



        JButton refresh =
                new JButton("Refresh");



        add(
                refresh,
                BorderLayout.SOUTH
        );



        refresh.addActionListener(
                e->loadOrders()
        );



        loadOrders();



        setVisible(true);


    }






    private void loadOrders(){


        model.setRowCount(0);



        try{


            Connection con =
                    DBConnection.getConnection();



            String query =

            "SELECT o.order_id,"+
            "p.product_name,"+
            "oi.quantity,"+
            "oi.price,"+
            "o.customer_id,"+
            "o.order_date,"+
            "o.status "+
            
            "FROM orders o "+
            
            "JOIN order_items oi "+
            "ON o.order_id = oi.order_id "+
            
            "JOIN products p "+
            "ON oi.product_id = p.product_id "+
            
            "WHERE p.vendor_id=?";




            PreparedStatement ps =
                    con.prepareStatement(query);



            ps.setInt(
                    1,
                    Session.userId
            );



            ResultSet rs =
                    ps.executeQuery();




            while(rs.next()){


                model.addRow(
                        new Object[]{


                                rs.getInt("order_id"),

                                rs.getString("product_name"),

                                rs.getInt("quantity"),

                                rs.getDouble("price"),

                                rs.getInt("customer_id"),

                                rs.getDate("order_date"),

                                rs.getString("status")


                        }
                );


            }



            rs.close();
            ps.close();
            con.close();



        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        }


    }



}