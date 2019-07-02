package fractal;

import javax.swing.*;
import java.io.FileNotFoundException;

class ControlGui {
    private JCheckBox autoItersCheckBox;
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
    private JLabel xinfo;
    private JLabel yinfo;
    private JLabel zoominf;
    private JLabel iteinf;

    public ControlGui() {
        Fractal.init();
        autoItersCheckBox.setSelected(Fractal.autoMaxIte);
        updateXInfo();
        updateYInfo();
        updateZoomInfo();
        updateIterInfo();
        Fractal.cross = true;
        Fractal.info = true;
        Fractal.prefix = "10";

        moveLeftButton.addActionListener(e -> {
            Fractal.moveX(-1);
            updateXInfo();
            isAutoGen();
        });
        moveRightButton.addActionListener(e -> {
            Fractal.moveX(1);
            updateXInfo();
            isAutoGen();
        });
        moveUpButton.addActionListener(e -> {
            Fractal.moveY(1);
            updateYInfo();
            isAutoGen();
        });
        moveDownButton.addActionListener(e -> {
            Fractal.moveY(-1);
            updateYInfo();
            isAutoGen();
        });

        zoomInButton.addActionListener(e -> {
            Fractal.zoom = Fractal.zoom * 10;
            updateZoomInfo();
            isAutoGen();
        });
        zoomOutButton.addActionListener(e -> {
            Fractal.zoom = Fractal.zoom / 10;
            updateZoomInfo();
            isAutoGen();
        });

        generateButton.addActionListener(e -> generate());

        moreItersButton.addActionListener(e -> {
            Fractal.max_iter = Fractal.max_iter * 2;
            Fractal.autoMaxIte = false;
            autoItersCheckBox.setSelected(false);
            updateIterInfo();
            isAutoGen();
        });
        lessItersButton.addActionListener(e -> {
            Fractal.max_iter = Fractal.max_iter / 2;
            Fractal.autoMaxIte = false;
            autoItersCheckBox.setSelected(false);
            updateIterInfo();
            isAutoGen();
        });
        autoItersCheckBox.addActionListener(e -> {
            Fractal.autoMaxIte = autoItersCheckBox.isSelected();
            isAutoGen();
        });

        saveSettingsButton.addActionListener(e -> {
            try {
                Fractal.saveSettings();
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

    private void updateIterInfo() {
        iteinf.setText(Integer.toString(Fractal.max_iter));
    }

    private void updateZoomInfo() {
        zoominf.setText("1E" + Math.log10(Fractal.zoom));
    }

    private void updateYInfo() {
        yinfo.setText(Double.toString(Fractal.center_y));
    }

    private void updateXInfo() {
        xinfo.setText(Double.toString(Fractal.center_x));
    }

    private void isAutoGen() {
        if (autoGenCheckBox.isSelected())
            generate();
    }

    private void generate() {
        new Thread(() -> Fractal.gen(progressBar)).start();
        updateIterInfo();
    }
}
