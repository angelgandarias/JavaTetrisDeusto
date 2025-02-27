import java.awt.Color;
import java.awt.Font;

import utils.ventanas.ventanaBitmap.VentanaGrafica;
//Ángel Gandarias Amadasun 2024
//Version 1.1.3
//1.1 Añadida tecla pausa 'Esc' (Junio)
//1.1.1 - 1.1.3 Cambios en mainTetris1 y Menu
public class Pieza{
    private int velocidad;
    protected int[][] shape;
    private int posX;
    private int posY;
    private VentanaGrafica ventana;
    protected Color color;
    public Pieza(int posX, int posY, VentanaGrafica ventana) {
        velocidad = 1;
        this.posX = posX;
        this.posY = posY;
        this.ventana = ventana;
    }

    public int getPosX() {
        return posX;
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }
    public int getPosY() {
        return posY;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }
    
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
    /**
     * Se ocupa de dibujar la pieza que lo llama
     * @param TAMANOBLOQUE El tamaño de cada bloque de la pieza
     * @param color El color de fondo de la ventana de juego
     */
    public void dibujaPieza(int TAMANOBLOQUE, Color color){
        for (int i = 0; i < this.shape.length; i++) {
            for (int j = 0; j < this.shape.length; j++) {
                if (shape[i][j] == 1){
                    if (color.equals(Color.BLACK)) {
                        ventana.dibujaRect(posX+(j*TAMANOBLOQUE), posY-(i*TAMANOBLOQUE), TAMANOBLOQUE, TAMANOBLOQUE, 2,Color.WHITE, this.color);
                    }else{
                        ventana.dibujaRect(posX+(j*TAMANOBLOQUE), posY-(i*TAMANOBLOQUE), TAMANOBLOQUE, TAMANOBLOQUE, 2,Color.DARK_GRAY, this.color);
                    }
                }
            } 
        }
    }
   
    /**
     * Rota la pieza que lo llama. Si no hay espacio, se mueve a donde quepa.
     * @param grid La rejilla de juego
     * @param TAMANOBLOQUE El tamaño de los bloques de la pieza
     */
    public void rotarPieza(int[][] grid, int TAMANOBLOQUE) {
        int[][] rot = new int[shape.length][shape.length];
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                rot[c][shape.length-1-r] = shape[r][c];
            }
        }
        int sum = 0;
        for (int i = 0; i < rot.length; i++) {
            if (rot[i][0]!= 0) {
                sum += 1;
            }
        }
        if (sum == 0){
            for (int i = 0; i < rot.length; i++) {
                for (int j = 1; j < rot.length; j++) {
                    rot[i][j-1] = rot[i][j];
                }
                rot[i][rot.length-1] = 0;
            }
        }
        sum = 0;
        for (int i = 0; i < rot.length; i++) {
            if (rot[0][i]!= 0) {
                sum += 1;
            }
            
        }
        if (sum == 0){
            for (int i = 0; i < rot.length; i++) {
                for (int j = 1; j < rot.length; j++) {
                    rot[j-1][i] = rot[j][i];
                }
                rot[rot.length-1][i] = 0;
            }
        }
        shape = rot;
    }
    /**
     * Mueve la pieza hacia abajo, utilizando el atributo velocidad
     * @param TAMANOBLOQUE determina cuanto se mueve hacia abajo
     * @param grid rejilla en la que se mueve la pieza
     */
    public void mover(int TAMANOBLOQUE, int[][] grid){
        posY += TAMANOBLOQUE*velocidad;
    }
    
    /**
     * Detecta pulsaciones de las teclas flecha derecha, izquierda, abajo, arriba, ctrl, alt
     * y mueve las piezas acorde a las teclas.
     * @param grid rejilla en la que se encuentra la pieza
     * @param TAMANOBLOQUE tamaño de cada bloque de la rejilla
     * @param contadorVel indica si la pieza se puede mover en este frame
     */
    public void interaccionTeclado(int[][] grid, int TAMANOBLOQUE, int contadorVel, Font f){
        int tecla = ventana.getCodUltimaTeclaTecleada();
        //Me interesa que para algunas acciones el movimiento sea continuo y para otras no.
        //También me interesa que no se puedan hacer dos cosas a la vez.
        //Si hay colisión tras cualquier movimiento, lo revierto
        if (ventana.isTeclaPulsada(39)&& contadorVel == 0){
            this.setPosX(this.getPosX()+TAMANOBLOQUE);
            if (detectaColision(grid, TAMANOBLOQUE)){
                setPosX(getPosX()-TAMANOBLOQUE);
            }
        }else if (ventana.isTeclaPulsada(37)&& contadorVel == 0){
            this.setPosX(this.getPosX()-TAMANOBLOQUE);
            if (detectaColision(grid, TAMANOBLOQUE)){
                setPosX(getPosX()+TAMANOBLOQUE);
            }
        }else if (ventana.isTeclaPulsada( 40)&& contadorVel == 0){
            if (posY < 800){
            this.setPosY(this.getPosY()+TAMANOBLOQUE);
        }
        }else if (tecla == 38){
            rotarPieza(grid, TAMANOBLOQUE);
            while (detectaColision(grid, TAMANOBLOQUE)) {
                rotarPieza(grid, TAMANOBLOQUE);
            }
        }else if (tecla == 17){
            while (!detectaColision(grid, TAMANOBLOQUE)) {
                this.setPosY(this.getPosY()+TAMANOBLOQUE);
            }
        }else if (tecla == 27){
            ventana.dibujaRect(100, 200, 600, 400, 5,Color.GRAY,Color.LIGHT_GRAY);
            ventana.dibujaTexto(150, 400, "Haga click en pantalla para continuar", f, Color.BLACK);
            ventana.esperaAClick();
        }
    }
    /**
     * Detecta colisiones con otros bloques
     * @param grid rejilla en la que se encuentra la pieza
     * @param TAMANOBLOQUE tamaño de cada bloque de la rejilla
     * @return
     */
    public boolean detectaColision(int[][] grid, int TAMANOBLOQUE){
        //recorro todas las posiciones tanto de la rejilla como de la pieza
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 0){
                    for (int ii = 0; ii < shape.length; ii++){
                        for (int jj = 0; jj < shape[ii].length; jj++){
                            if((shape[ii][jj] == 1) &&((getPosX()+(jj*TAMANOBLOQUE) == j*TAMANOBLOQUE-20) && (getPosY()-(ii*TAMANOBLOQUE) == i*TAMANOBLOQUE+140)) ){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
