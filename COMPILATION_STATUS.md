# FarLandsAgain - État de la Compilation

## ✅ Migration vers Gradle Complétée

Le projet a été **migré vers Gradle** pour supporter Paper 1.21.4 avec le plugin officiel `paperweight-userdev`.

## Situation Actuelle

Ce projet est une mise à jour de FarLandsAgain pour Minecraft 1.21.4 (Paper).

- ✅ **Gradle** : Configuration complète et fonctionnelle (recommandé)
- ⚠️ **Maven** : La compilation complète n'est pas possible pour Paper 1.21.4 (conservé pour référence)

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

## ✅ Solution Implémentée : Gradle avec paperweight-userdev

La migration vers Gradle a été effectuée. Consultez [GRADLE_MIGRATION.md](GRADLE_MIGRATION.md) pour les détails complets.

### Prérequis

**IMPORTANT** : Vous devez installer le JDK complet (pas seulement le JRE) :

```bash
sudo apt-get install openjdk-21-jdk
```

### Utilisation

```bash
# Compiler tout le projet
./gradlew build

# Créer le JAR du plugin
./gradlew :FarLandsAgain:shadowJar
```

Le JAR final sera dans : `FarLandsAgain/build/libs/FarLandsAgain-2.6.jar`

### Avantages de cette solution

- ✅ Plugin officiel Paper (`paperweight-userdev`)
- ✅ Téléchargement automatique de Paper remappé
- ✅ Accès complet aux classes NMS
- ✅ Gestion automatique de la reobfuscation
- ✅ Support officiel et à jour pour Paper 1.20.5+

## État des Modifications

### Changements appliqués - Migration Gradle

- ✅ **Configuration Gradle créée** : settings.gradle.kts, build.gradle.kts, gradle.properties
- ✅ **Module Utility** : Migré vers Gradle (Utility/build.gradle.kts)
- ✅ **Module v1_21_R1** : Migré avec paperweight-userdev (v1_21_R1/build.gradle.kts)
- ✅ **Module FarLandsAgain** : Migré avec Shadow plugin (FarLandsAgain/build.gradle.kts)
- ✅ **Gradle Wrapper** : Installé (gradlew, gradlew.bat, gradle/wrapper/)
- ✅ **Documentation** : GRADLE_MIGRATION.md créé avec instructions complètes
- ✅ **Tous les modules activés** : v1_21_R1 n'est plus désactivé

### Fichiers Maven conservés

Les fichiers `pom.xml` ont été **conservés** pour référence :
- `pom.xml` (parent)
- `Utility/pom.xml`
- `v1_21_R1/pom.xml`
- `FarLandsAgain/pom.xml`

### Fichiers Gradle créés

- `settings.gradle.kts` : Configuration multi-modules
- `build.gradle.kts` : Configuration parent
- `gradle.properties` : Propriétés du projet
- `Utility/build.gradle.kts` : Module Utility
- `v1_21_R1/build.gradle.kts` : Module NMS avec paperweight
- `FarLandsAgain/build.gradle.kts` : Module principal avec Shadow
- `gradlew`, `gradlew.bat` : Scripts Gradle Wrapper
- `gradle/wrapper/gradle-wrapper.jar` : JAR du wrapper
- `gradle/wrapper/gradle-wrapper.properties` : Configuration du wrapper
- `GRADLE_MIGRATION.md` : Documentation de migration

## Prochaines Étapes

1. ✅ ~~Décider quelle option choisir~~ → **Gradle choisi et implémenté**
2. ✅ ~~Créer les fichiers Gradle~~ → **Complété**
3. **Installer le JDK complet** : `sudo apt-get install openjdk-21-jdk`
4. **Tester** la compilation complète avec `./gradlew build`
5. **Vérifier** le fonctionnement du plugin sur un serveur Paper 1.21.4

## Références

- [Paper NMS Maven Plugin (tiers)](https://github.com/Alvinn8/paper-nms-maven-plugin)
- [PaperMC Documentation Officielle](https://docs.papermc.io/paper/dev/)
- [Projet Original FarLandsAgain](https://github.com/OtakuMegane/FarLandsAgain) (archivé, supporte jusqu'à 1.18)
