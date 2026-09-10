package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import database.DBConnection;


public class Inventory extends JFrame {


    JTable inventoryTable;

    DefaultTableModel model;


    JTextField quantityField;



    public Inventory(){


        setTitle("ShopSphere - Inventory Management");

        setSize(1000,600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());





        JLabel title =
                new JLabel("Inventory Management");


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







        // TABLE


        model =
                new DefaultTableModel();



        model.addColumn("Product ID");

        model.addColumn("Product Name");

        model.addColumn("Category");

        model.addColumn("Quantity");

        model.addColumn("Price");

        model.addColumn("Vendor ID");

        model.addColumn("Status");




        inventoryTable =
                new JTable(model);



        inventoryTable.setRowHeight(30);



        add(
                new JScrollPane(inventoryTable),
                BorderLayout.CENTER
        );










        // BOTTOM PANEL


        JPanel bottomPanel =
                new JPanel();



        quantityField =
                new JTextField(10);



        JButton updateStock =
                new JButton("Update Stock");



        JButton deleteProduct =
                new JButton("Delete Product");




        bottomPanel.add(
                new JLabel("New Quantity:")
        );


        bottomPanel.add(quantityField);


        bottomPanel.add(updateStock);


        bottomPanel.add(deleteProduct);




        add(
                bottomPanel,
                BorderLayout.SOUTH
        );






        loadInventory();









        // UPDATE STOCK


        updateStock.addActionListener(e -> {



            int row =
                    inventoryTable.getSelectedRow();



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
                inventoryTable.getValueAt(row,0).toString()
                );



                int quantity =
                Integer.parseInt(
                quantityField.getText()
                );



                Connection con =
                        DBConnection.getConnection();



                PreparedStatement ps =
                con.prepareStatement(
                "UPDATE products SET quantity=? WHERE product_id=?"
                );



                ps.setInt(1,quantity);

                ps.setInt(2,id);



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Stock Updated Successfully"
                );



                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }



        });









        // DELETE PRODUCT


        deleteProduct.addActionListener(e -> {



            int row =
                    inventoryTable.getSelectedRow();



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
                inventoryTable.getValueAt(row,0).toString()
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








    private void loadInventory(){


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



                int quantity =
                        rs.getInt("quantity");



                String status;



                if(quantity==0){

                    status="Out of Stock";

                }
                else if(quantity<=10){

                    status="Low Stock";

                }
                else{

                    status="Available";

                }




                model.addRow(
                        new Object[]{


                        rs.getInt("product_id"),


                        rs.getString("product_name"),


                        rs.getString("category"),


                        quantity,


                        rs.getDouble("price"),


                        rs.getInt("vendor_id"),


                        status


                        }
                );



            }



        }
        catch(Exception e){

            e.printStackTrace();

        }



    }







    private void refreshTable(){


        loadInventory();

        quantityField.setText("");

    }





    public static void main(String[] args){


        new Inventory();


    }



}