package view;

import database.DBConnection;
import session.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageProducts extends JFrame {


    private JTable table;
    private DefaultTableModel model;


    public ManageProducts() {


        setTitle("Manage Products");
        setSize(900,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());



        JLabel heading =
                new JLabel("Manage Products",
                SwingConstants.CENTER);


        heading.setFont(
                new Font("Segoe UI",
                Font.BOLD,26)
        );


        add(heading,BorderLayout.NORTH);



        model =
                new DefaultTableModel();


        model.setColumnIdentifiers(
                new String[]{
                        "Product ID",
                        "Product Name",
                        "Category",
                        "Price",
                        "Quantity"
                }
        );



        table =
                new JTable(model);


        add(new JScrollPane(table),
                BorderLayout.CENTER);




        JPanel bottom =
                new JPanel();


        JButton refresh =
                new JButton("Refresh");


        JButton delete =
                new JButton("Delete Product");


        JButton close =
                new JButton("Close");



        bottom.add(refresh);
        bottom.add(delete);
        bottom.add(close);



        add(bottom,BorderLayout.SOUTH);




        refresh.addActionListener(e->loadProducts());


        delete.addActionListener(e->deleteProduct());


        close.addActionListener(e->dispose());



        loadProducts();



        setVisible(true);

    }






    private void loadProducts(){


        model.setRowCount(0);



        try{


            Connection con =
                    DBConnection.getConnection();



            PreparedStatement ps =
                    con.prepareStatement(
                    "SELECT product_id,product_name,category,price,quantity "+
                    "FROM products WHERE vendor_id=?"
                    );



            ps.setInt(1,Session.userId);



            ResultSet rs =
                    ps.executeQuery();



            while(rs.next()){


                model.addRow(
                        new Object[]{

                        rs.getInt("product_id"),

                        rs.getString("product_name"),

                        rs.getString("category"),

                        rs.getDouble("price"),

                        rs.getInt("quantity")

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







    private void deleteProduct(){



        int row =
                table.getSelectedRow();



        if(row==-1){


            JOptionPane.showMessageDialog(
                    this,
                    "Select a product."
            );


            return;

        }




        try{


            Connection con =
                    DBConnection.getConnection();



            PreparedStatement ps =
                    con.prepareStatement(
                    "DELETE FROM products WHERE product_id=? AND vendor_id=?"
                    );



            ps.setInt(
                    1,
                    Integer.parseInt(
                    model.getValueAt(row,0).toString())
            );


            ps.setInt(
                    2,
                    Session.userId
            );



            ps.executeUpdate();



            ps.close();
            con.close();



            JOptionPane.showMessageDialog(
                    this,
                    "Product Deleted Successfully."
            );



            loadProducts();



        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        }


    }


}