package org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/1/8.
 */
public class BatchDeleteElementGenerator extends AbstractXmlElementGenerator {
    /*
        <delete id="batchRemoveUserByPks" parameterType="java.util.List">
           DELETE FROM LD_USER WHERE ID in
           <foreach item="item" index="index" collection="list" open="(" separator="," close=")">
             #{item}
           </foreach>
        </delete>
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("id", "batchDelete")); //$NON-NLS-1$
        context.getCommentGenerator().addComment(answer);

        StringBuilder deleteSql = new StringBuilder();
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
        deleteSql.append("delete from " + tableName + " where ");

        List<IntrospectedColumn> idColumns = introspectedTable.getPrimaryKeyColumns();
        for (IntrospectedColumn idColumn : idColumns) {
            deleteSql.append(idColumn.getActualColumnName() + " in ");
            break;
        }
        answer.addElement(new TextElement(deleteSql.toString()));

        XmlElement foreach = new XmlElement("foreach");
        foreach.addAttribute(new Attribute("collection", "list"));
        foreach.addAttribute(new Attribute("item", "item"));
        foreach.addAttribute(new Attribute("open", "("));
        foreach.addAttribute(new Attribute("separator", ","));
        foreach.addAttribute(new Attribute("close", ")"));
        foreach.addElement(new TextElement("#{item}"));
        answer.addElement(foreach);

        parentElement.addElement(answer);
    }
}
