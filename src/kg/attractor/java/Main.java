package kg.attractor.java;

import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.lesson45.Lesson45Server;
import kg.attractor.java.lesson46.Lesson46Server;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Lesson46Server("localhost", 8089
            ).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
