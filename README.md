# Pac-Man Pathfinding — Facharbeit Informatik

> **Vergleich und Implementierung von Pfadfindungsalgorithmen zur Erweiterung der Geister von Pac-Man in Java**  
> Facharbeit im Fach Informatik · Inda-Gymnasium Aachen · Jahrgangsstufe 12 · 2025/2026  
> Verfasser: Jakob Elijah Pütz

---

## Über dieses Projekt

Dieses Repository enthält den Quellcode und die schriftliche Ausarbeitung meiner Facharbeit im Fach Informatik. Ziel der Arbeit war es, verschiedene Pfadfindungsalgorithmen zu vergleichen und in einem bestehenden Java-Pac-Man-Spiel praktisch umzusetzen.

Als Grundlage diente das Open-Source-Projekt [Java Pac-Man von Drew Schuster](https://github.com/dtschust/javapacman), das gezielt um die folgenden Algorithmen und Modi erweitert wurde.

---

## Implementierte Algorithmen & Modi

| Modus | Beschreibung |
|---|---|
| `RANDOM` | Geister bewegen sich zufällig |
| `DIJKSTRA` | Kürzester Pfad ohne Heuristik (erkundet gleichmäßig alle Richtungen) |
| `ASTAR` | Kürzester Pfad mit Manhattan-Distanz als Heuristik (effizienter als Dijkstra) |
| `TEAM` | Koordiniertes Einkreisungsverhalten: Geister flankieren Pac-Man von allen Seiten |
| `RANDOM_EACH` | Jeder Geist verwendet einen zufällig zugewiesenen Algorithmus |

Der gewünschte Modus kann vor Spielbeginn per **Tastendruck** ausgewählt werden.

---

## Kernkonzepte

### Dijkstra-Algorithmus
Berechnet den kürzesten Weg von einem Startknoten zu **allen** anderen Knoten des Graphen. Zuverlässig und präzise, aber in Pac-Man vergleichsweise ineffizient, da das Ziel (Pac-Mans Position) stets bekannt ist.

### A\*-Algorithmus
Erweiterung von Dijkstra um eine **Heuristik** `h(n)`, die die Suche gezielt in Richtung des Ziels lenkt:

```
f(n) = g(n) + h(n)
```

- `g(n)` — bisherige Kosten vom Start bis Knoten n  
- `h(n)` — geschätzte Restkosten bis zum Ziel (Manhattan-Distanz)  
- `f(n)` — geschätzte Gesamtkosten

Wird `h(n) = 0` gesetzt, verhält sich A\* identisch zu Dijkstra.

### Teammodus
Jedem der vier Geister wird ein individueller Zielpunkt relativ zu Pac-Mans Position und Bewegungsrichtung zugewiesen:

- **Geist 1** — verfolgt Pac-Man direkt
- **Geist 2** — zielt 4 Felder **vor** Pac-Man (Abfangen von vorne)
- **Geist 3** — flankiert 4 Felder zur Seite
- **Geist 4** — flankiert 4 Felder zur entgegengesetzten Seite

Alle vier Geister nutzen intern A\*, um ihre jeweiligen Zielpunkte anzusteuern.

---

## Technische Details

- **Sprache:** Java
- **Spielfeld-Modellierung:** 2D-Array, jede begehbare Kachel = Knoten, Wände = Wert `1`
- **Strafkosten:** Rückwärtsbewegung kostet `10` statt `1`, um unnatürliches Verhalten zu vermeiden
- **Zentrale Klasse:** `Ghost` (Unterklasse von `Mover`)
- **Kern-Methode:** `findPath(targetX, targetY, useHeuristic, ...)` — implementiert sowohl Dijkstra als auch A\* in einer gemeinsamen Funktion

---

## Facharbeit

Die vollständige schriftliche Ausarbeitung ist in diesem Repository als PDF hinterlegt:
- [Facharbeit-Informatik.pdf](https://github.com/user-attachments/files/25750543/Facharbeit-Informatik.pdf)


---

## Quellen & Danksagung

- Ursprüngliches Pac-Man-Projekt: [dtschust/javapacman](https://github.com/dtschust/javapacman)
- Dijkstra, E.W. (1959): *A note on two problems in connexion with graphs*
- Hart, P. / Nilsson, N. / Raphael, B. (1968): *A Formal Basis for the Heuristic Determination of Minimum Cost Paths*
- Interaktive Algorithmen-Visualisierung: [PathFinding.js von Xueqiao Xu](https://qiao.github.io/PathFinding.js/visual/)

---

*Inda-Gymnasium Aachen · Kursleiter: Herr Minklai · Abgabe: 06.03.2026*
