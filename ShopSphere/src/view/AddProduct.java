package view;

import database.DBConnection;
import session.Session;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddProduct extends JFrame {

    private JTextField nameField, categoryField, priceField, quantityField;

    public AddProduct() {
        setTitle("Add Product");
        setSize(450,420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel=new JPanel(new GridLayout(5,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        panel.add(new JLabel("Product Name"));
        nameField=new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Category"));
        categoryField=new JTextField();
        panel.add(categoryField);

        panel.add(new JLabel("Price"));
        priceField=new JTextField();
        panel.add(priceField);

        panel.add(new JLabel("Quantity"));
        quantityField=new JTextField();
        panel.add(quantityField);

        JButton add=new JButton("Add Product");
        JButton close=new JButton("Close");
        panel.add(add);
        panel.add(close);
        add(panel);

        add.addActionListener(e->addProduct());
        close.addActionListener(e->dispose());

        setVisible(true);
    }

    private void addProduct(){
        try{
            if(nameField.getText().trim().isEmpty()||
               categoryField.getText().trim().isEmpty()||
               priceField.getText().trim().isEmpty()||
               quantityField.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this,"Fill all fields.");
                return;
            }

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=con.prepareStatement(
            "INSERT INTO products(product_name,category,price,quantity,vendor_id) VALUES(?,?,?,?,?)");

            ps.setString(1,nameField.getText().trim());
            ps.setString(2,categoryField.getText().trim());
            ps.setDouble(3,Double.parseDouble(priceField.getText().trim()));
            ps.setInt(4,Integer.parseInt(quantityField.getText().trim()));
            ps.setInt(5,Session.userId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Product Added Successfully.");

            nameField.setText("");
            categoryField.setText("");
            priceField.setText("");
            quantityField.setText("");

            ps.close();
            con.close();

        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this,"Price and Quantity must be numbers.");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }
    }
}
