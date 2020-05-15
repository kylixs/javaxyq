package com.javaxyq.tools;

import groovy.lang.MetaClass;
import groovy.model.ValueHolder;
import groovy.model.ValueModel;
import groovy.swing.factory.TableModelFactory;
import groovy.util.Factory;
import groovy.util.FactoryBuilderSupport;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.TableModel;

public class MyTableModelFactory extends TableModelFactory implements Factory{
	
	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (FactoryBuilderSupport.checkValueIsType(value, name, TableModel.class)) {
            return value;
        } else if (attributes.get(name) instanceof TableModel) {
            return attributes.remove(name);
        } else {
            ValueModel model = (ValueModel) attributes.remove("model");
            if (model == null) {
                Object list = attributes.remove("list");
                if (list == null) {
                    list = new ArrayList();
                }
                model = new ValueHolder(list);
            }
            return new MyTableModel(model);
        }
    }

    @Override
    public Object invokeMethod(String name, Object args) {
        return null;
    }

    @Override
    public Object getProperty(String propertyName) {
        return null;
    }

    @Override
    public void setProperty(String propertyName, Object newValue) {

    }

    @Override
    public MetaClass getMetaClass() {
        return null;
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {

    }
}