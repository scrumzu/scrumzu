package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import model.pbis.PBI;
import model.projects.Attribute;
import model.projects.Project;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import services.AttributeService;

import dao.AttributeDAO;

public class AttributeServiceTest {

	@Mock
	AttributeDAO mockAttributeDao;
	
	AttributeService attributeService;
	
	@Before
	public void setUp() throws Exception {
		attributeService = new AttributeService();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSave() {
		
		final String title2= "AFTERSAVE";
		final List<Attribute> attributes = new ArrayList<Attribute>();
		Attribute attribute = new Attribute(2L);
		attribute.setName("BEFORESAVE");
		attributes.add(attribute);
		Attribute attributeNew = new Attribute();
		attributeNew.setIdAttribute(null);
		attributeNew.setName("UNSAVE");
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Attribute attribute2 = attributes.get(attributes.indexOf((invocation.getArguments()[0])));
				attribute2.setName(title2);
				return attributes.contains(attribute2);
			}
		}).when(mockAttributeDao).update((Attribute) anyObject());
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Attribute attribute2 = (Attribute) (invocation.getArguments()[0]);
				attribute2.setName(title2);
				attributes.add(attribute2);	
				return attributes.contains(attribute2);
			}
		}).when(mockAttributeDao).saveAttribute((Attribute) anyObject());
		
		ReflectionTestUtils.setField(attributeService, "attributeDao", mockAttributeDao);
		
		int size=attributes.size();
		attributeService.save(attribute);
		assertEquals(size, attributes.size());
		assertEquals(attribute.getName(), title2);
		
		attributeService.save(attributeNew);
		
		assertTrue(attributes.contains(attributeNew));
		assertEquals(attributeNew.getName(), title2);
		assertEquals(size+1, attributes.size());
	}

	@Test
	public void testIsAttributePresentForProject() {

		when(mockAttributeDao.isAttributePresentForProject(anyString(), anyLong())).thenReturn(true);
		ReflectionTestUtils.setField(attributeService, "attributeDao", mockAttributeDao);
		
		assertTrue(attributeService.isAttributePresentForProject(anyString(), anyLong()));
		
	}

	@Test
	public void testDeleteAttributes() {

		final List<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 1; i < 6; i++) {
			attributes.add(new Attribute(""+i));
		}
		final List<PBI> pbis = new ArrayList<PBI>();
		for (int i = 1; i < 6; i++) {
			pbis.add(new PBI(i));
		}
		Project project = new Project();
		project.setPBIs(pbis);
		
		
		int size  = attributes.size();
		long[] ids = { 1, 2, 3 };
		Attribute attribute = new Attribute();
		attribute.setProject(project);
		when(mockAttributeDao.getAttribute(anyLong())).thenReturn(attribute);
		
		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long idToRemove = (Long) invocation.getArguments()[0];
					attributes.remove(new Attribute(""+idToRemove));
				return "Removed " + idToRemove;
			}
		}).when(mockAttributeDao).deleteAttribute(anyLong());
		
		
		
		ReflectionTestUtils.setField(attributeService, "attributeDao", mockAttributeDao);
		attributeService.deleteAttributes(ids);

		assertEquals(attributes.size(), (size-ids.length));
	
	}

}
