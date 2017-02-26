package org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * Created by Administrator on 2017/1/8.
 */
public class SelectByPrimaryKeysElementGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        // select xml element (as answer element)
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "selectByPrimaryKeys"));
        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
        context.getCommentGenerator().addComment(answer);

        // add select text element
        answer.addElement(new TextElement("select "));
        answer.addElement(getBaseColumnListElement());
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
        String idColumnName = introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName();
        answer.addElement(new TextElement("from " + tableName + " where " + idColumnName + " in "));

        // add foreach xml element
        XmlElement foreach = new XmlElement("foreach");
        foreach.addAttribute(new Attribute("collection", "list"));
        foreach.addAttribute(new Attribute("item", "item"));
        foreach.addAttribute(new Attribute("open", "("));
        foreach.addAttribute(new Attribute("separator", ","));
        foreach.addAttribute(new Attribute("close", ")"));
        foreach.addElement(new TextElement("#{item}"));
        answer.addElement(foreach);

        // append to parent
        parentElement.addElement(answer);
    }
}
