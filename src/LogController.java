import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogController {
    public static Map<String, List<Transaction>> creatUserTransaction(List<File> fileList)  {
        Map<String, List<Transaction>> UserList = new HashMap<>();
        for (File file : fileList) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Transaction transaction = createTransaction(line);
                    writeTransaction(UserList, transaction);
                }
            } catch (IOException e){
                throw new RuntimeException(e);
            }


        }
        return UserList;
    }

    private static void writeTransaction(Map<String, List<Transaction>> UserList, Transaction transaction) {
        if (UserList.containsKey(transaction.getUser())) {
            UserList.get(transaction.getUser()).add(transaction);
        } else {
            List<Transaction> list = new ArrayList<>();
            list.add(transaction);
            UserList.put(transaction.getUser(), list);
        }
        if (transaction.getUserTarget() != null) {
            Transaction transaction2 = recreateTransaction(transaction);
            if (UserList.containsKey(transaction2.getUser())) {
                UserList.get(transaction2.getUser()).add(transaction2);
            } else {
                List<Transaction> list = new ArrayList<>();
                list.add(transaction2);
                UserList.put(transaction2.getUser(), list);
            }
        }
    }

    private static Transaction createTransaction(String line) {
        Transaction transaction = new Transaction(line);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        transaction.setLocalDateTime(LocalDateTime.parse(line.substring(1, 20), formatter));
        String[] split = line.substring(22).split(" ");
        transaction.setUser(split[0]);

        if (split.length == 3) {
            transaction.setOperationType(OperationType.WITHDREW);
            transaction.setAmount(new BigDecimal(split[2]));
        } else if (split.length == 4) {
            transaction.setOperationType(OperationType.BALANCE_INQUIRY);
            transaction.setAmount(new BigDecimal(split[3]));
        } else if (split.length == 5) {

            transaction.setAmount(new BigDecimal(split[2]));
            transaction.setUserTarget(split[4]);

            if (split[1].equals("transferred")) {
                transaction.setOperationType(OperationType.TRANSFERRED);

            } else {
                transaction.setOperationType(OperationType.RECEIVED);
            }
        }

        return transaction;
    }

    private static Transaction recreateTransaction(Transaction transaction) {
        String[] oldLine = transaction.getString().split(" ");
        String newLine = transaction.getString().substring(0, 22) + oldLine[6] + " recived " + oldLine[4] + " from " + oldLine[2];
        System.out.println(newLine);

        return createTransaction(newLine);
    }
}
