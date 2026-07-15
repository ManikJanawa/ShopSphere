package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import database.DBConnection;


public class Customers extends JFrame {


    JTable table;

    DefaultTableModel model;


    JTextField nameField;
    JTextField emailField;
    JTextField phoneField;
    JTextField addressField;



    public Customers(){


        setTitle("ShopSphere - Customer Management");

        setSize(1000,600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());





        JLabel title =
                new JLabel("Customer Management");



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







        // FORM PANEL


        JPanel panel =
                new JPanel(
                        new GridLayout(6,2,10,10)
                );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );



        nameField =
                new JTextField();


        emailField =
                new JTextField();


        phoneField =
                new JTextField();


        addressField =
                new JTextField();




        JButton addButton =
                new JButton("Add Customer");


        JButton updateButton =
                new JButton("Update Customer");


        JButton deleteButton =
                new JButton("Delete Customer");





        panel.add(new JLabel("Name"));

        panel.add(nameField);



        panel.add(new JLabel("Email"));

        panel.add(emailField);



        panel.add(new JLabel("Phone"));

        panel.add(phoneField);



        panel.add(new JLabel("Address"));

        panel.add(addressField);



        panel.add(addButton);

        panel.add(updateButton);



        panel.add(deleteButton);



        add(
                panel,
                BorderLayout.WEST
        );









        // TABLE


        model =
                new DefaultTableModel();



        model.addColumn("Customer ID");

        model.addColumn("Customer Name");

        model.addColumn("Email");

        model.addColumn("Phone");

        model.addColumn("Address");




        table =
                new JTable(model);



        table.setRowHeight(30);



        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );



        loadCustomers();









        // ADD CUSTOMER


        addButton.addActionListener(e -> {



            try{


                Connection con =
                        DBConnection.getConnection();



                String sql =
                "INSERT INTO customers(customer_name,email,phone,address) VALUES(?,?,?,?)";



                PreparedStatement ps =
                        con.prepareStatement(sql);



                ps.setString(
                        1,
                        nameField.getText()
                );


                ps.setString(
                        2,
                        emailField.getText()
                );


                ps.setString(
                        3,
                        phoneField.getText()
                );


                ps.setString(
                        4,
                        addressField.getText()
                );



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Customer Added Successfully"
                );



                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }



        });









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


                        addressField.setText(
                                table.getValueAt(row,4).toString()
                        );



                    }



                });










        // UPDATE CUSTOMER


        updateButton.addActionListener(e -> {



            int row =
                    table.getSelectedRow();



            if(row==-1){


                JOptionPane.showMessageDialog(
                        this,
                        "Select Customer First"
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
                "UPDATE customers SET customer_name=?,email=?,phone=?,address=? WHERE customer_id=?";



                PreparedStatement ps =
                        con.prepareStatement(sql);



                ps.setString(
                        1,
                        nameField.getText()
                );


                ps.setString(
                        2,
                        emailField.getText()
                );


                ps.setString(
                        3,
                        phoneField.getText()
                );


                ps.setString(
                        4,
                        addressField.getText()
                );


                ps.setInt(
                        5,
                        id
                );



                ps.executeUpdate();




                JOptionPane.showMessageDialog(
                        this,
                        "Customer Updated"
                );



                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }



        });









        // DELETE CUSTOMER


        deleteButton.addActionListener(e -> {



            int row =
                    table.getSelectedRow();



            if(row==-1){


                JOptionPane.showMessageDialog(
                        this,
                        "Select Customer First"
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
                "DELETE FROM customers WHERE customer_id=?"
                );



                ps.setInt(
                        1,
                        id
                );



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Customer Deleted"
                );



                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }



        });









        setVisible(true);


    }








    private void loadCustomers(){


        try{


            model.setRowCount(0);



            Connection con =
                    DBConnection.getConnection();



            PreparedStatement ps =
            con.prepareStatement(
            "SELECT * FROM customers"
            );



            ResultSet rs =
                    ps.executeQuery();



            while(rs.next()){



                model.addRow(
                        new Object[]{


                                rs.getInt("customer_id"),


                                rs.getString("customer_name"),


                                rs.getString("email"),


                                rs.getString("phone"),


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


        loadCustomers();


        nameField.setText("");

        emailField.setText("");

        phoneField.setText("");

        addressField.setText("");

    }



}