package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import database.DBConnection;


public class Products extends JFrame {


    JTextField nameField;
    JTextField categoryField;
    JTextField priceField;
    JTextField quantityField;
    JTextField vendorField;


    JTable table;

    DefaultTableModel model;



    public Products(){


        setTitle("ShopSphere - Product Management");

        setSize(1000,600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());



        JLabel title =
                new JLabel("Product Management");


        title.setFont(
                new Font("Segoe UI",Font.BOLD,30)
        );


        title.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );


        add(title,BorderLayout.NORTH);





        JPanel formPanel =
                new JPanel(
                        new GridLayout(8,2,10,10)
                );


        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );



        nameField = new JTextField();

        categoryField = new JTextField();

        priceField = new JTextField();

        quantityField = new JTextField();

        vendorField = new JTextField();




        JButton addButton =
                new JButton("Add Product");


        JButton updateButton =
                new JButton("Update Product");


        JButton deleteButton =
                new JButton("Delete Product");





        formPanel.add(new JLabel("Product Name"));
        formPanel.add(nameField);



        formPanel.add(new JLabel("Category"));
        formPanel.add(categoryField);



        formPanel.add(new JLabel("Price"));
        formPanel.add(priceField);



        formPanel.add(new JLabel("Quantity"));
        formPanel.add(quantityField);



        formPanel.add(new JLabel("Vendor ID"));
        formPanel.add(vendorField);



        formPanel.add(addButton);

        formPanel.add(updateButton);



        formPanel.add(deleteButton);



        add(
                formPanel,
                BorderLayout.WEST
        );







        model =
                new DefaultTableModel();



        model.addColumn("Product ID");

        model.addColumn("Product Name");

        model.addColumn("Category");

        model.addColumn("Price");

        model.addColumn("Quantity");

        model.addColumn("Vendor ID");




        table =
                new JTable(model);



        table.setRowHeight(30);



        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );



        loadProducts();







        // ADD PRODUCT


        addButton.addActionListener(e -> {



            try{


                if(nameField.getText().isEmpty()
                        || categoryField.getText().isEmpty()
                        || priceField.getText().isEmpty()
                        || quantityField.getText().isEmpty()
                        || vendorField.getText().isEmpty()){


                    JOptionPane.showMessageDialog(
                            this,
                            "Fill all fields"
                    );

                    return;

                }




                Connection con =
                        DBConnection.getConnection();



                String sql =
                "INSERT INTO products(product_name,category,price,quantity,vendor_id) VALUES(?,?,?,?,?)";



                PreparedStatement ps =
                        con.prepareStatement(sql);



                ps.setString(
                        1,
                        nameField.getText()
                );


                ps.setString(
                        2,
                        categoryField.getText()
                );


                ps.setDouble(
                        3,
                        Double.parseDouble(
                                priceField.getText()
                        )
                );


                ps.setInt(
                        4,
                        Integer.parseInt(
                                quantityField.getText()
                        )
                );


                ps.setInt(
                        5,
                        Integer.parseInt(
                                vendorField.getText()
                        )
                );



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Product Added Successfully"
                );



                refreshTable();


            }
            catch(Exception ex){


                ex.printStackTrace();


                JOptionPane.showMessageDialog(
                        this,
                        "Error Adding Product"
                );

            }



        });










        // SELECT TABLE ROW


        table.addMouseListener(
                new java.awt.event.MouseAdapter(){


            public void mouseClicked(
                    java.awt.event.MouseEvent e
            ){



                int row =
                        table.getSelectedRow();



                nameField.setText(
                        table.getValueAt(row,1).toString()
                );


                categoryField.setText(
                        table.getValueAt(row,2).toString()
                );


                priceField.setText(
                        table.getValueAt(row,3).toString()
                );


                quantityField.setText(
                        table.getValueAt(row,4).toString()
                );


                vendorField.setText(
                        table.getValueAt(row,5).toString()
                );


            }


        });










        // UPDATE PRODUCT


        updateButton.addActionListener(e -> {



            int row =
                    table.getSelectedRow();



            if(row==-1){


                JOptionPane.showMessageDialog(
                        this,
                        "Select Product First"
                );


                return;

            }



            try{


                int id =
                Integer.parseInt(
                        table.getValueAt(row,0).toString()
                );



                Connection con =
                        DBConnection.getConnection();



                String sql =
                "UPDATE products SET product_name=?,category=?,price=?,quantity=?,vendor_id=? WHERE product_id=?";



                PreparedStatement ps =
                        con.prepareStatement(sql);



                ps.setString(1,nameField.getText());

                ps.setString(2,categoryField.getText());

                ps.setDouble(3,
                        Double.parseDouble(
                                priceField.getText()
                        )
                );


                ps.setInt(4,
                        Integer.parseInt(
                                quantityField.getText()
                        )
                );


                ps.setInt(5,
                        Integer.parseInt(
                                vendorField.getText()
                        )
                );


                ps.setInt(6,id);



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Product Updated"
                );



                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }



        });










        // DELETE PRODUCT


        deleteButton.addActionListener(e -> {



            int row =
                    table.getSelectedRow();



            if(row==-1){


                JOptionPane.showMessageDialog(
                        this,
                        "Select Product First"
                );


                return;

            }



            try{


                int id =
                Integer.parseInt(
                        table.getValueAt(row,0).toString()
                );



                Connection con =
                        DBConnection.getConnection();



                PreparedStatement ps =
                con.prepareStatement(
                "DELETE FROM products WHERE product_id=?"
                );



                ps.setInt(1,id);



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Product Deleted"
                );



                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }



        });








        setVisible(true);

    }








    private void loadProducts(){



        try{


            model.setRowCount(0);



            Connection con =
                    DBConnection.getConnection();



            PreparedStatement ps =
            con.prepareStatement(
            "SELECT * FROM products"
            );



            ResultSet rs =
                    ps.executeQuery();



            while(rs.next()){



                model.addRow(
                        new Object[]{


                                rs.getInt("product_id"),


                                rs.getString("product_name"),


                                rs.getString("category"),


                                rs.getDouble("price"),


                                rs.getInt("quantity"),


                                rs.getInt("vendor_id")


                        }
                );


            }



        }
        catch(Exception e){

            e.printStackTrace();

        }



    }








    private void refreshTable(){


        loadProducts();


        nameField.setText("");

        categoryField.setText("");

        priceField.setText("");

        quantityField.setText("");

        vendorField.setText("");

    }



}