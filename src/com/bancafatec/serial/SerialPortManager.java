package com.bancafatec.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.bancafatec.model.BancaModel;
import com.bancafatec.gui.MainFrame; 
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SerialPortManager implements Runnable {
    private SerialPort chosenPort;
    private BancaModel model;
    private MainFrame gui; 
    private volatile boolean running = true;

    public SerialPortManager(BancaModel model, MainFrame gui) {
        this.model = model;
        this.gui = gui; 
    }

    public static String[] getAvailablePortNames() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] portNames = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            portNames[i] = ports[i].getSystemPortName();
        }
        return portNames;
    }

    public boolean connect(String portName) {
        chosenPort = SerialPort.getCommPort(portName);
        chosenPort.setComPortParameters(9600, 8, 1, 0);
        chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        if (chosenPort.openPort()) {
            running = true;
            Thread serialThread = new Thread(this);
            serialThread.start();
            return true;
        } else {
            return false;
        }
    }

    public void disconnect() {
        running = false;
        if (chosenPort != null && chosenPort.isOpen()) {
            chosenPort.closePort();
        }
    }

    @Override
    public void run() {
        InputStream in = chosenPort.getInputStream();
        StringBuilder lineBuffer = new StringBuilder();

        while (running) {
            try {
                if (in.available() > 0) {
                    int byteRead = in.read();
                    if (byteRead != -1) {
                        char c = (char) byteRead;
                        
                        if (c == '\n' || c == '\r') {
                            String line = lineBuffer.toString().trim();
                            lineBuffer.setLength(0);

                            if (!line.isEmpty()) {
                                processSerialData(line);
                            }
                        } else {
                            lineBuffer.append(c);
                        }
                    }
                } else {
                    Thread.sleep(10);
                }
            } catch (IOException | InterruptedException e) {
                running = false;
            }
        }
    }

    private void processSerialData(String data) {

        if (data.startsWith("D:")) { 
            String distanceValue = data.substring(2);
            if (distanceValue.equals("TIMEOUT") || distanceValue.equals("INVALID")) {
                model.processarDistancia(Float.NaN); 
            } else {
                try {
                    float distance = Float.parseFloat(distanceValue);
                    model.processarDistancia(distance); 
                } catch (NumberFormatException e) {
                    model.processarDistancia(Float.NaN); 
                }
            }
        } else if (data.startsWith("K:")) { 
            try {
                char key = data.substring(2).charAt(0);
                model.processarTeclado(key);
            } catch (StringIndexOutOfBoundsException e) {
            }
        }
    }
}
