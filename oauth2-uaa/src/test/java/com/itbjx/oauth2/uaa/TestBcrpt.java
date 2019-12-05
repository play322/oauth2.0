package com.itbjx.oauth2.uaa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
public class TestBcrpt {

	@Test
	public void testBCrypt(){

		//对密码进行加密
		String hashpw = BCrypt.hashpw("secret", BCrypt.gensalt());
		System.out.println(hashpw);

		//校验密码
		boolean checkpw = BCrypt.checkpw("111", "$2a$10$Q3mqogkxZvfg1yKSJIxQGuUdocSqmRkgi/GfYVDJYiPXdiViSiUTe");
		boolean checkpw2 = BCrypt.checkpw("222", "$2a$10$3/Io.nv3v4I9JtSLb2ktoud8fkC30ah5XsNODF3XQamWCXSstUhQu");
		System.out.println(checkpw);
		System.out.println(checkpw2);
	}
}

