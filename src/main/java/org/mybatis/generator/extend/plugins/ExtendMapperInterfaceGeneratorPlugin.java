package org.mybatis.generator.extend.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2017/1/10.
 */
public class ExtendMapperInterfaceGeneratorPlugin extends PluginAdapter {
    private FullyQualifiedJavaType currentModelType;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.currentModelType = topLevelClass.getType();
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 判断需要生成的Mapper接口是否已经存在，已存在则不重新生成
        String mapperInterfaceClassName = interfaze.getType().getFullyQualifiedName();
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
        String targetProject = javaClientGeneratorConfiguration.getTargetProject();
        File file = new File(targetProject, mapperInterfaceClassName.replace(".", File.separator) + ".java");
        if (file.exists()) {
            return false;
        }

        Properties properties = getProperties();
        String annotations = properties.getProperty("annotations");
        // 注解
        if (null != annotations) {
            FullyQualifiedJavaType annotationType = null;
            for (String annotation : annotations.split(",")) {
                annotationType = new FullyQualifiedJavaType(annotation);
                interfaze.addImportedType(annotationType);
                interfaze.addAnnotation("@" + annotationType.getShortName());
            }
        }
        // 父接口
        String superInterfaces = properties.getProperty("superInterfaces");
        String primaryKeyType = properties.getProperty("primaryKeyType");
        if (null != superInterfaces) {
            FullyQualifiedJavaType superInterfaceType = null;
            for (String superInterface : superInterfaces.split(",")) {
                superInterfaceType = new FullyQualifiedJavaType(superInterface);
                superInterfaceType.addTypeArgument(new FullyQualifiedJavaType(currentModelType.getShortName()));
                superInterfaceType.addTypeArgument(new FullyQualifiedJavaType(primaryKeyType));
                interfaze.addSuperInterface(superInterfaceType);
                interfaze.addImportedType(new FullyQualifiedJavaType(superInterface));
            }
        }
        // add import
        interfaze.addImportedType(new FullyQualifiedJavaType(currentModelType.getFullyQualifiedName()));

        return true;
    }


}
