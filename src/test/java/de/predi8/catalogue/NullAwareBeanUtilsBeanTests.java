package de.predi8.catalogue;

import de.predi8.catalogue.event.NullAwareBeanUtilsBean;
import de.predi8.catalogue.model.Article;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class NullAwareBeanUtilsBeanTests {

	@Test
	public void testNullAwareBean() throws InvocationTargetException, IllegalAccessException {
		Article orig = new Article("123", "abc", new BigDecimal(1.99));
		Article dst = new Article("123", null, null);

		new NullAwareBeanUtilsBean().copyProperties(dst, orig);

		assertThat(dst).hasFieldOrPropertyWithValue("name", "abc");
	}
}
