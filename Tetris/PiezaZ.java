import java.awt.Color;

import utils.ventanas.ventanaBitmap.VentanaGrafica;
//Ángel Gandarias Amadasun 2024

public class PiezaZ extends Pieza implements Reiniciable {
    public PiezaZ(int posX, int posY, VentanaGrafica ventana){
        super(posX, posY, ventana);
        shape = new int[4][4];
        shape[0][0] = 1;
        shape[0][1] = 1;
        shape[1][1] = 1;
        shape[1][2] = 1;
        color = Color.RED;
    }
    @Override
    public void setShapeToInitial() {
        shape = new int[4][4];
        shape[0][0] = 1;
        shape[0][1] = 1;
        shape[1][1] = 1;
        shape[1][2] = 1;
    }
}
