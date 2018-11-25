package oop.Level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

abstract class LevelLoader {

    static List<String> load(InputStream stream) {
        List<String> strings = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            bufferedReader.readLine();
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                strings.add(string);
            }
        } catch (Exception e) {
            System.out.println("No more level");
            return null;
        }
        return strings;
    }
}
