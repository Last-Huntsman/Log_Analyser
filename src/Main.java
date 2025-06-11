import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputDir = "Files/LogsFile";
        String outputDir = "Files/transactions_by_users";

        File folder = new File(inputDir);
        List<File> fileList = new LogReader().listFilesForFolder(folder);
        Map<String, List<Transaction>> mapTransaction = LogController.creatUserTransaction(fileList);

        UserWriter userWriter = new UserWriter(outputDir);
        userWriter.writeUsersLog(mapTransaction);
    }
}
