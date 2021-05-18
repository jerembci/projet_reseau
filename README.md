# Projet_reseau:

----
Groupe: *Jérémy Bonci, Tristan Chaumont, Paula Friebertshäuser*


## Description du projet  


--------------------


Ce projet consiste à mettre en place un serveur web qui est capable de gérer une requête GET en HTTP/1.1 (sans librairies déjà définies).  
Ce que le serveur gère:
* Attente d'une connexion et détection d'une connexion   
  &rarr; un thread pour chaque connexion  ✔
*  Parsing de la requête   ✔
* Interpretation de la requête (Afficher les sites) ✔
* Affichage de la requête ✔
* Potection d'une site avec une authentification basique ✔
* Gestion des erreurs (400, 403, 404, 500) ✔
* **BONUS**: Gestion du listing des répertoires ✔
️


## Technologies utilisées

--------------------
* [Java](https://docs.oracle.com/en/java/javase/13/docs/api/index.html) Version 11  - Langage du projet
* [Maven](https://maven.apache.org/) -  Pour la gestion des dépendances
* [Slf4j](http://slf4j.org/manual.html) - Utilisé pour générer le log

## Utilisation  

--------------------

### *Installation*

* Rendez-vous à la racine du projet et exécutez `mvn clean && mvn install`.

### *Informations sur le dossier 'sites/'*

Vous pouvez changer le répertoire racine dans le fichier `config.properties` (voir partie ***Configuration*** ci-dessous).
Si vous le faites, veillez à toujours avoir le dossier `error-pages/` à l'intérieur (`<repertoire_racine>/error-pages/`), sinon les pages d'erreur 403, 404, etc. ne s'afficheront pas 😔

### *Comment lancer le projet ?*

Tout d'abord, rendez-vous dans le fichier `hosts` de votre OS, et ajoutez-y les deux lignes suivantes :
```
127.0.0.1	verti.com
127.0.0.1	dopetrope.com
```

Par la suite, vous avez deux manières de lancer le projet : 

Soit (exécution java) 
1.  Lancez la classe `Server` 
2.  Ouvrez *localhost* (par défaut port 80) dans le navigateur de votre choix  
    &rarr; vous arrivez à la page de démarrage qui propose de tester les deux sites qui sont hebergés sur le serveur:
    * [dopetrope](http://dopetrope.com)   
      (protegé par une authentification, *voir chapitre authentification*)
    * [verti](http://verti.com)  

Soit (exécution .bat/.sh) 
1. Si vous choisissez d'exécuter le projet depuis un autre endroit que la racine, alors copiez le fichier `config.properties` et le dossier `sites/` à l'endroit où vous allez faire votre exécution.
2. Exécutez le fichier `reseau.bat` ou `reseau.sh` qui se trouve dans le dossier `bindist-win/bin/` ou `bindist-unix/bin/` (toujours en fonction de votre OS) 
3. Ouvrez *localhost* (par défaut port 80) dans le navigateur de votre choix  
   &rarr; vous arrivez à la page de démarrage qui propose de tester les deux sites qui sont hebergés sur le serveur:
   * [dopetrope](http://dopetrope.com)   
     (protegé par une authentification, *voir chapitre authentification*)
   * [verti](http://verti.com)  

##### *Information sur le port utilisé*

Si jamais vous décidez d'utiliser un port autre que 80, les liens dans l'index.html du localhost ne vont pas fonctionner lors de la redirection vers *verti.com* ou *dopetrope.com*, car on ne pouvait pas ajouter le port dynamiquement dans l'html 😅 

Dans ce cas, il faut directement accéder aux sites en passant par la barre d'adresse.

### *Configuration*  

Dans le fichier *config.properties*, on peut 
* changer le port `port= 80`  
  En cas de changement de port
  il faut explicitement ajouter le port après localhost ou l'URL. *(exemple: verti.com:9090)*      
    
* changer le répertoire racine (webroot), dans lequel se trouvent les pages `webroot= sites`
* activer/desactiver l'affichage du listing des répertoires `listing= true`



### *Authentification*  

Le site dopetrop est protégé par une authentification basique. Pour accéder au site on peut utiliser les données d'accès suivantes (voir aussi */sites/dopetrop/.httpsswd*):  
* 👤 **Nom d'utilisateur:** username   
  🔒 **Mot de passe:** password
* 👤 **Nom d'utilisateur:** username1  
  🔒 **Mot de passe:** test

