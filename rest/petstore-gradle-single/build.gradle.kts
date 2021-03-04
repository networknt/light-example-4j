
plugins {
    base
    java
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    val light4jVersion: String by project
    compile("com.networknt", "config", light4jVersion)
    compile("com.networknt", "utility", light4jVersion)
    compile("com.networknt", "security", light4jVersion)
    compile("com.networknt", "client", light4jVersion)
    compile("com.networknt", "audit", light4jVersion)
    compile("com.networknt", "info", light4jVersion)
    compile("com.networknt", "health", light4jVersion)
    compile("com.networknt", "status", light4jVersion)
    compile("com.networknt", "exception", light4jVersion)
    compile("com.networknt", "body", light4jVersion)
    compile("com.networknt", "dump", light4jVersion)
    compile("com.networknt", "mask", light4jVersion)
    
    compile("com.networknt", "metrics", light4jVersion)
    
    compile("com.networknt", "handler", light4jVersion)
    compile("com.networknt", "sanitizer", light4jVersion)
    compile("com.networknt", "traceability", light4jVersion)
    compile("com.networknt", "correlation", light4jVersion)
    compile("com.networknt", "service", light4jVersion)
    compile("com.networknt", "registry", light4jVersion)
    compile("com.networknt", "balance", light4jVersion)
    compile("com.networknt", "cluster", light4jVersion)
    compile("com.networknt", "portal-registry", light4jVersion)
    compile("com.networknt", "logger-config", light4jVersion)
    compile("com.networknt", "decryptor", light4jVersion)
    compile("com.networknt", "server", light4jVersion)
    compile("com.networknt", "openapi-parser", light4jVersion)
    compile("com.networknt", "openapi-meta", light4jVersion)
    compile("com.networknt", "openapi-security", light4jVersion)
    compile("com.networknt", "openapi-validator", light4jVersion)
    compile("com.networknt", "specification", light4jVersion)
    compile("com.networknt", "http-entity", light4jVersion)
    
    
    
    val jacksonVersion: String by project
    compile("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    val jsonSchemaValidatorVersion: String by project
    compile("com.networknt", "json-schema-validator", jsonSchemaValidatorVersion)
    val logbackVersion: String by project
    compile("ch.qos.logback", "logback-classic", logbackVersion)
    val undertowVersion: String by project
    compile("io.undertow", "undertow-core", undertowVersion)
    
    
    
    
    
    val junitVersion: String by project
    testImplementation("junit", "junit", junitVersion)
    
}

group = "com.networknt"
version = "3.0.1"
repositories {
    mavenLocal() // mavenLocal must be added first.
    jcenter()
    
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

