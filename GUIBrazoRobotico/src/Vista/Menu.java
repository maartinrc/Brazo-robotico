package Vista;

import Beans.Brazo;
import Beans.Rutina;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import jssc.SerialPortException;
import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener

/**
 *
 * @author zdm
 */
public class Menu extends JFrame {

    PanamaHitek_Arduino ino;
    JPanel panelBox, panelBox2;
    JPanel[] miniPaneles;
    JComboBox comboPos, comboRutina;
    JLabel[] lblSliders;
    JSlider[] sliderCuerpo;
    JButton[] btnOpciones;
    String[] nombreBtn = {"Guardar", "Usar", "Abortar", "TOMA", "Crear rutina", "Agregar", "Cargar rutina",};
    String[] nombreSlider = {"Pinza", "Muñeca", "Codo", "Hombro", "Base"};
    String[] valorBrazo = {"", "", "", "", ""};
    String rutina = "222,";

    public Menu() {
        iniciar();
        construir();
        lanzar();

    }

    public void iniciar() {
        ino = new PanamaHitek_Arduino();
        try {
            ino.arduinoTX("/dev/ttyACM1", 9600);
        } catch (ArduinoException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        sliderCuerpo = new JSlider[5];
        btnOpciones = new JButton[nombreBtn.length];
        panelBox = new JPanel();
        panelBox2 = new JPanel();
        miniPaneles = new JPanel[4];
        lblSliders = new JLabel[nombreSlider.length];
        comboPos = new JComboBox();
        comboRutina = new JComboBox();

    }

    public void construir() {
        Manejadora manejadora = new Manejadora();

        this.setLayout(new BorderLayout());
        panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));

        for (int i = 0; i < sliderCuerpo.length - 1; i++) {
            sliderCuerpo[i] = new JSlider(JSlider.HORIZONTAL, 0, 180, 0);//Posicion,min,max,inicio
            lblSliders[i] = new JLabel(nombreSlider[i]);
            sliderCuerpo[i].setMajorTickSpacing(10);
            sliderCuerpo[i].setMinorTickSpacing(1);
            sliderCuerpo[i].setPaintLabels(true);
            sliderCuerpo[i].setPaintTicks(true);
            panelBox.add(lblSliders[i]);
            panelBox.add(sliderCuerpo[i]);
        }
        sliderCuerpo[4] = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        lblSliders[4] = new JLabel(nombreSlider[4]);
        sliderCuerpo[4].setMajorTickSpacing(10);
        sliderCuerpo[4].setMinorTickSpacing(1);
        sliderCuerpo[4].setPaintTicks(true);
        sliderCuerpo[4].setPaintLabels(true);
        panelBox.add(lblSliders[4]);
        panelBox.add(sliderCuerpo[4]);

        panelBox2.setLayout(new BoxLayout(panelBox2, BoxLayout.Y_AXIS));
        for (int i = 0; i < miniPaneles.length; i++) {
            miniPaneles[i] = new JPanel();
            miniPaneles[i].setLayout(new FlowLayout());

        }

        for (int i = 0; i < nombreBtn.length; i++) {
            btnOpciones[i] = new JButton(nombreBtn[i]);
            btnOpciones[i].addActionListener(manejadora);
            btnOpciones[i].setEnabled(false);

        }

        comboPos.setEnabled(false);
        comboRutina.setEnabled(false);
        btnOpciones[0].setEnabled(true);
        btnOpciones[3].setEnabled(true);

        panelBox2.add(btnOpciones[0]);
        miniPaneles[0].add(comboPos);
        miniPaneles[0].add(btnOpciones[1]);
        miniPaneles[1].add(btnOpciones[4]);
        miniPaneles[2].add(comboRutina);
        miniPaneles[2].add(btnOpciones[5]);
        miniPaneles[3].add(btnOpciones[6]);
        panelBox2.add(miniPaneles[0]);
        panelBox2.add(miniPaneles[1]);
        panelBox2.add(miniPaneles[2]);
        panelBox2.add(miniPaneles[3]);
        panelBox2.add(btnOpciones[2]);
        panelBox2.add(btnOpciones[3]);

        this.add(panelBox, BorderLayout.CENTER);
        this.add(panelBox2, BorderLayout.EAST);
    }

    public void lanzar() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 325);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);

    }

    private class Manejadora implements ActionListener {

        int o = 0; //contador de posiciones
        int c = 0; // contador de rutinas

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnOpciones[0]) {
                comboPos.setEnabled(true);
                btnOpciones[1].setEnabled(true);
                btnOpciones[2].setEnabled(true);
                btnOpciones[4].setEnabled(true);

                Brazo b = new Brazo();
                o++;
                b.setBase(sliderCuerpo[4].getValue());
                b.setHombro(sliderCuerpo[3].getValue());
                b.setCodo(sliderCuerpo[2].getValue());
                b.setMuneca(sliderCuerpo[1].getValue());
                b.setPinza(sliderCuerpo[0].getValue());
                b.setDescripcion("Posicion" + o);

                comboPos.addItem(b);
            } else if (e.getSource() == btnOpciones[1]) {
                Brazo l = (Brazo) comboPos.getSelectedItem();

                String data = l.getPinza() + "," + l.getMuneca() + "," + l.getCodo() + "," + l.getHombro() + "," + l.getBase();
                try {
                    ino.sendData(data);
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println(l.getPinza() + "," + l.getMuneca() + "," + l.getCodo() + "," + l.getHombro() + "," + l.getBase());
            } else if (e.getSource() == btnOpciones[4]) {   //btn Crear rutina           
                comboRutina.setEnabled(true);
                btnOpciones[5].setEnabled(true);
                Rutina rut = new Rutina();

                //rut.setRutina("222,"); //

                Brazo b = (Brazo) comboPos.getSelectedItem();

                c++;
                rut.setRutina(String.valueOf(b.getHombro()) + ",");
                rut.setRutina(String.valueOf(b.getBase()) + ",");
                rut.setRutina(String.valueOf(b.getCodo()) + ",");
                rut.setRutina(String.valueOf(b.getMuneca()) + ",");
                rut.setRutina(String.valueOf(b.getPinza()));
                rut.setDescripcion("Rutina" + c);
                comboRutina.addItem(rut);

            } else if (e.getSource() == btnOpciones[5]) { // btn agregar
                btnOpciones[6].setEnabled(true);

                Brazo b = (Brazo) comboPos.getSelectedItem();
                
                Rutina rut = (Rutina) comboRutina.getSelectedItem();
                rut.setRutina(",");
                
                rut.setRutina(String.valueOf(b.getHombro()) + ",");
                rut.setRutina(String.valueOf(b.getBase()) + ",");
                rut.setRutina(String.valueOf(b.getCodo()) + ",");
                rut.setRutina(String.valueOf(b.getMuneca()) + ",");
                rut.setRutina(String.valueOf(b.getPinza()));
                
                
                comboRutina.removeItemAt(comboRutina.getSelectedIndex());
                comboRutina.addItem(rut);

            } else if (e.getSource() == btnOpciones[6]) {
                Rutina datosR =  (Rutina) comboRutina.getItemAt(comboRutina.getSelectedIndex());
                String datosP = datosR.getRutina();
                try {
                    ino.sendData(datosP);
                    System.out.println(datosP);
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

                JOptionPane.showMessageDialog(null, "Se está ejecutnado la siguiente rutina: " + datosP, "Rutina en ejecución", JOptionPane.WARNING_MESSAGE);
            } else if (e.getSource() == btnOpciones[2]) {//enviar * para iniciar la secuencia de movimientos
                try {
                    ino.sendData("666");
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (e.getSource() == btnOpciones[3]) {
                try {
                    ino.sendData("111");
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

}
