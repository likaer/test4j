package org.test4j.testng.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.test4j.module.spring.testedbeans.aop.Bird;
import org.test4j.module.spring.testedbeans.aop.Cat;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

public class AutoProxyTest extends JTester {
	@Test
	public void testAutoProxyTest() {
		String[] paths = { "org/jtester/module/spring/testedbeans/aop/animal-aop.xml" };
		ApplicationContext ctx = new ClassPathXmlApplicationContext(paths);
		Cat tiger = (Cat) ctx.getBean("tiger");
		tiger.hasHotBlood();
		Bird albatross = (Bird) ctx.getBean("albatross");
		albatross.hasBeak();
	}

	@Test
	public void testAutoProxyTest2() {
		String[] paths = { "org/jtester/module/spring/testedbeans/aop/animal-aop2.xml" };
		ApplicationContext ctx = new ClassPathXmlApplicationContext(paths);
		Cat tiger = (Cat) ctx.getBean("tiger");
		tiger.hasHotBlood();
		Bird albatross = (Bird) ctx.getBean("albatross");
		albatross.hasBeak();
	}
}