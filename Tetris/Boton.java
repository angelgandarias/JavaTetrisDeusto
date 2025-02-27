import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import utils.ventanas.ventanaBitmap.VentanaGrafica;
//Ángel Gandarias Amadasun 2024

public class Boton {
    private int posX;
    private int posY;
    private String texto;
    VentanaGrafica ventana;
    Color colorBoton = Color.CYAN;
    public Boton(int posX, int posY, String texto, VentanaGrafica ventana) {
        this.posX = posX;
        this.posY = posY;
        this.texto = texto;
        this.ventana = ventana;
        
    }
    
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    /**
     * Comprueba si una pulsación del ratón entra dentro del botón.
     * @param click la pulsación del ratón que se va a comprobar.
     * @return true sólo si click entra dentro de las coordenadas del botón.
     */
    public boolean botonPresionado(Point click){
        if (!(click == null)&&((click.getX()>this.getPosX() && click.getX()<this.getPosX()+150)&&(click.getY()>this.getPosY() && click.getY()<this.getPosY()+100))){
            //Cambio el color del botón una décima de segundo para dar feedback visual al usuario.
            this.setColor(Color.CYAN.darker());
            this.dibujar();
            ventana.espera(100);
            this.setColor(Color.CYAN);
            this.dibujar();
            return true;
        }else{
            return false;
        }
    }
    
    public void setColor(Color color) {
        this.colorBoton = color;
    }
    /**
     * Dibuja el botón en la ventana elegida, de color blanco con el texto indicado en color negro.
     */
    public void dibujar(){
        Font f = new Font("ARIAL", Font.ROMAN_BASELINE, 25);
        ventana.dibujaRect(posX, posY, 150, 100, 4, Color.GRAY, colorBoton);
        ventana.dibujaTextoCentrado(posX+75, posY+50,0,0, texto, f, Color.BLACK);
    }
}