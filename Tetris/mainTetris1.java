import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import utils.ventanas.ventanaBitmap.VentanaGrafica;
//Ángel Gandarias Amadasun 2024
//Versión 1.1.3
//1.1 Añadido boton de pausa 'Esc'  (Junio)
//1.1.1 Eliminada posibilidad de cambiar tamaño ventana y añadido icono personalizado  (Agosto)
//1.1.2 - 1.1.3 Cambios en Menu  (Agosto)

public class mainTetris1 {

    /** Ancho de la ventana del juego
     */
    public static final int ANCHO = 800;
    /** Alto de la ventana del juego
     */
    public static final int ALTO = 805;
    
    /** Ventana del juego
     */
    public static VentanaGrafica ventana;
    /** Tiempo de espera entre fotogramas
     */
    public static int MSPERFRAME = 40;
    /** Lista de piezas en pantalla
     */
    private static ArrayList<Pieza> listaPiezas;
    /** Tamaño de cada bloque en pixels
     */
    public static int TAMANOBLOQUE = 30;
    /**El desfase que es necesario aplicar en los calculos de posición X del tablero
    */
    public static int DESFASEX = 20;
    /**El desfase que es necesario aplicar en los calculos de posición Y del tablero
    */
    public static int DESFASEY = 140; 

    private static Random random = new Random();
    /** Pieza en movimiento
     */
    private static Pieza piezaEnCurso;
    /** Siguiente pieza en movimiento
     */
    private static Pieza piezaSiguiente;
    /**Pieza guardada en Hold
     */
    private static Pieza piezaHold;
    /**Indica si el botón Hold está disponible
     */
    private static boolean holding;
    /**Almacena la puntuación
     */
    private static int puntuacion = 0; 
    
    public static int main(Color color) {
        ventana = new VentanaGrafica(ANCHO,ALTO, "Tetris",color);
        ventana.getJFrame().setResizable(false);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("logoMiTetris.jpg"));
        } catch (IOException e) {
        }
        ventana.getJFrame().setIconImage(img);
        ventana.espera(500); //Para que el juego no empiece en cuanto se presiona el boton
        Font f = new Font("ARIAL", Font.ROMAN_BASELINE, 15);
        Font f2 = new Font("ARIAL", Font.ROMAN_BASELINE, 30);
        listaPiezas = new ArrayList<Pieza>(); //Voy a guardar todas las piezas en esta lista
        //Creo la primera pieza y la coloco dentro del juego
        creapiezaSiguiente();
        piezaEnCurso = piezaSiguiente;
        piezaHold = null;
        piezaEnCurso.setPosX(130);
        piezaEnCurso.setPosY(DESFASEY);
        creapiezaSiguiente();
        listaPiezas.add(piezaEnCurso);
        ventana.repaint();
        int[][] grid = crearGrid();
        Color colorInterfaz = Color.WHITE;
        int contSpeed = 0;// Utilizo este contador para manejar la velocidad de movimiento
        int contInput = 0;//Utilizo este contador para controlar la velocidad con la que se aplica el input4
        int nivel = 0; //La velocidad dependerá del nivel
        if (color.equals(Color.WHITE)) {
            colorInterfaz = Color.BLACK;
        }
        //Inicio el bucle de juego
        while (!ventana.estaCerrada()){
            if (holdPieza(holding)){
                holding = true;
                if (piezaHold == null) {
                    piezaHold = piezaEnCurso;
                    piezaEnCurso = null;   
                }else{
                Pieza almacen = piezaHold;
                piezaHold = piezaEnCurso;
                piezaEnCurso = almacen;
                }
            }
            if (piezaEnCurso == null){
                piezaEnCurso = piezaSiguiente;
                piezaEnCurso.setPosX(130);
                piezaEnCurso.setPosY(DESFASEY);
                listaPiezas.add(piezaEnCurso);
                creapiezaSiguiente();
            }
            int longitPunt =String.valueOf(puntuacion).length()*16;//Este valor me sirve para evitar que la "caja" de la puntuación se quede pequeña
            //Dibujo todos los elementos del juego
            ventana.dibujaRect(200, 10, 10+longitPunt, 40, 3, Color.BLUE);
            ventana.dibujaTexto(200, 80, "Nivel: "+String.valueOf(nivel), f2, colorInterfaz);
            piezaSiguiente.dibujaPieza(TAMANOBLOQUE, color);
            ventana.dibujaRect(10, 140, 360, 660, 4,colorInterfaz );
            ventana.dibujaRect(500, 10, 200, 200, 4, colorInterfaz);
            ventana.dibujaRect(500, 360, 200, 200, 4, colorInterfaz);
            ventana.dibujaTexto(540, 800, "Ángel Gandarias Amadasun, 1º, 2024", f, colorInterfaz);
            ventana.dibujaTexto(560, 600, "HOLD", f2, colorInterfaz);
            ventana.dibujaTexto(560, 250, "NEXT", f2, colorInterfaz);
            ventana.dibujaTexto(202, 40, String.valueOf(puntuacion), f2, colorInterfaz);
            dibujaTablero(grid,color);
            for (Pieza pieza : listaPiezas){
                pieza.dibujaPieza(TAMANOBLOQUE, color);
            }
            ventana.repaint();
            //La velocidad de juego depende de este contSpeedador y del nivel.
            if (contSpeed >= 10-nivel){
                piezaEnCurso.mover(TAMANOBLOQUE, grid);
                 contSpeed= 0;
            }
            piezaEnCurso.interaccionTeclado(grid, TAMANOBLOQUE, contInput, f2);
           if (piezaEnCurso.detectaColision(grid, TAMANOBLOQUE)) {
                puntuacion += 100;
                //Anulo la pieza
                piezaEnCurso.setVelocidad(0);
                piezaEnCurso.setPosY(piezaEnCurso.getPosY()-TAMANOBLOQUE);
                //Creo las nuevas colisiones
                grid = creaColisiones(grid);
                listaPiezas = eliminarPieza(listaPiezas);
                piezaEnCurso = null;
                //Elimino las lineas completas
                grid = eliminarLinea(grid);
                //Doy permiso al jugador para que utilice el Hold en el siguiente turno
                holding = false;
            }
            contSpeed += 1;
            contInput += 1;
            if (!(contInput<=1)) {
                contInput = 0;
            }
            //La cantidad de puntos necesaria para subir el nivel es cada vez más alta
            if (puntuacion >1000*nivel*nivel){
                nivel += 1;
            }
            for (int j = 1; j < grid[1].length-1; j++) {
                if (grid[1][j] == 1) {
                    ventana.acaba();
                }
            }
            ventana.espera(MSPERFRAME);
            ventana.borra();
        }
        return puntuacion;
    }
    /**
     * Genera una pieza nueva de manera aleatoria en posición X=565, Y=125
     */
    public static void creapiezaSiguiente() {
		int rand = random.nextInt(7);
        //Resto TAMANOBLOQUE piezas que tienen un hueco vacio a la izquierda para compensarlo
		if (rand == 0) { 
			piezaSiguiente = new PiezaT(565,125, ventana);
		} else if (rand == 1) {
			piezaSiguiente = new PiezaL(565-TAMANOBLOQUE,125, ventana);
        } else if (rand == 2){
            piezaSiguiente = new PiezaJ(565-TAMANOBLOQUE,125, ventana);
        } else if (rand == 3){
            piezaSiguiente = new PiezaO(565-TAMANOBLOQUE,125, ventana);
        } else if (rand == 4){
            piezaSiguiente = new PiezaS(565,125, ventana);
        } else if (rand == 5){
            piezaSiguiente = new PiezaZ(565,125, ventana);
        } else if (rand == 6){
            piezaSiguiente = new PiezaI(565,125, ventana);
        }
    }
    /**
     * Genera las colisiones del tablero, y asigna a cada casilla su color
     * @param grid El tablero al que se le van a añadir nuevos bloques
     * @return grid con nuevos bloques
     */
    public static int[][] creaColisiones( int[][] grid){
        //Recorro el tablero
        for (int i = 0; i < piezaEnCurso.shape.length; i++) {
            for (int j = 0; j < piezaEnCurso.shape.length; j++) {
                if (piezaEnCurso.shape[i][j] == 1) {
                    if ((piezaEnCurso.getPosY()-140-TAMANOBLOQUE*i)<0){
                        ventana.acaba();
                        break;
                    }
                    if (piezaEnCurso instanceof PiezaI){
                        grid[bloqueRejillaY(piezaEnCurso, grid, TAMANOBLOQUE*i)][bloqueRejillaX(piezaEnCurso, grid, TAMANOBLOQUE*j)] = 2;
                    }else if (piezaEnCurso instanceof PiezaJ){
                        grid[bloqueRejillaY(piezaEnCurso, grid, TAMANOBLOQUE*i)][bloqueRejillaX(piezaEnCurso, grid, TAMANOBLOQUE*j)] = 3;
                    }else if (piezaEnCurso instanceof PiezaL){
                        grid[bloqueRejillaY(piezaEnCurso, grid, TAMANOBLOQUE*i)][bloqueRejillaX(piezaEnCurso, grid, TAMANOBLOQUE*j)] = 4;
                    }else if (piezaEnCurso instanceof PiezaO){
                        grid[bloqueRejillaY(piezaEnCurso, grid, TAMANOBLOQUE*i)][bloqueRejillaX(piezaEnCurso, grid, TAMANOBLOQUE*j)] = 5;
                    }else if (piezaEnCurso instanceof PiezaS){
                        grid[bloqueRejillaY(piezaEnCurso, grid, TAMANOBLOQUE*i)][bloqueRejillaX(piezaEnCurso, grid, TAMANOBLOQUE*j)] = 6;
                    }else if (piezaEnCurso instanceof PiezaT){
                        grid[bloqueRejillaY(piezaEnCurso, grid, TAMANOBLOQUE*i)][bloqueRejillaX(piezaEnCurso, grid, TAMANOBLOQUE*j)] = 7;
                    }else if (piezaEnCurso instanceof PiezaZ){
                        grid[bloqueRejillaY(piezaEnCurso, grid, TAMANOBLOQUE*i)][bloqueRejillaX(piezaEnCurso, grid, TAMANOBLOQUE*j)] = 8;
                    }
                }
            }
        }   
        return grid;
    }
    /**
     * Crea una rejilla, con colisiones a los lados y en el fondo para marcar los límite. Los limites están marcados con unos.
     * @return Grid con límites
     */
    public static int[][] crearGrid(){
        int[][] grid = new int[23][14];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0;
            }
        }
        for (int i = 0; i < grid[grid.length-1].length; i++) {
            grid[grid.length-1][i] = 1;
            
        }
        for (int i = 0; i < grid.length; i++) {
            grid[i][0] = 1;
            grid[i][grid[i].length-1] = 1;
        }
        return grid;
    }
    /**
     * Elimina las lineas de la rejilla que esté completamente llenas y hace que las demás caigan como por gravedad
     * @param grid rejilla que se va a comprobar
     * @return rejilla grid con lineas llenas eliminadas
     */
    public static int[][] eliminarLinea(int[][] grid){
        for (int i = 0; i < grid.length-1; i++) {
            int sum = 0;
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] !=0) {
                    sum +=1;
                }
            }
            if (sum ==grid[i].length) {
                puntuacion += 1000;
                for (int ii = i; ii > 0; ii--) { //Recorro el tablero
                    for (int j = 1; j < grid[ii].length-1; j++) { //No toco las casillas que proporcionan colisiones a los laterales
                        grid[ii][j] = grid[ii-1][j]; //Cada linea POR ENCIMA de la linea completada se convierte en la linea que tiene encima de sí misma
                        //Dibujo por encima de cada bloque de la linea completada a gris para darle un efecto especial
                        ventana.dibujaRect(j*TAMANOBLOQUE-DESFASEX, i*TAMANOBLOQUE+DESFASEY, TAMANOBLOQUE, TAMANOBLOQUE, 2,Color.GRAY, Color.LIGHT_GRAY); 
                        //Dibujo por encima de cada bloque de la linea completada a gris
                    }
                }
                ventana.espera(100); //Doy una décima de segundo al jugador para ver el efecto

            }
        }
        return grid;
    }
    /**
     * Elimino cualquier pieza innecesaria de la lista
     * @param listaPiezas La lista de la que voy a eliminar piezas
     * @return La lista con las piezas innecesarias eliminadas
     */
    public static ArrayList<Pieza> eliminarPieza(ArrayList<Pieza> listaPiezas){
        ArrayList<Pieza> copiaPiezas = new ArrayList<Pieza>();
        for (Pieza pieza : listaPiezas) {
            if (!pieza.equals(piezaEnCurso)) {
                copiaPiezas.add(pieza);
            }
        }
        return copiaPiezas;
    }
    /**
     * Detecta si se pulsa la tecla ALT, y en caso de que eso pase mete la pieza actual en HOLD y saca
     *  la que haya ahí (si HOLD está vacio no saca nada al tablero) 
     * @param holding
     * @return
     */
    public static boolean holdPieza(boolean holding){
        if (piezaEnCurso!=null) {    
            if (ventana.isTeclaPulsada(16)&& holding == false) {//Si se pulsa la tecla ALT y no se ha usado HOLD todavía
                if (piezaHold != null) { //Si ya hay una pieza en HOLD
                    if (piezaEnCurso instanceof Reiniciable) {
                        ((Reiniciable)piezaEnCurso).setShapeToInitial();
                        
                    }
                    piezaHold.setPosX(130);
                    piezaHold.setPosY(140);
                }
                piezaEnCurso.setPosX(600);
                piezaEnCurso.setPosY(460);
                return true;
            }
        }
        return false;

    }
    /**
     * Dibuja la rejilla, con bloques incluidos
     * @param grid La rejilla a dibujar
     * @param color El color a utilizar para la rejilla
     */
    public static void dibujaTablero(int[][] grid,Color color){
        if (color.equals(Color.BLACK)) {
            color = Color.WHITE;
        }else{
            color= Color.BLACK;
        }
        for (int i = 0; i< grid.length-1; i++){
            for (int j = 1; j < grid[i].length-1; j++) {
                if (grid[i][j] == 2) {
                    ventana.dibujaRect(j*TAMANOBLOQUE-DESFASEX, i*TAMANOBLOQUE+DESFASEY, TAMANOBLOQUE, TAMANOBLOQUE, 2,color, Color.CYAN);
                }else if (grid[i][j] == 3) {
                    ventana.dibujaRect(j*TAMANOBLOQUE-DESFASEX, i*TAMANOBLOQUE+DESFASEY, TAMANOBLOQUE, TAMANOBLOQUE, 2,color, Color.BLUE);
                }else if (grid[i][j] == 4) {
                    ventana.dibujaRect(j*TAMANOBLOQUE-DESFASEX, i*TAMANOBLOQUE+DESFASEY, TAMANOBLOQUE, TAMANOBLOQUE, 2,color, Color.ORANGE);
                }else if (grid[i][j] == 5) {
                    ventana.dibujaRect(j*TAMANOBLOQUE-DESFASEX, i*TAMANOBLOQUE+DESFASEY, TAMANOBLOQUE, TAMANOBLOQUE, 2,color, Color.YELLOW);
                }else if (grid[i][j] == 6) {
                    ventana.dibujaRect(j*TAMANOBLOQUE-DESFASEX, i*TAMANOBLOQUE+DESFASEY, TAMANOBLOQUE, TAMANOBLOQUE, 2,color, Color.GREEN);
                }else if (grid[i][j] == 7) {
                    ventana.dibujaRect(j*TAMANOBLOQUE-DESFASEX, i*TAMANOBLOQUE+DESFASEY, TAMANOBLOQUE, TAMANOBLOQUE, 2,color, Color.MAGENTA);
                }else if (grid[i][j] == 8) {
                    ventana.dibujaRect(j*TAMANOBLOQUE-DESFASEX, i*TAMANOBLOQUE+DESFASEY, TAMANOBLOQUE, TAMANOBLOQUE, 2,color, Color.RED);
                }else{
                ventana.dibujaRect(j*TAMANOBLOQUE-DESFASEX, i*TAMANOBLOQUE+DESFASEY, TAMANOBLOQUE, TAMANOBLOQUE, 1, Color.DARK_GRAY);
                }
            }
        }
    }
    /**
     * Devuelve la posición Y de un bloque de una pieza en la rejilla
     * @param pieza Pieza cuya posición se calcula
     * @param grid Rejilla utilizada
     * @param posBloque posición del bloque respecto a la esquina inferior izquierda de una pieza
     * @return Posición en Y del bloque en la rejilla
     */
    public static int bloqueRejillaY(Pieza pieza, int[][]grid, int posBloque){
        return (pieza.getPosY()-DESFASEY-posBloque)/TAMANOBLOQUE;
    }
    /**
     * Devuelve la posición X de un bloque de una pieza en la rejilla
     * @param pieza Pieza cuya posición se calcula
     * @param grid Rejilla utilizada
     * @param posBloque posición del bloque respecto a la esquina inferior izquierda de una pieza
     * @return Posición en X del bloque en la rejilla
     */
    public static int bloqueRejillaX(Pieza pieza, int[][]grid, int posBloque){
        return (pieza.getPosX()+DESFASEX+posBloque)/TAMANOBLOQUE;
    }
    
}
