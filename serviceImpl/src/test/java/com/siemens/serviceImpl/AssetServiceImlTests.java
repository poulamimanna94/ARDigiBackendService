package com.siemens.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
class AssetServiceImlTests {
	//classes={com.siemens.DvpiApplication.class}
	
	@Autowired AssetServiceImpl assetServiceImpl;
	
	@Test
	void testFunction() {
		 assertThat("The Lord of the Rings").isNotNull();
	}
	
//	@Test
//	void save() {
//		Asset ast=assetServiceImpl.save(null, null);
//		assertThatThrownBy(() -> { assetServiceImpl.save(null, null) });
//	}
}
