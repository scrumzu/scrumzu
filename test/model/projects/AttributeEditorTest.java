package model.projects;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class AttributeEditorTest {
	@Test
	public void testSetAsText()
	{
		AttributeEditor ae = new AttributeEditor();
		ae.setAsText(null);
		assertNotNull(ae.getValue());
		ae.setAsText("");
		assertNotNull(ae.getValue());
	}

}
