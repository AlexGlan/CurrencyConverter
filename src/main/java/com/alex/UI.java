package com.alex;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UI extends JFrame {
    Logic logic;
    // Styles
    Color clrPrimaryDark = new Color(28, 29, 33);
    Color clrPrimaryGrey = new Color(39, 42, 49);
    Color clrAccent = new Color(26, 112, 247);
    Color clrLight = new Color(255, 255, 255);
    Color clrPlaceholder = new Color(150, 150, 150);
    Font fontSmall = new Font("Roboto", Font.PLAIN, 12);
    Font fontBig = new Font("Roboto", Font.PLAIN, 37);
    Font fontPrimaryPlain = new Font("Roboto", Font.PLAIN, 23);  
    Font fontPrimaryBold = new Font("Roboto", Font.BOLD, 23);
    
    JTextField inputField;
    JTextField outputField;
    JPanel btnPanel;
   
    public UI() {
        logic = new Logic(this);
        initializeUI();
        logic.putPlaceholder();
        logic.setupComboBox();
        logic.setupButtons();        
        configureFrame();
    }

    private void initializeUI() {
        // Frame
        setTitle("Currency Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 510);
        setBackground(clrPrimaryDark);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(clrPrimaryDark);
        this.setContentPane(contentPane);        
        setLayout(null);
        setResizable(false);
        // Input textfield
        inputField = new JTextField();
        inputField.setBounds(18, 75, 300, 75);
        configureTextField(inputField);
        // Output textfield
        outputField = new JTextField();
        outputField.setBounds(18, 180, 300, 75);
        configureTextField(outputField);
        // Button panel
        btnPanel = new JPanel();
        btnPanel.setBounds(18, 275, 300, 180);
        btnPanel.setLayout(new GridLayout(4, 3, 5, 5));   
        btnPanel.setBackground(clrPrimaryDark);           
    }

    private void configureFrame() {
        add(logic.currencyBoxFrom);
        add(logic.currencyBoxTo);
        add(inputField);
        add(outputField);
        add(btnPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void configureTextField(JTextField textField) {
        textField.setBackground(clrPrimaryGrey);
        textField.setForeground(clrLight);
        textField.setFont(fontBig);
        textField.setBorder(new EmptyBorder(10, 15, 10, 15));
        textField.setEditable(false);
    }

    public void configureComboBox(JComboBox<String> dropdown) {
        dropdown.setBackground(clrPrimaryGrey);
        dropdown.setForeground(clrLight);
        dropdown.setFont(fontSmall);
        dropdown.setBorder(BorderFactory.createEmptyBorder());
        dropdown.setFocusable(false);
    }   

    public void configureComboBoxLayout(JComboBox<String> currencyBoxTo, JComboBox<String> currencyBoxFrom) {
        currencyBoxTo.setBounds(18, 150, 150, 30);
        currencyBoxFrom.setBounds(18, 45, 150, 30);
    }

    public void configureNumButtonUI(JButton btn) {
        btn.setFont(fontPrimaryPlain);
        btn.setBackground(clrPrimaryGrey);
        btn.setForeground(clrLight);
        btn.setFocusable(false);
        btn.setBorderPainted(false);
    }

    public void configureFunButtonUI(JButton btn) {
        btn.setFont(fontPrimaryBold);
        btn.setFocusable(false);
        btn.setBorderPainted(false);
        btn.setBackground(clrPrimaryGrey);
        btn.setForeground(clrAccent);

        if (btn.getText().equals("=")) {
            btn.setBackground(clrAccent);
            btn.setForeground(clrLight);
        } 
        if (btn.getText().equals(".")) {
            btn.setForeground(clrLight);
        }
    }

    public void configureButtonLayout(JButton[] numButtons, JButton[] funButtons) {
        // 1st row
        btnPanel.add(numButtons[1]);
        btnPanel.add(numButtons[2]);
        btnPanel.add(numButtons[3]);
        btnPanel.add(funButtons[1]);
        // 2nd row
        btnPanel.add(numButtons[4]);
        btnPanel.add(numButtons[5]);
        btnPanel.add(numButtons[6]);
        btnPanel.add(funButtons[2]);
        // 3rd row
        btnPanel.add(numButtons[7]);
        btnPanel.add(numButtons[8]);
        btnPanel.add(numButtons[9]);
        btnPanel.add(funButtons[0]);
        // 4th row
        btnPanel.add(numButtons[10]);
        btnPanel.add(numButtons[0]);
        btnPanel.add(funButtons[3]);
    }

    public JTextField getInputField() {
        return inputField;
    }

    public JTextField getOutputField() {
        return outputField;
    }
}