# TD4 Moniteurs

## Question 1

### Début et fin de méthode

Lock / Unlock

### Wait

On append dans le wait set le thread courant, on déverouille la zc et on laisse un autre thread y accéder

### Libération

Tous les threads sont en concurrence -> vol de cycle.

## Question 3

| Invocations Parallèles | O/N |
|:----------------------:|:---:|
|    a.M1() // a.M1()    |  N  |
|    a.M1() // a.M2()    |  N  |
|    a.M1() // b.M1()    |  O  |
|    a.M1() // a.M5()    |  O  |
|    a.M1() // C.M6()    |  O  |
|    a.M1() // C.M3()    |  O  |
|    C.M3() // C.M4()    |  N  |
|    C.M3() // C.M5()    |  O  |

## Question 4

Voir /src

## Question 5
