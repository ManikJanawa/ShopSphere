package view;

import database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SalesReport extends JFrame {

    JTable table;
    DefaultTableModel model;

    JLabel totalOrdersLabel;
    JLabel totalRevenueLabel;

    public SalesReport() {

        setTitle("ShopSphere - Sales Report");
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Sales Report", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(heading, BorderLayout.NORTH);

        model = new DefaultTableModel();

        model.addColumn("Order ID");
        model.addColumn("Customer ID");
        model.addColumn("Order Date");
        model.addColumn("Amount");
        model.addColumn("Status");

        table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2,2,10,10));

        totalOrdersLabel = new JLabel("Total Orders : 0");
        totalRevenueLabel = new JLabel("Total Revenue : ₹0");

        JButton refresh = new JButton("Refresh");
        JButton close = new JButton("Close");

        bottom.add(totalOrdersLabel);
        bottom.add(totalRevenueLabel);
        bottom.add(refresh);
        bottom.add(close);

        add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> loadReport());

        close.addActionListener(e -> dispose());

        loadReport();

        setVisible(true);
    }

    private void loadReport() {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM orders");

            ResultSet rs = ps.executeQuery();

            int totalOrders = 0;
            double totalRevenue = 0;

            while(rs.next()) {

                totalOrders++;

                totalRevenue += rs.getDouble("total_amount");

                model.addRow(new Object[]{

                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getDate("order_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")

                });

            }

            totalOrdersLabel.setText("Total Orders : " + totalOrders);
            totalRevenueLabel.setText("Total Revenue : ₹" + totalRevenue);

            rs.close();
            ps.close();
            con.close();

        }
        catch(Exception e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        }

    }

}