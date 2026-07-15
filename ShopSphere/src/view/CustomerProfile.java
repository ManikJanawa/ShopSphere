package view;

import session.Session;
import database.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerProfile extends JFrame {

    JLabel nameLabel;
    JLabel emailLabel;
    JLabel roleLabel;

    public CustomerProfile() {

        setTitle("Customer Profile");
        setSize(600,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel heading = new JLabel("Customer Profile", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        panel.add(heading, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(3,2,15,15));
        details.setBorder(BorderFactory.createEmptyBorder(30,40,30,40));

        details.add(new JLabel("Full Name"));
        nameLabel = new JLabel();

        details.add(nameLabel);

        details.add(new JLabel("Email"));
        emailLabel = new JLabel();

        details.add(emailLabel);

        details.add(new JLabel("Role"));
        roleLabel = new JLabel();

        details.add(roleLabel);

        panel.add(details, BorderLayout.CENTER);

        JButton close = new JButton("Close");

        JPanel bottom = new JPanel();
        bottom.add(close);

        panel.add(bottom, BorderLayout.SOUTH);

        close.addActionListener(e -> dispose());

        loadProfile();

        add(panel);

        setVisible(true);

    }

    private void loadProfile() {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM users WHERE user_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Session.userId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                nameLabel.setText(
                        rs.getString("full_name")
                );

                emailLabel.setText(
                        rs.getString("email")
                );

                roleLabel.setText(
                        rs.getString("role")
                );

            }

        }

        catch (Exception e){

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        }

    }

}