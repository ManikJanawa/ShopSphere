package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import database.DBConnection;


public class Vendors extends JFrame {


    JTable table;

    DefaultTableModel model;


    JTextField nameField;
    JTextField emailField;
    JTextField phoneField;
    JTextField shopField;
    JTextField addressField;



    public Vendors(){


        setTitle("ShopSphere - Vendor Management");

        setSize(1100,600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());




        JLabel title =
                new JLabel("Vendor Management");


        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );


        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        title.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );


        add(title,BorderLayout.NORTH);







        JPanel panel =
                new JPanel(
                        new GridLayout(7,2,10,10)
                );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );



        nameField = new JTextField();

        emailField = new JTextField();

        phoneField = new JTextField();

        shopField = new JTextField();

        addressField = new JTextField();




        JButton addButton =
                new JButton("Add Vendor");


        JButton updateButton =
                new JButton("Update Vendor");


        JButton deleteButton =
                new JButton("Delete Vendor");




        panel.add(new JLabel("Vendor Name"));
        panel.add(nameField);


        panel.add(new JLabel("Email"));
        panel.add(emailField);


        panel.add(new JLabel("Phone"));
        panel.add(phoneField);


        panel.add(new JLabel("Shop Name"));
        panel.add(shopField);


        panel.add(new JLabel("Address"));
        panel.add(addressField);


        panel.add(addButton);
        panel.add(updateButton);


        panel.add(deleteButton);



        add(
                panel,
                BorderLayout.WEST
        );









        model =
                new DefaultTableModel();



        model.addColumn("Vendor ID");

        model.addColumn("Vendor Name");

        model.addColumn("Email");

        model.addColumn("Phone");

        model.addColumn("Shop Name");

        model.addColumn("Address");



        table =
                new JTable(model);



        table.setRowHeight(30);



        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );



        loadVendors();








        // SELECT ROW


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


                        emailField.setText(
                                table.getValueAt(row,2).toString()
                        );


                        phoneField.setText(
                                table.getValueAt(row,3).toString()
                        );


                        shopField.setText(
                                table.getValueAt(row,4).toString()
                        );


                        addressField.setText(
                                table.getValueAt(row,5).toString()
                        );


                    }


                });









        // ADD VENDOR


        addButton.addActionListener(e -> {


            try{


                Connection con =
                        DBConnection.getConnection();



                PreparedStatement ps =
                con.prepareStatement(
                "INSERT INTO vendors(vendor_name,email,phone,shop_name,address) VALUES(?,?,?,?,?)"
                );



                ps.setString(1,nameField.getText());

                ps.setString(2,emailField.getText());

                ps.setString(3,phoneField.getText());

                ps.setString(4,shopField.getText());

                ps.setString(5,addressField.getText());



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Vendor Added Successfully"
                );



                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }


        });









        // UPDATE VENDOR


        updateButton.addActionListener(e -> {


            int row =
                    table.getSelectedRow();



            if(row==-1){


                JOptionPane.showMessageDialog(
                        this,
                        "Select Vendor First"
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
                "UPDATE vendors SET vendor_name=?,email=?,phone=?,shop_name=?,address=? WHERE vendor_id=?"
                );



                ps.setString(1,nameField.getText());

                ps.setString(2,emailField.getText());

                ps.setString(3,phoneField.getText());

                ps.setString(4,shopField.getText());

                ps.setString(5,addressField.getText());

                ps.setInt(6,id);



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Vendor Updated"
                );



                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }


        });









        // DELETE VENDOR


        deleteButton.addActionListener(e -> {


            int row =
                    table.getSelectedRow();



            if(row==-1){


                JOptionPane.showMessageDialog(
                        this,
                        "Select Vendor First"
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
                "DELETE FROM vendors WHERE vendor_id=?"
                );



                ps.setInt(1,id);



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Vendor Deleted"
                );



                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }


        });





        setVisible(true);

    }








    private void loadVendors(){


        try{


            model.setRowCount(0);



            Connection con =
                    DBConnection.getConnection();



            PreparedStatement ps =
            con.prepareStatement(
            "SELECT * FROM vendors"
            );



            ResultSet rs =
                    ps.executeQuery();



            while(rs.next()){



                model.addRow(
                        new Object[]{


                        rs.getInt("vendor_id"),

                        rs.getString("vendor_name"),

                        rs.getString("email"),

                        rs.getString("phone"),

                        rs.getString("shop_name"),

                        rs.getString("address")


                        }
                );


            }



        }
        catch(Exception e){

            e.printStackTrace();

        }


    }







    private void refreshTable(){


        loadVendors();


        nameField.setText("");

        emailField.setText("");

        phoneField.setText("");

        shopField.setText("");

        addressField.setText("");

    }



}