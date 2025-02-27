import java.awt.Color;

import utils.ventanas.ventanaBitmap.VentanaGrafica;
//Ángel Gandarias Amadasun 2024

public class PiezaJ extends Pieza implements Reiniciable {
    public PiezaJ(int posX, int posY, VentanaGrafica ventana){
        super(posX, posY, ventana);
        shape = new int[4][4];
        shape[0][1] = 1;
        shape[0][2] = 1;
        shape[1][2] = 1;
        shape[2][2] = 1;
        color = Color.BLUE;
    }

    @Override
    public void setShapeToInitial() {
        shape = new int[4][4];
        shape[0][1] = 1;
        shape[0][2] = 1;
        shape[1][2] = 1;
        shape[2][2] = 1;
    }
}