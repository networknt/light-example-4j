
plugins {
    java
}

dependencies {
    val jacksonVersion: String by project
    implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    val slf4jVersion: String by project
    implementation("org.slf4j", "slf4j-api", slf4jVersion)
    val junitVersion: String by project
    testImplementation("junit", "junit", junitVersion)
}
