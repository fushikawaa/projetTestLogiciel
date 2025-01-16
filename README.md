## Projet de test logiciel 

## Membres de l'équipe

PERRIN Lucie, DENOCE Allan, BRAUN Jules

****

## Lancer l'application
Pour lancer l'interface en lignes de commande, il faut commencer par compiler le projet. Pour cela, lancer la commande suivante dans le dossier `projet` :
`mvn compile`
Ensuite, on lance l'application en archivant le projet et en l'exécutant :
`mvn package`
`java -jar target/projet-1.0-SNAPSHOT-jar-with-dependencies.jar`

**Exemple de commande avec des résultats valides :**
Ville de départ : Marseille
Ville de voyage : Paris
Ville de retour : Lille


## Lancer les tests
**Tests unitaires**
Les tests unitaires peuvent être lancés grâce à la commande :
`mvn clean test`

**Tests d'intégration**
Pour lancer les tests d'intégration, il faut compiler l'ensemble des tests avec la commande `mvn clean test` puis exécuter uniquement les tests d'intégration avec la commande :
`mvn failsafe:integration-test`