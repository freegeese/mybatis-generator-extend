package org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * Created by Administrator on 2017/1/8.
 */
public class SelectByConditionsElementGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");

        answer.addAttribute(new Attribute("id", "selectByConditions"));

        FullyQualifiedJavaType parameterType = introspectedTable.getRules()
                .calculateAllFieldsClass();

        answer.addAttribute(new Attribute("parameterType", "java.util.Map"));

        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));

        context.getCommentGenerator().addComment(answer);

        // 查询语句 -----------------------------------
        StringBuilder sb = new StringBuilder();

        sb.append("select ");
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement());

        sb.setLength(0);
        sb.append("from ");
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        XmlElement whereElement = new XmlElement("where");
        answer.addElement(whereElement);

        for (IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(introspectedTable
                .getNonPrimaryKeyColumns())) {
            // if 标签
            XmlElement ifElement = new XmlElement("if");
            sb.setLength(0);
            String javaProperty = introspectedColumn.getJavaProperty();
            sb.append(javaProperty);
            sb.append(" != null");
            ifElement.addAttribute(new Attribute("test", sb.toString()));

            sb.setLength(0);
            if (whereElement.getElements().size() > 0) {
                sb.append("and ");
            }
            // 列名
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            // 参数值
            sb.append(" ${" + javaProperty + "}");
            ifElement.addElement(new TextElement(sb.toString()));
            // 将 if 标签添加到 where 标签中
            whereElement.addElement(ifElement);
        }

        // order by
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "orderBy != null"));
        ifElement.addElement(new TextElement("order by ${orderBy}"));
        whereElement.addElement(ifElement);

        parentElement.addElement(answer);

    }
}
