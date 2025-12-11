plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    // Paper API (provided par le serveur)
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    // Dépendances vers les modules du projet
    implementation(project(":Utility"))
    implementation(project(":v1_21_R1", configuration = "reobf"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")

        // Exclure les signatures
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }

    build {
        dependsOn(shadowJar)
    }
}