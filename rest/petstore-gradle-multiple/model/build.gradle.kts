
plugins {
    java
}

dependencies {
    val jacksonVersion: String by project
    compile("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    val slf4jVersion: String by project
    compile("org.slf4j", "slf4j-api", slf4jVersion)
    val junitVersion: String by project
    testImplementation("junit", "junit", junitVersion)
}
