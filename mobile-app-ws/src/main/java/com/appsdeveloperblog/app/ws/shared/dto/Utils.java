package com.appsdeveloperblog.app.ws.shared.dto;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {
	public String randomString(int length) {
		Random random = new Random();
		final String albhabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder();

		for (int j = 0; j < length; j++) {
			int k = random.nextInt(62);
			sb.append(albhabet.charAt(k)); // alphabet.length() =62

		}
		System.out.println("String builder " +sb);
		String t = new String(sb);
		return t;
	}

}