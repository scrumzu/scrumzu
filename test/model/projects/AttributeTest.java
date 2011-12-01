package model.projects;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributeTest {

	@Test
	public void testGetCamelName()
	{
		Attribute a = new Attribute();
		a.setName("Dla Tomka");
		assertEquals("dlaTomka", a.getCamelName());
		a.setName("dlaTomka");
		assertEquals("dlatomka", a.getCamelName());
	}
}
