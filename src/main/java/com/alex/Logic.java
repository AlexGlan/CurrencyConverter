package com.alex;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.io.IOException;
import java.math.RoundingMode;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Logic {
    UI ui;

    BigDecimal rate, result;
    JComboBox<String> currencyBoxFrom;
    JComboBox<String> currencyBoxTo;
    JButton[] numButtons = new JButton[11];
    JButton[] funButtons = new JButton[4];
    JButton decButton, clrButton, delButton, equButton;
    // Set default currency
    String convertFrom = "NZD"; 
    String convertTo = "NZD";
    // Supported currencies
    HashMap<String, String> currencies = new HashMap<String, String>() {{
        put("Euro", "EUR");
        put("United States Dollar", "USD");
        put("British Pound Sterling", "GBP");
        put("Swiss Franc", "CHF");
        put("Canadian Dollar", "CAD");
        put("Japanese Yen", "JPY");
        put("New Zealand Dollar", "NZD");
        put("Bahraini Dinar", "BHD");
        put("Singapore Dollar", "SGD");
        put("Mexican Peso", "MXN");
        put("Norwegian Krone", "NOK");
        put("Turkish Lira", "TRY");
        put("Thai Baht", "THB");
        put("Argentine Peso", "ARS");
        put("South Korean Won", "KRW");
        put("Chinese Yuan", "CNY");
        put("Chinese Yuan (Offshore)", "CNH");
        put("Hong Kong Dollar", "HKD");        
    }};
    
    public Logic(UI ui) {
        this.ui = ui;
    }
    
    public void putPlaceholder() {
        ui.inputField.setText("0");
        ui.inputField.setForeground(ui.clrPlaceholder);
    }

    public void removePlaceholder() {
        ui.inputField.setText("");
        ui.inputField.setForeground(ui.clrLight);
    }

    private String[] setCurrencies() {
        return currencies.keySet().toArray(new String[currencies.size()]);
    }

    public void setupComboBox() {
        currencyBoxTo = new JComboBox<String>(setCurrencies());
        ui.configureComboBox(currencyBoxTo); 
        currencyBoxTo.addActionListener(new ButtonListener());
        
        currencyBoxFrom = new JComboBox<>(setCurrencies());
        ui.configureComboBox(currencyBoxFrom);
        currencyBoxFrom.addActionListener(new ButtonListener());

        ui.configureComboBoxLayout(currencyBoxTo, currencyBoxFrom);
    }

    public void setupButtons() {
        decButton = new JButton(".");
        clrButton = new JButton("C");
        delButton = new JButton("CE");
        equButton = new JButton("=");
        
        funButtons[0] = decButton;
        funButtons[1] = clrButton;
        funButtons[2] = delButton;
        funButtons[3] = equButton;

        for (int btn = 0; btn < numButtons.length; btn++) {
            if (btn != numButtons.length - 1) {
                numButtons[btn] = new JButton(String.valueOf(btn));
            } else {
                numButtons[btn] = new JButton("00");
            }
            numButtons[btn].addActionListener(new ButtonListener());
            ui.configureNumButtonUI(numButtons[btn]);
        }

        for (int btn = 0; btn < funButtons.length; btn++) {
            funButtons[btn].addActionListener(new ButtonListener());
            ui.configureFunButtonUI(funButtons[btn]);
        }
        ui.configureButtonLayout(numButtons, funButtons);
    }

    // Retrieves real-time exchange rate
    private void getExchangeRate() throws ClientProtocolException, IOException {
        String URL = "https://api.exchangerate.host/convert"
                + "?from=" + convertFrom
                +"&to=" + convertTo;
        // Make HTTP request
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(URL);
        CloseableHttpResponse response = client.execute(request);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            // Get exchange rate from JSON
            JSONObject json = new JSONObject(responseBody);
            rate = json.getBigDecimal("result");

            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
    }

    // Calculate result of the conversion
    private void calculateResult(BigDecimal amount) {
        result = rate.multiply(amount)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Add functionality to buttons
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField inputField = ui.getInputField();
            JTextField outputField = ui.getOutputField();
            // Get currency symbol
            if (e.getSource() == currencyBoxFrom) {
                convertFrom = currencies.get(currencyBoxFrom.getSelectedItem());
            }
            if (e.getSource() == currencyBoxTo) {
                convertTo = currencies.get(currencyBoxTo.getSelectedItem());
            }
            // Functionality for number buttons
            for (int i = 0; i < numButtons.length; i++) {
                if (e.getSource() == numButtons[i]) {
                    if (inputField.getText().equals("0")) {
                        removePlaceholder();
                    }
                    if (e.getSource() != numButtons[numButtons.length - 1]) {
                        inputField.setText(inputField.getText().concat(String.valueOf(i)));
                    } else {
                        if (inputField.getText().isEmpty()) {
                            inputField.setText("10");
                        } else {
                            inputField.setText(inputField.getText().concat("00"));
                        }
                    }
                }
            }
            // Functionality for operation buttons
            // Delete last digit
            if (e.getSource() == delButton) {
                String str = inputField.getText();
                inputField.setText("");
                for (int i = 0; i < str.length() - 1; i++) {
                    inputField.setText(inputField.getText() + str.charAt(i));
                }
            }
            // Clear all numbers
            if (e.getSource() == clrButton) {
                inputField.setText("");
                outputField.setText("");
                putPlaceholder();
            }
            // Put a decimal point
            if (e.getSource() == decButton) {
                if (inputField.getText().contains(".")) {
                    return;
                } else {
                    inputField.setText(inputField.getText().concat("."));
                }                
            }
            // Convert
            if (e.getSource() == equButton) {
                BigDecimal amount = new BigDecimal(inputField.getText());
                try {
                    getExchangeRate();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return;
                }
                calculateResult(amount);
                outputField.setText(result.toString());           
            }            
        }
    }  
}