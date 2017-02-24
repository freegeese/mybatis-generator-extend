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
public class SelectByEntityElementGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", "selectByEntity")); //$NON-NLS-1$

        FullyQualifiedJavaType parameterType = introspectedTable.getRules()
                .calculateAllFieldsClass();

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType.getFullyQualifiedName()));

        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));

        context.getCommentGenerator().addComment(answer);

        // 查询语句 -----------------------------------
        StringBuilder sb = new StringBuilder();

        sb.append("select "); //$NON-NLS-1$
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement());

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        XmlElement whereElement = new XmlElement("where"); //$NON-NLS-1$
        answer.addElement(whereElement);

        for (IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(introspectedTable
                .getNonPrimaryKeyColumns())) {
            // if 标签
            XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            ifElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$

            sb.setLength(0);
            if (whereElement.getElements().size() > 0) {
                sb.append("and ");
            }
            // 列名
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            // 参数值
            String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
            sb.append(" = ").append(parameterClause);
//            if (introspectedColumn.isStringColumn()) {
//                sb.append(" like \"%\"" + parameterClause + "\"%\" ");
//            } else {
//                sb.append(" = ").append(parameterClause);
//            }

            ifElement.addElement(new TextElement(sb.toString()));
            // 将 if 标签添加到 where 标签中
            whereElement.addElement(ifElement);
        }

        parentElement.addElement(answer);

    }
}
