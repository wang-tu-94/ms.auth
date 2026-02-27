# 🔐 MS Auth - Microservice d'Authentification

<div align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Java_21-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens" alt="JWT" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
</div>

<br />

Ce dépôt contient le code source du microservice d'authentification (**ms.auth**). Construit avec Spring Boot et Spring Security, il gère l'inscription des utilisateurs, la connexion et l'émission de tokens JWT sécurisés pour l'ensemble de l'écosystème. Il gère également les comptes de service (Service Accounts) pour la communication inter-microservices.

## 📋 Table des matières
- [Fonctionnalités](#-fonctionnalités)
- [Prérequis](#-prérequis)
- [Installation et Lancement (Local)](#-installation-et-lancement-local)
- [Lancement avec Docker](#-lancement-avec-docker)
- [Tests](#-tests)
- [Aperçu de l'API REST](#-aperçu-de-lapi-rest)

---

## ✨ Fonctionnalités
- **Gestion des Utilisateurs** : Inscription sécurisée avec hachage des mots de passe (BCrypt).
- **Authentification JWT** : Émission et validation de tokens JWT (JSON Web Tokens).
- **Comptes de Service** : Création de jetons persistants (sans expiration) pour authentifier d'autres microservices (`ROLE_SERVICE_ACCOUNT`).
- **Sécurité** : Filtre de sécurité robuste avec Spring Security bloquant les accès non autorisés.
- **Base de données** : PostgreSQL en production, H2 (en mémoire) pour le développement local.
- **CI/CD** : Déploiement automatisé et build d'images Docker via GitHub Actions.

---

## 🛠 Prérequis

Pour exécuter ce projet localement, assurez-vous d'avoir installé :
- **Java 21** (JDK 21)
- **Docker** (pour construire ou exécuter l'image conteneurisée)

---

## 🚀 Installation et Lancement (Local)

### 1. Cloner le projet
```bash
git clone [https://github.com/wang-tu-94/ms.auth.git](https://github.com/wang-tu-94/ms.auth.git)
cd ms.auth
```

### 2. Configuration
Par défaut, le projet est configuré pour utiliser le profil `local
