/*
 * MiniProjet_MasterMind
 * TDC - Groupe 1
 * Martin Rouanet
 * 20 Novembre 2024
 */
package cpo_miniprojet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author martin
 */
public class CPO_Mini_Projet extends javax.swing.JFrame {
    private Partie partie; // Référence à la classe Partie
    private JButton[][] boutons; // Matrice de boutons pour représenter le plateau graphique
    private int nbToursMax = 12;
    private int tailleCombinaison = 4;
    private int tourCourant = 0; // Le tour actuel

    public CPO_Mini_Projet() {
        ArrayList<Character> couleursDisponibles = new ArrayList<>();
        couleursDisponibles.add('R');
        couleursDisponibles.add('B');
        couleursDisponibles.add('G');
        couleursDisponibles.add('Y');

        this.partie = new Partie(tailleCombinaison, nbToursMax, couleursDisponibles);
        
        // Afficher les règles avant d'initialiser les composants
        afficherRegles();
        
        initComponents();
        
        initialiserPlateauGraphique();
    }
    
    private void afficherRegles() {
        // Crée un JDialog pour afficher les règles
        JDialog reglesDialog = new JDialog(this, "Règles du jeu Mastermind", true);
        reglesDialog.setSize(700, 700);
        reglesDialog.setLocationRelativeTo(this);
        reglesDialog.setLayout(new BorderLayout());

        // Contenu des règles
        String regles = "<html><body style='padding:10px; font-size:12px;'>"
                + "<h2>Règles du jeu Mastermind :</h2>"
                + "<ol>"
                + "<li>Devinez la combinaison secrète composée de " + tailleCombinaison + " couleurs.</li>"
                + "<li>Les couleurs possibles sont : R (Rouge), B (Bleu), G (Vert), Y (Jaune).</li>"
                + "<li>Vous avez " + nbToursMax + " tentatives pour trouver la bonne combinaison.</li>"
                + "<li>Après chaque tentative, vous obtiendrez un retour indiquant :"
                + "<ul><li>Combien de pions sont corrects et bien placés (pions noirs).</li>"
                + "<li>Combien de pions sont corrects mais mal placés (pions blancs).</li></ul></li>"
                + "</ol>"
                + "<p>Bonne chance !</p>"
                + "</body></html>";

        JLabel labelRegles = new JLabel(regles);
        labelRegles.setVerticalAlignment(SwingConstants.TOP);
        reglesDialog.add(new JScrollPane(labelRegles), BorderLayout.CENTER);

        // Bouton pour fermer la fenêtre
        JButton boutonFermer = new JButton("OK");
        boutonFermer.addActionListener(e -> reglesDialog.dispose());
        reglesDialog.add(boutonFermer, BorderLayout.SOUTH);

        reglesDialog.setVisible(true);
    }

    private void initialiserPlateauGraphique() {
        boutons = new JButton[nbToursMax][tailleCombinaison + 2];
        PlateauDeJeu.setLayout(new GridLayout(nbToursMax, tailleCombinaison + 3)); // Ajouter une colonne pour le bouton "Valider"

        for (int i = 0; i < nbToursMax; i++) {
            for (int j = 0; j < tailleCombinaison; j++) {
                boutons[i][j] = new JButton();
                boutons[i][j].setEnabled(i == 0); // Activer uniquement la première ligne
                boutons[i][j].setText("");
                int ligne = i; // Variable locale pour capturer la valeur de `i`
                int colonne = j;

                // Ajouter un écouteur d'événements pour le bouton
                boutons[i][j].addActionListener(e -> {
                    String texte = JOptionPane.showInputDialog(this, 
                            "Entrez le nom du pion (R, B, G, Y) :", 
                            "Nom du Pion", 
                            JOptionPane.PLAIN_MESSAGE);

                    if (texte != null && !texte.isEmpty()) {
                        texte = texte.toUpperCase();
                        if (texte.matches("[RBGY]")) {
                            boutons[ligne][colonne].setText(texte);
                            boutons[ligne][colonne].setForeground(Color.BLACK);
                            // Met à jour la tentative dans la classe Partie
                            partie.setTentativeCouleur(ligne, colonne, texte.charAt(0));
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                    "Entrée invalide. Seules les lettres R, B, G, Y sont autorisées.", 
                                    "Erreur", 
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                PlateauDeJeu.add(boutons[i][j]);
            }

            // Ajouter un bouton "Valider" pour chaque ligne
            JButton boutonValider = new JButton("Valider");
            boutonValider.setEnabled(i == 0); // Activer uniquement pour la première ligne
            int ligne = i; // Variable locale pour capturer la valeur de `i`

            boutonValider.addActionListener(e -> {
                if (ligne == tourCourant && verifierLigneComplete(ligne)) {
                    incrementerTour(); // Passer au tour suivant
                    mettreAJourScore(ligne); // Met à jour le score pour la ligne actuelle

                    // Obtenir la solution secrète
                    char[] solutionSecrete = partie.getSolution();
                    // Calculer et afficher les pions blancs et noirs
                    int pionsBlancs = calculerPionsBlancs(ligne, solutionSecrete);
                    int pionsNoirs = calculerPionsNoirs(ligne, solutionSecrete);

                    // Afficher les résultats sur l'interface graphique
                    boutons[ligne][tailleCombinaison].setText(Integer.toString(pionsBlancs)); // Bouton blanc
                    boutons[ligne][tailleCombinaison + 1].setText(Integer.toString(pionsNoirs)); // Bouton noir

                    // Afficher la solution secrète si le joueur n'a pas trouvé après 12 tours
                    if (ligne == nbToursMax - 1 && !solutionEstTrouvee()) {
                        JOptionPane.showMessageDialog(this,
                            "La solution secrète était : " + new String(partie.getSolution()),
                        "Solution Secrète",
                        JOptionPane.INFORMATION_MESSAGE);
                    terminerJeu(false); // Fin du jeu en cas d'échec
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                    "Veuillez remplir tous les boutons avant de valider.", 
                    "Erreur", 
                    JOptionPane.WARNING_MESSAGE);
                }
            });

            PlateauDeJeu.add(boutonValider);

            // Ajouter les deux boutons supplémentaires à la fin de chaque ligne
            boutons[i][tailleCombinaison] = new JButton();
            boutons[i][tailleCombinaison].setEnabled(i == 0); // Activer uniquement la première ligne
            boutons[i][tailleCombinaison].setBackground(Color.WHITE);
            PlateauDeJeu.add(boutons[i][tailleCombinaison]);

            boutons[i][tailleCombinaison + 1] = new JButton();
            boutons[i][tailleCombinaison + 1].setEnabled(i == 0); // Activer uniquement la première ligne
            boutons[i][tailleCombinaison + 1].setBackground(Color.BLACK);
            boutons[i][tailleCombinaison + 1].setForeground(Color.WHITE);
            PlateauDeJeu.add(boutons[i][tailleCombinaison + 1]);
        }
    }

    private boolean verifierLigneComplete(int ligne) {
        for (int j = 0; j < tailleCombinaison; j++) {
            if (boutons[ligne][j].getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void incrementerTour() {
        if (tourCourant < nbToursMax - 1) {
            // Désactiver les boutons de la ligne actuelle, y compris le bouton "Valider"
            for (int j = 0; j < tailleCombinaison; j++) {
                boutons[tourCourant][j].setEnabled(false);
            }
            // Désactiver les boutons noirs et blancs de la ligne actuelle
            boutons[tourCourant][tailleCombinaison].setEnabled(false); // Bouton blanc
            boutons[tourCourant][tailleCombinaison + 1].setEnabled(false); // Bouton noir

            // Désactiver le bouton "Valider" de la ligne actuelle
            JButton boutonValiderActuel = (JButton) PlateauDeJeu.getComponent(tourCourant * (tailleCombinaison + 3) + tailleCombinaison);
            boutonValiderActuel.setEnabled(false);

            // Passer au tour suivant
            tourCourant++;

            // Activer les boutons de la ligne suivante
            for (int j = 0; j < tailleCombinaison; j++) {
                boutons[tourCourant][j].setEnabled(true);
            }
            // Activer les boutons noirs et blancs de la ligne suivante
            boutons[tourCourant][tailleCombinaison].setEnabled(true); // Bouton blanc
            boutons[tourCourant][tailleCombinaison + 1].setEnabled(true); // Bouton noir

            // Activer le bouton "Valider" de la ligne suivante
            JButton boutonValiderSuivant = (JButton) PlateauDeJeu.getComponent(tourCourant * (tailleCombinaison + 3) + tailleCombinaison);
            boutonValiderSuivant.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Tous les tours sont terminés !", 
                    "Fin de Partie", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Pion[] getTentative(int ligne) {
        Pion[] tentative = new Pion[tailleCombinaison];
        for (int j = 0; j < tailleCombinaison; j++) {
            String texte = boutons[ligne][j].getText();
            if (!texte.isEmpty()) {
                tentative[j] = new Pion(texte.charAt(0));
            }
        }
        return tentative;
    }

    // Méthode pour calculer les pions blancs et noirs en comparant la tentative avec la solution secrète
    private int calculerPionsBlancs(int ligne, char[] solutionSecrete) {
        Pion[] tentative = getTentative(ligne);
        boolean[] solutionUtilisee = new boolean[tailleCombinaison];
        int pionsBlancs = 0;

        // Marquer les pions bien placés pour éviter les doublons
        for (int i = 0; i < tailleCombinaison; i++) {
            if (tentative[i] != null && tentative[i].getCouleur() == solutionSecrete[i]) {
                solutionUtilisee[i] = true;
            }
        }

        // Rechercher les pions corrects mais mal placés
        for (int i = 0; i < tailleCombinaison; i++) {
            if (tentative[i] != null && tentative[i].getCouleur() != solutionSecrete[i]) {
                for (int j = 0; j < tailleCombinaison; j++) {
                    if (!solutionUtilisee[j] && tentative[i].getCouleur() == solutionSecrete[j]) {
                        solutionUtilisee[j] = true;
                        pionsBlancs++;
                        break;
                    }
                }
            }
        }

        return pionsBlancs;
    }

    private int calculerPionsNoirs(int ligne, char[] solutionSecrete) {
        Pion[] tentative = getTentative(ligne);
        int pionsNoirs = 0;

        for (int i = 0; i < tailleCombinaison; i++) {
            if (tentative[i] != null && tentative[i].getCouleur() == solutionSecrete[i]) {
                pionsNoirs++;
            }
        }

        return pionsNoirs;
    }
    
    private void mettreAJourScore(int ligne) {
    // Obtenir la solution secrète
    char[] solutionSecrete = partie.getSolution();
    // Calculer les pions blancs et noirs
    int pionsBlancs = calculerPionsBlancs(ligne, solutionSecrete);
    int pionsNoirs = calculerPionsNoirs(ligne, solutionSecrete);

    // Mettre à jour les informations sur l'interface graphique
    boutons[ligne][tailleCombinaison].setText(Integer.toString(pionsBlancs)); // Bouton pions blancs
    boutons[ligne][tailleCombinaison + 1].setText(Integer.toString(pionsNoirs)); // Bouton pions noirs

    // Vérifier si le joueur a trouvé la solution
    if (pionsNoirs == tailleCombinaison) {
        JOptionPane.showMessageDialog(this,
                "Félicitations ! Vous avez trouvé la solution en " + (ligne + 1) + " tentatives.\n" +
                "La solution secrète était : " + new String(solutionSecrete),
                "Victoire",
                JOptionPane.INFORMATION_MESSAGE);
        terminerJeu(true); // Arrêter le jeu avec succès
        return;
    }

    // Afficher la solution secrète uniquement à la fin du jeu si elle n'a pas été trouvée
    if (ligne == nbToursMax - 1 && !solutionEstTrouvee()) {
        JOptionPane.showMessageDialog(this,
                "La solution secrète était : " + new String(solutionSecrete),
                "Solution Secrète",
                JOptionPane.INFORMATION_MESSAGE);
        terminerJeu(false); // Fin du jeu en cas d'échec
    }
    }

    // Méthode pour arrêter le jeu
    private void terminerJeu(boolean victoire) {
        for (int i = 0; i < boutons.length; i++) {
            for (int j = 0; j < boutons[i].length; j++) {
                boutons[i][j].setEnabled(false); // Désactiver tous les boutons
            }
        }
        if (!victoire) {
            JOptionPane.showMessageDialog(this,
                    "Vous avez perdu ! La solution était : " + new String(partie.getSolution()),
                    "Défaite",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Méthode pour vérifier si la solution a été trouvée avant le dernier tour
    private boolean solutionEstTrouvee() {
        for (int i = 0; i < nbToursMax; i++) {
            for (int j = 0; j < tailleCombinaison; j++) {
                if (!boutons[i][j].getText().isEmpty() && partie.getSolution()[j] != boutons[i][j].getText().charAt(0)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Initialise les composants générés automatiquement par l'IDE.
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PlateauDeJeu = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(700, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PlateauDeJeu.setBackground(new java.awt.Color(153, 153, 255));
        PlateauDeJeu.setPreferredSize(new java.awt.Dimension(1350, 850));

        javax.swing.GroupLayout PlateauDeJeuLayout = new javax.swing.GroupLayout(PlateauDeJeu);
        PlateauDeJeu.setLayout(PlateauDeJeuLayout);
        PlateauDeJeuLayout.setHorizontalGroup(
            PlateauDeJeuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1350, Short.MAX_VALUE)
        );
        PlateauDeJeuLayout.setVerticalGroup(
            PlateauDeJeuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        getContentPane().add(PlateauDeJeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CPO_Mini_Projet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CPO_Mini_Projet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CPO_Mini_Projet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CPO_Mini_Projet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CPO_Mini_Projet().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PlateauDeJeu;
    // End of variables declaration//GEN-END:variables
}