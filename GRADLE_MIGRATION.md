# Migration vers Gradle - FarLandsAgain 1.21.4

## ✅ Migration Complétée

Le projet a été migré de Maven vers Gradle pour supporter Paper 1.21.4 avec le plugin officiel `paperweight-userdev`.

### Fichiers Gradle créés

- `settings.gradle.kts` - Configuration des modules
- `build.gradle.kts` - Configuration racine du projet
- `gradle.properties` - Propriétés du projet
- `Utility/build.gradle.kts` - Module Utility
- `v1_21_R1/build.gradle.kts` - Module NMS avec paperweight-userdev
- `FarLandsAgain/build.gradle.kts` - Module principal avec Shadow plugin
- `gradlew` et `gradlew.bat` - Scripts Gradle Wrapper
- `gradle/wrapper/` - Fichiers du Wrapper Gradle 8.12

### Structure du projet

```
FLupdate/
├── Utility/              # Classes utilitaires partagées
├── v1_21_R1/            # Code NMS pour Paper 1.21.4
└── FarLandsAgain/       # Plugin principal
```

## 📋 Prérequis pour la Compilation

### JDK Requis

Pour compiler le projet avec Gradle, vous devez installer le **JDK complet** (pas seulement le JRE) :

```bash
# Sur Ubuntu/Debian
sudo apt-get update
sudo apt-get install openjdk-21-jdk

# Vérifier l'installation
javac -version
```

### Alternative : Téléchargement manuel du JDK

Si vous n'avez pas les droits sudo, vous pouvez télécharger et installer un JDK localement :

1. Téléchargez OpenJDK 21 depuis [Adoptium](https://adoptium.net/temurin/releases/)
2. Extrayez l'archive dans un dossier local
3. Définissez JAVA_HOME :
   ```bash
   export JAVA_HOME=/chemin/vers/jdk-21
   export PATH=$JAVA_HOME/bin:$PATH
   ```

## 🔨 Commandes Gradle

### Compiler le projet
```bash
./gradlew build
```

### Nettoyer et compiler
```bash
./gradlew clean build
```

### Compiler uniquement FarLandsAgain
```bash
./gradlew :FarLandsAgain:build
```

### Créer le JAR final
```bash
./gradlew :FarLandsAgain:shadowJar
```

Le JAR du plugin sera dans : `FarLandsAgain/build/libs/FarLandsAgain-2.6.jar`

## 🎯 Avantages de Gradle

### Pourquoi Gradle pour Paper 1.21.4 ?

1. **paperweight-userdev** - Plugin officiel Paper qui :
   - Télécharge automatiquement Paper remappé avec Mojang mappings
   - Donne accès aux classes NMS internes
   - Gère automatiquement la reobfuscation du code NMS

2. **Support officiel** - Gradle est la méthode recommandée par Paper pour les versions 1.20.5+

3. **Plus simple** - Pas besoin de :
   - Télécharger manuellement les JARs Paper
   - Configurer des dépendances system locales
   - Installer des plugins Maven tiers non maintenus

4. **IDE Integration** - Meilleur support dans IntelliJ IDEA et autres IDEs

## 📖 Différences avec Maven

### Dépendances NMS

**Maven (ancienne méthode) :**
```xml
<!-- Ne fonctionne plus pour Paper 1.21.4 -->
<dependency>
    <groupId>io.papermc.paper</groupId>
    <artifactId>paper-server</artifactId>
    <scope>system</scope>
    <systemPath>${project.basedir}/libs/paper.jar</systemPath>
</dependency>
```

**Gradle (nouvelle méthode) :**
```kotlin
// Télécharge et configure automatiquement
paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
```

### Ombre / Shadow

**Maven :** `maven-shade-plugin`
**Gradle :** `com.github.johnrengelman.shadow`

Même fonctionnalité : créer un JAR avec toutes les dépendances incluses.

## 🔄 Fichiers Maven conservés

Les fichiers `pom.xml` ont été **conservés** pour référence et compatibilité. Vous pouvez :
- Utiliser Gradle (recommandé pour Paper 1.21.4)
- Garder Maven (si vous downgrade vers Paper 1.20.4 ou antérieur)

## 📚 Ressources

- [Documentation Paper - Project Setup](https://docs.papermc.io/paper/dev/project-setup/)
- [Documentation paperweight-userdev](https://docs.papermc.io/paper/dev/userdev/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)

## ⚙️ Configuration

### Versions utilisées

- **Gradle** : 8.12
- **Java** : 21
- **Paper** : 1.21.4-R0.1-SNAPSHOT
- **paperweight-userdev** : 2.0.0-beta.12
- **Shadow Plugin** : 8.1.1

### Modifications possibles

Pour changer la version de Paper, éditez `v1_21_R1/build.gradle.kts` :
```kotlin
paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
```

Pour changer la version du projet, éditez `gradle.properties` :
```properties
version=2.6
```

## 🐛 Résolution de problèmes

### Erreur: "javac: command not found"
→ Installez le JDK complet (voir section Prérequis)

### Erreur: "paperweightDevelopmentBundle contains more than one file"
→ Nettoyez le cache Gradle: `./gradlew clean --refresh-dependencies`

### Le build est lent la première fois
→ C'est normal, Gradle télécharge et configure Paper. Les builds suivants seront plus rapides.

### Erreurs NMS après compilation
→ Assurez-vous d'utiliser le JAR reobfusqué: `v1_21_R1/build/libs/v1_21_R1-2.6-reobf.jar`

## 📝 Notes

- Les fichiers dans `v1_21_R1/libs/` ne sont plus nécessaires pour Gradle mais ont été conservés
- Le module v1_21_R1 est maintenant activé (il était commenté dans Maven)
- La compilation génère des JARs reobfusqués automatiquement pour v1_21_R1
- Le JAR final de FarLandsAgain inclut automatiquement Utility et v1_21_R1
