package com.example.rna.exam.ia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
public class IaApplication {

	public static void main(String[] args) {
		/*String rutaArchivo = "/home/edgar/Documentos/ia/src/main/resources/static/TRAIN.txt";
		ReaderText fileReader = new ReaderText();
		int [][]matriz= fileReader.leerArchivo(rutaArchivo);
		fileReader.imprimirMatriz(matriz);*/

		SpringApplication.run(IaApplication.class, args);
	}

}
