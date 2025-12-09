# FarLandsAgain - État de la Compilation

## Situation Actuelle

Ce projet est une mise à jour de FarLandsAgain pour Minecraft 1.21.4 (Paper). La compilation complète avec Maven **n'est pas possible** dans l'état actuel.

### Ce qui fonctionne ✅

- **Module Utility** : Compile correctement
- Configuration Maven multi-modules : Correctement structurée
- Dépendances Paper API : Configurées et fonctionnelles

### Ce qui ne fonctionne pas ❌

- **Module v1_21_R1** : Ne compile pas - manque les classes NMS (Net Minecraft Server) internes de Paper
- **Module FarLandsAgain** : Dépend de v1_21_R1, donc ne peut pas compiler non plus

## Pourquoi cette situation ?

### Changement dans Paper 1.20.5+

À partir de Paper 1.20.5, Paper utilise les **Mojang mappings** par défaut et ne publie plus de JAR de développement accessible via Maven. Le code NMS (qui modifie directement les classes internes du serveur) nécessite des JARs remappés spéciaux.

### Problèmes rencontrés

1. **Les JARs locaux ne suffisent pas** : Les fichiers `paper-1.21.4.jar` dans `v1_21_R1/libs/` sont des launchers Paperclip, pas des JARs de développement contenant les classes NMS
2. **paper-nms-maven-plugin** : Le plugin tiers Maven pour gérer le NMS Paper ne supporte pas encore la version 1.21.4
3. **Pas d'artefact Maven officiel** : Paper ne publie pas de `paper-server` avec classes NMS via Maven

## Solutions Possibles

### Option 1 : Migrer vers Gradle ⭐ (Recommandé)

**Avantages :**
- Solution officielle recommandée par Paper pour 1.20.5+
- Plugin `paperweight-userdev` gère automatiquement les mappings NMS
- Accès aux classes internes remappées dans l'IDE
- Support officiel et à jour

**Inconvénients :**
- Nécessite de réécrire la configuration build (pom.xml → build.gradle)
- Courbe d'apprentissage si vous n'êtes pas familier avec Gradle

**Ressources :**
- [PaperMC Project Setup](https://docs.papermc.io/paper/dev/project-setup/)
- [paperweight-userdev Documentation](https://docs.papermc.io/paper/dev/userdev/)

### Option 2 : Downgrade vers Paper 1.20.4 ou antérieur

**Avantages :**
- Configuration Maven actuelle fonctionnerait (avec ajustements mineurs)
- Plugin `paper-nms-maven-plugin` supporte ces versions

**Inconvénients :**
- Le plugin ne supportera pas Minecraft 1.21.4
- Nécessite de revoir le code NMS pour une version antérieure

### Option 3 : Attendre un support Maven pour 1.21.4

**Avantages :**
- Garde la configuration Maven actuelle
- Pas de réécriture nécessaire

**Inconvénients :**
- Délai incertain - le plugin `paper-nms-maven-plugin` est un projet tiers
- Aucune garantie qu'il supportera 1.21.4
- Bloque le développement en attendant

### Option 4 : Utiliser un Paper remappé local

**Avantages :**
- Garde Maven
- Compilation possible avec les bons JARs

**Inconvénients :**
- Complexe : nécessite de générer manuellement le JAR Paper remappé
- Nécessite BuildTools ou paperweight
- Configuration délicate et peu documentée
- Difficile à maintenir et partager avec d'autres développeurs

## Recommandation

**Pour un développement moderne avec Paper 1.21.4, la migration vers Gradle avec paperweight-userdev est la meilleure solution.**

C'est la seule approche officiellement supportée et maintenue par l'équipe Paper pour les versions 1.20.5+.

## État des Modifications

### Changements appliqués dans cette branche

- ✅ Configuration Maven multi-modules corrigée
- ✅ Propriétés de compilation Java 21 fixées (source/target au lieu de release)
- ✅ Plugin maven-shade configuré dans le module FarLandsAgain pour créer un JAR shadé
- ✅ Dépendances entre modules correctement établies
- ⚠️ Module v1_21_R1 temporairement désactivé (commenté dans pom.xml parent)
- ⚠️ Dépendance v1_21_R1 dans FarLandsAgain temporairement désactivée

### Fichiers modifiés

- `pom.xml` (parent) : Ordre des modules, configuration compiler plugin
- `FarLandsAgain/pom.xml` : Ajout maven-shade-plugin, dépendance v1_21_R1 commentée
- `Utility/pom.xml` : Propriétés redondantes supprimées
- `v1_21_R1/pom.xml` : Tentatives de configuration pour paper-nms-maven-plugin

## Prochaines Étapes

1. **Décider** quelle option choisir (Gradle recommandé)
2. **Si Gradle** : Créer les fichiers build.gradle et settings.gradle
3. **Si autre option** : Adapter selon le choix retenu
4. **Tester** la compilation complète
5. **Vérifier** le fonctionnement du plugin sur un serveur Paper 1.21.4

## Références

- [Paper NMS Maven Plugin (tiers)](https://github.com/Alvinn8/paper-nms-maven-plugin)
- [PaperMC Documentation Officielle](https://docs.papermc.io/paper/dev/)
- [Projet Original FarLandsAgain](https://github.com/OtakuMegane/FarLandsAgain) (archivé, supporte jusqu'à 1.18)
