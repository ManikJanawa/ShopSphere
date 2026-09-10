package view;

import javax.swing.*;
import java.awt.*;

public class CustomerDashboard extends JFrame {

    public CustomerDashboard() {

        setTitle("ShopSphere - Customer Dashboard");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        //---------------- SIDEBAR ----------------//

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 150, 243));
        sidebar.setPreferredSize(new Dimension(230, 650));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));


        JLabel logo = new JLabel("Customer Panel");

        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);



        sidebar.add(Box.createRigidArea(new Dimension(0,40)));
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0,50)));



        JButton browse = new JButton("Browse Products");
        JButton cart = new JButton("My Cart");
        JButton orders = new JButton("My Orders");
        JButton profile = new JButton("Profile");
        JButton logout = new JButton("Logout");



        JButton buttons[] = {

                browse,
                cart,
                orders,
                profile,
                logout

        };



        for(JButton btn : buttons){

            btn.setMaximumSize(new Dimension(180,40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0,15)));

        }




        //---------------- MAIN PANEL ----------------//


        JPanel mainPanel = new JPanel(new BorderLayout());



        JLabel heading = new JLabel("Welcome Customer");

        heading.setFont(new Font("Segoe UI",Font.BOLD,32));

        heading.setBorder(
                BorderFactory.createEmptyBorder(30,30,20,20)
        );


        mainPanel.add(
                heading,
                BorderLayout.NORTH
        );



        JPanel cards = new JPanel(
                new GridLayout(1,3,20,20)
        );


        cards.setBorder(
                BorderFactory.createEmptyBorder(30,30,30,30)
        );



        cards.add(
                createCard("Available Products","250")
        );


        cards.add(
                createCard("Cart Items","5")
        );


        cards.add(
                createCard("Orders","3")
        );



        mainPanel.add(
                cards,
                BorderLayout.CENTER
        );



        add(sidebar,BorderLayout.WEST);

        add(mainPanel,BorderLayout.CENTER);





        //---------------- BUTTON ACTIONS ----------------//



        browse.addActionListener(e -> {

            new CustomerProducts();

        });



        cart.addActionListener(e -> {

            new CustomerCart();

        });



        // MY ORDERS BUTTON

        orders.addActionListener(e -> {

            new CustomerOrders();

        });




        profile.addActionListener(e -> {

            new CustomerProfile();

        });




        logout.addActionListener(e -> {

            dispose();

            new Login();

        });



        setVisible(true);

    }





    private JPanel createCard(String title,String value){


        JPanel card = new JPanel(
                new GridLayout(2,1)
        );


        card.setBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY)
        );



        JLabel t = new JLabel(title);

        t.setHorizontalAlignment(
                SwingConstants.CENTER
        );



        JLabel v = new JLabel(value);

        v.setHorizontalAlignment(
                SwingConstants.CENTER
        );



        t.setFont(
                new Font("Segoe UI",Font.BOLD,18)
        );


        v.setFont(
                new Font("Segoe UI",Font.BOLD,28)
        );



        card.add(t);

        card.add(v);



        return card;

    }


}