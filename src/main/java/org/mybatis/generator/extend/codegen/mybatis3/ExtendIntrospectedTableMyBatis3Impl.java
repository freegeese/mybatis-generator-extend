package org.mybatis.generator.extend.codegen.mybatis3;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.extend.GeneratorUtils;
import org.mybatis.generator.extend.codegen.mybatis3.xmlmapper.ExtendXMLMapperGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/8.
 */
public class ExtendIntrospectedTableMyBatis3Impl extends IntrospectedTableMyBatis3Impl {

    // 文档对象（xml文件）与生成文件对象之间的映射
    private static Map<String, GeneratedXmlFile> documentWithGeneratedFileMap = new HashMap<>();

    // 编译单元（java文件）与生成文件对象之间的映射
    private static Map<String, GeneratedJavaFile> compilationUnitWithGeneratedFileMap = new HashMap<>();

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
     * <p>
     * 覆盖获取获取生成Xml文件对象的接口，添加可覆盖xml内容的属性(isMerge：true 是合并，false 是覆盖)
     * 保存编译单元与Java文件对象之间的映射关系
     * </p>
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
                documentWithGeneratedFileMap.put(GeneratorUtils.getDocumentNamespace(document), gxf);
            }
        }
        return answer;
    }

    /**
     * <p>
     * 覆盖获取获取生成Java文件对象的接口，保存编译单元与Java文件对象之间的映射关系
     * </p>
     *
     * @return
     */
    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> javaFiles = super.getGeneratedJavaFiles();
        int size = javaFiles.size();
        GeneratedJavaFile javaFile = null;
        String javaFileClassName = null;
        for (int i = 0; i < size; i++) {
            javaFile = javaFiles.get(i);
            javaFileClassName = GeneratorUtils.getJavaFileClassName(javaFile.getCompilationUnit());
            compilationUnitWithGeneratedFileMap.put(javaFileClassName, javaFile);
        }
        return javaFiles;
    }

    /**
     * 获取文档对象对应的生成文件对象
     *
     * @param document
     * @return
     */
    public static GeneratedXmlFile getGeneratedXmlFile(Document document) {
        return documentWithGeneratedFileMap.get(GeneratorUtils.getDocumentNamespace(document));
    }

    /**
     * 获取编译单元对应的生成文件对象
     *
     * @param compilationUnit
     * @return
     */
    public static GeneratedJavaFile getGeneratedJavaFile(CompilationUnit compilationUnit) {
        return compilationUnitWithGeneratedFileMap.get(GeneratorUtils.getJavaFileClassName(compilationUnit));
    }
}
