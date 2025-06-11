import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserWriter {
    private String outputDir;
    UserWriter(String outputDir) {
        this.outputDir = outputDir;
        new File(outputDir).mkdirs();
    }
    public void  writeUsersLog(Map<String,List<Transaction>> mapTransaction){
        for (String key : mapTransaction.keySet()) {
            writeUserLog(key,mapTransaction.get(key));
        }
    }
    private void writeUserLog(String username, List<Transaction> logs) {
        Collections.sort(logs); // Сортировка по времени
        String filename = outputDir + "/" + username + ".log";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Transaction log : logs) {
                writer.println(log.getString());
            }

            // Добавляем финальный баланс
//            writer.println(generateFinalBalanceLine(username, calculateFinalBalance(username, logs)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
