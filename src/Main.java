import java.io.File;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String InputDir = "LogsFile";
        String OutputDir = "LogsFile\\transactions_by_users";
        final File folder = new File(InputDir);
        List<File> fileList = new LogReader().listFilesForFolder(folder);
        Map<String,List<Transaction>> mapTransaction = LogController.creatUserTransaction(fileList);
        UserWriter userWriter = new UserWriter(OutputDir);
        userWriter.writeUsersLog(mapTransaction);
    }
}
