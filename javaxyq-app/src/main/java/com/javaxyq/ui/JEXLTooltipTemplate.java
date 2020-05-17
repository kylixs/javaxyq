/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-5-29
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.ui;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.UnifiedJEXL;
import org.apache.commons.jexl2.UnifiedJEXL.Expression;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.Context;
import com.javaxyq.widget.Player;

/**
 * @author gongdewei
 * @date 2010-5-29 create
 */
@Slf4j
public class JEXLTooltipTemplate implements TooltipTemplate {

    private final Context context;
    private final Map<String, Expression> expressions;

    public JEXLTooltipTemplate(Context context) {
        super();
        this.context = context;
        expressions = new HashMap<>();
    }

    @Override
    public String getTooltipText(String tpl) {
        Expression expression = expressions.get(tpl);
        if (expression == null) {
            try {
                JexlEngine jexl = new JexlEngine();
                UnifiedJEXL ujexl = new UnifiedJEXL(jexl);
                expression = ujexl.parse(tpl);
                expressions.put(tpl, expression);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        if (expression != null) {
            Map<String, Object> properties = new HashMap<>();
            Player player = context.getPlayer();
            if (player != null) {
                properties = ApplicationHelper.getApplication().getDataManager().getProperties(player);
            }
            JexlContext jexlcontext = new MapContext(properties);
            return expression.evaluate(jexlcontext).toString();
        }
        return null;
    }

}
