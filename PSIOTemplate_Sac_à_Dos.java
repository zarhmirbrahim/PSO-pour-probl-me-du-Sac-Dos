import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PSIOTemplate_Sac_à_Dos {
    private JFrame frame;
    private JTextField fileInput;
    private JComboBox<String> problemTypeComboBox;
    private JTextField c1Input;
    private JTextField c2Input;
    private JTextField inertiaWeightInput;
    private JTextField numIterationsInput;
    private JTextField numParticlesInput;
    private JTextField vMaxInput;
    private JTextField numPrecisionInput;
    private JTextField maxWeightInput;
    private JTextField numObjectsInput;
    private JTextArea outputArea;
    private JFileChooser fileChooser;
    private JPanel middlePanel; // Déplacez la déclaration ici

    public PSIOTemplate_Sac_à_Dos() {
        frame = new JFrame("PSO Template");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top panel for file input and problem type selection
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JLabel fileLabel = new JLabel("Fichier :");
        fileInput = new JTextField(15);
        fileInput.setEditable(false);
        JButton browseButton = new JButton("Parcourir");
        problemTypeComboBox = new JComboBox<>(new String[]{"Sac à Dos", "Voyage du commerce", "Problème quelconque"});


        topPanel.add(fileLabel);
        topPanel.add(fileInput);
        topPanel.add(browseButton);
        topPanel.add(problemTypeComboBox);

        // Initialize file chooser
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        // Add action listener to browse button
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileInput.setText(selectedFile.getAbsolutePath());
                }
            }
        });
     // Add action listener to problem type combo box
        problemTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedProblemType = (String) problemTypeComboBox.getSelectedItem();
                if (selectedProblemType.equals("Voyage du commerce")) {
                    // Redirect to another page
                    frame.dispose(); 
                    new PSOTemplate();
                }
            }
        });

        // Middle panel for PSO parameters
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(0, 2));
        middlePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Initialize all components
        JLabel c1Label = new JLabel("C1 :");
        c1Input = new JTextField(10);
        JLabel c2Label = new JLabel("C2 :");
        c2Input = new JTextField(10);
        JLabel inertiaWeightLabel = new JLabel("Poids d'inertie :");
        inertiaWeightInput = new JTextField(10);
        JLabel numIterationsLabel = new JLabel("Nombre d'itérations :");
        numIterationsInput = new JTextField(10);
        JLabel numParticlesLabel = new JLabel("Nombre de particules :");
        numParticlesInput = new JTextField(10);
        JLabel vMaxLabel = new JLabel("Vitesse maximale :");
        vMaxInput = new JTextField(10);
        JLabel numPrecisionLabel = new JLabel("Précision :");
        numPrecisionInput = new JTextField(10);
        JLabel maxWeightLabel = new JLabel("Poids maximal :");
        maxWeightInput = new JTextField(10);
        JLabel numObjectsLabel = new JLabel("Nombre d'objets :");
        numObjectsInput = new JTextField(10);

        // Add all components to middlePanel
        middlePanel.add(c1Label);
        middlePanel.add(c1Input);
        middlePanel.add(c2Label);
        middlePanel.add(c2Input);
        middlePanel.add(inertiaWeightLabel);
        middlePanel.add(inertiaWeightInput);
        middlePanel.add(numIterationsLabel);
        middlePanel.add(numIterationsInput);
        middlePanel.add(numParticlesLabel);
        middlePanel.add(numParticlesInput);
        middlePanel.add(vMaxLabel);
        middlePanel.add(vMaxInput);
        middlePanel.add(numPrecisionLabel);
        middlePanel.add(numPrecisionInput);
        middlePanel.add(maxWeightLabel);
        middlePanel.add(maxWeightInput);
        middlePanel.add(numObjectsLabel);
        middlePanel.add(numObjectsInput);

        // Add button "Trouver" after numPrecisionInput
        middlePanel.add(new JLabel()); // Placeholder for alignment
        JButton trouverButton = new JButton("Trouver");
        trouverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInputs()) {
                    runPSOAlgorithm();
                } else {
                    JOptionPane.showMessageDialog(frame, "Veuillez saisir des valeurs valides pour tous les champs.");
                }
            }
        });
        middlePanel.add(trouverButton, BorderLayout.CENTER); // Position the button in the center

        // Bottom panel for output
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JLabel outputLabel = new JLabel("Sortie :");
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);

        bottomPanel.add(outputLabel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private boolean validateInputs() {
        // Check if input fields are empty and if they contain valid numbers
        try {
            double c1 = Double.parseDouble(c1Input.getText());
            double c2 = Double.parseDouble(c2Input.getText());
            double inertiaWeight = Double.parseDouble(inertiaWeightInput.getText());
            int numIterations = Integer.parseInt(numIterationsInput.getText());
            int numParticles = Integer.parseInt(numParticlesInput.getText());
            double vMax = Double.parseDouble(vMaxInput.getText());
            int numPrecision = Integer.parseInt(numPrecisionInput.getText());
            int maxWeight = Integer.parseInt(maxWeightInput.getText());
            int numObjects = Integer.parseInt(numObjectsInput.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void runPSOAlgorithm() {
        double c1 = Double.parseDouble(c1Input.getText());
        double c2 = Double.parseDouble(c2Input.getText());
        double inertiaWeight = Double.parseDouble(inertiaWeightInput.getText());
        int numIterations = Integer.parseInt(numIterationsInput.getText());
        int numParticles = Integer.parseInt(numParticlesInput.getText());
        int maxWeight = Integer.parseInt(maxWeightInput.getText());
        String filePath = fileInput.getText();
        
        String result = PSO_Sac_à_Dos.runAlgorithm(c1, c2, inertiaWeight, numIterations, numParticles, maxWeight, filePath);
        updateOutput(result);
    }

    private void updateOutput(String result) {
        outputArea.setText(result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PSIOTemplate_Sac_à_Dos();
            }
        });
    }
}
