/*
 * JavaXYQ Source Code 
 * LayerListTransferHandler LayerListTransferHandler.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.tools;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.DefaultListModel;
/**
 * @author dewitt
 *
 */
class LayerListTransferHandler extends TransferHandler {
	private static final DataFlavor delfaultFlavor = new DataFlavor(java.util.List.class, "List");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		if (!(comp instanceof JList)) {
			return false;
		}
		for (DataFlavor flavor : transferFlavors) {
			if (delfaultFlavor.equals(flavor))
				return true;
		}
		return false;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		if (c instanceof JList) {
			JList list = (JList) c;
			Object[] items = list.getSelectedValues();
			return new ListTransferable(items);
		}
		return null;
	}

	public int getSourceActions(JComponent c) {
		return MOVE;
	}

	@Override
	public boolean importData(JComponent comp, Transferable t) {
		try {
			JList layerList = comp;
			DefaultListModel layerListModel = layerList.getModel();
			JList.DropLocation dropLocation = layerList.getDropLocation();
			if(!dropLocation)return;//maybe invoke by copy-paste
			int insertIndex =dropLocation? dropLocation.getIndex():layerList.selectedIndex;
			Object[] items = (Object[]) t.getTransferData(delfaultFlavor);
			//for ?
			for (int i = 0; i < items.length; i++) {
				int index = layerListModel.indexOf(items[i]);
				if (index < insertIndex)
					insertIndex--;
				layerListModel.remove(index);
			}
			for (int i = items.length - 1; i >= 0; i--) {
				layerListModel.insertElementAt(items[i], insertIndex);
			}
			layerList.setSelectionInterval(insertIndex, insertIndex + items.length - 1);
			//fire other listeners
			layerList.firePropertyChange('LayerOrder', -1, insertIndex);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// handle exception
		}
		return false;
	}
}

class ListTransferable implements Transferable {
	Object[] items;
	private static final DataFlavor delfaultFlavor = new DataFlavor(java.util.List.class, "List");
	public ListTransferable(Object[] items) {
		this.items = items;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		// simply return the data
		return items;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return [delfaultFlavor] ;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return delfaultFlavor.equals(flavor);
	}

}