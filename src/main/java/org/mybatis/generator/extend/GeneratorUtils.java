package org.mybatis.generator.extend;

import org.mybatis.generator.api.GeneratedFile;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * Created by zhangguangyong on 2016/7/26.
 */
public class GeneratorUtils {


    private static ShellCallback shellCallback = new DefaultShellCallback(false);

    /**
     * 添加自定义内容到java文件，在最后一行前添加
     *
     * @param customCode
     * @param formattedContent
     * @return
     */
    public static String addCustomCodeToFormattedContent(String customCode, String formattedContent) {
        StringBuilder retVal = new StringBuilder();
        String[] lines = formattedContent.split(System.lineSeparator());
        int length = lines.length;
        for (int i = 0; i < length; i++) {
            // 在最后一行之前添加自定义内容
            if (i == length - 1) {
                retVal.append(System.lineSeparator() + customCode).append(System.lineSeparator()).append(lines[i]);
                break;
            }
            retVal.append(lines[i]).append(System.lineSeparator());
        }
        return retVal.toString();
    }

    /**
     * 获取xml文件的命名空间
     *
     * @param document
     * @return
     */
    public static String getDocumentNamespace(Document document) {
        return document.getRootElement().getAttributes().get(0).getValue();
    }

    /**
     * 获取java文件的全类名
     *
     * @param compilationUnit
     * @return
     */
    public static String getJavaFileClassName(CompilationUnit compilationUnit) {
        FullyQualifiedJavaType type = compilationUnit.getType();
        return type.getPackageName() + "." + type.getShortName();
    }


    /**
     * 获取GeneratedFile对应的物理路径的文件
     *
     * @param generatedFile
     * @return
     * @throws ShellException
     */
    public static File getActualFile(GeneratedFile generatedFile) throws ShellException {
        File dir = shellCallback.getDirectory(generatedFile.getTargetProject(), generatedFile.getTargetPackage());
        return new File(dir, generatedFile.getFileName());
    }

    /**
     * 获取GeneratedFile对应文件的内容
     *
     * @param generatedFile
     * @return
     */
    public static String getFileContent(GeneratedFile generatedFile) {
        try {
            File file = getActualFile(generatedFile);
            StringBuilder fileContent = new StringBuilder();
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line = null;
                while (null != (line = br.readLine())) {
                    fileContent.append(line).append(System.lineSeparator());
                }
                br.close();
                return fileContent.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void modifyFieldValue(Object target, String fieldName, Object newValue) throws IllegalAccessException, NoSuchFieldException {
        Field field = getDeclaredField(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, newValue);
        field.setAccessible(false);
    }

    public static Field getDeclaredField(Class cls, String fieldName) throws NoSuchFieldException {
        Field field = cls.getDeclaredField(fieldName);
        if (null != field) {
            return field;
        }

        Class superclass = cls.getSuperclass();
        if (null != superclass) {
            return getDeclaredField(superclass, fieldName);
        }

        return null;
    }

    public static Object getDeclaredFieldValue(Object target, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getDeclaredField(target.getClass(), fieldName);
        field.setAccessible(true);
        Object value = field.get(target);
        field.setAccessible(false);
        return value;
    }

    public static String firstUpper(String text){
        if(text.length() == 1){
            return text.toUpperCase();
        }
        return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
