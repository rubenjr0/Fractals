package fractal;

import javax.swing.*;
import java.io.FileNotFoundException;

class ControlGui {
    private JCheckBox autoItersCheckBox;
    private Fractal mb = new Fractal();
    private JButton generateButton;
    private JButton moveLeftButton;
    private JButton moveRightButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton moreItersButton;
    private JButton lessItersButton;
    private JPanel mainPanel;
    private JCheckBox autoGenCheckBox;
    private JButton saveSettingsButton;
    private JProgressBar progressBar;

    public ControlGui() {
        autoItersCheckBox.setSelected(mb.autoMaxIte);

        moveLeftButton.addActionListener(e -> {
            mb.moveX(-1.0);
            if (autoGenCheckBox.isSelected())
                mb.gen(progressBar);
        });
        moveRightButton.addActionListener(e -> {
            mb.moveX(1.0);
            if (autoGenCheckBox.isSelected())
                mb.gen(progressBar);
        });
        moveUpButton.addActionListener(e -> {
            mb.moveY(1.0);
            if (autoGenCheckBox.isSelected())
                mb.gen(progressBar);
        });
        moveDownButton.addActionListener(e -> {
            mb.moveY(-1.0);
            if (autoGenCheckBox.isSelected())
                mb.gen(progressBar);
        });

        zoomInButton.addActionListener(e -> {
            mb.setZoom(mb.zoom * 10);
            if (autoGenCheckBox.isSelected())
                mb.gen(progressBar);
        });
        zoomOutButton.addActionListener(e -> {
            mb.setZoom(mb.zoom / 10);
            if (autoGenCheckBox.isSelected())
                mb.gen(progressBar);
        });

        generateButton.addActionListener(e -> mb.gen(progressBar));

        moreItersButton.addActionListener(e -> {
            mb.setMax_iter(mb.max_iter * 2);
            mb.autoMaxIte = false;
            autoItersCheckBox.setSelected(false);
            if (autoGenCheckBox.isSelected())
                mb.gen(progressBar);
        });
        lessItersButton.addActionListener(e -> {
            mb.setMax_iter(mb.max_iter / 2);
            mb.autoMaxIte = false;
            autoItersCheckBox.setSelected(false);
            if (autoGenCheckBox.isSelected())
                mb.gen(progressBar);
        });
        autoItersCheckBox.addActionListener(e -> {
            mb.autoMaxIte = autoItersCheckBox.isSelected();
            if (autoGenCheckBox.isSelected())
                mb.gen(progressBar);
        });
        saveSettingsButton.addActionListener(e -> {
            try {
                mb.saveSettings();
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ControlGui");
        frame.setContentPane(new ControlGui().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
