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
public class BatchInsertElementGenerator extends AbstractXmlElementGenerator {
    /*
        <insert id="batchInsert" useGeneratedKeys="true" keyProperty="id">
            insert into t_user (username, password) values
            <foreach collection="list" item="item" separator=",">
                (#{item.username,jdbcType=VARCHAR}, #{item.password,jdbcType=VARCHAR})
            </foreach>
        </insert>
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("id", "batchInsert")); //$NON-NLS-1$
        answer.addAttribute(new Attribute("useGeneratedKeys", "true")); //$NON-NLS-1$
        answer.addAttribute(new Attribute("keyProperty", "id")); //$NON-NLS-1$
        context.getCommentGenerator().addComment(answer);

        StringBuilder insertColumns = new StringBuilder();
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
        insertColumns.append("insert into " + tableName + " (");

        StringBuilder insertValues = new StringBuilder();
        insertValues.append("(");

        String itemKey = "item";
        Iterator<IntrospectedColumn> iterator = introspectedTable.getAllColumns().iterator();
        while (iterator.hasNext()) {
            IntrospectedColumn column = iterator.next();
            if (column.isIdentity()) {
                continue;
            }
            insertColumns.append(Ibatis2FormattingUtilities.getEscapedColumnName(column));
            // (#{item.username,jdbcType=VARCHAR}, #{item.password,jdbcType=VARCHAR})
            insertValues.append("#{" + itemKey + "." + column.getActualColumnName() + ", jdbcType=" + column.getJdbcTypeName() + "}");
            if (iterator.hasNext()) {
                insertColumns.append(", "); //$NON-NLS-1$
                insertValues.append(", "); //$NON-NLS-1$
            }
        }
        insertColumns.append(") values");
        answer.addElement(new TextElement(insertColumns.toString()));

        insertValues.append(")");
        XmlElement foreach = new XmlElement("foreach");
        foreach.addAttribute(new Attribute("collection", "list"));
        foreach.addAttribute(new Attribute("item", itemKey));
        foreach.addAttribute(new Attribute("separator", ","));
        foreach.addElement(new TextElement(insertValues.toString()));
        answer.addElement(foreach);
        parentElement.addElement(answer);
    }
}
