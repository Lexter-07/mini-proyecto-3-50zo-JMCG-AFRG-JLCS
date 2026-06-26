# Cincuentazo (50zo) - JavaFX

## Description

Cincuentazo (50zo) is a desktop card game application developed in Java using JavaFX and the MVC architectural pattern. 
The project implements a strategic game where players must play cards to a central table without the total sum exceeding a strict limit of 50 points. It features a modern graphical interface, an intelligent machine opponent system with multiple difficulty levels, and multithreading for turn management and UI updates.

---

# Features

- Dynamic generation of cards and decks
- Validation of valid and invalid card moves
- Interactive graphical interface using JavaFX
- Play against 1, 2, or 3 AI-controlled opponents
- Artificial Intelligence with heuristic decision-making 
- Multithreading system (`TurnThread`) for turn lifecycles and background processing
- Centralized scene navigation manager (`SceneManager`)
- Visual feedback for active turns and card selections
- Automated deck recycling when the draw pile is empty
- Elimination system and victory condition detection

---

# Technologies Used

- Java 17
- JavaFX
- FXML
- CSS
- MVC Architecture
- Multithreading / Concurrency
- IntelliJ IDEA

---

# Project Structure

```text
src/main/java/com/example/cincuentazo/
│
├── config/
│   └── GameSettings.java
│
├── controller/
│   ├── GameController.java
│   ├── MenuController.java
│    └──FinalController.java

├── exceptions/
│   ├── EmptyDeckException.java
│   ├── InvalidCardException.java
│   ├── InvalidMoveException.java
│   └── InvalidMoveException.java
│
├── model/
│   ├── ia/
│   │   └── IAPlayer.java
│   ├── intefaces/
│   │   ├── ICard.java
│   │   ├── IDeck.java
│   │   ├── IGameModel.java
│   │   ├── IPlayer.java
│   │   └── ITurn.java
│   ├── threads/
│   │   ├── TimeThread.java
│   │   └── TurnThread.java
│   ├── Card.java
│   ├── Deck.java
│   ├── GameModel.java
│   ├── GameRules.java
│   ├── Player.java
│   ├── Suit.java
│   ├── Translation.java
│   └── Turn.java
│
├── view/
│   ├── Path.java
│   └── SceneManager.java
│
└── Main.java
```

---

# Installation

## Requirements

- JDK 17 or higher
- JavaFX SDK
- IntelliJ IDEA (recommended)
  
---

# Clone the repository
```bash
git clone https://github.com/Lexter-07/mini-proyecto-3-50zo-JMCG-AFRG-JLCS.git
```

---
# Open the project

Open the project in IntelliJ IDEA.

---

# Configure JavaFX

Add the JavaFX SDK to the project libraries and VM options.

Example VM options:

```bash
--module-path "PATH_TO_FX/lib" --add-modules javafx.controls,javafx.fxml
```

---

# Run the project

Execute the `Main.java` file.

---

# Gameplay

## Objective

Play cards to the central table without exceeding the strict limit of 50 points. The last player remaining who can make a valid move wins the game.

---
#Card Rules
- Numbered cards (2-8, 10): Add their face value to the table.
- Card 9: Adds exactly 0 points.
- Face cards (J, Q, K): Subtract 10 points (crucial for saving yourself).
- Ace (A): Adds 10 points if the total does not exceed 50; otherwise, it adds 1 point.
---
# Controls

| Action | Description |
|---|---|
| Mouse Click | Select the number of AI opponents in the menu |
| Mouse Click | Select a card from your hand |
| Play Button | Confirm and play the selected card to the table |
| Exit Button | Close the application securely |

---

# Visual System

| Visual Element | Description |
|---|---|
| Player Image | Indicates when a player (machine or human) is active |
| Faded Image | Indicates if the machine has been eliminated |
| Card Highlight | Shows which card in the hand is currently selected |
| Status Pop-ups | System alerts for game over, victory, or paused states |

---

# Architecture

The project follows the MVC pattern:

- **Model** → Game logic, AI decision-making (`IAPlayer`), multithreading (`TurnThread`), and mathematical validation (`GameRules`).
- **View** → JavaFX stages, FXML files, CSS styles, and a centralized `SceneManager`.
- **Controller** → User interaction and communication between the UI and background models (`GameController`, `MenuController`).

---

# Authors

Developed by:

- Andrés Felipe Rodríguez García (AndresF395)
- Jorge Luis Castro Scarpetta (Lexter-07)
- Jose Manuel Cardona Gil (Josem-2415)

---

# License

This project is for academic and educational purposes.
