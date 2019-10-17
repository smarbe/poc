package lv.nixx.poc.java8.core.Interface;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class InterfaceWithFieldTest {


    @Test
    public void test() {
        A a = new A();
        a.setName("a.name");

        B b = new B();
        b.setName("b.name");

        System.out.println(a.properties);

        assertEquals("a.name", a.getName());
        assertEquals("b.name", b.getName());

        assertEquals("processed",  a.process());
    }


    class Parent {
        String process() {
            return "processed";
        }
    }

    private interface InterfaceWithField {

        Map<String, String> properties = new HashMap<>();

        default void setName(String name) {
            properties.put(this.hashCode() + ":name", name);
        }

        default String getName() {
            return properties.get(this.hashCode() + ":name");
        }
    }

    private class A extends Parent implements InterfaceWithField {
    }

    private class B extends Parent implements InterfaceWithField {
    }

}
