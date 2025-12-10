plugins {
    id("io.papermc.paperweight.userdev")
}

dependencies {
    // Paper API et NMS avec Mojang mappings via paperweight
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")

    // Dépendance vers le module Utility
    implementation(project(":Utility"))
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}