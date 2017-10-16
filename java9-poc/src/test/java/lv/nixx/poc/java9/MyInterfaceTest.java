package lv.nixx.poc.java9;

import org.junit.Test;

public class MyInterfaceTest {

	@Test
	public void interfaceImpl() {
		MyInterface c = new MyInterfaceImpl();
		c.simpleMethod();
		c.anotherMethodWithDefault();
	}
	
	@Test
	public void overridenMethod() {
		
		MyInterface c = new MyInterfaceImpl( ) {
			
			public void methodWithDefault() {
				System.out.println("methodWithDefault:another impl");
			}
		};
		c.simpleMethod();
		c.methodWithDefault();
	}
	
}
