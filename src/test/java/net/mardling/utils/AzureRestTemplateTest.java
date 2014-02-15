package net.mardling.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AzureRestTemplateTest extends AzureRestTemplate {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testRestTemplate() {
		AzureRestTemplate template=new AzureRestTemplate();
		template.restTemplate();
	}

}
