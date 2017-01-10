package org.mybatis.generator.extend.api.dom;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.extend.GeneratorUtils;
import org.mybatis.generator.extend.codegen.mybatis3.ExtendIntrospectedTableMyBatis3Impl;
import org.mybatis.generator.extend.config.ExtendProperties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扩展Java代码格式化，以便再修改Java文件的时候可以自动保留下自定义的业务代码
 *
 * @author zhangguangyong <a href="#">1243610991@qq.com</a>
 * @date 2017/1/10 14:28
 * @sine 0.0.1
 */
public class ExtendJavaFormatter extends DefaultJavaFormatter {
    @Override
    public String getFormattedContent(CompilationUnit compilationUnit) {
        // 格式化后的源文件代码
        String formattedContent = super.getFormattedContent(compilationUnit);
        // 自定义代码开始标记
        String customCodeStartMark = context.getProperty(ExtendProperties.CUSTOM_CODE_START_MARK);
        // 自定义代码结标记
        String customCodeEndMark = context.getProperty(ExtendProperties.CUSTOM_CODE_END_MARK);

        // 行分隔符
        String lineSeparator = System.lineSeparator();
        // 自定义代码正则匹配
        String customCodeMatchRegex = "(?s)\\p{Blank}*/\\*\\s*" + customCodeStartMark + ".*" + customCodeEndMark + "\\s*\\*/";

        // 检查是否已经生成了需要生成的文件
        GeneratedJavaFile javaFile = ExtendIntrospectedTableMyBatis3Impl.getGeneratedJavaFile(compilationUnit);
        String fileContent = GeneratorUtils.getFileContent(javaFile);
        // 如果fileContent为空，说明还没有生成过此文件
        if (null != fileContent && fileContent.trim().length() > 0) {
            // 匹配自定义代码
            Matcher m = Pattern.compile(customCodeMatchRegex).matcher(fileContent);
            if (m.find()) {
                // 保留自定义代码
                return GeneratorUtils.addCustomCodeToFormattedContent(m.group(), formattedContent);
            }
        }

        // mapper interface
        if (compilationUnit.isJavaInterface()) {
            return formattedContent;
        }

        // 没有生成过此文件添加空的自定义代码标记
        String emptyCustomCode = lineSeparator + "\t/*" + customCodeStartMark + "*/" + lineSeparator + lineSeparator + "\t/*" + customCodeEndMark + "*/";
        return GeneratorUtils.addCustomCodeToFormattedContent(emptyCustomCode, formattedContent);
    }
}
