package app.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FilesMergeSort {

    private static final int BUFFER_SIZE = 1000;

    private final String sortMode;
    private final String typeMode;
    private final Path outputFile;
    private final List<Path> inputFiles;

    public FilesMergeSort(String sortMode, String typeMode, Path outputFile, List<Path> inputFiles) {
        this.sortMode = sortMode;
        this.typeMode = typeMode;
        this.outputFile = outputFile;
        this.inputFiles = inputFiles;
    }

    public void execute() throws IOException {
        Files.deleteIfExists(outputFile);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)))) {
            List<BufferedReader> readers = new ArrayList<>();
            for (Path inputFile : inputFiles) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(inputFile)));
                readers.add(reader);
            }

            while (readers.size() > 0) {
                boolean needReloadAfterReaderClose = false;
                List<String> currentValues = new ArrayList<>();
                List<BufferedReader> readersCopied = new ArrayList<>(readers);
                for (BufferedReader reader : readersCopied) {
                    reader.mark(BUFFER_SIZE);
                    String currentLine = reader.readLine();
                    reader.reset();
                    if (currentLine == null) {
                        reader.close();
                        readers.remove(reader);
                        needReloadAfterReaderClose = true;
                        break;
                    }
                    currentValues.add(currentLine);
                }
                if (needReloadAfterReaderClose) {
                    continue;
                }
                String target = getTargetElement(currentValues, typeMode, sortMode);
                writer.write(target);
                writer.newLine();
                readers.get(currentValues.indexOf(target)).readLine();
            }
        }
    }

    public String getTargetElement(List<String> list, String typeMode, String sortMode) {
        String target;
        if (typeMode.equals("s")) {
            if (sortMode.equals("a")) {
                target = list.stream().min(String::compareTo).get();
            } else {
                target = list.stream().max(String::compareTo).get();
            }
        } else {
            if (sortMode.equals("a")) {
                target = list.stream().map(Integer::valueOf).min(Integer::compareTo).get().toString();
            } else {
                target = list.stream().map(Integer::valueOf).max(Integer::compareTo).get().toString();
            }
        }
        return target;
    }
}