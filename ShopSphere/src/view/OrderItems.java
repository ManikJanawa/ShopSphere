package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;


public class OrderItems extends JFrame {


    JTable table;
    DefaultTableModel model;


    JTextField orderIdField;
    JTextField productIdField;
    JTextField quantityField;
    JTextField priceField;



    public OrderItems(){


        setTitle("ShopSphere - Order Items");
        setSize(900,600);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());



        JLabel title =
                new JLabel("Order Items Management");


        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );


        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        add(title,BorderLayout.NORTH);





        JPanel panel = new JPanel();


        panel.setLayout(
                new GridLayout(5,2,10,10)
        );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );



        orderIdField = new JTextField();

        productIdField = new JTextField();

        quantityField = new JTextField();

        priceField = new JTextField();



        JButton addButton =
                new JButton("Add Item");


        JButton deleteButton =
                new JButton("Delete Item");




        panel.add(new JLabel("Order ID"));
        panel.add(orderIdField);


        panel.add(new JLabel("Product ID"));
        panel.add(productIdField);


        panel.add(new JLabel("Quantity"));
        panel.add(quantityField);


        panel.add(new JLabel("Price"));
        panel.add(priceField);


        panel.add(addButton);
        panel.add(deleteButton);



        add(panel,BorderLayout.WEST);






        model = new DefaultTableModel();



        model.addColumn("Item ID");
        model.addColumn("Order ID");
        model.addColumn("Product ID");
        model.addColumn("Quantity");
        model.addColumn("Price");



        table = new JTable(model);



        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );



        loadItems();






        addButton.addActionListener(e -> {


            try{


                Connection con =
                DBConnection.getConnection();



                String sql =
                "INSERT INTO order_items(order_id,product_id,quantity,price) VALUES(?,?,?,?)";



                PreparedStatement ps =
                con.prepareStatement(sql);



                ps.setInt(
                        1,
                        Integer.parseInt(orderIdField.getText())
                );


                ps.setInt(
                        2,
                        Integer.parseInt(productIdField.getText())
                );


                ps.setInt(
                        3,
                        Integer.parseInt(quantityField.getText())
                );


                ps.setDouble(
                        4,
                        Double.parseDouble(priceField.getText())
                );



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Item Added Successfully"
                );



                model.setRowCount(0);

                loadItems();


            }
            catch(Exception ex){

                ex.printStackTrace();

            }



        });







        deleteButton.addActionListener(e -> {


            int row =
                    table.getSelectedRow();



            if(row!=-1){


                try{


                    int id =
                    Integer.parseInt(
                    table.getValueAt(row,0).toString()
                    );



                    Connection con =
                    DBConnection.getConnection();



                    PreparedStatement ps =
                    con.prepareStatement(
                    "DELETE FROM order_items WHERE item_id=?"
                    );



                    ps.setInt(1,id);


                    ps.executeUpdate();



                    model.removeRow(row);



                }
                catch(Exception ex){

                    ex.printStackTrace();

                }


            }


        });





        setVisible(true);

    }







    private void loadItems(){


        try{


            Connection con =
            DBConnection.getConnection();



            PreparedStatement ps =
            con.prepareStatement(
            "SELECT * FROM order_items"
            );



            ResultSet rs =
            ps.executeQuery();



            while(rs.next()){


                model.addRow(
                        new Object[]{

                                rs.getInt("item_id"),

                                rs.getInt("order_id"),

                                rs.getInt("product_id"),

                                rs.getInt("quantity"),

                                rs.getDouble("price")

                        }
                );


            }


        }
        catch(Exception e){

            e.printStackTrace();

        }


    }



}
