package main.kotlin.io.samspel.asg.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.openapi.wm.impl.IdeFrameImpl;
import main.kotlin.io.samspel.asg.container.ArgumentContainer;
import main.kotlin.io.samspel.asg.container.ResultContainer;
import main.kotlin.io.samspel.asg.container.SchematicContainer;
import main.kotlin.io.samspel.asg.state.StateStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GenerateAngularDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox2;
    private JCheckBox checkBox1;
    private JTextField textField1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private ResultContainer result;
    private boolean pressedOk = false;
    private Map<String, SchematicContainer> schematics = new HashMap<>();
    private Map<String, ArgumentContainer> arguments = new HashMap<>();
    private SchematicContainer selectedSchematic;
    private StateStorage state;


    public GenerateAngularDialog(Project project) {
        state = StateStorage.Companion.getInstance(project);
        initSchematics();
        setContentPane(contentPane);
        setModal(true);

        setLocationRelativeTo(getParentWindow(project));
        setTitle("Generate Angular Schematic");
        getRootPane().setDefaultButton(buttonOK);

        comboBox2.removeAllItems();

        for (Map.Entry<String, SchematicContainer> stringSchematicContainerEntry : schematics.entrySet()) {
            comboBox2.addItem(stringSchematicContainerEntry.getKey());
        }

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        comboBox2.addItemListener(e -> {
            if (!selectedSchematic.getName().equals(e.getItem().toString())) {
                changeSchematic(e.getItem().toString());
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
        contentPane.registerKeyboardAction(e ->
                onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String name = textField1.getText();

        for (int i = 0; i < selectedSchematic.getArgs().length; i++) {
            ArgumentContainer arg = selectedSchematic.getArgs()[i];
            if (i == 0)
                arg.setValue(checkBox1.isSelected());
            else if (i == 1)
                arg.setValue(checkBox2.isSelected());
            else
                arg.setValue(checkBox3.isSelected());

            switch (arg.getName()) {
                case "html":
                    state.setArgHtml(arg.getValue());
                    break;
                case "spec":
                    state.setArgSpec(arg.getValue());
                    break;
                case "style":
                    state.setArgStyle(arg.getValue());
                    break;
                case "routing":
                    state.setArgRouting(arg.getValue());
                    break;
                default:
            }
        }

        result = new ResultContainer(name, selectedSchematic.getName(), selectedSchematic.getArgs());
        pressedOk = true;
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public ResultContainer getResult() {
        return result;
    }

    public static void main(String[] args) {
        GenerateAngularDialog dialog = new GenerateAngularDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public boolean isPressedOk() {
        return pressedOk;
    }

    private void initSchematics() {
        arguments.put("html", new ArgumentContainer("html", "Generate external html", state.getArgHtml(), "--inline-template=false", "--inline-template=true"));
        arguments.put("spec", new ArgumentContainer("spec", "Generate spec file", state.getArgSpec(), "--spec=true", "--spec=false"));
        arguments.put("style", new ArgumentContainer("style", "Style with scss instead of css", state.getArgStyle(), "--styleext=scss", "--styleext=css"));
        arguments.put("routing", new ArgumentContainer("routing", "Generate a routing module", state.getArgRouting(), "--routing=true", "--routing=false"));


        schematics.put("component", new SchematicContainer(
                "component",
                arguments.get("html"),
                arguments.get("spec"),
                arguments.get("style")
        ));

        schematics.put("class", new SchematicContainer(
                "class",
                arguments.get("spec")
        ));

        schematics.put("enum", new SchematicContainer(
                "enum"
        ));

        schematics.put("interface", new SchematicContainer(
                "interface"
        ));

        schematics.put("module", new SchematicContainer(
                "module",
                arguments.get("routing"),
                arguments.get("spec")
        ));

        schematics.put("directive", new SchematicContainer(
                "directive",
                arguments.get("spec")
        ));

        schematics.put("guard", new SchematicContainer(
                "guard",
                arguments.get("spec")
        ));

        schematics.put("pipe", new SchematicContainer(
                "pipe",
                arguments.get("spec")
        ));

        schematics.put("service", new SchematicContainer(
                "service",
                arguments.get("spec")
        ));

        selectedSchematic = schematics.get("component");
        changeSchematic("component");
    }

    private void changeSchematic(String name) {
        selectedSchematic = schematics.get(name);

        checkBox1.setVisible(false);
        checkBox2.setVisible(false);
        checkBox3.setVisible(false);
        int i = 0;
        for (ArgumentContainer arg : selectedSchematic.getArgs()) {
            switch (i) {
                case 0:
                    checkBox1.setVisible(true);
                    checkBox1.setText(arg.getTitle());
                    checkBox1.setSelected(arg.getValue());
                    break;
                case 1:
                    checkBox2.setVisible(true);
                    checkBox2.setText(arg.getTitle());
                    checkBox2.setSelected(arg.getValue());
                    break;
                default:
                    checkBox3.setVisible(true);
                    checkBox3.setText(arg.getTitle());
                    checkBox3.setSelected(arg.getValue());
            }
            i++;
        }
    }

    private Window getParentWindow(Project project) {
        WindowManagerEx windowManager = (WindowManagerEx) WindowManager.getInstance();

        Window window = windowManager.suggestParentWindow(project);
        if (window == null) {
            Window focusedWindow = windowManager.getMostRecentFocusedWindow();
            if (focusedWindow instanceof IdeFrameImpl) {
                window = focusedWindow;
            }
        }
        return window;
    }
}
