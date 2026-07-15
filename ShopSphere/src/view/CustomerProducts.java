package view;

import database.DBConnection;
import session.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomerProducts extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton refreshButton, closeButton;
    private JTextField searchField;

    public CustomerProducts() {

        setTitle("Browse Products");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Available Products", SwingConstants.CENTER);

        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));

        heading.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(heading, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel searchLabel = new JLabel("Search Product :");

        searchField = new JTextField(20);

        searchPanel.add(searchLabel);

        searchPanel.add(searchField);

        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "Product ID","Product Name","Category","Price","Quantity"
        });

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        refreshButton = new JButton("Refresh");
        JButton addCartButton = new JButton("Add To Cart");
        closeButton = new JButton("Close");

        bottom.add(refreshButton);
        bottom.add(addCartButton);
        bottom.add(closeButton);

        add(bottom, BorderLayout.SOUTH);

        loadProducts("");
        searchField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
            loadProducts(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            loadProducts(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            loadProducts(searchField.getText());
            }

        });

        refreshButton.addActionListener(e -> {

            searchField.setText("");

            loadProducts("");

        });

            addCartButton.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(this,
                "Please select a product first.");

                return;

            }

            try {

                int productId =
                Integer.parseInt(
                        model.getValueAt(row,0).toString());

                Connection con =
                DBConnection.getConnection();

                PreparedStatement check =
                con.prepareStatement(
                "SELECT * FROM cart WHERE customer_id=? AND product_id=?");

                check.setInt(1, Session.userId);
                check.setInt(2, productId);

                ResultSet rs =
                check.executeQuery();

                if(rs.next()){

                    JOptionPane.showMessageDialog(
                    this,
                    "Product already exists in cart.");

                }

                else{

                    PreparedStatement ps =
                    con.prepareStatement(
                    "INSERT INTO cart(customer_id,product_id,quantity) VALUES(?,?,?)");

                    ps.setInt(1, Session.userId);
                    ps.setInt(2, productId);
                    ps.setInt(3,1);

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(
                    this,
                    "Product Added To Cart Successfully.");

                    ps.close();

                }

                rs.close();
                check.close();
                con.close();

            }

            catch(Exception ex){

                JOptionPane.showMessageDialog(
                this,
                ex.getMessage());

            }

        });

        closeButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadProducts(String keyword) {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                "SELECT product_id,product_name,category,price,quantity " +
                "FROM products " +
                "WHERE quantity>0 AND product_name LIKE ?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                model.addRow(new Object[]{

                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        String.format("₹%.2f", rs.getDouble("price")),
                        rs.getInt("quantity")

                });

            }

            rs.close();
            ps.close();
            con.close();

        }

        catch(Exception e){

            JOptionPane.showMessageDialog(this,e.getMessage());

        }

    }

}

