import java.awt.Color;

import utils.ventanas.ventanaBitmap.VentanaGrafica;
//Ángel Gandarias Amadasun 2024

public class PiezaI extends Pieza implements Reiniciable {
    public PiezaI(int posX, int posY, VentanaGrafica ventana){
        super(posX, posY, ventana);
        shape = new int[4][4];
        shape[0][1] = 1;
        shape[1][1] = 1;
        shape[2][1] = 1;
        shape[3][1] = 1;
        color = Color.CYAN;
    }

    @Override
    public void setShapeToInitial() {
        shape = new int[4][4];
        shape[0][1] = 1;
        shape[1][1] = 1;
        shape[2][1] = 1;
        shape[3][1] = 1;
    }
    
}