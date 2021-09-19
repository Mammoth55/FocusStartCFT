package app;

import app.model.ErrorCode;
import app.service.FilesMergeSort;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private final static String SORT_PATTERN = "-(a|d)";
    private final static String TYPE_PATTERN = "-(s|i)";
    private final static String FILENAME_PATTERN = "[a-zA-Zа-яёА-ЯЁ0-9\\\\\\/\\._-]+";

    private static String sortMode = "a";
    private static String typeMode;
    private static Path outputFile;
    private static List<Path> inputFiles = new ArrayList<>();

    public static void main(String[] args) {
        try {
            initFromArgs(new ArrayList<>(Arrays.asList(args)));
            FilesMergeSort filesMergeSort = new FilesMergeSort(sortMode, typeMode, outputFile, inputFiles);
            filesMergeSort.execute();
        } catch (Exception e) {
            System.out.println("Критическая ошибка : " + e.getMessage());
        }
    }

    private static void initFromArgs(List<String> args) throws Exception {
        if (args.size() < 3) {
            throw new Exception(ErrorCode.MISSING_NECESSARY_ARGUMENT);
        }

        if (args.get(0).matches(SORT_PATTERN)) {
            if (args.size() == 3) {
                throw new Exception(ErrorCode.MISSING_NECESSARY_ARGUMENT);
            }
            sortMode = args.get(0).substring(1);
            args.remove(0);
        } else {
            if (args.get(1).matches(TYPE_PATTERN)) {
                System.out.println(ErrorCode.WRONG_SORT_MODE + " : '" + args.get(0) + "'");
                args.remove(0);
            }
        }

        if (!args.get(0).matches(TYPE_PATTERN)) {
            throw new Exception(ErrorCode.WRONG_TYPE);
        }
        typeMode = args.get(0).substring(1);

        if (!args.get(1).matches(FILENAME_PATTERN)) {
            throw new Exception(ErrorCode.WRONG_FILE_NAME);
        }
        outputFile = Paths.get(args.get(1));

        for (int index = 2; index < args.size(); index++) {
            if (!args.get(index).matches(FILENAME_PATTERN)) {
                System.out.println(ErrorCode.WRONG_FILE_NAME + " : '" + args.get(index) + "'");
            } else {
                inputFiles.add(Paths.get(args.get(index)));
            }
        }
        if (inputFiles.size() == 0) {
            throw new Exception(ErrorCode.WRONG_FILE_NAME);
        }
    }
}
