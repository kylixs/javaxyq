/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.jidesoft.list.ImagePreviewList;
import com.jidesoft.list.ImagePreviewPanel;
import com.jidesoft.list.ImagePreviewList.PreviewImageIcon;
import com.jidesoft.swing.JideSwingUtilities;

/**
 * @author 龚德伟
 * @history 2008-7-8 龚德伟 新建
 */
public class FileObjectPreviewPanel extends ImagePreviewPanel implements ListCellRenderer {

    private static final int X_AIXS = 0;

    private static final int Y_AIXS = 1;

    private int orientation = Y_AIXS;

    private JPanel cover = new JPanel(new BorderLayout(2, 2));

    public FileObjectPreviewPanel(int orientation) {
        this.orientation = orientation;
    }

    public FileObjectPreviewPanel() {
    }

    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        ImagePreviewList imagepreviewlist = (ImagePreviewList) list;
        setHighlightBackground(imagepreviewlist.getHighlightBackground());
        setGridBackground(imagepreviewlist.getGridBackground());
        setGridForground(imagepreviewlist.getGridForground());
        cover.setForeground(imagepreviewlist.getGridForground());
//        setGridForeground(imagepreviewlist.getGridForeground());
//        cover.setForeground(imagepreviewlist.getGridForeground());
        cover.setBackground(imagepreviewlist.getGridBackground());

        PreviewImageIcon previewImageIcon = (PreviewImageIcon) value;

        ImageIcon icon = previewImageIcon.getImageIcon();
        String title = previewImageIcon.getTitle();
        Dimension size = previewImageIcon.getSize();
        String description = previewImageIcon.getDescription();
        int showDetails = imagepreviewlist.getShowDetails();
        return createPanel(title, icon, size, description, showDetails, isSelected, cellHasFocus);
    }

    private JComponent createPanel(String title, ImageIcon imageicon, Dimension dimension,
            String description, int showDetails, boolean selected, boolean focus) {
        setImageTitle(title);
        setImageDescription(description);
        setIcon(imageicon);
        setImageSize(dimension);
        setShowDetails(showDetails);
        setSelected(selected);
        setFocused(focus);
        if (orientation == Y_AIXS) {
            //JComponent detailsPanel = this.createDetailsPanel();
            JComponent detailsPanel = new JLabel(title, JLabel.CENTER);
            detailsPanel.setPreferredSize(new Dimension(100, 20));
            this.setShowDetails(ImagePreviewList.SHOW_NONE);
            JideSwingUtilities.setOpaqueRecursively(detailsPanel, false);
//            this.setLayout(new BorderLayout(2,2));
            this.add(detailsPanel, BorderLayout.SOUTH);
        }
        return this;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

}
