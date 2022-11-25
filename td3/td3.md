# TD3

## Exercice 1

T1

```c
x = B // a1
A = x // b1
```

T2

```c
y = A // a2
B = y // b2
```

a1-b1-a2-b2 : A=2 B=2

a1-a2-b1-b2 : A=2 B=1

a2-a1-b1-b2 : A=2 B=1

## Exercice 2

```c
int cpt = 0;
fonction incr :
    cpt = cpt + 1; // 1.load cpt accu 2.inc 3.store accu cpt

// thread T1 :
...
incr() ;
...

// thread T2 :
...
incr() ;
...
```

T1.

- a) accu = 0

T2.

- a) accu = 1
- b) accu = 1
- c) cpt =  1

T1. accu = 1

- b) accu = 1

## Exercice 4

Si l'éxécution est longue, cela arrete le processeur jusqu'à la fin du processus.

## Exercice 6

```c
occupant[i] = true;
last = i;
while (occupant[1-i] && last = 1);
SC
occupant[i] = false;
```