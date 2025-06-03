package com.bancafatec.main;

import com.bancafatec.gui.MainFrame;
import com.bancafatec.model.BancaModel;
import com.bancafatec.serial.SerialPortManager;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BancaModel model = new BancaModel();
            MainFrame frame = new MainFrame(model); 
            SerialPortManager serialManager = new SerialPortManager(model, frame);
            frame.setSerialPortManager(serialManager);
            frame.setVisible(true);
        });
    }
}
