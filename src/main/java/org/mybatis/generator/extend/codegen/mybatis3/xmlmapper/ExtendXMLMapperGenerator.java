package org.mybatis.generator.extend.codegen.mybatis3.xmlmapper;

import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.elements.*;

/**
 * Created by Administrator on 2017/1/8.
 */
public class ExtendXMLMapperGenerator extends XMLMapperGenerator {

    @Override
    protected XmlElement getSqlMapElement() {
        XmlElement sqlMapElement = super.getSqlMapElement();
        // 查询所有
        addSelectAllElement(sqlMapElement);
        // 根据多个主键查询
        addSelectByPrimaryKeysElement(sqlMapElement);
        // 实体查询
        addSelectByEntityElement(sqlMapElement);
        // 自定义条件查询
        addSelectByConditionsElement(sqlMapElement);
        // 统计
        addCountElement(sqlMapElement);
        // 批量插入
        addBatchInsertElement(sqlMapElement);
        // 批量更新
        addBatchUpdateElement(sqlMapElement);
        // 批量删除
        addBatchDeleteElement(sqlMapElement);

        return sqlMapElement;
    }

    protected void addSelectByPrimaryKeysElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new SelectByPrimaryKeysElementGenerator(), parentElement);
    }

    protected void addSelectByConditionsElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new SelectByConditionsElementGenerator(), parentElement);
    }

    protected void addBatchDeleteElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new BatchDeleteElementGenerator(), parentElement);
    }

    protected void addBatchUpdateElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new BatchUpdateElementGenerator(), parentElement);
    }

    protected void addBatchInsertElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new BatchInsertElementGenerator(), parentElement);
    }

    protected void addCountElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new CountElementGenerator(), parentElement);
    }

    protected void addSelectByEntityElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new SelectByEntityElementGenerator(), parentElement);
    }

    protected void addSelectAllElement(XmlElement parentElement) {
        initializeAndExecuteGenerator(new SelectAllElementGenerator(), parentElement);
    }
}
