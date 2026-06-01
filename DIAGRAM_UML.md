# Diagramme UML - Projet Othello-Reversi

## Vue d'ensemble du projet

Le projet Othello-Reversi est une implémentation du jeu Othello (aussi appelé Reversi) en Java avec une interface graphique Swing. L'architecture suit le pattern MVC (Modèle-Vue-Contrôleur).

### Packages :
- **model** : Logique métier du jeu
- **view** : Interface graphique utilisateur

---

## Diagramme UML Détaillé

```
┌─────────────────────────────────────────────────────────────────────┐
│                           PACKAGE MODEL                             │
└─────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────┐
│         Cell                 │
├──────────────────────────────┤
│ - x: int                     │
│ - y: int                     │
│ - state: CellState          │
├──────────────────────────────┤
│ + Cell(x: int, y: int)      │
│ + getX(): int               │
│ + getY(): int               │
│ + getState(): CellState     │
│ + setState(state)           │
│ + isEmpty(): boolean        │
│ + isBlack(): boolean        │
│ + isWhite(): boolean        │
└──────────────────────────────┘
         △
         │ uses
         │
    ┌────┴─────────────────────────┐
    │                              │
┌───┴──────────────┐      ┌────────┴─────────┐
│  CellState       │      │   Player         │
│  (Enum)          │      ├──────────────────┤
├──────────────────┤      │ - color: CellSt. │
│ EMPTY            │      │ - score: int     │
│ BLACK            │      ├──────────────────┤
│ WHITE            │      │ + Player(color)  │
└──────────────────┘      │ + getColor()     │
                          │ + getScore()     │
                          │ + setScore()     │
                          │ + incrementScore()│
                          │ + decrementScore()│
                          │ + isBlack()      │
                          │ + isWhite()      │
                          └──────────────────┘


┌──────────────────────────────┐
│         Board                │
├──────────────────────────────┤
│ - size: int                  │
│ - grid: Cell[][]            │
├──────────────────────────────┤
│ + Board(size: int)          │
│ + Board()                   │
│ + getCell(x, y): Cell       │
│ + isValidPosition(x, y)     │
│ + placePiece(x, y, state)   │
│ + getGrid(): Cell[][]       │
│ + countPieces(state): int   │
│ + getSize(): int            │
└──────────────────────────────┘
         △
         │ contains
         │
    ┌────┴─────┐
    │ 1..64     │
    │ (8x8)     │


┌──────────────────────────────┐
│         Move                 │
├──────────────────────────────┤
│ - x: int                     │
│ - y: int                     │
│ - player: Player            │
│ - capturedPieces: List      │
├──────────────────────────────┤
│ + Move(x, y, player)        │
│ + getX(): int               │
│ + getY(): int               │
│ + getPlayer(): Player       │
│ + getCapturedPieces()       │
│ + setCapturedPieces()       │
└──────────────────────────────┘


┌────────────────────────────────────┐
│         GameState                  │
├────────────────────────────────────┤
│ - board: Board                     │
│ - player1: Player                  │
│ - player2: Player                  │
│ - currentPlayer: Player            │
│ - gameOver: boolean                │
│ - winner: Player                   │
│ - moveHistory: List<Move>          │
├────────────────────────────────────┤
│ + GameState(boardSize: int)        │
│ + GameState()                      │
│ + getBoard(): Board                │
│ + getPlayer1(): Player             │
│ + getPlayer2(): Player             │
│ + getCurrentPlayer(): Player       │
│ + setCurrentPlayer(color)          │
│ + switchPlayer()                   │
│ + addMove(move)                    │
│ + isGameOver(): boolean            │
│ + setGameOver(boolean)             │
│ + getWinner(): Player              │
│ + setWinner(winner)                │
│ + getMoveHistory(): List<Move>     │
│ + updateScores()                   │
└────────────────────────────────────┘


┌────────────────────────────────────┐
│       GameSnapshot                 │
├────────────────────────────────────┤
│ - move: Move                       │
│ - boardState: CellState[][]        │
│ - boardSize: int                   │
│ - currentPlayer: CellState         │
│ - blackScore: int                  │
│ - whiteScore: int                  │
│ - moveNumber: int                  │
├────────────────────────────────────┤
│ + GameSnapshot(move, board,        │
│   size, player, bScore,            │
│   wScore, moveNum)                 │
│ + getMove(): Move                  │
│ + getBoardState(): CellState[][]   │
│ + getBoardSize(): int              │
│ + getCurrentPlayer(): CellState    │
│ + getBlackScore(): int             │
│ + getWhiteScore(): int             │
│ + getMoveNumber(): int             │
│ - copyBoardState()                 │
└────────────────────────────────────┘


┌────────────────────────────────────┐
│         Rules                      │
│       (Static Methods)             │
├────────────────────────────────────┤
│ - DIRECTIONS: int[][]              │
├────────────────────────────────────┤
│ + isValidMove(board, x, y,         │
│   player): boolean                 │
│ + getCapturedPieces(board, x, y,   │
│   player): List<int[]>             │
│ + checkDirection(board, x, y,      │
│   dx, dy, opponent): List<int[]>   │
│ + getOpponentColor(color)          │
│   : CellState                      │
│ + applyMove(board, move)           │
│ + canPlay(board, player):boolean   │
│ + countFlipped(board, player)      │
│   : int                            │
└────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────────────┐
│                          PACKAGE VIEW                               │
└─────────────────────────────────────────────────────────────────────┘


┌──────────────────────────────┐
│      OthelloApp              │
│  extends JFrame              │
├──────────────────────────────┤
│ - controller: GameController │
├──────────────────────────────┤
│ + OthelloApp()               │
│ + showMenu()                 │
│ + startGame(mode)            │
│ + showMenuFromGame()         │
└──────────────────────────────┘
         │ uses
         │
         ▼


┌──────────────────────────────────────┐
│     GameController                   │
├──────────────────────────────────────┤
│ - SAVE_FILE_NAME: String             │
│ - app: OthelloApp                    │
│ - saveFile: File                     │
│ - gameState: GameState               │
│ - gameFrame: JFrame                  │
│ - boardPanel: JPanel                 │
│ - statusLabel: JLabel                │
│ - pauseButton: JButton               │
│ - undoButton: JButton                │
│ - redoButton: JButton                │
│ - playwithkeyboardButton: JButton    │
│ - mode: int                          │
│ - HistoryArea: JTextArea             │
│ - snapshots: List<GameSnapshot>      │
│ - redoSnapshots: List<GameSnapshot>  │
├──────────────────────────────────────┤
│ + GameController(app)                │
│ + startGame(mode)                    │
│ - resetGame(boardSize)               │
│ - saveSnapshot(move)                 │
│ - chooseBoardSize(): int             │
│ - buildGameFrame()                   │
│ - createBoardPanel(): JPanel         │
│ + refreshBoard()                     │
│ + playMove(x, y)                     │
│ - undoMove()                         │
│ - redoMove()                         │
│ - playWithKeyboard()                 │
│ - showPauseDialog()                  │
│ - saveGame()                         │
│ - loadGame(): boolean                │
│ - getStatusText(): String            │
│ - showAlert(title, message)          │
└──────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────────────┐
│                      RELATIONS ENTRE CLASSES                        │
└─────────────────────────────────────────────────────────────────────┘

GameState "1" *─────────── "1" Board
           "1" *─────────── "1" Player (player1)
           "1" *─────────── "1" Player (player2)
           "1" *─────────── "1" Player (currentPlayer)
           "1" o─────────── "1" Player (winner)
           "1" *─────────── "*" Move (moveHistory)

Board  "1" o─────────── "64" Cell (grid)

Move   "1" *────────────── "1" Player
       "1" o────────────── "*" int[] (capturedPieces)

GameSnapshot "1" o────────── "0..1" Move
             "1" *────────── "1" Board (boardState)

Rules (utilise) ──→ Board
         ────→ Player
         ────→ Cell
         ────→ Move

GameController "1" o────────── "1" OthelloApp
               "1" *────────── "1" GameState
               "1" *────────── "*" GameSnapshot (snapshots)
               "1" *────────── "*" GameSnapshot (redoSnapshots)
               utilise ───→ Rules
               utilise ───→ Move
```

---

## Flux de la Application

```
┌───────────────┐
│ Démarrage     │
│  (main)       │
└───────┬───────┘
        │
        ▼
┌─────────────────────────┐
│  OthelloApp             │
│  showMenu()             │
└────────┬────────────────┘
         │
         ├─→ Mode 1: PvP
         ├─→ Mode 2: vs IA
         └─→ Mode 3: Charger
         
         │
         ▼
┌─────────────────────────────┐
│  GameController             │
│  startGame(mode)            │
└────────┬────────────────────┘
         │
         ├─→ buildGameFrame()
         ├─→ createBoardPanel()
         └─→ refreshBoard()
         
         │
         ▼
┌─────────────────────────────┐
│  GameState                  │
│  (Gestion de la partie)     │
└────────┬────────────────────┘
         │
         ├─→ Board (plateau)
         ├─→ Player1 (Noir)
         ├─→ Player2 (Blanc)
         └─→ Rules (validation)
         
         │
         ▼
┌─────────────────────────────┐
│  Interaction Utilisateur    │
│  playMove(x, y)             │
└────────┬────────────────────┘
         │
         ├─→ Rules.isValidMove()
         ├─→ Rules.getCapturedPieces()
         ├─→ saveSnapshot()
         ├─→ GameState.updateScores()
         ├─→ GameState.switchPlayer()
         └─→ refreshBoard()

```

---

## Design Patterns Utilisés

1. **Model-View-Controller (MVC)**
   - Model : `GameState`, `Board`, `Player`, `Move`, `Rules`
   - View : `OthelloApp`, components Swing
   - Controller : `GameController`

2. **Snapshot Pattern** (`GameSnapshot`)
   - Pour l'implémentation de l'annulation/rétablissement

3. **Strategy Pattern** (potentiel)
   - `Rules` encapsule la logique métier de validation

4. **Singleton Pattern** (implicite)
   - Une instance de `GameState` par partie
   - Une instance de `GameController` par fenêtre

---

## Dépendances Externes

- **Swing** : `javax.swing.*` pour l'interface graphique
- **Java Collections** : `java.util.*` pour les listes
- **Java IO** : `java.io.*` pour la sauvegarde/chargement

---

## Points Clés de l'Architecture

1. **Séparation des responsabilités** : 
   - La logique du jeu est isolée dans `model`
   - La présentation reste dans `view`

2. **Historique et Annulation** :
   - Les `snapshots` permettent l'undo/redo complet

3. **Flexibilité de la taille du plateau** :
   - Board peut avoir différentes tailles (4x4, 6x6, 8x8, etc.)

4. **Sauvegarde/Chargement** :
   - Permet de reprendre une partie

5. **Validation centralisée** :
   - `Rules` gère toute la logique de validation des coups
