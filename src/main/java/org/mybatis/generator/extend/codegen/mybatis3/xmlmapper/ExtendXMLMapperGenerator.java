package org.mybatis.generator.extend.codegen.mybatis3.xmlmapper;

import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.elements.CountElementGenerator;
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
        // 条件查询(字符串使用Like查询)
        addSelectByRecordElement(sqlMapElement);
        // 统计
        addCountElement(sqlMapElement);
        return sqlMapElement;
    }

    private void addCountElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new CountElementGenerator(), parentElement);
    }

    protected void addSelectByRecordElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new SelectByRecordElementGenerator(), parentElement);
    }

    protected void addSelectAllElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new SelectAllElementGenerator(), parentElement);
    }
}
