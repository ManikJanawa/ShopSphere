package view;

import database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminOrders extends JFrame {

    JTable table;
    DefaultTableModel model;

    JButton refreshButton;
    JButton updateStatusButton;

    public AdminOrders() {

        setTitle("Admin Orders Management");

        setSize(1100, 550);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());



        JLabel heading = new JLabel(
                "All Customer Orders",
                SwingConstants.CENTER
        );

        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 26)
        );

        add(heading, BorderLayout.NORTH);



        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[]{

                "Order ID",
                "Customer ID",
                "Product",
                "Quantity",
                "Amount",
                "Payment",
                "Payment Status",
                "Order Date",
                "Status"

        });



        table = new JTable(model);

        table.setRowHeight(30);

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        add(new JScrollPane(table), BorderLayout.CENTER);



        JPanel bottomPanel = new JPanel();



        refreshButton = new JButton("Refresh");

        updateStatusButton = new JButton("Update Status");



        bottomPanel.add(refreshButton);

        bottomPanel.add(updateStatusButton);



        add(bottomPanel, BorderLayout.SOUTH);



        refreshButton.addActionListener(e -> {

            loadOrders();

        });



        updateStatusButton.addActionListener(e -> {

            int row = table.getSelectedRow();

            if(row == -1){

                JOptionPane.showMessageDialog(
                this,
                "Please select an order."
                );

                return;
            }

            int orderId = Integer.parseInt(
            model.getValueAt(row,0).toString()
            );

            String[] statusList = {

                    "Pending",
                    "Confirmed",
                    "Shipped",
                    "Delivered"

            };

            String newStatus = (String) JOptionPane.showInputDialog(

                    this,

                    "Select New Status",

                    "Update Order Status",

                    JOptionPane.PLAIN_MESSAGE,

                    null,

                    statusList,

                    model.getValueAt(row,8)

            );

            if(newStatus == null){

                return;

            }

            try{

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(

                "UPDATE orders SET status=? WHERE order_id=?"

                );

                ps.setString(1,newStatus);

                ps.setInt(2,orderId);

                int result = ps.executeUpdate();

                ps.close();
                con.close();

                if(result>0){

                    JOptionPane.showMessageDialog(

                    this,

                    "Order Status Updated Successfully."

                    );

                    loadOrders();

                }

            }

            catch(Exception ex){

                    JOptionPane.showMessageDialog(

                this,

                ex.getMessage()

                );

            }

        });



        loadOrders();



        setVisible(true);

    }



    
    private void loadOrders() {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "SELECT o.order_id, " +
                "o.customer_id, " +
                "p.product_name, " +
                "oi.quantity, " +
                "oi.price, " +
                "o.payment_method, " +
                "o.payment_status, " +
                "o.order_date, " +
                "o.status " +
                "FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "ORDER BY o.order_id DESC";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                double amount = rs.getDouble("price") * rs.getInt("quantity");

                model.addRow(new Object[]{

                        rs.getInt("order_id"),

                        rs.getInt("customer_id"),

                        rs.getString("product_name"),

                        rs.getInt("quantity"),

                        String.format("₹%.2f", amount),

                        rs.getString("payment_method"),

                        rs.getString("payment_status"),

                        rs.getDate("order_date"),

                        rs.getString("status")

                });

            }

            rs.close();
            ps.close();
            con.close();

        }

        catch (Exception e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        }

    }
}