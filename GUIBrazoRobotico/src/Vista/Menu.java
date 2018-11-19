package Vista;

import Beans.Brazo;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import jssc.SerialPortException;

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

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(0, new JLabel("Min - 0"));
        labels.put(90, new JLabel("90"));
        labels.put(180, new JLabel("Max - 180"));

        for (int i = 0; i < sliderCuerpo.length - 1; i++) {
            sliderCuerpo[i] = new JSlider(JSlider.HORIZONTAL, 0, 180, 0);//Posicion,min,max,inicio
            lblSliders[i] = new JLabel(nombreSlider[i]);
            sliderCuerpo[i].setMajorTickSpacing(5);
            sliderCuerpo[i].setMinorTickSpacing(1);
            sliderCuerpo[i].setPaintTicks(true);
            sliderCuerpo[i].setPaintLabels(true);
            sliderCuerpo[i].setLabelTable(labels);
            panelBox.add(lblSliders[i]);
            panelBox.add(sliderCuerpo[i]);
        }
        sliderCuerpo[4] = new JSlider(JSlider.HORIZONTAL, 0, 180, 0);
        lblSliders[4] = new JLabel(nombreSlider[4]);
        sliderCuerpo[4].setMajorTickSpacing(10);
        sliderCuerpo[4].setMinorTickSpacing(1);
        sliderCuerpo[4].setPaintTicks(true);
        sliderCuerpo[4].setPaintLabels(true);
        sliderCuerpo[4].setLabelTable(labels);
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
        this.setSize(650, 425);
        this.setLocationRelativeTo(null);
        //   this.setResizable(false);
        this.setVisible(true);

    }

    private class Manejadora implements ActionListener {

        int o = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnOpciones[0]) {
                Brazo b = new Brazo();
                o++;
                b.setBase(sliderCuerpo[0].getValue());
                b.setHombro(sliderCuerpo[1].getValue());
                b.setCodo(sliderCuerpo[2].getValue());
                b.setMuneca(sliderCuerpo[3].getValue());
                b.setPinza(sliderCuerpo[4].getValue());
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
                       ino.sendData("*");
                   }catch(ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

}
