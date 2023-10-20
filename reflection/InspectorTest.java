import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Vector;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;



public class InspectorTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Inspector inspector;
    Vector objectsToInspect = new Vector();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        inspector = new Inspector();
    }

    @Test
    public void testInspectArray() {
        int[] arr = {1, 2, 3, 4, 5};
        // We're just checking if the method runs without exception for this case
        assertDoesNotThrow(() -> inspector.inspect(arr, false));
    }

    @Test
    public void testString() {
        String str = "Hello, World!";
        assertDoesNotThrow(() -> inspector.inspect(str, false));
    }

    @Test
    public void testInspectWithRecursion() {
        List <String> list = new ArrayList<>();
        list.add("One");
        list.add("Two");
        // Recursion would inspect the strings inside the list too
        assertDoesNotThrow(() -> inspector.inspect(list, true));
    }

    @Test
    public void testInspectFields() {
        TestClass dummy = new TestClass();
        assertDoesNotThrow(() -> inspector.inspect(dummy, false));
    }

    @Test
    public void testInspectSuperClass() {
        inspector.inspectSuperClass(new ArrayList<>(0), ArrayList.class, objectsToInspect,false);
        assertTrue(outContent.toString().contains("java.util.AbstractList"));
    }
    @Test
    public void testInspectSuperClass1() {
        inspector.inspectSuperClass(new Object(), Object.class, objectsToInspect,false);
        assertTrue(outContent.toString().contains("SUPERCASS: NONE"));
    }
    // You may have more complex scenarios or objects with interfaces, etc. that you'd want to test.
    // Consider creating dummy classes and interfaces for such test cases.

    static class TestClass {
        private String field1 = "test";
        public int field2 = 42;
    }

    static class Testing extends TestClass {
        protected double field3 = 0.42;
    }




}
