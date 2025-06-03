package com.bancafatec.gui;

import com.bancafatec.model.BancaModel;
import com.bancafatec.serial.SerialPortManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class MainFrame extends JFrame implements PropertyChangeListener {
    private BancaModel model;
    private SerialPortManager serialManager;

    private JLabel lblLine1;
    private JLabel lblLine2;
    private JLabel lblFateCoins;
    private JComboBox<String> portSelector;
    private JButton connectButton;
    private JTextArea logArea;
    private JScrollPane logScrollPane;

    private JLabel lblCopyright;

    public MainFrame(BancaModel model) {
        this.model = model;
        this.model.addPropertyChangeListener(this);

        setTitle("Lanchonete da Fatec - Sistema de Caixa");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel connectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel portLabel = new JLabel("Porta Serial:");
        portSelector = new JComboBox<>(SerialPortManager.getAvailablePortNames());
        connectButton = new JButton("Conectar");
        
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serialManager == null) {
                    logError("Erro interno: Gerenciador Serial não inicializado.");
                    return;
                }
                if (connectButton.getText().equals("Conectar")) {
                    String selectedPort = (String) portSelector.getSelectedItem();
                    if (selectedPort != null && serialManager.connect(selectedPort)) {
                        connectButton.setText("Desconectar");
                        portSelector.setEnabled(false);
                        log("Conectado à porta: " + selectedPort);
                    } else {
                        logError("Falha ao conectar à porta: " + selectedPort);
                    }
                } else {
                    serialManager.disconnect();
                    connectButton.setText("Conectar");
                    portSelector.setEnabled(true);
                    log("Desconectado da porta.");
                }
            }
        });
        connectionPanel.add(portLabel);
        connectionPanel.add(portSelector);
        connectionPanel.add(connectButton);
        add(connectionPanel, BorderLayout.NORTH);

        JPanel lcdPanel = new JPanel();
        lcdPanel.setLayout(new GridLayout(2, 1));
        lcdPanel.setBackground(new Color(0, 50, 0));
        lcdPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        lblLine1 = new JLabel(model.getDisplayLine1());
        lblLine1.setForeground(new Color(0, 255, 0));
        lblLine1.setFont(new Font("Monospaced", Font.BOLD, 26));
        lblLine1.setHorizontalAlignment(SwingConstants.CENTER);

        lblLine2 = new JLabel(model.getDisplayLine2());
        lblLine2.setForeground(new Color(0, 255, 0));
        lblLine2.setFont(new Font("Monospaced", Font.PLAIN, 20));
        lblLine2.setHorizontalAlignment(SwingConstants.CENTER);

        lcdPanel.add(lblLine1);
        lcdPanel.add(lblLine2);
        add(lcdPanel, BorderLayout.CENTER);

        JPanel statusAndLogPanel = new JPanel(new BorderLayout(5, 5));
        statusAndLogPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));

        lblFateCoins = new JLabel("Saldo: " + String.format("%.2f", model.getFateCoins()) + " FC");
        lblFateCoins.setFont(new Font("Arial", Font.BOLD, 16));
        statusPanel.add(lblFateCoins);

        lblCopyright = new JLabel("© Lanchonete Fatec - Sistema de Caixa");
        lblCopyright.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCopyright.setForeground(Color.DARK_GRAY); 
        statusPanel.add(lblCopyright); 

        statusAndLogPanel.add(statusPanel, BorderLayout.NORTH);

        logArea = new JTextArea(5, 40);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.LIGHT_GRAY);
        logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Log de Comunicação"));
        statusAndLogPanel.add(logScrollPane, BorderLayout.CENTER);
        
        add(statusAndLogPanel, BorderLayout.SOUTH);
    }

    public void setSerialPortManager(SerialPortManager serialManager) {
        this.serialManager = serialManager;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            if ("displayLine1".equals(evt.getPropertyName())) {
                lblLine1.setText((String) evt.getNewValue());
            } else if ("displayLine2".equals(evt.getPropertyName())) {
                lblLine2.setText((String) evt.getNewValue());
            } else if ("fateCoins".equals(evt.getPropertyName())) {
                lblFateCoins.setText("Saldo: " + String.format("%.2f", (Double) evt.getNewValue()) + " FC");
            } 
            else if ("showPurchaseLog".equals(evt.getPropertyName())) {
                @SuppressWarnings("unchecked")
                List<String> logEntries = (List<String>) evt.getNewValue();
                showPurchaseLogDialog(logEntries);
            }
        });
    }

    private void showPurchaseLogDialog(List<String> logEntries) {
        JDialog logDialog = new JDialog(this, "Histórico de Compras", true);
        logDialog.setSize(400, 500);
        logDialog.setLocationRelativeTo(this);
        logDialog.setLayout(new BorderLayout());

        JTextArea logDisplayArea = new JTextArea();
        logDisplayArea.setEditable(false);
        logDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logDisplayArea.setBackground(Color.DARK_GRAY);
        logDisplayArea.setForeground(Color.WHITE);

        StringBuilder fullLog = new StringBuilder();
        for (String entry : logEntries) {
            fullLog.append(entry).append("\n");
        }
        logDisplayArea.setText(fullLog.toString());

        JScrollPane scrollPane = new JScrollPane(logDisplayArea);
        logDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Fechar");
        closeButton.addActionListener(e -> logDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        logDialog.add(buttonPanel, BorderLayout.SOUTH);

        logDialog.setVisible(true);
    }

    public void log(String message) {
        System.out.println("[INFO] " + message);
    }

    public void logError(String message) {
        System.err.println("[ERRO] " + message);
    }
}
