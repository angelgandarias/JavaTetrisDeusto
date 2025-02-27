import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import utils.ventanas.ventanaBitmap.VentanaGrafica;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//Ángel Gandarias Amadasun 2024
//Versión 1.1.3
//1.1 - 1.1.1 Cambios en Pieza y mainTetris1  (Junio y Agosto)
//1.1.2 Añadida ventana de error y corregido error de input que podia romper lectura highscores. (Agosto)
//1.1.3 Centrada ventana input nombre de usuario y corregido usuario null guardandose.  (Agosto)
public class Menu {
    private static VentanaGrafica ventanaMenu;
    static Font f = new Font(Font.DIALOG, Font.BOLD, 60);
    static Font f2 = new Font("DIALOG", Font.ROMAN_BASELINE, 20);
    static Font f3 = new Font("SERIF", Font.ITALIC, 15);

    //Utilizaré el objeto botón para crear interactuables en los que el usuario pueda hacer click
    //para seleccionar opciones en los menus.
    public static void main(String[] args) {
        ventanaMenu = new VentanaGrafica(600, 600, "Tetris Menu", Color.BLACK);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("logoMiTetris.jpg"));
        } catch (IOException e) {
        }
        ventanaMenu.getJFrame().setIconImage(img);
        
        Boton juego = new Boton(25, 300, "Jugar", ventanaMenu);
        Boton opciones = new Boton(225,300, "Opciones", ventanaMenu);
        Boton puntuaciones = new Boton(425, 300, "HighScores", ventanaMenu);
        Color color = Color.BLACK;
        ArrayList<HighScore>scores = cargarFichero("HighScores.csv");
        //Las piezas deberían estar ordenadas desde la última ejecución, pero las reordeno
        // por si el csv ha sido editado
        scores.sort(null);
        while (!ventanaMenu.estaCerrada()) {
            juego.dibujar();
            opciones.dibujar();
            puntuaciones.dibujar();
            ventanaMenu.dibujaTextoCentrado(300, 60, -8, 8,  "TETRIS 1.1.3", f, Color.DARK_GRAY);
            ventanaMenu.dibujaTextoCentrado(300, 60, 0, 0,  "TETRIS 1.1.3", f, Color.BLUE);
            ventanaMenu.dibujaTextoCentrado(450, 570, 0, 0,  "Programado por Ángel Gandarias Amadasun", f3, Color.DARK_GRAY);
            Point click = ventanaMenu.esperaAClick();
            if (juego.botonPresionado(click)) {
                ventanaMenu.acaba();
                int puntuacion = mainTetris1.main(color);
                VentanaGrafica gameOver = new VentanaGrafica(500, 500, "GAMEOVER", Color.BLACK);
                gameOver.dibujaTextoCentrado(250, 100, 0, 0, "GAME OVER", f, Color.YELLOW);
                gameOver.dibujaTextoCentrado(250, 400, 0, 0, "¡Tu puntuación ha sido de "+ String.valueOf(puntuacion)+ " puntos!", f2, Color.WHITE);
                if (scores.size() >0) {
                    int maxScore = scores.get(0).getPoints();
                if (puntuacion > maxScore){
                    gameOver.dibujaTextoCentrado(250, 200, 0, 0, "¡Nueva puntuación máxima!", f2, Color.WHITE);
                }else{
                    gameOver.dibujaTextoCentrado(250, 200, 0, 0, "Puntuación a superar:"+ maxScore, f2, Color.WHITE);
                }
                }else{
                   gameOver.dibujaTextoCentrado(250, 200, 0, 0, "¡Nueva puntuación máxima!", f2, Color.WHITE);
                }
                Boton guardar = new Boton(175, 250, "Guardar", gameOver);
                guardar.dibujar();
                Point clickEnd = gameOver.esperaAClick();
                if (guardar.botonPresionado(clickEnd)) {
                    String user = "";
                    boolean validUser = false;
                    while (!validUser) {
                        user = gameOver.leeTexto(125, 400, 250, 100, "Introduzca un nombre de usuario", f2, Color.BLACK);
                        if (user==null) {
                            gameOver.acaba();
                            break;
                        }
                        if (user.contains(";")){
                            mostrarError("Nombre no puede incluir ';'");
                        }else if (user.length()>20) {
                            mostrarError("Nombre no puede tener más de 20 caracteres");
                        }else{
                            validUser = true;
                        }
                    }
                    if (user==null) {
                        break;
                    }
                    HighScore gameScore = new HighScore(puntuacion,user);
                    //Compruebo si el usuario ya existe para sobreescribir su puntuación
                    boolean userFound = false;
                    for (HighScore highScore : scores) {
                        if (highScore.getUser().equals(user)) {
                            highScore.setPoints(gameScore.getPoints());
                            userFound = true;
                            break;
                        }
                    }
                    //Si el usuario no existe, lo añado junto con la puntuación
                    if (!userFound) {
                        scores.add(gameScore);
                    }
                        scores.sort(null);
                        guardarFichero(scores);
                        System.out.println(scores);
                        gameOver.acaba();
                    }

                }
            if (opciones.botonPresionado(click)) {
                VentanaGrafica options = new VentanaGrafica(500, 500, "Opciones", color);
                Boton modo = new Boton(50, 250, "Modo", options);
                Boton instrucciones = new Boton(300, 250, "Cómo Jugar", options);
                while (!options.estaCerrada()) {
                    options.borra();
                    if (color.equals(Color.BLACK)) {
                        options.dibujaTextoCentrado(modo.getPosX()+75, 200, 0, 0, "Oscuro", f2, Color.WHITE);
                    }else{
                        options.dibujaTextoCentrado(modo.getPosX()+75, 200, 0, 0, "Claro", f2, Color.BLACK);
                    }
                    modo.dibujar();
                    instrucciones.dibujar();
                    Point click2 = options.esperaAClick();
                    if (modo.botonPresionado(click2)){ //Dependiendo del estado actual, cambio el color de fondo.
                        if(color.equals(Color.BLACK)){
                            color = Color.WHITE;
                        }else{
                            color = Color.BLACK;
                        }
                    }
                    if (instrucciones.botonPresionado(click2)) {
                        options.borra();
                        //El color del texto depende de el modo (oscuro o claro)
                        Color color2 = Color.BLACK;
                        if (color.equals(Color.BLACK)){
                            color2 = Color.WHITE;
                        }//Dibujo cada linea de las instrucciones una debajo de la otra
                        options.dibujaTextoCentrado(options.getAnchura()/2, 50, 0, 0, "Instrucciones", f, Color.BLUE);
                        options.dibujaTexto(25, 125, "*Flechas derecha, izquierda y abajo para mover pieza", f3, color2);
                        options.dibujaTexto(25, 150, "*Flecha arriba para rotar pieza", f3, color2);
                        options.dibujaTexto(25, 175, "*Control coloca las piezas directamente", f3, color2);
                        options.dibujaTexto(25, 200, "*ALT mete y saca piezas en del espacio HOLD (una por colocación)", f3, color2);
                        options.dibujaTexto(25, 225, "*Coloca las piezas para ganar puntos", f3, color2);
                        options.dibujaTexto(25, 250, "*Si llenas una linea, se elimina y recibes puntuación extra", f3, color2);
                        options.dibujaTexto(25, 275, "*La velocidad aumenta dependiendo de la puntuación", f3, color2);
                        options.dibujaTexto(25, 300, "*Tu puntuación se guardará SÓLO si pulsas el boton guardar", f3, color2);
                        options.dibujaTexto(25, 325, " al acabar de jugar", f3, color2);
                        options.dibujaTextoCentrado(options.getAnchura()/2, 375,0,0, "¡Gana la máxima puntuación posible", f2, color2);
                        options.dibujaTextoCentrado(options.getAnchura()/2, 400,0,0, "y entra en el top 5!", f2, color2);
                        options.dibujaTexto(25, 450, "Toca en cualquier punto de la pantalla para salir", f3, Color.GRAY);
                        //Espero un click para quitar las instrucciones
                        options.esperaAClick();
                    }
                    options.setColorFondo(color);
                }
            }
            if (puntuaciones.botonPresionado(click)) {
                VentanaGrafica highScores = new VentanaGrafica(500, 500, "HighScores", color);
                highScores.dibujaRect(100, 120, 300, 260, 3, Color.CYAN);
                highScores.dibujaTextoCentrado(250, 50, 0, 0, "HIGHSCORES", f, Color.RED);
                if (scores.size() > 0) {
                    if (scores.size()<5) {
                        for (int i = 0; i < scores.size(); i++) { //Muestro todas las puntuaciones.
                            highScores.dibujaTextoCentrado(250, 150+50*i, 0, 0,scores.get(i).toString(), f2, Color.YELLOW);
                        
                        }
                    }else{
                        for (int i = 0; i < 5; i++) { //Muestro las cinco puntuaciones más altas.
                            highScores.dibujaTextoCentrado(250, 50, 0, 0, "HIGHSCORES", f, Color.RED);
                            highScores.dibujaRect(100, 120, 300, 260, 3, Color.CYAN);
                            highScores.dibujaTextoCentrado(250, 150+50*i, 0, 0,scores.get(i).toString(), f2, Color.YELLOW);
                        }
                    }
                    //Si no hay puntuaciones, aviso al usuario de que tiene que jugar para guardar puntuaciones.
                }else{
                    highScores.dibujaTextoCentrado(250, 200, 0, 0,"Todavía no hay puntuaciones.", f2, Color.YELLOW);
                    highScores.dibujaTextoCentrado(250, 225, 0, 0,"Una vez juegues una partida", f2, Color.YELLOW);
                    highScores.dibujaTextoCentrado(250, 250, 0, 0,"y guardes la puntuación,", f2, Color.YELLOW);
                    highScores.dibujaTextoCentrado(250, 275, 0, 0,"aparecerá aquí", f2, Color.YELLOW);
                }
            }
        }
    }
    /**
     * Carga una lista de HighScores con los datos de un archivo csv
     * @param score el nombre del archivo csv
     * @return lista con highscores
     */
    public static ArrayList<HighScore> cargarFichero(String score){
        ArrayList<HighScore> scores = new ArrayList<HighScore>();
        try {
            Scanner scanner = new Scanner(new FileInputStream(score));
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                try {
                    HighScore hs = HighScore.scoreFromCSV(linea);
                    scores.add(hs); 
                } catch (NumberFormatException|NoSuchElementException e) {
                    String error = new String("Error: El fichero está vacio o tiene un formato erroneo");
                    System.out.println(error);
                    mostrarError(error);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            String error = new String("Error: No existe el fichero 'highScores'. Se generará automaticamente de ser necesario");
            System.out.println(error);
            mostrarError(error);
        }
        return scores;
    }
    /**
     * Guarda las puntuaciones de una lista de Highscores en un fichero csv
     * @param scores el nombre del fichero csv
     */
    public static void guardarFichero(ArrayList<HighScore> scores){
        try {
            PrintStream ps = new PrintStream("HighScores.csv");
            for (HighScore highScore : scores) {
                ps.println(highScore.highScoreToCSV());
            }
            ps.close();
        } catch (FileNotFoundException e) {
        }
    }
    /**
     * Muestra un mensaje de error al usuario en una ventana 300x200 que desaparece a los
     * pocos segundos
     * @param error Mensaje de error a mostrar al usuario
     */
    public static void mostrarError(String error){
        VentanaGrafica vError = new VentanaGrafica(300, 200, "Error", Color.BLACK);
        //Dibujo simbolo error (Circulo y exclamación rojos)
        vError.dibujaCirculo(150, 100, 92, 8, Color.RED, Color.BLACK);
        vError.dibujaRect(140, 32, 20, 100, 1, Color.RED,Color.RED);
        vError.dibujaRect(140, 146, 20, 20, 1, Color.RED,Color.RED);
        vError.repaint();
        vError.espera(1500);
        vError.borra();
        //Muestro mensaje de error ajustado para que quepa en la ventana
        int errorLength = error.length();
        errorLength = errorLength/28;
        for (int i = 0; i <= errorLength; i++) {
            if (i != errorLength) {
                vError.dibujaTexto(10, 100+i*25-10*errorLength, error.substring(i*28, i*28+28), f2, Color.RED);
            }else{
                vError.dibujaTexto(10, 100+i*25-10*errorLength, error.substring(i*28, error.length()), f2, Color.RED);
            }
        }
        vError.espera(1000+errorLength*1000);
        vError.acaba();
    }
    
}
