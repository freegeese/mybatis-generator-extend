package org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;

/**
 * Created by Administrator on 2017/1/8.
 */
public class BatchUpdateElementGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update"); 
        answer.addAttribute(new Attribute("id", "batchUpdate")); 
        context.getCommentGenerator().addComment(answer);

        // <foreach collection="list" item="item" index="index" open="" close="" separator=";">
        XmlElement foreach = new XmlElement("foreach");
        foreach.addAttribute(new Attribute("collection", "list"));
        foreach.addAttribute(new Attribute("item", "item"));
        foreach.addAttribute(new Attribute("open", ""));
        foreach.addAttribute(new Attribute("close", ""));
        foreach.addAttribute(new Attribute("separator", ";"));

        answer.addElement(foreach);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); 
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        foreach.addElement(new TextElement(sb.toString()));

        XmlElement setElement = new XmlElement("set"); 
        foreach.addElement(setElement);

        List<IntrospectedColumn> nonPrimaryKeyColumns = introspectedTable.getNonPrimaryKeyColumns();
        int index = 0;
        for (IntrospectedColumn column : nonPrimaryKeyColumns) {
            XmlElement ifElement = new XmlElement("if"); 
            String property = column.getJavaProperty();
            ifElement.addAttribute(new Attribute("test", "item." + property + " != null"));
            sb.setLength(0);
            sb.append(Ibatis2FormattingUtilities.getEscapedColumnName(column));
            sb.append(" = "); 
            sb.append("#{item." + property + ", jdbcType=" + column.getJdbcTypeName() + "}");
            index++;
            if (index < nonPrimaryKeyColumns.size()) {
                sb.append(",");
            }
            ifElement.addElement(new TextElement(sb.toString()));

            setElement.addElement(ifElement);
        }

        boolean and = false;
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); 
            } else {
                sb.append("where "); 
                and = true;
            }

            sb.append(Ibatis2FormattingUtilities.getEscapedColumnName(column));
            sb.append(" = "); 
            sb.append("#{item." + column.getActualColumnName() + ", jdbcType=" + column.getJdbcTypeName() + "}");
            foreach.addElement(new TextElement(sb.toString()));
        }

        parentElement.addElement(answer);
    }
}
