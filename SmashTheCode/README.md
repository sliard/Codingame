#Objectif

Regrouper des blocs de couleur pour remporter la victoire.
Concours sur : https://www.codingame.com/contests/smash-the-code

#Règles

Chaque joueur joue sur sa propre grille. Les grilles font chacune 6 cases de large et 12 cases de haut. A chaque tour, chaque joueur reçoit deux blocs liés entre eux à placer dans leur grille. Les deux blocs forment une paire. Fonctionnement des blocs :
Une paire de blocs peut subir une rotation avant d'être lâchée dans la grille. Une fois libres, les blocs ne sont plus connectés et bougent indépendamment.
Chaque bloc est coloré aléatoirement. Les couleurs sont représentées par des nombres de 1 à 5.
Le système vous indique 8 tours en avance les prochaines paires à venir.
Les deux joueurs reçoivent les mêmes blocs.
Comment placer les blocs :
Les blocs sont lâchés depuis le haut de la grille, et s'arrêtent quand ils touchent la dernière ligne, ou un autre bloc dans la même colonne.
Quand 4 blocs de la même couleur, ou plus, sont regroupés, ils disparaissent. Ils ne doivent pas nécessairement former une ligne droite.
Quand un groupe est détruit, les blocs du dessus tombent et s'arrêtent en bas de la grille ou quand ils tombent sur un autre bloc. Si la nouvelle configuration crée de nouveaux groupes de 4 blocs ou plus, ces groupes disparaissent également. On appelle cela une chaîne.
En créant des chaînes, vous augmentez votre potentiel d'attaque et vous pourrez automatiquement attaquer votre adversaire.

Fonctionnement des attaques :
En créant des groupes et des chaînes, vous générez des points de nuisance. Au-delà d'un seuil de points de nuisance, des blocs têtes-de-mort apparaissent dans la grille de votre adversaire.
Les blocs têtes-de-mort agissent comme des blocs de couleur mais ne disparaissent pas lorsqu'ils forment un groupe. Ils sont retirés de la grille quand un bloc de couleur explose à côté d'eux.
Les blocs têtes-de-mort sont lâchés dans la grille du joueur en une ligne complète, un bloc par colonne.
Les blocs têtes-de-mort sont représentés par un 0.
 
Conditions de victoire
Survivre plus longtemps que votre adversaire ou obtenir un meilleur score à la fin du temps imparti.
 
Conditions de défaite
Votre programme renvoie une action incorrecte
Votre programme dépasse les limites de temps.
Vous placez un bloc sur une colonne pleine.
 	Règles pour les experts

Les chaînes et les groupes donnent des points de score et des points de nuisance.
Les points de score sont utilisés pour départager deux joueurs qui seraient encore en vie après 200 tours de jeu.

A chaque tour, vous pouvez déclencher l'explosion d'un ou plusieurs groupes, provoquant ou non une chaîne. Chaque moment ou un ensemble de groupes disparaît est appelée une étape.
Points de Score :
Les points marqués à chaque tours sont calculés par étape à l'aide de la formule suivante :
score = (10 * B) * (CP + CB + GB)
Avec :
B le nombre de blocs détruits durant cette étape
CP la puissance de la chaîne. Ce nombre démarre à 0 pour la première étape. Il vaut 8 lors de la deuxième étape et est multiplié par deux à chaque étape suivante.
CB le Bonus de Couleur, qui dépend du nombre de groupes de couleurs différentes qui sont détruits à chaque étape. Il vaut :
0 pour 1 couleurs
2 pour 2 couleurs
4 pour 3 couleurs
8 pour 4 couleurs
16 pour 5 couleurs
GB est le Bonus de Group, qui dépend du nombre de blocs détruits par groupe. Quand plusieurs groupes sont détruits, les bonus de chaque groupe sont additionnés. Il vaut :
0 pour 4 blocs
1 pour 5 blocs
2 pour 6 blocs
3 pour 7 blocs
4 pour 8 blocs
5 pour 9 blocs
6 pour 10 blocs
8 pour 11 blocs ou plus
La valeur de (CP + CB + GB) est limitée entre 1 et 999 inclus.
Points de Nuisance :
A chaque tour, vous générez un nombre de points de nuisance égal au nombre de points de score pour ce tour, divisé par 70​. Ces points de nuisance s'ajoutent à ceux générés au tour précédent. Si cette somme est supérieure à 6​, une ligne de blocs tête-de-morts est lâchée dans la grille adverse pour chaque six points de nuisance. Ces points sont décomptés du total de points de nuisance.
Par exemple, si vous générez 14.5 points de nuisance, au début du tour suivant 2 lignes de blocs tête-de-morts apparaîtront dans la grille de votre adversaire, et il vous restera 2.5 points de nuisance conservés pour le prochain tour.
 	Note

Votre programme doit d'abord lire les données d'initialisation depuis l'entrée standard, puis, dans une boucle infinie, lire les données contextuelles de la partie et écrire sur la sortie standard les actions pour votre personnage.
 	Entrées du jeu

Entrées pour un tour de jeu
Les 8 premières lignes: 2 entiers séparés par des espaces, colorA et colorB: Les couleurs des blocs à placer dans votre grille.
Les 12 lignes suivantes: 6 caractères représentant une ligne de votre grille, de haut en bas.
Les 12 lignes suivantes: 6 caractères représentant une ligne de la grille adverse, de haut en bas.

Chaque ligne de la grille peut contenir les caractères :
. pour une cellule vide.
0 pour un bloc tête-de-mort.
Un entier de 1 à 5 pour un bloc de couleur.
Sortie pour un tour de jeu
Une unique ligne: 2 entiers séparés par des espaces: x la colonne choisie pour lâcher votre paire de blocs. rotation la valeur de rotation de la paire de blocs.

Pour une paire de blocs comme celle-ci [A,B] La rotation peut prendre les valeurs suivantes:

    0:  A B
    
          A
    1:    B
    
    2:  B A
    
    3:    B
          A

Vous pouvez ajouter du texte après vos instructions, celui-ci sera affiché dans le moniteur
Contraintes

1 ≤ colorA ≤ 5

1 ≤ colorB ≤ 5

0 ≤ x < 6

Temps de réponse par tour ≤ 100ms
