#!/bin/bash

# Script pour nettoyer la configuration IntelliJ IDEA et forcer une resynchronisation Gradle

echo "🧹 Nettoyage de la configuration IntelliJ IDEA..."

# Supprimer les fichiers .iml (configuration des modules IntelliJ)
echo "Suppression des fichiers .iml..."
find . -name "*.iml" -type f -delete

# Supprimer le dossier .idea/modules (cache des modules)
if [ -d ".idea/modules" ]; then
    echo "Suppression du cache des modules..."
    rm -rf .idea/modules
fi

# Nettoyer et reconstruire avec Gradle
echo ""
echo "🔨 Nettoyage et reconstruction avec Gradle..."
./gradlew clean build --refresh-dependencies

echo ""
echo "✅ Nettoyage terminé!"
echo ""
echo "📌 Prochaines étapes dans IntelliJ IDEA:"
echo "   1. Fermer le projet (File > Close Project)"
echo "   2. Rouvrir le projet"
echo "   3. Attendre la synchronisation Gradle automatique"
echo "   4. OU cliquer sur l'icône 🔄 dans la fenêtre Gradle"
echo ""
