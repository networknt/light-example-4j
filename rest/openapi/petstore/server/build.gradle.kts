
plugins {
    application
}

application {
    mainClassName = "com.networknt.sever.Server"
}

dependencies {
    implementation(project(":model"))
    implementation(project(":service"))
    val light4jVersion: String by project
    implementation("com.networknt", "config", light4jVersion)
    implementation("com.networknt", "utility", light4jVersion)
    implementation("com.networknt", "security", light4jVersion)
    implementation("com.networknt", "client", light4jVersion)
    implementation("com.networknt", "audit", light4jVersion)
    implementation("com.networknt", "info", light4jVersion)
    implementation("com.networknt", "health", light4jVersion)
    implementation("com.networknt", "status", light4jVersion)
    implementation("com.networknt", "exception", light4jVersion)
    implementation("com.networknt", "body", light4jVersion)
    implementation("com.networknt", "dump", light4jVersion)
    implementation("com.networknt", "mask", light4jVersion)
    
    implementation("com.networknt", "metrics", light4jVersion)
    
    implementation("com.networknt", "handler", light4jVersion)
    implementation("com.networknt", "sanitizer", light4jVersion)
    implementation("com.networknt", "traceability", light4jVersion)
    implementation("com.networknt", "correlation", light4jVersion)
    implementation("com.networknt", "service", light4jVersion)
    implementation("com.networknt", "registry", light4jVersion)
    implementation("com.networknt", "balance", light4jVersion)
    implementation("com.networknt", "cluster", light4jVersion)
    implementation("com.networknt", "portal-registry", light4jVersion)
    implementation("com.networknt", "logger-config", light4jVersion)
    implementation("com.networknt", "decryptor", light4jVersion)
    implementation("com.networknt", "server", light4jVersion)
    implementation("com.networknt", "openapi-parser", light4jVersion)
    implementation("com.networknt", "openapi-meta", light4jVersion)
    implementation("com.networknt", "openapi-security", light4jVersion)
    implementation("com.networknt", "openapi-validator", light4jVersion)
    implementation("com.networknt", "specification", light4jVersion)
    
    
    
    val jacksonVersion: String by project
    implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    val jsonSchemaValidatorVersion: String by project
    implementation("com.networknt", "json-schema-validator", jsonSchemaValidatorVersion)
    val logbackVersion: String by project
    implementation("ch.qos.logback", "logback-classic", logbackVersion)
    val undertowVersion: String by project
    implementation("io.undertow", "undertow-core", undertowVersion)
    
    
    
    
    
    val junitVersion: String by project
    testImplementation("junit", "junit", junitVersion)
    
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "com.networknt.server.Server"
    }
    from(configurations.runtime.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
