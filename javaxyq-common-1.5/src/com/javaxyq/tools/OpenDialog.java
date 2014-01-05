package com.javaxyq.tools;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class OpenDialog extends JFileChooser {

    private static final long serialVersionUID = 1L;

    //private static OpenDialog openDialog=new OpenDialog();

    public OpenDialog() {
        super();
        setMultiSelectionEnabled(true);
    }

    public int showDialog(Component parent, File dir, String title, FileFilter filter) {
        setCurrentDirectory(dir);
        setFileFilter(filter);
        setDialogTitle(title);

        return showOpenDialog(parent);
    }

    @Override
    protected JDialog createDialog(Component parent) throws HeadlessException {
        JDialog dialog = super.createDialog(parent);
        Container contentPanel = dialog.getContentPane();
//		contentPanel.add(getBottomComp(),BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        return dialog;
    }

    /**
     * Returns the specified component's toplevel <code>Frame</code> or <code>Dialog</code>.
     * 
     * @param parentComponent the <code>Component</code> to check for a <code>Frame</code> or
     *        <code>Dialog</code>
     * @return the <code>Frame</code> or <code>Dialog</code> that contains the component, or the
     *         default frame if the component is <code>null</code>, or does not have a valid
     *         <code>Frame</code> or <code>Dialog</code> parent
     * @exception HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns
     *            <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
        if (parentComponent == null)
            return JOptionPane.getRootFrame();
        if (parentComponent instanceof Frame || parentComponent instanceof Dialog)
            return (Window) parentComponent;
        return getWindowForComponent(parentComponent.getParent());
    }
}
