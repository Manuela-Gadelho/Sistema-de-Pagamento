package com.bancafatec.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

import com.bancafatec.model.SoundManager;

public class BancaModel {
    // Variáveis de estado
    private int estado = 0; 
                            // 1: Menu Sucos (espera seleção de suco)
                            // 2: Menu Salgados (espera seleção de salgado)
                            // 3: Menu Doces (espera seleção de doce)
                            // 4: FateCoins (visualização de saldo)

                            // Estados de compra específicos:
                            // Sucos: 10: Laranja, 11: Abacaxi, 12: Maracujá
                            // Salgados: 20: Coxinha, 21: Pão de Queijo, 22: Esfiha
                            // Doces: 30: Brigadeiro, 31: Bolo de Cenoura, 32: Pudim

                            // Estados para Administração
                            // 90: Aguardando Senha Admin
                            // 91: Menu Admin
                            // 92: Admin - Adicionar FateCoins

                            // NOVOS ESTADOS PARA CARRINHO
                            // 50: Item Adicionado ao Carrinho (feedback)
                            // 51: Aguardando Senha de Pagamento do Carrinho (NOVO)

    private double fateCoins = 50.00;
    private String displayLine1 = "Lanchonete Fatec";
    private String displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
    private boolean proximityLedOn = false; 

    private boolean purchaseInProgress = false; 

    private final String ADMIN_PASSWORD = "123";
    private StringBuilder currentPasswordAttempt = new StringBuilder();

    private final String PAYMENT_PASSWORD = "456";
    private StringBuilder currentPaymentPasswordAttempt = new StringBuilder();

    private List<CartItem> cartItems = new ArrayList<>();
    private double cartTotal = 0.0;

    private List<PurchaseRecord> purchaseHistory = new ArrayList<>();

    private PropertyChangeSupport support;

    public BancaModel() {
        this.support = new PropertyChangeSupport(this);
        SoundManager.loadSounds();
        updateDisplayAndNotify();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public String getDisplayLine1() {
        return displayLine1;
    }

    public String getDisplayLine2() {
        return displayLine2;
    }

    public double getFateCoins() {
        return fateCoins;
    }

    public boolean isProximityLedOn() {
        return proximityLedOn;
    }

    public void processarTeclado(char tecla) {
        SoundManager.playBeepSound(); 
        System.out.println("[INFO] Tecla recebida: " + tecla);

        if (tecla == '*') {
            if (estado >= 90 && estado <= 92) { 
                estado = 0;
                displayLine1 = "Lanchonete Fatec";
                displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
                currentPasswordAttempt.setLength(0);
            } else if (estado == 51) { 
                cartItems.clear(); 
                cartTotal = 0.0;
                currentPaymentPasswordAttempt.setLength(0); 
                estado = 0;
                displayLine1 = "Lanchonete Fatec";
                displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
            } else if (estado == 50) { 
                estado = 0; 
                displayLine1 = "Lanchonete Fatec";
                displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
            } else { 
                estado = 0;
                displayLine1 = "Lanchonete Fatec";
                displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
            }
            purchaseInProgress = false;
            updateDisplayAndNotify();
            return;
        }

        if (tecla == '#' && (estado == 1 || estado == 2 || estado == 3 || estado == 0 || estado == 50)) { 
            if (!cartItems.isEmpty()) {
                estado = 51; 
                displayLine1 = "Total: R$" + String.format("%.2f", cartTotal);
                displayLine2 = "SENHA PAGAR: ";
                currentPaymentPasswordAttempt.setLength(0); 
                purchaseInProgress = false; 
                updateDisplayAndNotify();
                return;
            } else {
                displayLine1 = "Carrinho Vazio!";
                displayLine2 = "Adicione itens.";
                updateDisplayAndNotify();
                SoundManager.playErrorSound(); 
                new Timer(1500, e -> {
                    if(estado == 0) {
                        displayLine1 = "Lanchonete Fatec";
                        displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
                    } else {
                        loadSubmenuDisplay();
                    }
                    updateDisplayAndNotify();
                    ((Timer)e.getSource()).stop();
                }).start();
                return;
            }
        }
        
        switch (estado) {
            case 0: 
                if (tecla == '1') {
                    estado = 1;
                    displayLine1 = "Menu Sucos      ";
                    displayLine2 = "1.Laranja 2.Abacaxi 3.Maracuja";
                } else if (tecla == '2') {
                    estado = 2;
                    displayLine1 = "Menu Salgados   ";
                    displayLine2 = "1.Coxinha 2.PaoQueijo 3.Esfiha";
                } else if (tecla == '3') {
                    estado = 3;
                    displayLine1 = "Menu Doces      ";
                    displayLine2 = "1.Brigadeiro 2.Bolo 3.Pudim";
                } else if (tecla == '4') {
                    estado = 4;
                    displayLine1 = "Seu Saldo:";
                    displayLine2 = String.format("%.2f", fateCoins) + " FateCoins";
                    new Timer(2500, e -> {
                        estado = 0;
                        displayLine1 = "Lanchonete Fatec";
                        displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
                        purchaseInProgress = false;
                        updateDisplayAndNotify();
                        ((Timer)e.getSource()).stop();
                    }).start();
                } else if (tecla == 'A') {
                    estado = 90;
                    displayLine1 = "ADMIN Mode:";
                    displayLine2 = "SENHA: ";
                    currentPasswordAttempt.setLength(0);
                } else if (tecla == 'D') {
                    fateCoins += 500.00;
                    displayLine1 = "500 FC Adicionadas";
                    displayLine2 = "Saldo: " + String.format("%.2f", fateCoins);
                    updateDisplayAndNotify();
                    new Timer(1500, e -> {
                        estado = 0;
                        displayLine1 = "Lanchonete Fatec";
                        displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
                        purchaseInProgress = false;
                        updateDisplayAndNotify();
                        ((Timer)e.getSource()).stop();
                    }).start();
                }
                break;
            case 1:
                handleItemSelection(tecla, "Suco", new double[]{4.50, 4.00, 5.00}, new String[]{"Laranja", "Abacaxi", "Maracuja"});
                break;
            case 2: 
                handleItemSelection(tecla, "Salgado", new double[]{5.00, 3.50, 4.00}, new String[]{"Coxinha", "Pao de Queijo", "Esfiha"});
                break;
            case 3: 
                handleItemSelection(tecla, "Doce", new double[]{2.00, 6.00, 4.50}, new String[]{"Brigadeiro", "Bolo de Cenoura", "Pudim"});
                break;
            
            case 50: 
                if (tecla == '1') {
                    estado = 1;
                    displayLine1 = "Menu Sucos      ";
                    displayLine2 = "1.Laranja 2.Abacaxi 3.Maracuja";
                } else if (tecla == '2') {
                    estado = 2;
                    displayLine1 = "Menu Salgados   ";
                    displayLine2 = "1.Coxinha 2.PaoQueijo 3.Esfiha";
                } else if (tecla == '3') {
                    estado = 3;
                    displayLine1 = "Menu Doces      ";
                    displayLine2 = "1.Brigadeiro 2.Bolo 3.Pudim";
                } else if (tecla == '4') {
                    estado = 0;
                    displayLine1 = "Lanchonete Fatec";
                    displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
                } else { 
                }
                break;
            case 51: 
                if (Character.isDigit(tecla)) {
                    currentPaymentPasswordAttempt.append(tecla);
                    displayLine2 = "SENHA PAGAR: " + "*".repeat(currentPaymentPasswordAttempt.length());
                    updateDisplayAndNotify();
                    
                    if (currentPaymentPasswordAttempt.length() == PAYMENT_PASSWORD.length()) {
                        if (currentPaymentPasswordAttempt.toString().equals(PAYMENT_PASSWORD)) {
                            finalizePurchase();
                        } else {
                            displayLine1 = "Senha Incorreta!";
                            displayLine2 = "Tente Novamente.";
                            currentPaymentPasswordAttempt.setLength(0); 
                            SoundManager.playErrorSound(); 
                            new Timer(1500, e -> {
                                displayLine1 = "Total: R$" + String.format("%.2f", cartTotal);
                                displayLine2 = "SENHA PAGAR: ";
                                updateDisplayAndNotify();
                                ((Timer)e.getSource()).stop();
                            }).start();
                        }
                    }
                } else if (tecla == '#') { 
                    if (currentPaymentPasswordAttempt.toString().equals(PAYMENT_PASSWORD)) {
                        finalizePurchase();
                    } else {
                        displayLine1 = "Senha Incorreta!";
                        displayLine2 = "Tente Novamente.";
                        currentPaymentPasswordAttempt.setLength(0);
                        SoundManager.playErrorSound(); 
                        new Timer(1500, e -> {
                            displayLine1 = "Total: R$" + String.format("%.2f", cartTotal);
                            displayLine2 = "SENHA PAGAR: ";
                            updateDisplayAndNotify();
                            ((Timer)e.getSource()).stop();
                        }).start();
                    }
                }
                break;
            case 90: 
                if (Character.isDigit(tecla)) {
                    currentPasswordAttempt.append(tecla);
                    displayLine2 = "SENHA: " + "*".repeat(currentPasswordAttempt.length());
                    updateDisplayAndNotify();
                    
                    if (currentPasswordAttempt.length() == ADMIN_PASSWORD.length()) {
                        if (currentPasswordAttempt.toString().equals(ADMIN_PASSWORD)) {
                            estado = 91;
                            displayLine1 = "Bem-vindo ADMIN!";
                            displayLine2 = "1.Add FC 2.Ver Log";
                            currentPasswordAttempt.setLength(0);
                        } else {
                            displayLine1 = "ADMIN Mode:";
                            displayLine2 = "Senha Incorreta!";
                            currentPasswordAttempt.setLength(0);
                            SoundManager.playErrorSound();
                            new Timer(1500, e -> {
                                estado = 90;
                                displayLine1 = "ADMIN Mode:";
                                displayLine2 = "SENHA: ";
                                updateDisplayAndNotify();
                                ((Timer)e.getSource()).stop();
                            }).start();
                        }
                    }
                } else if (tecla == '#') {
                    if (currentPasswordAttempt.toString().equals(ADMIN_PASSWORD)) {
                        estado = 91;
                        displayLine1 = "Bem-vindo ADMIN!";
                        displayLine2 = "1.Add FC 2.Ver Log";
                        currentPasswordAttempt.setLength(0);
                    } else {
                        displayLine1 = "ADMIN Mode:";
                        displayLine2 = "Senha Incorreta!";
                        currentPasswordAttempt.setLength(0);
                        SoundManager.playErrorSound(); 
                        new Timer(1500, e -> {
                            estado = 90;
                            displayLine1 = "ADMIN Mode:";
                            displayLine2 = "SENHA: ";
                            updateDisplayAndNotify();
                            ((Timer)e.getSource()).stop();
                        }).start();
                    }
                }
                break;
            case 91: 
                if (tecla == '1') {
                    estado = 92;
                    displayLine1 = "ADMIN Add FC:";
                    displayLine2 = "Valor (1-9): ";
                } else if (tecla == '2') {
                    displayLine1 = "Carregando Log...";
                    displayLine2 = "";
                    updateDisplayAndNotify();
                    
                    List<String> logEntries = getPurchaseLogFormatted();
                    support.firePropertyChange("showPurchaseLog", null, logEntries);
                    
                    new Timer(2000, e -> {
                        estado = 91;
                        displayLine1 = "Bem-vindo ADMIN!";
                        displayLine2 = "1.Add FC 2.Ver Log";
                        updateDisplayAndNotify();
                        ((Timer)e.getSource()).stop();
                    }).start();
                }
                break;
            case 92:
                if (Character.isDigit(tecla)) {
                    try {
                        int valor = Character.getNumericValue(tecla) * 100;
                        fateCoins += valor;
                        displayLine1 = valor + " FC Add!";
                        displayLine2 = "Novo Saldo: " + String.format("%.2f", fateCoins);
                        updateDisplayAndNotify();
                        new Timer(1500, e -> {
                            estado = 91;
                            displayLine1 = "Bem-vindo ADMIN!";
                            displayLine2 = "1.Add FC 2.Ver Log";
                            updateDisplayAndNotify();
                            ((Timer)e.getSource()).stop();
                        }).start();
                    } catch (NumberFormatException ex) {
                    }
                } else {
                    displayLine1 = "Valor Invalido!";
                    displayLine2 = "Use 1-9. Voltar.";
                    updateDisplayAndNotify();
                    SoundManager.playErrorSound(); 
                    new Timer(1500, e -> {
                        estado = 91;
                        displayLine1 = "Bem-vindo ADMIN!";
                        displayLine2 = "1.Add FC 2.Ver Log";
                        updateDisplayAndNotify();
                        ((Timer)e.getSource()).stop();
                    }).start();
                }
                break;
        }
        updateDisplayAndNotify();
    }

    private void handleItemSelection(char tecla, String category, double[] prices, String[] names) {
        int itemIndex = Character.getNumericValue(tecla) - 1;
        if (itemIndex >= 0 && itemIndex < prices.length) {
            String selectedItemName = names[itemIndex];
            double selectedItemPrice = prices[itemIndex];

            CartItem newItem = new CartItem(selectedItemName, selectedItemPrice);
            cartItems.add(newItem);
            cartTotal += selectedItemPrice;

            // ALTERAÇÃO AQUI: Exibe o nome do item e seu preço individual
            displayLine1 = selectedItemName + " R$" + String.format("%.2f", selectedItemPrice);
            displayLine2 = "Carrinho: R$" + String.format("%.2f", cartTotal);
            estado = 50;
            updateDisplayAndNotify();
            SoundManager.playBeepSound(); 

            new Timer(1500, e -> {
                displayLine1 = "Add mais ou # Pagar";
                displayLine2 = "1.Sucos 2.Salgados 3.Doces";
                updateDisplayAndNotify();
                ((Timer)e.getSource()).stop();
            }).start();
        } else {
            displayLine1 = "Selec. Invalida!";
            displayLine2 = "Tente Novamente.";
            updateDisplayAndNotify();
            SoundManager.playErrorSound(); 
            new Timer(1500, e -> {
                loadSubmenuDisplay();
                updateDisplayAndNotify();
                ((Timer)e.getSource()).stop();
            }).start();
        }
    }

    private void loadSubmenuDisplay() {
        switch (estado) {
            case 1:
                displayLine1 = "Menu Sucos      ";
                displayLine2 = "1.Laranja 2.Abacaxi 3.Maracuja";
                break;
            case 2:
                displayLine1 = "Menu Salgados   ";
                displayLine2 = "1.Coxinha 2.PaoQueijo 3.Esfiha";
                break;
            case 3:
                displayLine1 = "Menu Doces      ";
                displayLine2 = "1.Brigadeiro 2.Bolo 3.Pudim";
                break;
            case 0:
                displayLine1 = "Lanchonete Fatec";
                displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
                break;
            case 50:
                displayLine1 = "Add mais ou # Pagar";
                displayLine2 = "1.Sucos 2.Salgados 3.Doces";
                break;
            case 51:
                displayLine1 = "Total: R$" + String.format("%.2f", cartTotal);
                displayLine2 = "SENHA PAGAR: " + "*".repeat(currentPaymentPasswordAttempt.length());
                break;
        }
    }

    private void finalizePurchase() {
        double precoAtual = cartTotal;
        String nomeItemAtual = "Carrinho de Compras";

        if (precoAtual > 0) { 
            purchaseInProgress = true;

            if (fateCoins >= precoAtual) {
                fateCoins -= precoAtual;
                
                displayLine1 = "Compra Carrinho OK!";
                displayLine2 = "Bom apetite! Saldo: " + String.format("%.2f", fateCoins);
                
                purchaseHistory.add(new PurchaseRecord(new ArrayList<>(cartItems), cartTotal));
                System.out.println("[INFO] Compra do Carrinho realizada e registrada no histórico.");
                
                cartItems.clear();
                cartTotal = 0.0;
                SoundManager.playSuccessSound();
                currentPaymentPasswordAttempt.setLength(0);
                
                new Timer(2000, e -> {
                    estado = 0;
                    displayLine1 = "Lanchonete Fatec";
                    displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
                    purchaseInProgress = false;
                    updateDisplayAndNotify();
                    ((Timer)e.getSource()).stop();
                }).start();
            } else {
                displayLine1 = "Saldo Insuficiente!";
                displayLine2 = "Faltam R$" + String.format("%.2f", (precoAtual - fateCoins));
                updateDisplayAndNotify();
                System.out.println("[INFO] Saldo insuficiente para " + nomeItemAtual);
                SoundManager.playErrorSound(); 
                currentPaymentPasswordAttempt.setLength(0); 
                
                new Timer(2000, e -> {
                    estado = 0; 
                    displayLine1 = "Lanchonete Fatec";
                    displayLine2 = "1.Sucos 2.Salgados 3.Doces 4.Saldo";
                    purchaseInProgress = false;
                    updateDisplayAndNotify();
                    ((Timer)e.getSource()).stop();
                }).start();
            }
        }
    }

    public void processarDistancia(float distance) {
        boolean newProximityLedOn;
        if (Float.isNaN(distance) || distance <= 0 || distance > 20.0) { 
            newProximityLedOn = false;
        } else {
            newProximityLedOn = true;
        }
        
        if (newProximityLedOn != proximityLedOn) {
            proximityLedOn = newProximityLedOn;
            support.firePropertyChange("proximityLedOn", !proximityLedOn, proximityLedOn);
        }

        if (newProximityLedOn && estado >= 90) {
        } else if (newProximityLedOn && estado != 51 && estado < 90) {
        }
    }

    public List<String> getPurchaseLogFormatted() {
        if (purchaseHistory.isEmpty()) {
            List<String> emptyLog = new ArrayList<>();
            emptyLog.add("Nenhuma compra registrada ainda.");
            return emptyLog;
        }
        return purchaseHistory.stream()
                              .map(PurchaseRecord::toString)
                              .collect(Collectors.toList());
    }

    private void updateDisplayAndNotify() {
        support.firePropertyChange("displayLine1", null, displayLine1);
        support.firePropertyChange("displayLine2", null, displayLine2);
        support.firePropertyChange("fateCoins", null, fateCoins);
    }
}
