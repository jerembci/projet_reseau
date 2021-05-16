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
* [Java](https://docs.oracle.com/en/java/javase/13/docs/api/index.html) Version 13  - Langage du projet
* [Maven](https://maven.apache.org/) -  Pour la gestion des dépendances
* [Slf4j](http://slf4j.org/manual.html) - Utilisé pour générer le log

## Utilisation  

--------------------

### *Comment lancer le projet?*

1.  Lancez `Server` 
2.  Ouvrez *localhost:80* dans le navigateur de votre choix  
    &rarr; vous arrivez à la page de démarrage qui propose de tester les deux sites qui sont hebergés sur le serveur:
    * [dopetrope](http://dopetrope.com)   
      (protegé par une authentification, *voir chapitre authentification*)
    * [verti](http://verti.com)  


### *Configuration*  

Dans le fichier *config.properties* on peut 
* changer le port `port= 80`  
  En cas de changement de port
  il faut explicitement ajouter le port après localhost ou l'URL. *(exemple: )*      
    
* changer le répertoire racine (webroot), dans lequel se trouvent les pages `webroot= sites`
* activer/desactiver l'affichage du listing des répertoires `listing= true`



### *Authentification*  

Le site dopetrop est protégé par une authentification basique. Pour accéder au site on peut utiliser les données d'accès suivantes (voir aussi */sites/dopetrop/.httpsswd*):  
* 👤 **Nom d'utilisateur:** username   
  🔒 **Mot de passe:**
* 👤 **Nom d'utilisateur:** username1  
  🔒 **Mot de passe:** test

