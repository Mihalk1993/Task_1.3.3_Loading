import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    private static String pathToSaves = "C:/Games/saveGames/";
    private static String zipSaves = "C:/Games/saveGames/saveGames.zip";

    public static void main(String[] args) {
        openZip(zipSaves, pathToSaves);
        GameProgress save1 = openProgress(pathToSaves);
        System.out.println(save1);
    }

    public static void openZip(String originalPath, String destinationPath) {
        try (FileInputStream fis = new FileInputStream(originalPath);
             ZipInputStream zin = new ZipInputStream(fis)) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(destinationPath + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameProgress openProgress(String pathToFile) {
        GameProgress gameProgress = null;
        List<File> files = new ArrayList<>();
        File dir = new File(pathToFile);
        if (dir.isDirectory()) {
            for (File item : Objects.requireNonNull(dir.listFiles())) {
                if (item.isFile() && !item.getName().contains(".zip")) {
                    files.add(item);
                }
            }
        }
        try (FileInputStream fis = new FileInputStream(pathToFile + files.get(1).getName());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gameProgress;
    }

}
