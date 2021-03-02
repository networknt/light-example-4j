
plugins {
    java
}

dependencies {
    val light4jVersion: String by project
    implementation("com.networknt", "service", light4jVersion)
    implementation("com.networknt", "client", light4jVersion)
    val jacksonVersion: String by project
    implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    val slf4jVersion: String by project
    implementation("org.slf4j", "slf4j-api", slf4jVersion)
    val undertowVersion: String by project
    implementation("io.undertow", "undertow-core", undertowVersion)
    val logbackVersion: String by project
    implementation("ch.qos.logback", "logback-classic", logbackVersion)
    val junitVersion: String by project
    testImplementation("junit", "junit", junitVersion)
}
