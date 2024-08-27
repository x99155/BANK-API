# Banking - API RESTful avec Spring Boot

Ce projet est une application **RESTful API** développée avec **Spring Boot** qui permet de gérer les clients d'une banque, leurs comptes bancaires, ainsi que les opérations effectuées sur ces comptes. L'objectif principal est de fournir une solution simple et efficace pour les besoins de gestion bancaire.

## Fonctionnalités

- **Gestion des clients :**
    - Création, consultation, mise à jour et suppression des clients de la banque.

- **Gestion des comptes bancaires :**
    - Création de comptes bancaires pour les clients.
    - Consultation des détails des comptes bancaires.
    - Mise à jour des informations des comptes bancaires.
    - Suppression de comptes bancaires.

- **Gestion des opérations sur les comptes :**
    - **Versements** : Ajouter des fonds sur un compte bancaire.
    - **Retraits** : Retirer des fonds d'un compte bancaire.
    - **Virements** : Transférer des fonds entre deux comptes bancaires.

## Prérequis

Avant de lancer l'application, assurez-vous d'avoir installé les éléments suivants :

- **JDK 17** ou supérieur
- **Maven 3.8+**
- **Docker** pour containeuriser la base de données (ici mariadb avec l'interface adminer)
- **Spring Boot 3.x**

## Installation et exécution

1. **Cloner le dépôt :**
   ```bash
   git clone https://github.com/x99155/BANK-API.git
   cd bank-api
