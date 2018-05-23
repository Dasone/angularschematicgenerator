package main.kotlin.io.samspel.asg.ui;

import main.kotlin.io.samspel.asg.dialog.GenerateDialogDTO;

import javax.swing.*;
import java.awt.event.*;

public class GenerateAngularDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox2;
    private JCheckBox generateExternalHtmlCheckBox;
    private JTextField textField1;
    private JCheckBox generateSpecFileCheckBox;
    private JCheckBox scssCheckBox;
    private GenerateDialogDTO result;
    private boolean pressedOk = false;

    public GenerateAngularDialog() {
        setContentPane(contentPane);
        setModal(true);
        setLocationRelativeTo(null);
        setTitle("Generate Angular Schematic");
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        String name = textField1.getText();
        String type = (String) comboBox2.getSelectedItem();
        boolean generateHtml = generateExternalHtmlCheckBox.isSelected();
        boolean hasSpec = generateSpecFileCheckBox.isSelected();
        boolean isScss = scssCheckBox.isSelected();
        result = new GenerateDialogDTO(name, type, generateHtml, hasSpec, isScss);
        pressedOk = true;
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public GenerateDialogDTO getResult() {
        return result;
    }

    public static void main(String[] args) {
        GenerateAngularDialog dialog = new GenerateAngularDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public boolean isPressedOk() {
        return pressedOk;
    }
}
