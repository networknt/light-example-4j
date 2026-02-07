
plugins {
    base
    java
    application
}


java {
    // sourceCompatibility = JavaVersion.VERSION_11
    // targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenLocal()
    mavenCentral()
}

val light4jVersion: String by project
val jacksonVersion: String by project
val jsonSchemaValidatorVersion: String by project
val logbackVersion: String by project
val undertowVersion: String by project
val junitVersion: String by project

dependencies {
    implementation("com.networknt:config:$light4jVersion")
    implementation("com.networknt:utility:$light4jVersion")
    implementation("com.networknt:security:$light4jVersion")
    implementation("com.networknt:client:$light4jVersion")
    implementation("com.networknt:audit:$light4jVersion")
    implementation("com.networknt:info:$light4jVersion")
    implementation("com.networknt:health:$light4jVersion")
    implementation("com.networknt:status:$light4jVersion")
    implementation("com.networknt:exception:$light4jVersion")
    implementation("com.networknt:body:$light4jVersion")
    implementation("com.networknt:dump:$light4jVersion")
    implementation("com.networknt:mask:$light4jVersion")
    implementation("com.networknt:metrics:$light4jVersion")
    implementation("com.networknt:handler:$light4jVersion")
    implementation("com.networknt:sanitizer:$light4jVersion")
    implementation("com.networknt:traceability:$light4jVersion")
    implementation("com.networknt:correlation:$light4jVersion")
    implementation("com.networknt:service:$light4jVersion")
    implementation("com.networknt:registry:$light4jVersion")
    implementation("com.networknt:balance:$light4jVersion")
    implementation("com.networknt:cluster:$light4jVersion")
    implementation("com.networknt:portal-registry:$light4jVersion")
    implementation("com.networknt:logger-config:$light4jVersion")
    implementation("com.networknt:decryptor:$light4jVersion")
    implementation("com.networknt:server:$light4jVersion")
    implementation("com.networknt:openapi-parser:$light4jVersion")
    implementation("com.networknt:openapi-meta:$light4jVersion")
    implementation("com.networknt:openapi-security:$light4jVersion")
    implementation("com.networknt:openapi-validator:$light4jVersion")
    implementation("com.networknt:specification:$light4jVersion")
    implementation("com.networknt:http-entity:$light4jVersion")

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.networknt:json-schema-validator:$jsonSchemaValidatorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.undertow:undertow-core:$undertowVersion")

    testImplementation("junit:junit:$junitVersion")
}

group = "com.networknt"
version = "3.0.1"

application {
    mainClass.set("com.networknt.server.Server")
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "com.networknt.server.Server"
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = version
    }
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set("${project.name}-fat")
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "com.networknt.server.Server"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
