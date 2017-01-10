package org.mybatis.generator.extend.api.dom;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.extend.GeneratorUtils;
import org.mybatis.generator.extend.codegen.mybatis3.ExtendIntrospectedTableMyBatis3Impl;
import org.mybatis.generator.extend.config.ExtendProperties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扩展XML代码格式化，以便再修改XML文件的时候可以自动保留下自定义的业务代码
 * @author zhangguangyong <a href="#">1243610991@qq.com</a>
 * @date 2017/1/10 14:30
 * @sine 0.0.1
 */
public class ExtendXmlFormatter extends DefaultXmlFormatter {
    @Override
    public String getFormattedContent(Document document) {
        // 格式化后的源文件代码
        String formattedContent = super.getFormattedContent(document);
        // 自定义代码开始标记
        String customCodeStartMark = context.getProperty(ExtendProperties.CUSTOM_CODE_START_MARK);
        // 自定义代码结标记
        String customCodeEndMark = context.getProperty(ExtendProperties.CUSTOM_CODE_END_MARK);

        // 行分隔符
        String lineSeparator = System.lineSeparator();
        // 自定义代码正则匹配
        String customCodeMatchRegex = "(?s)\\p{Blank}*<!--.*" + customCodeStartMark + ".*-->.*<!--.*" + customCodeEndMark + ".*-->";

        // 检查是否已经生成了需要生成的文件
        GeneratedXmlFile xmlFile = ExtendIntrospectedTableMyBatis3Impl.getGeneratedXmlFile(document);
        String fileContent = GeneratorUtils.getFileContent(xmlFile);
        // 如果fileContent为空，说明还没有生成过此文件
        if (null != fileContent && fileContent.trim().length() > 0) {
            // 匹配自定义代码
            Matcher m = Pattern.compile(customCodeMatchRegex).matcher(fileContent);
            if (m.find()) {
                // 保留自定义代码
                return GeneratorUtils.addCustomCodeToFormattedContent(m.group(), formattedContent);
            }
        }

        // 不存在自定义代码就添加空的自定义代码
        String emptyCustomCode = "\t<!--" + customCodeStartMark + "-->" + lineSeparator + lineSeparator + "\t<!--" + customCodeEndMark + "-->";
        return GeneratorUtils.addCustomCodeToFormattedContent(emptyCustomCode, formattedContent);
    }
}
