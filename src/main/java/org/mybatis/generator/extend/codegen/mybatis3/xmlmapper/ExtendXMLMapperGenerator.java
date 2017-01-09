package org.mybatis.generator.extend.codegen.mybatis3.xmlmapper;

import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.elements.SelectAllElementGenerator;
import org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.elements.SelectByRecordElementGenerator;

/**
 * Created by Administrator on 2017/1/8.
 */
public class ExtendXMLMapperGenerator extends XMLMapperGenerator {

    @Override
    protected XmlElement getSqlMapElement() {
        XmlElement sqlMapElement = super.getSqlMapElement();
        // 查询所有
        addSelectAllElement(sqlMapElement);
        // 条件查询
        addSelectByRecordElment(sqlMapElement);
        // Like条件查询

        return sqlMapElement;
    }

    protected void addSelectByRecordElment(XmlElement parentElement) {
        initializeAndExecuteGenerator(new SelectByRecordElementGenerator(), parentElement);
    }

    protected void addSelectAllElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new SelectAllElementGenerator(), parentElement);
    }
}
