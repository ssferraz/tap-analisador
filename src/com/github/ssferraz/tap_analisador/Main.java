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
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o nome do diretório: ");
        
        String path = Paths.get("").toAbsolutePath().toString() + File.separator;
        String directory = sc.nextLine();
        
        directory = path + directory;

        sc.close();

        if (!Files.exists(Paths.get(directory))) {
            System.out.println("O diretório fornecido não existe.");
            return;
        }
        
        if (!Files.isDirectory(Paths.get(directory))) {
            System.out.println("O caminho fornecido não é um diretório válido.");
            return;
        }

        
        File[] files = new File(directory).listFiles((dir, name) -> name.endsWith(".srt"));

        if (files.length == 0) {
            System.out.println("Não há arquivos .srt no diretório fornecido.");
            return;
        }

        Path results = Paths.get("resultados");
        if (!Files.exists(results)) {
            Files.createDirectory(results);
        }

        
        Arrays.stream(files)
        .forEach(file -> {
            String filename = file.getName();

            try {
                String content = Files.readString(Paths.get(file.getPath()));

                String result = content.toLowerCase();

                result = result.replaceAll("\\b(-->)\\b|[<>]|[1234567890.:;?!@#$%&♪_'\\\",-]+", "");
                
                String[] words = result.split("\\s+");

                Map<String, Integer> count = new HashMap<>();
                Arrays.stream(words)
                    .forEach(word -> count.put(word, count.getOrDefault(word, 0) + 1));

                WordResult[] finalResult = count.entrySet().stream()
                    .map(entry -> new WordResult(entry.getKey(), entry.getValue()))
                    .toArray(WordResult[]::new);

                Arrays.sort(finalResult);

                String json = Arrays.toString(finalResult)
                    .replace("[", "[\n\t")
                    .replace(", ", ",\n\t")
                    .replace("]", "\n]");

                FileWriter writer = new FileWriter("resultados/" + filename + ".json");
                writer.write(json);
                writer.close();
            } catch (IOException e) {
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

    @Override
    public int compareTo(WordResult word) {
        return Integer.compare(word.frequency, this.frequency);
    }

    @Override
    public String toString() {
        return String.format("{\n\t\"palavra\": \"%s\",\n\t\"frequencia\": %d\n}", word, frequency);
    }
}
