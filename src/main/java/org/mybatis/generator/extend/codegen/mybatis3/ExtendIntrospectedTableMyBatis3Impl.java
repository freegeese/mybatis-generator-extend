package org.mybatis.generator.extend.codegen.mybatis3;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.ExtendXMLMapperGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/8.
 */
public class ExtendIntrospectedTableMyBatis3Impl extends IntrospectedTableMyBatis3Impl {

    /**
     * 创建Xml Mapper File 生成器
     *
     * @param javaClientGenerator
     * @param warnings
     * @param progressCallback
     */
    @Override
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings, ProgressCallback progressCallback) {
        // 使用扩展的Xml Mapper生成器
        xmlMapperGenerator = new ExtendXMLMapperGenerator();
        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
    }

    /**
     * 重写生成 Xml Mapper 生成器构建过程，传入可覆盖生成Xml参数:isMerge(是否合并)
     *
     * @return
     */
    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
        // 添加是否合并参数 ------------------
        boolean merge = true;
        String isMerge = context.getSqlMapGeneratorConfiguration().getProperty("isMerge");
        if (null != isMerge) {
            merge = Boolean.valueOf(isMerge);
        }
        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                    getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(),
                    merge, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }
        return answer;
    }
}
