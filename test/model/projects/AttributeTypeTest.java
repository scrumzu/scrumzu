package model.projects;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeTypeTest {

	@Test
	public void testAttributeType() {
		assertEquals(AttributeType.DOUBLE.getValue(),"Double");
		assertEquals("Double", AttributeType.DOUBLE.toString());
	}
}
