package view;

import database.DBConnection;
import session.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomerCart extends JFrame {


    private JTable table;
    private DefaultTableModel model;



    public CustomerCart() {


        setTitle("My Cart");
        setSize(850,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());



        JLabel heading =
                new JLabel("My Cart",
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
                        "Cart ID",
                        "Product Name",
                        "Price",
                        "Quantity",
                        "Added Date"
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


        JButton remove =
                new JButton("Remove");


        JButton checkout =
                new JButton("Checkout");


        JButton close =
                new JButton("Close");



        bottom.add(refresh);
        bottom.add(remove);
        bottom.add(checkout);
        bottom.add(close);



        add(bottom,BorderLayout.SOUTH);




        refresh.addActionListener(e->loadCart());



        remove.addActionListener(e->removeItem());



        checkout.addActionListener(e->checkout());



        close.addActionListener(e->dispose());



        loadCart();



        setVisible(true);


    }






    private void removeItem(){


        int row =
                table.getSelectedRow();



        if(row==-1){


            JOptionPane.showMessageDialog(
                    this,
                    "Select an item first."
            );


            return;

        }



        try{


            Connection con =
                    DBConnection.getConnection();



            PreparedStatement ps =
                    con.prepareStatement(
                    "DELETE FROM cart WHERE cart_id=? AND customer_id=?"
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
                    "Item removed."
            );



            loadCart();



        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        }


    }







    private void checkout(){
        String paymentMethod = "Cash on Delivery";

        int choice = JOptionPane.showConfirmDialog(

        this,

        "Payment Method : Cash on Delivery\n\nDo you want to place this order?",

        "Checkout",

        JOptionPane.YES_NO_OPTION

        );

        if(choice != JOptionPane.YES_OPTION){

            return;

        }



        try{


            Connection con =
                    DBConnection.getConnection();



            con.setAutoCommit(false);



            // Calculate total

            PreparedStatement totalPs =
                    con.prepareStatement(

                    "SELECT SUM(p.price*c.quantity) AS total "+
                    "FROM cart c JOIN products p "+
                    "ON c.product_id=p.product_id "+
                    "WHERE c.customer_id=?"

                    );



            totalPs.setInt(
                    1,
                    Session.userId
            );



            ResultSet totalRs =
                    totalPs.executeQuery();



            double total=0;



            if(totalRs.next()){

                total =
                totalRs.getDouble("total");

            }



            if(total==0){
                con.rollback();


                JOptionPane.showMessageDialog(
                        this,
                        "Cart is empty."
                );


                return;

            }






            // Create Order

            PreparedStatement orderPs =
                    con.prepareStatement(

                    "INSERT INTO orders(customer_id,order_date,total_amount,status,payment_method,payment_status) VALUES(?,?,?,?,?,?)",

                    Statement.RETURN_GENERATED_KEYS

                    );



            orderPs.setInt(1, Session.userId);

            orderPs.setDate(
                    2,
                    new java.sql.Date(System.currentTimeMillis())
            );

            orderPs.setDouble(
                    3,
                    total
            );

            orderPs.setString(
                    4,
                    "Pending"
            );

            orderPs.setString(
                    5,
                    paymentMethod
            );

            orderPs.setString(
                    6,
                    "Pending"
            );



            orderPs.executeUpdate();



            ResultSet key =
                    orderPs.getGeneratedKeys();



            int orderId=0;



            if(key.next()){

                orderId =
                key.getInt(1);

            }







            // Insert order items + update stock


            PreparedStatement cartPs =
                    con.prepareStatement(

                    "SELECT product_id,quantity FROM cart WHERE customer_id=?"

                    );



            cartPs.setInt(
                    1,
                    Session.userId
            );



            ResultSet cartRs =
                    cartPs.executeQuery();






            while(cartRs.next()){


                int productId =
                cartRs.getInt("product_id");



                int qty =
                cartRs.getInt("quantity");




                // Stock update

                PreparedStatement stockPs =
                        con.prepareStatement(

                        "UPDATE products SET quantity=quantity-? WHERE product_id=?"

                        );



                stockPs.setInt(
                        1,
                        qty
                );


                stockPs.setInt(
                        2,
                        productId
                );



                stockPs.executeUpdate();


                stockPs.close();





                // Get price

                PreparedStatement pricePs =
                        con.prepareStatement(

                        "SELECT price FROM products WHERE product_id=?"

                        );


                pricePs.setInt(
                        1,
                        productId
                );



                ResultSet priceRs =
                        pricePs.executeQuery();



                double price=0;



                if(priceRs.next()){

                    price =
                    priceRs.getDouble("price");

                }







                // Insert order item

                PreparedStatement itemPs =
                        con.prepareStatement(

                        "INSERT INTO order_items(order_id,product_id,quantity,price) VALUES(?,?,?,?)"

                        );



                itemPs.setInt(
                        1,
                        orderId
                );


                itemPs.setInt(
                        2,
                        productId
                );


                itemPs.setInt(
                        3,
                        qty
                );


                itemPs.setDouble(
                        4,
                        price
                );



                itemPs.executeUpdate();



                itemPs.close();
                priceRs.close();
                pricePs.close();


            }





            // Clear cart

            PreparedStatement clearPs =
                    con.prepareStatement(

                    "DELETE FROM cart WHERE customer_id=?"

                    );



            clearPs.setInt(
                    1,
                    Session.userId
            );



            clearPs.executeUpdate();




            con.commit();



            JOptionPane.showMessageDialog(

                    this,

                    "Order Placed Successfully.\n\n"

                    + "Payment Method : Cash on Delivery\n"

                    + "Payment Status : Pending"

            );



            loadCart();



            con.close();


        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        }



    }








    private void loadCart(){


        model.setRowCount(0);



        try{


            Connection con =
                    DBConnection.getConnection();



            PreparedStatement ps =
                    con.prepareStatement(

                    "SELECT c.cart_id,p.product_name,p.price,c.quantity,c.added_date "+
                    "FROM cart c JOIN products p "+
                    "ON c.product_id=p.product_id "+
                    "WHERE c.customer_id=?"

                    );



            ps.setInt(
                    1,
                    Session.userId
            );



            ResultSet rs =
                    ps.executeQuery();




            while(rs.next()){


                model.addRow(
                        new Object[]{

                        rs.getInt("cart_id"),

                        rs.getString("product_name"),

                        rs.getDouble("price"),

                        rs.getInt("quantity"),

                        rs.getTimestamp("added_date")

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