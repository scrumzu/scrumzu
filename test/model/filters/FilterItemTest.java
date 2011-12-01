package model.filters;

import static org.junit.Assert.*;

import model.projects.Project;

import org.junit.Test;

public class FilterItemTest {

	@Test
	public void testEqualsObject() {
		FilterItem fi1= new FilterItem();
		fi1.setAndOr("and");
		fi1.setField("team");
		fi1.setOperator("eq");
		fi1.setValue("1");
		
		FilterItem fi2= new FilterItem(fi1.getAndOr(), fi1.getField(), fi1.getOperator(), fi1.getValue());
		assertTrue(fi1.equals(fi2));
		assertFalse(fi1.equals(null));
		assertTrue(fi1.equals(fi1));
		assertFalse(fi1.equals(new Project()));
		
		fi1.setAndOr("or");
		assertFalse(fi1.equals(fi2));
		fi1.setAndOr("and");
		fi1.setField("project");
		assertFalse(fi1.equals(fi2));
		fi1.setField("team");
		fi1.setOperator("nq");
		assertFalse(fi1.equals(fi2));
		fi1.setOperator("eq");
		fi1.setValue("2");
		assertFalse(fi1.equals(fi2));
		
		
		fi1.setValue("1");
		fi1.setAndOr(null);
		assertFalse(fi1.equals(fi2));
		fi1.setAndOr("and");
		fi1.setField(null);
		assertFalse(fi1.equals(fi2));
		fi1.setField("team");
		fi1.setOperator(null);
		assertFalse(fi1.equals(fi2));
		fi1.setOperator("eq");
		fi1.setValue(null);
		assertFalse(fi1.equals(fi2));
	}
	@Test
	public void testToString()
	{
		FilterItem fi1= new FilterItem();
		fi1.setAndOr("and");
		fi1.setField("team");
		fi1.setOperator("eq");
		fi1.setValue("1");
		assertNotNull(fi1.toString());
	}
	@Test
	public void testHashCode()
	{
		FilterItem fi1= new FilterItem();
		fi1.setAndOr("and");
		fi1.setField("team");
		fi1.setOperator("eq");
		fi1.setValue("1");
		int result = fi1.hashCode();
		assertFalse(result==0);
	}

}
