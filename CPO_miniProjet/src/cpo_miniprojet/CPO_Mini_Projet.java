/*
 * MiniProjet_MasterMind
 * TDC - Groupe 1
 * Martin Rouanet
 * 20 Novembre 2024
 */
package cpo_miniprojet;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author marti
 */
public class CPO_Mini_Projet extends javax.swing.JFrame {
    private Partie partie; // Référence à la classe Partie
    private JButton[][] boutons; // Matrice de boutons pour représenter le plateau graphique
    private int nbToursMax = 12;
    private int tailleCombinaison = 4;

    /**
     * Creates new form CPO_Mini_Projet
     */
    public CPO_Mini_Projet() {
        // Initialiser les couleurs disponibles
        ArrayList<Character> couleursDisponibles = new ArrayList<>();
        couleursDisponibles.add('R');
        couleursDisponibles.add('B');
        couleursDisponibles.add('G');
        couleursDisponibles.add('Y');

        // Créer une instance de Partie
        this.partie = new Partie(tailleCombinaison, nbToursMax, couleursDisponibles);

        // Initialiser les composants
        initComponents();

        // Configurer le plateau graphique
        boutons = new JButton[nbToursMax][tailleCombinaison];
        PlateauDeJeu.setLayout(new GridLayout(nbToursMax, tailleCombinaison));

        for (int i = 0; i < nbToursMax; i++) {
            for (int j = 0; j < tailleCombinaison; j++) {
                boutons[i][j] = new JButton(); // Créer un bouton pour chaque cellule
                boutons[i][j].setEnabled(false); // Désactiver les boutons (lecture seule)
                PlateauDeJeu.add(boutons[i][j]); // Ajouter le bouton au plateau graphique
            }
        }
    }

    /**
     * Met à jour l'interface graphique en fonction de l'état actuel de la matrice.
     */
    public void mettreAJourPlateau() {
        Pion[][] tableauTentatives = partie.getTableauTentatives(); // Obtenir la matrice
        for (int i = 0; i < nbToursMax; i++) {
            for (int j = 0; j < tailleCombinaison; j++) {
                if (tableauTentatives[i][j] != null) {
                    boutons[i][j].setText(String.valueOf(tableauTentatives[i][j].getCouleur()));
                } else {
                    boutons[i][j].setText(""); // Cellule vide
                }
            }
        }
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
        Entrer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PlateauDeJeu.setBackground(new java.awt.Color(153, 153, 255));

        javax.swing.GroupLayout PlateauDeJeuLayout = new javax.swing.GroupLayout(PlateauDeJeu);
        PlateauDeJeu.setLayout(PlateauDeJeuLayout);
        PlateauDeJeuLayout.setHorizontalGroup(
            PlateauDeJeuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        PlateauDeJeuLayout.setVerticalGroup(
            PlateauDeJeuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        getContentPane().add(PlateauDeJeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 400, 400));

        Entrer.setText("jButton1");
        Entrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EntrerActionPerformed(evt);
            }
        });
        getContentPane().add(Entrer, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 160, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void EntrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EntrerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EntrerActionPerformed

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
    private javax.swing.JButton Entrer;
    private javax.swing.JPanel PlateauDeJeu;
    // End of variables declaration//GEN-END:variables
}
