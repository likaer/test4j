package org.test4j.junit;

import org.junit.runner.RunWith;
import org.test4j.module.ICore;
import org.test4j.module.core.utility.JMockitHelper;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;

@RunWith(Test4JProxyRunner.class)
public abstract class Test4J implements ICore, ISpring, IDatabase {
    static {
        JMockitHelper.getJMockitJavaagentHit();
    }
}
