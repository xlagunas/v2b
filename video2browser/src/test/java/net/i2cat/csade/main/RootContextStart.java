package net.i2cat.csade.main;

import static org.junit.Assert.*;
import net.i2cat.csade.configuration.RootContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(classes = { RootContext.class })
@Transactional
public class RootContextStart {
	private static final Logger logger = LoggerFactory.getLogger(RootContextStart.class);

	@Test
	public void test() {
		logger.info("Hola Mon!");

		assertTrue(true);
	}

}
