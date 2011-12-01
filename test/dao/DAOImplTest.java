package dao;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import daoImpl.DAOImpl;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
@RunWith(MockitoJUnitRunner.class)
public class DAOImplTest {

	
	
	@Mock
	private SessionFactory mockSessionFactory;
	
	DAOImpl daoImpl;
	
	@Before
	public void setUp() throws Exception {
		daoImpl = new DAOImpl();
	}

	@Test
	public void testCurrentSession() {
		Session session = mock(Session.class);
		when(mockSessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(daoImpl, "sessionFactory", mockSessionFactory);
		Session result = daoImpl.currentSession();
		assertEquals(result, session);
	}

}
