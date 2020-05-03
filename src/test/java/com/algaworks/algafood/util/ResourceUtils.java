package com.algaworks.algafood.util;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceUtils {

	public String getJsonFromName(String nameFile) {
		StringBuilder str = new StringBuilder();
		
		try {
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(nameFile);
			Scanner scan = new Scanner(stream);
			while (scan.hasNext()) {
				str.append(scan.nextLine());
			}
			System.out.println("Conteúdo: \n");
			System.out.println(scan);
			return str.toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toString();

	}

}
