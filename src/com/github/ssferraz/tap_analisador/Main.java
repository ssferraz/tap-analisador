package com.github.ssferraz.tap_analisador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		// Cria scanner para ler a entrada do usuário
		Scanner sc = new Scanner(System.in);
		System.out.print("Digite o nome do diretório: ");

		// Obtém o caminho absoluto do diretório informado pelo usuário
		String path = Paths.get("").toAbsolutePath().toString() + File.separator;
		String directory = sc.nextLine();

		directory = path + directory;

		sc.close();

		// Verifica se o diretório informado pelo usuário existe
		if (!Files.exists(Paths.get(directory))) {
			System.out.println("O diretório fornecido não existe.");
			return;
		}

		// Verifica se o diretório informado pelo usuário é válido
		if (!Files.isDirectory(Paths.get(directory))) {
			System.out.println("O caminho fornecido não é um diretório válido.");
			return;
		}

		// Obtém a lista de arquivos .srt do diretório informado pelo usuário
		File[] files = new File(directory).listFiles((dir, name) -> name.endsWith(".srt"));

		if (files.length == 0) {
			System.out.println("Não há arquivos .srt no diretório fornecido.");
			return;
		}

		// Cria o diretório "resultados" se ele não existir
		Path results = Paths.get("resultados");
		if (!Files.exists(results)) {
			Files.createDirectory(results);
		}

		// Processa cada arquivo .srt encontrado no diretório
		Arrays.stream(files)
		.forEach(file -> {
			String filename = file.getName();

			try {
				// Lê o conteúdo do arquivo
				String content = Files.readString(Paths.get(file.getPath()));

				// Converte todo o texto para letras minúsculas e remove caracteres especiais
				String result = content.toLowerCase();
				result = result.replaceAll("\\b(-->)\\b|[<>]|[1234567890.:;?!@#$%&♪_'\\\",-]+", "");

				// Separa as palavras em um array e conta a frequência de cada uma
				String[] words = result.split("\\s+");
				Map<String, Integer> count = new HashMap<>();
				Arrays.stream(words)
				.forEach(word -> count.put(word, count.getOrDefault(word, 0) + 1));

				// Converte o resultado em um array de WordResult 

				WordResult[] finalResult = count.entrySet().stream()
						.map(entry -> new WordResult(entry.getKey(), entry.getValue()))
						.toArray(WordResult[]::new);

				// Ordena pelo número de ocorrências
				Arrays.sort(finalResult);

				// Converte o array para JSON
				String json = Arrays.toString(finalResult)
						.replace("[", "[\n\t")
						.replace(", ", ",\n\t")
						.replace("]", "\n]");

				// Escreve o resultado em um arquivo JSON
				FileWriter writer = new FileWriter("resultados/" + filename + ".json");
				writer.write(json);
				writer.close();
			} catch (IOException e) {
				// Imprime mensagem de erro caso ocorra uma exceção ao escrever o arquivo
				System.out.println(e.getMessage());
			}
		});
	}
}

class WordResult implements Comparable<WordResult> {
	private String word;
	private int frequency;

	public WordResult(String palavra, int frequencia) {
		this.word = palavra;
		this.frequency = frequencia;
	}

	// Compara dois objetos WordResult com base em sua frequência
	@Override
	public int compareTo(WordResult word) {
		return Integer.compare(word.frequency, this.frequency);
	}
	
	// Retorna uma string formatada como um objeto JSON com as propriedades "palavra" e "frequencia"
	@Override
	public String toString() {
		return String.format("{\n\t\"palavra\": \"%s\",\n\t\"frequencia\": %d\n}", word, frequency);
	}
}
