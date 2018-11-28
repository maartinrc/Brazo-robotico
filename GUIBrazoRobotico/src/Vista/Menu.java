package Vista;

import Beans.Brazo;
import Beans.Rutina;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import jssc.SerialPortException;

/**
 *
 * @author zdm
 */
public class Menu extends JFrame {

    PanamaHitek_Arduino ino; //objeto tipo PanamaHitek_Arduino para mandar datos vía serial a Arduino desde Java
    JPanel panelBox, panelBox2; //Paneles para los objetos de la GUI (IZQ , DER)
    JPanel[] miniPaneles; //Paneles para el acomodo de componentes en conjunto
    JComboBox comboPos, comboRutina; //JComboBoxes para "almacenar" las posiciones y rutinas, respectivamente
    JLabel[] lblSliders;  //Arreglo de etiquetas para identificar que JSlider pertenece a que parte del brazo
    JSlider[] sliderCuerpo;//JSliders para dar valores a las posiciones del brazo
    JButton[] btnOpciones; //Arreglo de botones
    String[] nombreBtn = {"Guardar", "Usar", "Abortar", "TOMA", "Crear rutina", "Agregar", "Cargar rutina",}; //arreglo de Strings para crear botones, en caso de que se requiera un botón nuevo, agregar el nombre en este arreglo y se crea automáticamente 
    String[] nombreSlider = {"Pinza", "Muñeca", "Codo", "Hombro", "Base"}; // arreglo de Strings para crear Sliders, mismo caso del botón

    public Menu() {// en el contructor se ejecutan los métodos siguientes
        iniciar();
        construir();
        lanzar();

    }

    public void iniciar() { //En este método se crean los Objetos
        ino = new PanamaHitek_Arduino();
        try {
            ino.arduinoTX("/dev/ttyACM1", 9600); //se establece la comunicación
        } catch (ArduinoException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        sliderCuerpo = new JSlider[5];
        btnOpciones = new JButton[nombreBtn.length]; // se crea el arreglo de botones en base a la longitud del arreglo del nombre de los mismos
        panelBox = new JPanel();//Se crea panel
        panelBox2 = new JPanel();// se crea panel
        miniPaneles = new JPanel[4];
        lblSliders = new JLabel[nombreSlider.length]; // se crea el arreglo de JSliders en base a la lonigtud del arreglo de nombre de los mismos
        comboPos = new JComboBox(); //Se crea JComboBox para las posiciones del brazo
        comboRutina = new JComboBox();//Se crea JComboBox paras las rutinas

    }

    public void construir() {
        Manejadora manejadora = new Manejadora();//Se crea objeto tipo Manejadora que se asocia a los botones para que la clase "escuche" los eventos

        this.setLayout(new BorderLayout());//Se define el layout del panel principal
        panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));// se define el layout del panel 

        for (int i = 0; i < sliderCuerpo.length - 1; i++) { // se crean los jslider para 4 ejes, el quinto, que es la base se hace a parte por el rango de movimiento
            sliderCuerpo[i] = new JSlider(JSlider.HORIZONTAL, 0, 180, 0);//Posicion,min,max,inicio
            lblSliders[i] = new JLabel(nombreSlider[i]);
            sliderCuerpo[i].setMajorTickSpacing(10);
            sliderCuerpo[i].setMinorTickSpacing(1);
            sliderCuerpo[i].setPaintLabels(true);
            sliderCuerpo[i].setPaintTicks(true);
            panelBox.add(lblSliders[i]);
            panelBox.add(sliderCuerpo[i]);
        }
        sliderCuerpo[4] = new JSlider(JSlider.HORIZONTAL, 0, 360, 0); //Posicion,min,max,inicio *** BASE
        lblSliders[4] = new JLabel(nombreSlider[4]);
        sliderCuerpo[4].setMajorTickSpacing(10);
        sliderCuerpo[4].setMinorTickSpacing(1);
        sliderCuerpo[4].setPaintTicks(true);
        sliderCuerpo[4].setPaintLabels(true);
        panelBox.add(lblSliders[4]);
        panelBox.add(sliderCuerpo[4]);

        panelBox2.setLayout(new BoxLayout(panelBox2, BoxLayout.Y_AXIS));// se define el layout del panel 
        for (int i = 0; i < miniPaneles.length; i++) { //Se crean los paneles y se les asigna el layout
            miniPaneles[i] = new JPanel();
            miniPaneles[i].setLayout(new FlowLayout());

        }

        for (int i = 0; i < nombreBtn.length; i++) {
            btnOpciones[i] = new JButton(nombreBtn[i]);
            btnOpciones[i].addActionListener(manejadora);
            btnOpciones[i].setEnabled(false);

        }

        //Se deshabilitan algunos componentes para impedir que el usuario intente romper el programa, se activarán de acuerdo al flujo del programa
        comboPos.setEnabled(false);
        comboRutina.setEnabled(false);
        btnOpciones[0].setEnabled(true);
        btnOpciones[3].setEnabled(true);
        
//Se agregran los componentes de forma que se vea entendible en la interfaz
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

    public void lanzar() {// se lanza la aplicacion
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 325);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);

    }

    private class Manejadora implements ActionListener { //clase interna que "escuchará" a los botones, dependiendo a qué botón se le dió clic se ejecutará una parte de código

        int o = 0; //contador de posiciones
        int c = 0; // contador de rutinas

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnOpciones[0]) { // clic Botón guardar
                //Se habilitan botones y comboBox de posiciones
                comboPos.setEnabled(true);
                btnOpciones[1].setEnabled(true);
                btnOpciones[2].setEnabled(true);
                btnOpciones[4].setEnabled(true);

                Brazo b = new Brazo(); // se crea un objeto tipo brazo y se le asigna valores, tambien una descripción
                o++;
                b.setBase(sliderCuerpo[4].getValue());
                b.setHombro(sliderCuerpo[3].getValue());
                b.setCodo(sliderCuerpo[2].getValue());
                b.setMuneca(sliderCuerpo[1].getValue());
                b.setPinza(sliderCuerpo[0].getValue());
                b.setDescripcion("Posicion" + o);

                comboPos.addItem(b); //se agrega el objeto al comboBox
            } else if (e.getSource() == btnOpciones[1]) { //clic al boton usar
                Brazo l = (Brazo) comboPos.getSelectedItem();// se crea un objeto tipo brazo en base al que está seleccionado en el JComboBox

                String data = l.getPinza() + "," + l.getMuneca() + "," + l.getCodo() + "," + l.getHombro() + "," + l.getBase(); // se concatenan los valores al String data, el contenido de esta variable será enviada por serial Arduino
                try {
                    ino.sendData(data); //Se envíain los datos
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println(l.getPinza() + "," + l.getMuneca() + "," + l.getCodo() + "," + l.getHombro() + "," + l.getBase()); //Se imprime una notificación de que rutina se está ejecutando
            } else if (e.getSource() == btnOpciones[4]) {   //clic en boton  Crear rutina           
                //Se habilitan los componentes necesarios
                comboRutina.setEnabled(true);
                btnOpciones[5].setEnabled(true);
                Rutina rut = new Rutina();// se crea objeto tipo rutina

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

                
                //lo que se hace en las siguientes líneas es sacar un brazo del comboBox de Posiciones, luego, saco una rutina del comboBox de rutinas y se inserta el brazo en la rutina, 
                //luego se borra la rutina anterior y se agrega la nueva que está con posiciones nuevas en el mismo lugar, o sea, se reemplaza.
                Brazo b = (Brazo) comboPos.getSelectedItem();// se crea un objeto tipo brazo en base al que está seleccionado en el JComboBox
                
                Rutina rut = (Rutina) comboRutina.getSelectedItem(); // se crea un objeto tipo rutina en base al que está seleccionado en el JComboBox
                rut.setRutina(",");//Se le agrega una  ,
                
                rut.setRutina(String.valueOf(b.getHombro()) + ",");
                rut.setRutina(String.valueOf(b.getBase()) + ",");
                rut.setRutina(String.valueOf(b.getCodo()) + ",");
                rut.setRutina(String.valueOf(b.getMuneca()) + ",");
                rut.setRutina(String.valueOf(b.getPinza()));
                
                
                comboRutina.removeItemAt(comboRutina.getSelectedIndex());
                comboRutina.addItem(rut);

            } else if (e.getSource() == btnOpciones[6]) {//clic btn Agregar
                //Se obtiene una rutina del combo y se pasa a String, luego, el contenido de esa variable String se pasa vía serial a Arduino
                Rutina datosR =  (Rutina) comboRutina.getItemAt(comboRutina.getSelectedIndex()); 
                String datosP = datosR.getRutina();
                try {
                    ino.sendData(datosP);
                    System.out.println(datosP);
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

                JOptionPane.showMessageDialog(null, "Se está ejecutnado la siguiente rutina: " + datosP, "Rutina en ejecución", JOptionPane.WARNING_MESSAGE); //Se imprime una notificación de que rutina se está ejecutando
            } else if (e.getSource() == btnOpciones[2]) {//enviar * para iniciar la secuencia de movimientos
                try {
                    ino.sendData("666"); //Identificador de que se ejecute una secuncia *no es una rutina*
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (e.getSource() == btnOpciones[3]) {
                try {
                    ino.sendData("111"); //Identificador para que se ejecute una rutina predenterminada
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

}
