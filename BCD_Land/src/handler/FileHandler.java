package handler;

// .txt: user, landrec, transrec, landinfo
// incl read, add, delete, modify

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class FileHandler {
    @SuppressWarnings("unchecked")
    public static <T> List<T> readData(String fileName) {
        List<T> dataList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                T obj = (T) ois.readObject();
                dataList.add(obj);
            }
        } catch (EOFException e) {
            // End of file reached
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static <T> void writeData(List<T> dataList, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            for (T obj : dataList) {
                oos.writeObject(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void addObject(T newItem, String fileName) {
        List<T> dataList = readData(fileName);
        dataList.add(newItem);
        writeData(dataList, fileName);
    }
}
