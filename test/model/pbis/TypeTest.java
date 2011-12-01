package model.pbis;

import static org.junit.Assert.*;

import org.junit.Test;

public class TypeTest {

	@Test
	public void testType() {
		assertEquals(Type.DESIGN.getValue(),"Design");
		assertEquals("Design", Type.DESIGN.toString());
	}
}
