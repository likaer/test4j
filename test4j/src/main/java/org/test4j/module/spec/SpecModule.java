package org.test4j.module.spec;

import org.test4j.module.core.Module;
import org.test4j.module.core.internal.TestListener;
import org.test4j.module.spec.internal.MixProxy;
import org.test4j.module.spec.internal.ScenarioResult;

import java.lang.reflect.Method;

public class SpecModule implements Module {
    private static ThreadLocal<ScenarioResult> Curr_Result = new ThreadLocal<>();

    public static ScenarioResult currScenario() {
        return Curr_Result.get();
    }

    @Override
    public void init() {
    }

    @Override
    public void afterInit() {
    }

    @Override
    public TestListener getTestListener() {
        return new SpecListener();
    }

    protected class SpecListener extends TestListener {

        @Override
        protected String getName() {
            return "SpecListener";
        }


        @Override
        public void beforeSetup(Object testedObject, Method testMethod) {
            if (testedObject instanceof IStory) {
                MixProxy.createMixes(testedObject);
                MixProxy.mix(testedObject);
                Curr_Result.set(new ScenarioResult("NoName"));
            }
        }

        @Override
        public void afterMethod(Object testObject, Method testMethod, Throwable testThrowable) {
            if (testObject instanceof IStory) {
                currScenario().print();
            }
            Curr_Result.remove();
        }
    }
}
