package fractal;

import javax.swing.*;

class ControlGui {
    private FractalHandler fh;
    private JButton generateButton;
    private JButton moveLeftButton;
    private JButton moveRightButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton m100itersButton;
    private JButton a100ItersButton;
    private JPanel mainPanel;
    private JCheckBox autoGenCheckBox;
    private JButton saveSettingsButton;
    private JProgressBar progressBar;
    private JLabel xInfo;
    private JLabel yInfo;
    private JLabel zoomInfo;
    private JLabel iteInfo;
    private JButton m10itersButton;
    private JButton a10ItersButton;
    private JCheckBox crossCheckBox;
    private JButton resetButton;

    public ControlGui() {
        fh = new FractalHandler();

        updatexInfo();
        updateyInfo();
        updatezoomInfoo();
        updateIterInfo();

        moveLeftButton.addActionListener(e -> {
            fh.f.moveCX(-1);
            updatexInfo();
            isAutoGen();
        });
        moveRightButton.addActionListener(e -> {
            fh.f.moveCX(1);
            updatexInfo();
            isAutoGen();
        });
        moveUpButton.addActionListener(e -> {
            fh.f.moveCY(1);
            updateyInfo();
            isAutoGen();
        });
        moveDownButton.addActionListener(e -> {
            fh.f.moveCY(-1);
            updateyInfo();
            isAutoGen();
        });

        zoomInButton.addActionListener(e -> {
            fh.f.setZoom(fh.f.getZoom() / 10);
            updatezoomInfoo();
            isAutoGen();
        });
        zoomOutButton.addActionListener(e -> {
            fh.f.setZoom(fh.f.getZoom() * 10);
            updatezoomInfoo();
            isAutoGen();
        });

        crossCheckBox.addActionListener(e -> {
            fh.f.setCross(crossCheckBox.isSelected());
            isAutoGen();
        });

        generateButton.addActionListener(e -> generate());

        m100itersButton.addActionListener(e -> {
            fh.f.setMaxIter(fh.f.getMaxIter() - 100);
            updateIterInfo();
            isAutoGen();
        });
        m10itersButton.addActionListener(e -> {
            fh.f.setMaxIter(fh.f.getMaxIter() - 10);
            updateIterInfo();
            isAutoGen();
        });
        a100ItersButton.addActionListener(e -> {
            fh.f.setMaxIter(fh.f.getMaxIter() + 100);
            updateIterInfo();
            isAutoGen();
        });
        a10ItersButton.addActionListener(e -> {
            fh.f.setMaxIter(fh.f.getMaxIter() + 10);
            updateIterInfo();
            isAutoGen();
        });

        saveSettingsButton.addActionListener(e -> fh.saveSettings(false));

        resetButton.addActionListener(e -> {
            fh.reset();
            updatexInfo();
            updateyInfo();
            updatezoomInfoo();
            updateIterInfo();
            isAutoGen();
        });
    }

    public static void main(String[] args) {
        ControlGui cg = new ControlGui();
        JFrame frame = new JFrame("ControlGui");
        frame.setContentPane(cg.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel getMainPanel() {
        return mainPanel;
    }

    private void updateIterInfo() {
        iteInfo.setText(Double.toString(fh.f.getMaxIter()));
    }

    private void updatezoomInfoo() {
        zoomInfo.setText("1E" + Math.log10(fh.f.getZoom()));
    }

    private void updateyInfo() {
        yInfo.setText(Double.toString(fh.f.getCY()));
    }

    private void updatexInfo() {
        xInfo.setText(Double.toString(fh.f.getCX()));
    }

    private void isAutoGen() {
        if (autoGenCheckBox.isSelected())
            generate();
    }

    private void generate() {
        // long startTime = System.nanoTime();
        new Thread(() -> fh.saveImage(fh.generate(progressBar))).start();
        // long elapsedTime = System.nanoTime() - startTime;
        // System.out.println(elapsedTime / 1000000f + " ms");

        updateIterInfo();
    }
}
