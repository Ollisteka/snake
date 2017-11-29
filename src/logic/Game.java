package logic;


import GUI.MenuWindow;
import lombok.Getter;
import lombok.Setter;

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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

  @Setter
  @Getter
  private int score;
  @Getter
  private Config config;
  @Getter
  @Setter
  private Map<Integer,Entrance> closedEntrances;
  @Getter
  private ArrayList<Level> levels;

  public Game(Config config) {
    score = 0;
    this.config = config;
    closedEntrances = new HashMap<>();
    levels = new ArrayList<>();
  }

  public Game(Config config, ArrayList<Level> levels) {
    score = 0;
    this.config = config;
    closedEntrances = getClosedEntrances(levels);
    this.levels = levels;
  }

  public static void main(String[] args) {
    new MenuWindow().setVisible(true);
  }

  public void addScore(){
    setScore(getScore() + 10);
    tryToOpenEntrance();
  }

  public void tryToOpenEntrance() {
    Integer score = getScore();
    if (getClosedEntrances().containsKey(score)) {
      Entrance openingEntrance = getClosedEntrances().get(score);
      opened: for (Level level : levels) {
                for (Entrance entrance : level.getEntrances())
                  if (entrance == openingEntrance){
                    entrance.setOpen(true);
                    break opened;
                  }
                }
      getClosedEntrances().remove(score);
    }
  }

  /**
   * Найти все закрытые входы в игре (на всех уровнях) и поставить им в соответствие очки,
   * котовые нужно набрать для открытия двери
   *
   * @param levels уровни, которые существуют в этой игре
   * @return словарик, где ключь - это очки, а значение - "вход"/дверь уровня
   */
  private Map<Integer,Entrance> getClosedEntrances(ArrayList<Level> levels) {
    Map<Integer,Entrance> closedEntrances = new HashMap<>();
    Integer count = 50;
    for (Level level : levels) {
      for (Entrance entrance : level.getEntrances()) {
        if (!entrance.isOpen()) {
          closedEntrances.put(count, entrance);
          count += 50;
        }
      }
    }

    return closedEntrances;
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

  public void serialize(int width, int height, Level level) throws IOException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
    String nowAsISO = dateFormat.format(new Date());
    String name = "Level_" + nowAsISO;
    FileOutputStream fos = new FileOutputStream(name + ".txt");
    String maze = convertToReadableFormat(width, height, level);
    Writer writer = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
    writer.write(maze);
    writer.close();
  }

  public String convertToReadableFormat(int width, int height, Level level) {
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

  public static Level deserialize() throws IOException, ClassNotFoundException {
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

  public static Level deserialize(String filename) throws IOException, ClassNotFoundException {
    ArrayList<String> lines = readLines(filename);
    return convertIntoLevel(lines, filename);
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
          entrances.add(new Entrance(j, i, Character.toLowerCase(line.charAt(j)), levelName, false));
        }
        width = j;
      }
      i++;
    }
    Config config = new Config(width, i, 25, 250);
    Level level = new Level(config, levelName);
    level.setMazeLocations(maze);
    level.setEntrances(entrances);
    return level;
  }

  public static ArrayList<String> findOutFiles() {
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
