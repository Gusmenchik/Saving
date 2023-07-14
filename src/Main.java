import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        // Создание экземпляров GameProgress
        GameProgress progress1 = new GameProgress(100, 3, 10, 156.8);
        GameProgress progress2 = new GameProgress(80, 5, 15, 254.2);
        GameProgress progress3 = new GameProgress(95, 4, 13, 198.5);

        // Сохранение сериализованных объектов GameProgress в папку savegames
        saveGame("D:\\Курсы программирования\\Games\\savegames\\save1.dat", progress1);
        saveGame("D:\\Курсы программирования\\Games\\savegames\\save2.dat", progress2);
        saveGame("D:\\Курсы программирования\\Games\\savegames\\save3.dat", progress3);

        // Запаковка файлов сохранений в архив zip
        List<String> filesToZip = new ArrayList<>();
        filesToZip.add("D:\\Курсы программирования\\Games\\savegames\\save1.dat");
        filesToZip.add("D:\\Курсы программирования\\Games\\savegames\\save2.dat");
        filesToZip.add("D:\\Курсы программирования\\Games\\savegames\\save3.dat");
        zipFiles("D:\\Курсы программирования\\Games\\savegames\\zip.zip", filesToZip);

        // Удаление файлов сохранений, не лежащих в архиве
        deleteUnzippedFiles(filesToZip);
    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Игровой прогресс сохранен: " + filePath);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить игровой прогресс: " + e.getMessage());
        }
    }

    public static void zipFiles(String zipFilePath, List<String> filesToZip) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            byte[] buffer = new byte[1024];
            for (String filePath : filesToZip) {
                File file = new File(filePath);
                if (!file.exists()) {
                    System.out.println("Файл не найден: " + filePath);
                    continue;
                }
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                fis.close();
                zos.closeEntry();
                System.out.println("Файл добавлен в архив: " + filePath);
            }
            System.out.println("Архив создан: " + zipFilePath);
        } catch (IOException e) {
            System.out.println("Не удалось создать архив: " + e.getMessage());
        }
    }

    public static void deleteUnzippedFiles(List<String> filesToKeep) {
        File savegamesFolder = new File("D:\\Курсы программирования\\Games\\savegames");
        File[] files = savegamesFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!filesToKeep.contains(file.getAbsolutePath())) {
                    if (file.delete()) {
                        System.out.println("Файл удален: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Не удалось удалить файл: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }
}

