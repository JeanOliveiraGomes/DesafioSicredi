package br.com.sicredi;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

@SpringBootApplication
public class SincronizacaoReceitaApplication { 

	public static void main(String[] args) {

		SpringApplication.run(SincronizacaoReceitaApplication.class, args);

		iniciarRotina(args);
	}

	private static void iniciarRotina(String[] args) {
		try {
			CSVReader reader = new CSVReader(new FileReader(args[0]), ';');
			int lineNumber = 0;
			CSVWriter writer = new CSVWriter(new FileWriter(args[0].substring(0, args[0].lastIndexOf("\\"))+"\\ResultContas.csv"), ';');
			String header [] = {"agencia", "conta", "saldo", "status"};
			writer.writeNext(header);
			processamentoDados(reader, lineNumber, writer);
			writer.close();
		}catch (Exception e) {
			//writer.close();
			e.printStackTrace();
		}
	}

	private static void processamentoDados(CSVReader reader, int lineNumber, CSVWriter writer) throws IOException {
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			lineNumber++;
			if (lineNumber == 1)
				continue;

			invocacaoReceita(writer, nextLine);
		}
	}

	private static void invocacaoReceita(CSVWriter writer, String[] nextLine) {
		try {
			Boolean result = ReceitaService.atualizarConta(nextLine[0], nextLine[1].replace("-", ""), Double.parseDouble(nextLine[2].replace(",", ".")),  nextLine[3]);
			String [] newnextLine = {nextLine[0], nextLine[1], nextLine[2], nextLine[3], result.toString()};  				
			writer.writeNext(newnextLine);

		} catch (Exception e) {
			String [] newnextLine = {nextLine[0], nextLine[1], nextLine[2], nextLine[3],"Falha"};  				
			writer.writeNext(newnextLine);
		}
	}

}
