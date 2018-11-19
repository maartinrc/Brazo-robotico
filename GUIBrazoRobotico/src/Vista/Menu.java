package Vista;

import Beans.Brazo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;


/**
 *
 * @author zdm
 */
public class Menu extends JFrame {

   
    JPanel panelBox, panelBox2, miniPanel;
    JComboBox comboPos;
    JLabel[] lblSliders;
    JSlider[] sliderCuerpo;
    JButton[] btnOpciones;
    String[] nombreBtn = {"Guardar", "Cargar"};
    String[] nombreSlider = {"Pinza", "Muñeca", "Codo", "Hombro", "Base"};

    public Menu() {
        iniciar();
        construir();
        lanzar();

    }

    public void iniciar() {
       
        sliderCuerpo = new JSlider[5];
        btnOpciones = new JButton[2];
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
        sliderCuerpo[4].setMajorTickSpacing(5);
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
        this.add(panelBox, BorderLayout.CENTER);
        this.add(panelBox2,BorderLayout.EAST);
    }

    public void lanzar() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(650, 425);
        this.setLocationRelativeTo(null);
        //   this.setResizable(false);
        this.setVisible(true);

    }

    private class Manejadora implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == btnOpciones[0]){
              Brazo b = new Brazo();
              b.setBase(sliderCuerpo[0].getValue());
              b.setHombro(sliderCuerpo[1].getValue());
              b.setCodo(sliderCuerpo[2].getValue());
              b.setMuneca(sliderCuerpo[3].getValue());
              b.setPinza(sliderCuerpo[4].getValue());
              b.setDescripcion(descripcion);
              
            //  comboPos.add();
          }
        }

    }

}
