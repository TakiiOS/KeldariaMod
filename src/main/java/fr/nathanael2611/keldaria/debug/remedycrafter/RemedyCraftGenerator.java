package fr.nathanael2611.keldaria.debug.remedycrafter;

import javax.swing.*;
import java.awt.*;

public class RemedyCraftGenerator extends JPanel
{

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Keldaria Remedy Craft Generator");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        frame.setContentPane(new RemedyCraftGenerator());

        frame.setVisible(true);
    }

    JTextField remedyNameField = new JTextField();


    public RemedyCraftGenerator()
    {

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
}
