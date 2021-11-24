package utils;

import model.dto.RecordDetails;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFileReader {
    final private static String DATE_FORMAT = "yyyy-MM-dd";

    public static List<RecordDetails> filesReader(String folderName) throws IOException {

        String absolutePath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
        String pathWithDir = absolutePath.concat("/Desktop/").concat(folderName);
        Path path = Paths.get(pathWithDir);

        if (Files.notExists(path)) {
            throw new NotDirectoryException(String.format("There is no folder with name \"%s\" on the Desktop. Try again!", folderName));
        }

        if (isEmpty(path)) {
            throw new NoSuchFileException(String.format("Create file with needed data in folder with name \"%s\" on the Desktop. Try again!", folderName));
        }

        List<RecordDetails> fileRecords = new ArrayList<>();
        File folder = path.toFile();
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {

                    fileRecords.addAll(fileReader(file.getAbsolutePath()));

                }
            }
        }
        return fileRecords;
    }

    public static List<RecordDetails> fileReader(String filePath) throws IOException {
        List<RecordDetails> fileRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String record;

            while ((record = br.readLine()) != null) {

                String[] data = record.split("\\s*,\\s*");

                if (data.length < 4) {
                    System.out.printf("The record %s can not be save. There is errors in it!\n", record);
                    continue;
                }

                Integer empId = parseIdNumber(data[0]);
                Integer projectId = parseIdNumber(data[1]);
                LocalDate dateFrom = parseDateFormat(data[2]);
                LocalDate dateTo = parseDateFormat(data[3]);


                if (empId == null || projectId == null || dateFrom == null || dateTo == null) {
                    System.out.printf("The record %s can not be save. There is errors in it!\n", record);
                } else {
                    if (dateFrom.isAfter(dateTo)) {
                        System.out.printf("The record %s can not be save. The dateFrom is after the dateTo!\n", record);
                        continue;
                    }

                    fileRecords.add(new RecordDetails(empId, projectId, dateFrom, dateTo));

                }
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File is not found!\n Please add text file with records of employees in format: EmpID/number/, ProjectID/number/, DateFrom/yyyy-MM-dd/, DateTo/yyyy-MM-dd/.\n You must give name of the file \"data.txt\" and save it into directory src/main/resources/ of the project.\n After that you can Run the project again.");
        } catch (IOException e) {
            throw new IOException("File read error!");
        }

        return fileRecords;
    }

    private static Integer parseIdNumber(String inputNumber) {
        try {
            return Integer.parseInt(inputNumber);
        } catch (NumberFormatException e) {
            System.out.printf("The format of the id number \"%s\" is broken!\n", inputNumber);
            return null;
        }
    }

    private static LocalDate parseDateFormat(String inputDate) {
        LocalDate correctDate = null;
        if (inputDate.toUpperCase().equals("NULL")) {
            correctDate = LocalDate.now();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            try {
                correctDate = LocalDate.parse(inputDate, formatter);
            } catch (DateTimeParseException e) {
                System.out.printf("The format of the date \"%s\" is broken!\n", inputDate);
            }
        }

        return correctDate;
    }


    private static boolean isEmpty(Path path) {
        return path.toFile().listFiles().length == 0;
    }


}
