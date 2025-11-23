# **Smiley Quest – Game Design Document (README)**

## **1. Vision**

Smiley Quest is a modern reinterpretation of the Pac-Man experience, blending fast-paced arcade gameplay with light combat, collectible weapons, customizable smiley-head characters, and a modular progression system.  
The goal is to deliver instant fun, expressive visuals, and long-term replayability while integrating modern systems such as character customization, enemy AI, scoring rules, and structured screen management.

### **1.1 Goals**
- Deliver a nostalgic arcade experience enhanced by modern mechanics (weapon abilities, enemy AI, customizable avatars).
- Ensure readable, responsive, expressive gameplay.
- Maintain replayability through level variety, scoring rules, and progressive difficulty.
- Apply robust Java OOP principles across the architecture (Encapsulation, Abstraction, Inheritance, Polymorphism).

### **1.2 Technical Goals**
- Build a clean, extensible entity system for Player, Enemies, Items, and UI.
- Implement a modular Screen Management System (Splash, Menu, Game, Pause, Settings, Game Over).
- Provide maintainable subsystems: Audio, Scoring, Timer, Enemy Rendering, Events.

---

## **2. Target Audience**

- Casual players and retro-gaming fans (ages 6+).
- Players who enjoy short challenges, fast sessions, and character customization.
- Platform: PC.

---

## **3. Core Gameplay**

### **3.1 Core Loop**
1. Navigate through a maze-like level.
2. Collect weapons and items.
3. Defeat or avoid enemies.
4. Find the golden chest to reach the next level.
5. Survive increasingly dangerous waves as difficulty scales.

### **3.2 Player Controls (PC)**
- **Arrow Keys**: Movement
- **Space**: Action / Attack
- **Escape**: Pause
- **M**: Menu

### **3.3 Player State & Progression**
- **Lives**: 3 per run (possible extra life between levels)
- **Health & Damage**: Based on weapon type and enemy class
- **Score** Components:
    - Time-based scoring (TimeBasedScoreRule)
    - Enemy kills
    - Level completion bonus
- **Weapons**: Temporary power-ups with special effects (sword, axe, bow, poison, etc.).
- **Difficulty Scaling**:
    - Enemy health increases
    - Enemy count increases
    - Faster AI behaviors
    - Tighter mazes and reduced power-ups

---

## **4. Player Character & Customization**

### **4.1 Smiley Heads**
Players choose between multiple smiley-face styles, each offering visual identity and personality.

### **4.2 Character Selection System**
Managed through **CharacterSelectScreen**:
- Displays all available smileys
- Stores the selection via **selectedSmileyIndex**
- Applies selected character when the GameWorld initializes

---

## **5. Enemies**

Enemies use dedicated sprites and are rendered via the **EnemyRenderer** system.  
Each enemy type features unique movement logic, behavior, speed, and attack cooldowns.

### **5.1 GhostEnemy**
- **Behavior**:
    - Diagonal movement
    - Partial wall phasing (~30% collisions ignored)
    - Occasional speed bursts
- **Move Logic**:
    - Soft interpolation toward player’s position

### **5.2 LarvaEnemy**
- **Behavior**:
    - Slow, passive
    - Aggressive only within detection radius
- **Move Logic**:
    - If distance < 180px → move toward the player

### **5.3 SkeletonWeakEnemy**
- **Behavior**:
    - Direct pursuit
    - Stops intermittently to attack
    - Medium speed and damage
- **Move Logic**:
    - Straight pathing toward player

### **5.4 SkeletonStrongEnemy**
- **Behavior**:
    - Faster movement
    - Performs lateral dodges
    - Higher damage

### **5.5 DragonEnemy (Mini-Boss, Levels 3–4)**
- **Behavior**:
    - Limited movement range
    - High health
- **Move Logic**:
    - Rotates around the player
    - Direction switches every 2 seconds

### **5.6 FinalBossEnemy (Baby Boss)**
- **Behavior**:
    - Very fast dashes
    - Area and line attacks
    - High damage and health
- **Move Logic**:
    - Sequential attack pattern:
        1. Rush
        2. Retreat
        3. Heavy attack

---

## **6. Level Design & Progression**

The base game contains **4 progressive levels** differentiated by:
- Maze geometry
- Corridor width
- Room layout
- Enemy number and type
- Weapon availability

**Difficulty Elements**:
- Faster and more complex enemy logic
- More dangerous patterns
- More strategic item placement

---

## **7. Power-Ups & Items**

Weapons differ by damage and temporary abilities.  
Each weapon collected also grants a **score bonus**.

---

## **8. User Interface**

### **8.1 HUD**
- Lives
- Score
- Weapon icon + cooldown indicator

### **8.2 Screens (via ScreenManager)**
- **SplashScreen** (TextureAtlas animation)
- **MenuScreen**
- **CharacterSelectScreen**
- **GameScreen**
- **PausedScreen**
- **SettingsScreen** (Audio, Controls)
- **GameOverScreen**
- **GameWinnerScreen**

Scene transitions are managed via **ScreenManager** and **GameState**.

---

## **9. Audio**

### **9.1 Music**
- Retro Pac-Man-inspired chiptune soundtrack
- Background looping controlled by **AudioManager**

**AudioManager Features**:
- Mute / unmute
- Persistent audio settings

---

## **10. Artistic Direction**
- Bright and expressive smiley faces
- Cartoon-styled enemies
- Color-coded weapons
- Minimalistic UI with bold shapes

---

## **11. Technology Stack**

- **Language**: Java
- **Framework**: LibGDX
- **Rendering**: SpriteBatch, TextureAtlas
- **IDE**: IntelliJ IDEA

### **Core Systems**
- ScreenManager
- AudioManager
- ScoreSystem
- EnemyRenderer
- GameTimer
- Observable / Observer Event System

---

## **12. High-Level Architecture**

### **12.1 Scene System**
Built on LibGDX’s `Screen` interface:  
`show`, `update`, `render`, `resize`, `pause`, `resume`, `hide`, `dispose`.

### **12.2 GameWorld**
- Holds Player, Enemies, Items, Level Logic
- Central game loop interacts with Scoring and Timer systems

### **12.3 Event System**
- Observer + Observable pattern
- Events communicated through **GameEvent**
- Used for scoring, UI refresh, audio triggers

### **12.4 Scoring System**
- Flexible, rule-based scoring using polymorphism
- Event-based scoring planned

---

## **13. Asset List**
- **Smiley heads**: 7
- **Enemies**: 5 types with unique sprites
- **Weapons**: sword, axe, bow, mace, poison
- **Tilemaps**: 4 levels
- **UI**: buttons, panels, fonts
- **Audio**: retro Pac-Man-style music

---

## **14. Future Improvements**
- Community level editor
- RPG-like progression
- Weapon crafting or upgrades
- Achievements, challenges (timers, scoring objectives, kill streaks)
- Seasonal modes

---

## **15. Installation**

### **Prerequisites**
- Java 17+
- IntelliJ IDEA (recommended)
- LibGDX environment

### **Steps**
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/smiley-quest.git
2. Open the project in IntelliJ IDEA
3. Import the Gradle project if prompted.
4. Run the **DesktopLauncher** configuration.

## **16. How to Play**

- **Step 1:** Start the game from the main menu.
- **Step 2:** Select your preferred smiley character.
- **Step 3:** Navigate the maze, collect items, defeat enemies.
- **Step 4:** Find the golden chest to complete the level.
- **Step 5:** Survive all levels to win Smiley Quest.

## **License**
@EPITECH

**Authors:** Vincent Naulleau, Johanna Angloma, Samyra Mangan Mben \
**Contact:** vincent1.naulleau@epitech.eu, johanna.angloma@epitech.eu, samyra.mangan-mben@epitech.eu