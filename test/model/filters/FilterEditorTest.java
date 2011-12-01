package model.filters;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import services.FilterService;

@RunWith(MockitoJUnitRunner.class)
public class FilterEditorTest {

	private FilterEditor filterEditor;
	@Mock
	FilterService mockFilterService;

	@Before
	public void setUp() throws Exception {
		filterEditor = new FilterEditor(mockFilterService);

	}

	@Test
	public void testSetAsTextString() {
		long id = 23;
		Filter filter = new Filter(id);
		when(mockFilterService.getFilterById(anyLong())).thenReturn(filter);

		String text = "" + id;
		filterEditor.setAsText(text);
		assertEquals(filter, filterEditor.getValue());
		filterEditor.setAsText(null);
		assertEquals(null, filterEditor.getValue());
		filterEditor.setAsText("");
		assertEquals(null, filterEditor.getValue());
		filterEditor.setAsText("null");
		assertEquals(null, filterEditor.getValue());
	}

	@Test
	public void testGetAsText() {
		long id = 23;
		Filter filter = new Filter(id);
		filterEditor.setValue(filter);

		String idFilter = filterEditor.getAsText();
		assertEquals(idFilter, "" + id);
		
		filterEditor.setValue(null);

		idFilter = filterEditor.getAsText();
		assertEquals(idFilter, null);
	}
	
	@Test
	public void testSetFormat()
	{
		Long id = 2L;
		filterEditor.setFormat(id);
		assertEquals(id, filterEditor.getFormat());
	}

}
