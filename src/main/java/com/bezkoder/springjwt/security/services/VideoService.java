package com.bezkoder.springjwt.security.services;

import com.bezkoder.springjwt.controllers.TestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class VideoService {


    Logger logger = LoggerFactory.getLogger(TestController.class);

    public String blurVideo(String video, String model){
        String scriptPath = "script.py";
        String modelChoice = model;
        String videoPath = video;
        String command = "python " + scriptPath + " " + modelChoice + " \"" + videoPath + "\"";
        logger.info("COMMAND PYTHON:  "+ command);

        try {
            // Créer un processus pour exécuter le script Python
            Process process = Runtime.getRuntime().exec(command);

            // Lire la sortie du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Attendre que le processus se termine
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Script Python exécuté avec succès.");
            } else {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
            return  null;
    }
}
