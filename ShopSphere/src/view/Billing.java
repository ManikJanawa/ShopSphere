package view;

import database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Billing extends JFrame {

    private JTable billingTable;
    private DefaultTableModel model;

    public Billing() {

        setTitle("ShopSphere - Billing & Invoice Management");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Billing & Invoice Management");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setHorizontalAlignment(JLabel.CENTER);

        mainPanel.add(title, BorderLayout.NORTH);

        String columns[] = {
                "Invoice ID",
                "Date",
                "Product",
                "Quantity",
                "Amount",
                "Status"
        };

        model = new DefaultTableModel(columns,0){

            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }

        };

        billingTable = new JTable(model);

        billingTable.setRowHeight(30);
        billingTable.setFont(new Font("Arial",Font.PLAIN,14));

        billingTable.getTableHeader().setFont(
                new Font("Arial",Font.BOLD,14));

        billingTable.getTableHeader().setBackground(
                new Color(33,150,243));

        billingTable.getTableHeader().setForeground(Color.WHITE);

        billingTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(JLabel.CENTER);

        for(int i=0;i<billingTable.getColumnCount();i++){

            billingTable.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);

        }

        JScrollPane scrollPane =
                new JScrollPane(billingTable);

        mainPanel.add(scrollPane,BorderLayout.CENTER);

        JButton refreshButton =
                new JButton("Refresh Billing");

        refreshButton.setFont(
                new Font("Arial",Font.BOLD,14));

        refreshButton.addActionListener(e->{

            model.setRowCount(0);
            loadBillingData();

        });

        JPanel bottom =
                new JPanel();

        bottom.add(refreshButton);

        mainPanel.add(bottom,BorderLayout.SOUTH);

        add(mainPanel);

        loadBillingData();

        setVisible(true);
    }

    private void loadBillingData(){

        model.setRowCount(0);

        try(
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(

                        "SELECT o.order_id,o.order_date,p.product_name," +
                                "oi.quantity,oi.price,o.status " +
                                "FROM orders o " +
                                "JOIN order_items oi ON o.order_id=oi.order_id " +
                                "JOIN products p ON oi.product_id=p.product_id " +
                                "ORDER BY o.order_id DESC"

                );

                ResultSet rs = ps.executeQuery()

        ){

            while(rs.next()){

                int orderId =
                        rs.getInt("order_id");

                String invoice =
                        "INV-"+String.format("%04d",orderId);

                String date =
                        rs.getDate("order_date").toString();

                String product =
                        rs.getString("product_name");

                int quantity =
                        rs.getInt("quantity");

                double price =
                        rs.getDouble("price");

                double total =
                        price*quantity;

                String amount =
                        String.format("₹%.2f",total);

                String status =
                        rs.getString("status");

                model.addRow(new Object[]{

                        invoice,
                        date,
                        product,
                        quantity,
                        amount,
                        status

                });

            }

        }
        catch(Exception e){

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

            e.printStackTrace();

        }

    }

}