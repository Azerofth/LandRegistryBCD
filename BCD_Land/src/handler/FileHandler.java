package handler;

// .txt: user, landrec, transrec, landinfo
// incl read, add, delete, modify

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

//    public static <T> void deleteObject(T itemToDelete, String fileName) {
//        List<T> dataList = readData(fileName);
//        dataList.remove(itemToDelete);
//        System.out.println(itemToDelete);
//        System.out.println(dataList);
//        writeData(dataList, fileName);
//    }

//    public static <T> void updateObject(T oldItem, T newItem, String fileName) {
//        List<T> dataList = readData(fileName);
//        if (dataList.remove(oldItem)) {
//            dataList.add(newItem);
//            writeData(dataList, fileName);
//        }
//    }
//    
//    public static <T> void updateObject(T oldItem, T newItem, String fileName) {
//        List<T> dataList = readData(fileName);
//
//        // Find the index of the old item in the list
//        int index = dataList.indexOf(oldItem);
//
//        if (index != -1) {
//            // Replace the old item with the new item at the same index
//            dataList.set(index, newItem);
//            writeData(dataList, fileName);
//            System.out.println("Object updated successfully.");
//        } else {
//            System.out.println("Object not found.");
//        }
//    }

}
