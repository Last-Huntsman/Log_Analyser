import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class UserWriter {
    private String outputDir;
    public UserWriter(String outputDir) throws IOException {
        this.outputDir = outputDir;
        Files.createDirectories(Paths.get(outputDir)); // Создаём чистую папку
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

