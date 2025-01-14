package org.test4j.example;

import org.junit.Test;
import org.test4j.junit.DataFrom;
import org.test4j.junit.Test4J;
import org.test4j.module.ICore;
import org.test4j.tools.datagen.DataProvider;

/**
 * 简单junit4测试示例
 */
public class JUnitTestDemo extends Test4J {
    @Test
    public void demo() {
        int count = 10;
        ICore.want.number(count).isEqualTo(10);
    }

    @DataFrom("dataForDataFrom")
    @Test
    public void testDataFrom(String actual, String expected) {
        want.string(actual).eq(expected);
    }

    public static DataProvider dataForDataFrom() {
        return new DataProvider() {
            {
                data("string", "string");
                data("we", "we");
            }
        };
    }
}
