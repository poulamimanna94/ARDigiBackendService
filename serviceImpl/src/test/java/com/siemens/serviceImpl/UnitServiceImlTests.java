package com.siemens.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.siemens.domain.Header;
import com.siemens.domain.Unit;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class UnitServiceImlTests {
	@Mock
	UnitServiceImpl unitServiceImpl;
	@Autowired Instant instant;
	
	@Test
	void testFunction() {
		//Long id, @NotNull String unitName, Instant createdAt, Instant modifiedAt, Set<Section> sections
		Set<Header> tmp=new HashSet();
		Unit testingUnit=new Unit(199992L,"A0",instant.now(),Instant.now(),tmp);
		 assertThat(unitServiceImpl.save(testingUnit)).isNull();
	}
	
	@Test
	void findAllTest() {
		 assertThat(unitServiceImpl.findAll()).hasSize(0);
	}

}
