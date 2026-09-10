package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import database.DBConnection;


public class Suppliers extends JFrame {


    JTable table;

    DefaultTableModel model;


    JTextField nameField;
    JTextField emailField;
    JTextField phoneField;
    JTextField addressField;



    public Suppliers(){


        setTitle("ShopSphere - Supplier Management");

        setSize(1000,600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());





        JLabel title =
                new JLabel("Supplier Management");


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








        // FORM


        JPanel form =
                new JPanel(
                        new GridLayout(6,2,10,10)
                );


        form.setBorder(
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
                new JButton("Add Supplier");


        JButton updateButton =
                new JButton("Update Supplier");


        JButton deleteButton =
                new JButton("Delete Supplier");





        form.add(new JLabel("Supplier Name"));
        form.add(nameField);



        form.add(new JLabel("Email"));
        form.add(emailField);



        form.add(new JLabel("Phone"));
        form.add(phoneField);



        form.add(new JLabel("Address"));
        form.add(addressField);



        form.add(addButton);
        form.add(updateButton);



        add(
                form,
                BorderLayout.WEST
        );









        // TABLE


        model =
                new DefaultTableModel();



        model.addColumn("Supplier ID");

        model.addColumn("Supplier Name");

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



        JPanel bottom =
                new JPanel();



        bottom.add(deleteButton);



        add(
                bottom,
                BorderLayout.SOUTH
        );



        loadSuppliers();









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









        // ADD SUPPLIER


        addButton.addActionListener(e -> {


            try{


                Connection con =
                        DBConnection.getConnection();



                PreparedStatement ps =
                con.prepareStatement(
                "INSERT INTO suppliers(supplier_name,email,phone,address) VALUES(?,?,?,?)"
                );



                ps.setString(1,nameField.getText());

                ps.setString(2,emailField.getText());

                ps.setString(3,phoneField.getText());

                ps.setString(4,addressField.getText());



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Supplier Added Successfully"
                );


                refreshTable();



            }
            catch(Exception ex){

                ex.printStackTrace();

            }


        });









        // UPDATE SUPPLIER


        updateButton.addActionListener(e -> {


            int row =
                    table.getSelectedRow();



            if(row==-1){

                JOptionPane.showMessageDialog(
                        this,
                        "Select Supplier First"
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
                "UPDATE suppliers SET supplier_name=?,email=?,phone=?,address=? WHERE supplier_id=?"
                );



                ps.setString(1,nameField.getText());

                ps.setString(2,emailField.getText());

                ps.setString(3,phoneField.getText());

                ps.setString(4,addressField.getText());

                ps.setInt(5,id);



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Supplier Updated"
                );



                refreshTable();


            }
            catch(Exception ex){

                ex.printStackTrace();

            }


        });









        // DELETE SUPPLIER


        deleteButton.addActionListener(e -> {


            int row =
                    table.getSelectedRow();



            if(row==-1){

                JOptionPane.showMessageDialog(
                        this,
                        "Select Supplier First"
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
                "DELETE FROM suppliers WHERE supplier_id=?"
                );



                ps.setInt(1,id);



                ps.executeUpdate();



                JOptionPane.showMessageDialog(
                        this,
                        "Supplier Deleted"
                );



                refreshTable();


            }
            catch(Exception ex){

                ex.printStackTrace();

            }


        });







        setVisible(true);


    }








    private void loadSuppliers(){


        try{


            model.setRowCount(0);



            Connection con =
                    DBConnection.getConnection();



            PreparedStatement ps =
            con.prepareStatement(
            "SELECT * FROM suppliers"
            );



            ResultSet rs =
                    ps.executeQuery();



            while(rs.next()){



                model.addRow(
                        new Object[]{


                        rs.getInt("supplier_id"),

                        rs.getString("supplier_name"),

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


        loadSuppliers();


        nameField.setText("");

        emailField.setText("");

        phoneField.setText("");

        addressField.setText("");

    }




    public static void main(String[] args){

        new Suppliers();

    }


}