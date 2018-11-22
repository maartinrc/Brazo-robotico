package Vista;

import Beans.Brazo;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    JPanel panelBox, panelBox2, miniPanel;
    JComboBox comboPos;
    JLabel[] lblSliders;
    JSlider[] sliderCuerpo;
    JButton[] btnOpciones;
    String[] nombreBtn = {"Guardar", "Cargar", "Abortar"};
    String[] nombreSlider = {"Pinza", "Muñeca", "Codo", "Hombro", "Base"};
    String [] valorBrazo ={"","","","",""};

    public Menu() {
        iniciar();
        construir();
        lanzar();

    }

    public void iniciar() {        
        ino = new PanamaHitek_Arduino();
        try {
            ino.arduinoTX("/dev/ttyACM0", 9600);
        } catch (ArduinoException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        sliderCuerpo = new JSlider[5];
        btnOpciones = new JButton[nombreBtn.length];
        panelBox = new JPanel();
        panelBox2 = new JPanel();
        miniPanel = new JPanel();
        lblSliders = new JLabel[nombreSlider.length];
        comboPos = new JComboBox();
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
        miniPanel.setLayout(new FlowLayout());
        for (int i = 0; i < nombreBtn.length; i++) {
            btnOpciones[i] = new JButton(nombreBtn[i]);
            btnOpciones[i].addActionListener(manejadora);

        }
        panelBox2.add(btnOpciones[0]);
        miniPanel.add(comboPos);
        miniPanel.add(btnOpciones[1]);
        panelBox2.add(miniPanel);
        panelBox2.add(btnOpciones[2]);
        this.add(panelBox, BorderLayout.CENTER);
        this.add(panelBox2, BorderLayout.EAST);
    }

    public void lanzar() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 325);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

    }

   

    private class Manejadora implements EventListener, ActionListener {

        int o = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnOpciones[0]) {
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
            } else if (e.getSource() == btnOpciones[2]) {//enviar * para iniciar la secuencia de movimientos
                   try{
                       ino.sendData("666");
                   }catch(ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        
        void stateChanged(ChangeEvent e){
            
        }

    } 
    
    


}
