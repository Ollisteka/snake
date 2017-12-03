package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Serialization {

  public static void serialize(int width, int height, Level level) throws IOException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
    String nowAsISO = dateFormat.format(new Date());
    String name = "Level_" + nowAsISO;
    FileOutputStream fos = new FileOutputStream(name + ".txt");
    String maze = convertToReadableFormat(width, height, level);
    Writer writer = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
    writer.write(maze);
    writer.close();
  }

  public static Level deserialize() {
    ArrayList<String> list = findOutFiles();
    Pattern p = Pattern.compile("Level_\\d\\d\\d\\d-\\d\\d-\\d\\d_\\d\\d-\\d\\d\\.txt");
    String level = "A";
    for (String name : list) {
      Matcher m = p.matcher(name);
      if (m.lookingAt()) {
        if (name.compareTo(level) > 0) {
          level = name;
        }
      }
    }
    ArrayList<String> lines = readLines(level);
    return convertIntoLevel(lines, "random");

  }

  public static Level deserialize(String filename) {
    ArrayList<String> lines = readLines(filename);
    return convertIntoLevel(lines, filename);
  }

  private static ArrayList<String> readLines(String filePath) {
    ArrayList<String> lines = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String sCurrentLine;

      while ((sCurrentLine = br.readLine()) != null) {
        lines.add(sCurrentLine);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lines;
  }

  private static String convertToReadableFormat(int width, int height, Level level) {
    String str = new String();
    Set<Wall> maze = level.getMazeLocations();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (maze.contains(new Wall(j, i))) {
          str += "#";
        } else {
          str += ".";
        }
      }
      str += "\r\n";
    }
    return str;
  }

  private static Level convertIntoLevel(ArrayList<String> lines, String levelName) {
    int i = 0;
    Set<Wall> maze = new HashSet<>();
    Set<Entrance> entrances = new HashSet<>();
    int width = 0;
    for (String line : lines) {
      for (int j = 0; j < line.length(); j++) {
        if (line.charAt(j) == '#') {
          maze.add(new Wall(j, i));
        }
        if (Character.isLowerCase(line.charAt(j))) {
          //вход\выход из уровня, открытый
          entrances.add(new Entrance(j, i, Character.toLowerCase(line.charAt(j)), levelName, true));
        }
        if (Character.isUpperCase(line.charAt(j))) {
          //вход\выход из уровня, закрыт
          entrances
              .add(new Entrance(j, i, Character.toLowerCase(line.charAt(j)), levelName, false));
        }
        width = j;
      }
      i++;
    }
    Config config = new Config(width + 1, i, 25, 250);
    Level level = new Level(config, levelName);
    level.setMazeLocations(maze);
    level.setEntrances(entrances);
    return level;
  }

  private static ArrayList<String> findOutFiles() {
    File dir = new File(".");
    File[] filesList = dir.listFiles();
    ArrayList fileL = new ArrayList();
    for (File file : filesList) {
      if (file.isFile()) {
        fileL.add(file.getName());
      }
    }
    return fileL;
  }
}
