# Configuration IntelliJ IDEA pour FLupdate

## Probleme : Erreurs "Cannot resolve symbol"

Si IntelliJ IDEA affiche des erreurs comme :
- `Cannot resolve symbol 'java'`
- `Cannot resolve symbol 'String'`
- `Cannot resolve symbol 'Override'`

C'est que l'IDE n'a pas synchronise le projet Gradle correctement.

## Solution Rapide : Script de Reset (RECOMMANDE)

Executez le script de nettoyage fourni :

```bash
./reset-intellij.sh
```

Puis dans IntelliJ IDEA :
1. **File > Close Project**
2. **Rouvrir le projet**
3. Attendre la synchronisation automatique

## Solution Manuelle : Synchroniser avec Gradle

### Methode 1 : Via le bouton Gradle (RECOMMANDE)

1. Ouvrir la fenetre **Gradle** (View > Tool Windows > Gradle)
2. Cliquer sur l'icone **Reload All Gradle Projects** (🔄)
3. Attendre la fin de la synchronisation

### Methode 2 : Via le menu

1. Aller dans **File > Invalidate Caches / Restart...**
2. Selectionner **Invalidate and Restart**
3. Une fois IntelliJ redémarre, il devrait synchroniser automatiquement

### Methode 3 : Via la ligne de commande puis reimport

```bash
# Dans le terminal
./gradlew clean build --refresh-dependencies
```

Puis dans IntelliJ :
1. **File > Close Project**
2. **Open** le projet à nouveau
3. Choisir **Open as Gradle Project** si demande

## Verification

Apres la synchronisation :
- Les imports Java (`java.util.*`) doivent etre reconnus
- Les classes Bukkit (`org.bukkit.*`) doivent etre reconnues
- Aucune erreur rouge dans les fichiers `.java`

## Notes

- Les erreurs dans les fichiers `.md` (markdown) sont normales - ils ne doivent pas etre compiles
- Le projet compile correctement via Gradle meme si l'IDE affiche des erreurs
- Gradle est la source de verite - si `./gradlew build` fonctionne, le code est correct

## Build et Test

Pour compiler sans passer par l'IDE :

```bash
# Compiler tout le projet
./gradlew clean build

# Le JAR final est genere ici :
FarLandsAgain/build/libs/FarLandsAgain-2.6.jar
```
