package org.test4j.module.core.utility;

import org.test4j.junit.Test4JBuilder;

import java.io.File;
import java.util.regex.Pattern;


public class JMockitHelper {

    private static final Pattern JAR_REGEX = Pattern.compile(".*jmockit[-._\\d]*(-SNAPSHOT)?.jar");
    private static final String Nodep_Jar_Path = "jmockit.jar";
    private static final String Fake_JUnit_Builder = "-Dfakes=" + Test4JBuilder.class.getName();

    private static String hitsMessage = null;

    /**
     * 返回 -javaagent:.../jmockit.xxx.jar 提示
     *
     * @return
     */
    public static String getJMockitJavaagentHit() {
        if (hitsMessage == null) {
            String jarPath = getJMockitJarPath();
            StringBuffer buff = new StringBuffer();
            buff.append("If JMockit isn't initialized. Please check that your JVM is started with command option:");
            buff.append("-javaagent:" + jarPath);
            hitsMessage = buff.toString();
            MessageHelper
                    .warn("If JMockit isn't initialized. Please check that your JVM is started with command option:");
            System.err.println("\t -javaagent:" + jarPath + " " + Fake_JUnit_Builder);
        }
        return hitsMessage;
    }

    private static String getJMockitJarPath() {
        String javaClazzPaths = System.getProperty("java.class.path");
        if (javaClazzPaths == null) {
            return Nodep_Jar_Path;
        }
        String[] classPath = javaClazzPaths.split(File.pathSeparator);
        if (classPath == null) {
            return Nodep_Jar_Path;
        }
        for (String cpEntry : classPath) {
            if (JAR_REGEX.matcher(cpEntry).matches()) {
                return cpEntry;
            }
        }
        return Nodep_Jar_Path;
    }
}
