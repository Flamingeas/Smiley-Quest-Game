# Smiley Quest

> **Version revisitée du classique Pac-Man — entre nostalgie et modernité.**

---

## Présentation

**Smiley Quest** est une réinvention du jeu d’arcade **Pac-Man**.
Le joueur contrôle une **tête smiley personnalisable** dans un labyrinthe fermé.
Objectif : **ramasser des armes**, **vaincre les ennemis** et **atteindre le coffre d’or** pour passer au niveau suivant.

Le jeu combine :
- Une **expérience arcade classique** modernisée
- Des **mécaniques de combat et de personnalisation**
- Des **commandes fluides** et une **direction artistique colorée**

---

## Vision & Objectifs

### Objectifs de conception
- Offrir une **expérience arcade familière** et **accessible à tous**
- Ajouter des éléments modernes : **customisation**, **armes**, **progression**
- Maintenir une **prise en main rapide** et un **fort potentiel de rejouabilité**

### Objectifs techniques
Le projet illustre les **4 piliers de la programmation orientée objet en Java** :
- Encapsulation
- Abstraction
- Héritage
- Polymorphisme

---

## Public cible
- Joueurs **casuals** et **nostalgiques** (6+)
- Plateforme : **PC (Windows / macOS)**

---

## Gameplay

### 🔸 Règles de base
- Labyrinthe fermé inspiré de *Pac-Man*
- Objectif : **atteindre le coffre** pour passer au niveau suivant
- Ennemis : d’abord statiques, puis mobiles (patrouille, poursuite selon pattern)

### 🔸 Contrôles
| Action | Touche |
|:--|:--|
| Déplacement | Flèches ou ZQSD |
| Action / Attaque | Espace ou X |
| Menu / Pause | Échap |

### 🔸 Vie & progression
- 3 vies par partie (bonus possibles en passant de niveau)
- Score basé sur le **temps**, les **ennemis éliminés**, et les **niveaux réussis**
- Les **armes** ramassées sur la carte donnent un bonus temporaire
- Difficulté croissante : plus d’ennemis, vitesse accrue, meilleure IA

---

## Personnalisation

### Têtes smiley
Chaque smiley possède :
- Une **animation unique** (clignement, clin d’œil, sourire)
- Des **bruitages personnalisés**

### Écran de customisation
- Interface simple et ludique
- Sélection de smiley avant le début du niveau

---

## Ennemis

### Types :
- **Poursuiveur**
- **Patrouilleur**
- **Tireur**
- **Boss**

### Comportement :
- **Pathfinding** simple pour la poursuite
- **Heuristiques** pour la patrouille
- **Mode vulnérable** lors de certains power-ups

---

## Niveaux & progression
- **5 niveaux de base**, difficulté croissante
- Labyrinthes variés : *small maze*, *open corridors*, *multi-room*
- Variation de :
  - Vitesse des ennemis
  - Nombre d’ennemis
  - Répartition des armes

---

## Power-ups & Items
- Armes disponibles : **épée**, **arc**, **hache**, **massue**
- Dégâts proportionnels à la puissance de l’arme
- Durée d’effet limitée (chronomètre)

---

## Architecture technique

### Structure haut-niveau :
- Système de **scènes** : Menu / Jeu / Paramètres / Résultats
- **Gestion du labyrinthe** : grille tilemap (layers : murs, objets, collisions)
- **Système d’entités** : Player, Enemies, Items
- **Gestion des collisions** et états (normal, bonus, malus, dead)

### Technologies :
- **Langage** : Java
- **IDE** : IntelliJ IDEA
- **Bibliothèques** : `JFrame`, `JPanel`, `Runnable`, `Java 2D Graphics`

---

## Installation & Exécution

### Prérequis
- **Java JDK 17+** installé sur votre machine
- **IntelliJ IDEA** (ou autre IDE compatible Java)
- Git (optionnel, pour cloner le dépôt)

### Étapes d’installation
1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/<ton-nom-utilisateur>/smiley-quest.git
   cd smiley-quest
   ```

2. **Ouvrir le projet** dans IntelliJ IDEA :
   - Fichier → Ouvrir → sélectionner le dossier du projet
   - Vérifier que le **SDK Java 17+** est bien sélectionné

3. **Compiler et exécuter**
   - Exécuter la classe principale `Main.java`
   - Le jeu s’ouvre dans une fenêtre Java 2D (`JFrame`)

### Exécution depuis le terminal
   ```bash
   javac -d bin src/*.java
   java -cp bin Main
   ```

---

## Direction artistique

- Style **cartoon minimaliste**
- Palette **vive et contrastée** : pastilles lumineuses, corridors sombres
- Têtes smiley **expressives et dynamiques**

---

## Son & musique

- **Musique chiptune** remasterisée
- Effets sonores pour : collecte, collisions, attaques, victoire
- Paramètres ajustables : volume musique, effets, mute

---

## Assets

| Catégorie | Contenu |
|:--|:--|
| Sprites | 9 têtes smiley, 4 types d’ennemis, power-ups |
| Tilemap | 4 thèmes de labyrinthes |
| UI | Boutons, icônes, polices |
| Audio | 7–10 effets sonores, 3 pistes musicales |

### Exemple :
- Sprite tête smiley : `64x64 PNG animé` (3 frames : idle, blink, smile)

---

## Améliorations futures

- Mode **co-op / versus local**
- **Éditeur de niveaux** communautaire
- **Progression RPG** (XP, niveaux, upgrades de têtes)

---

## Conclusion

**Smiley Quest** combine la **nostalgie des jeux d’arcade** et la **créativité moderne**.
Grâce à son univers coloré, ses mécaniques accessibles et sa personnalisation ludique, le projet met en avant la puissance de la **programmation orientée objet en Java** dans un cadre amusant et visuellement attrayant.

---

## Auteur & Licence

Projet développé en Java dans le cadre d’un apprentissage personnel.
Licence: EPITECH
Auteur : Samyra Mangan Mben, Vincent Naulleau, Johanna Angloma
