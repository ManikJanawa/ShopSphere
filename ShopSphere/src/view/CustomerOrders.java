package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import database.DBConnection;
import session.Session;

public class CustomerOrders extends JFrame {

    JTable orderTable;
    DefaultTableModel tableModel;


    public CustomerOrders() {

        setTitle("ShopSphere - My Orders");
        setSize(900,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));


        JLabel heading = new JLabel("My Orders");
        heading.setFont(new Font("Arial",Font.BOLD,28));
        heading.setHorizontalAlignment(JLabel.CENTER);


        tableModel = new DefaultTableModel();

        tableModel.addColumn("Order ID");
        tableModel.addColumn("Product");
        tableModel.addColumn("Price");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Order Date");
        tableModel.addColumn("Status");


        orderTable = new JTable(tableModel);

        orderTable.setRowHeight(25);
        orderTable.setFont(new Font("Arial",Font.PLAIN,14));


        JScrollPane scrollPane = new JScrollPane(orderTable);



        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");


        refreshButton.setFont(new Font("Arial",Font.BOLD,14));
        closeButton.setFont(new Font("Arial",Font.BOLD,14));



        JPanel buttonPanel = new JPanel();

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);



        refreshButton.addActionListener(e -> {

            loadOrders();

        });



        closeButton.addActionListener(e -> {

            dispose();

        });



        panel.add(heading,BorderLayout.NORTH);

        panel.add(scrollPane,BorderLayout.CENTER);

        panel.add(buttonPanel,BorderLayout.SOUTH);



        add(panel);


        loadOrders();


        setVisible(true);

    }





    private void loadOrders(){


        tableModel.setRowCount(0);


        try{


            Connection con = DBConnection.getConnection();



            String query =
            "SELECT o.order_id, " +
            "p.product_name, " +
            "oi.price, " +
            "oi.quantity, " +
            "o.order_date, " +
            "o.status " +
            "FROM orders o " +
            "JOIN order_items oi ON o.order_id = oi.order_id " +
            "JOIN products p ON oi.product_id = p.product_id " +
            "WHERE o.customer_id = ?";


            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, Session.userId);


            ResultSet rs = pst.executeQuery();



            while(rs.next()){


                Object row[] = {

                        rs.getInt("order_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getDate("order_date"),
                        rs.getString("status")

                };


                tableModel.addRow(row);


            }



            rs.close();

            pst.close();

            con.close();



        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    "Orders Load Error : " + e.getMessage()
            );


        }


    }





    public static void main(String args[]){


        new CustomerOrders();


    }



}
