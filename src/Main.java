import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String inputDir = "Files/LogsFile";
        String outputDir = "Files/transactions_by_users";

        File folder = new File(inputDir);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Input directory does not exist or is not a directory: " + inputDir);
            return;
        }

        LogReader logReader = new LogReader();
        List<File> fileList = logReader.listFilesRecursively(folder);

        try {
            Map<String, List<Transaction>> mapTransaction = LogController.createUserTransactions(fileList);
            UserWriter userWriter = new UserWriter(outputDir);
            userWriter.writeUsersLog(mapTransaction);
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}