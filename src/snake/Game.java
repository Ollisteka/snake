package snake;


import javax.swing.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game extends JFrame{

    public Game() {
        setTitle("Snake");
    }

    public static void main(String[] args) {
        new Menu().setVisible(true);
    }


    public static void serialize(Level maze) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String nowAsISO = dateFormat.format(new Date());
        String name = "Level_" + nowAsISO;
        FileOutputStream fos = new FileOutputStream(name + ".out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(maze);  // serializatin
        oos.flush();
        oos.close();
    }

    public Level deserialize() throws IOException, ClassNotFoundException {
        ArrayList<String> list = findOutFiles();
        Pattern p = Pattern.compile("Level_\\d\\d\\d\\d-\\d\\d-\\d\\d_\\d\\d-\\d\\d\\.out");
        String level = "A";
        for (String name : list) {
            Matcher m = p.matcher(name);
            if (m.lookingAt())
                if (name.compareTo(level) > 0)
                    level = name;
        }
        FileInputStream fis = new FileInputStream(level);
        try (ObjectInputStream oin = new ObjectInputStream(fis)) {
            return (Level) oin.readObject();
        }
    }

    public ArrayList findOutFiles(){
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
