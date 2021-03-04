
plugins {
    java
}

dependencies {
    compile(project(":model"))
    val light4jVersion: String by project
    compile("com.networknt", "utility", light4jVersion)
    compile("com.networknt", "http-entity", light4jVersion)
    val jacksonVersion: String by project
    compile("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    val slf4jVersion: String by project
    compile("org.slf4j", "slf4j-api", slf4jVersion)
    val undertowVersion: String by project
    compile("io.undertow", "undertow-core", undertowVersion)
    val logbackVersion: String by project
    compile("ch.qos.logback", "logback-classic", logbackVersion)
    val junitVersion: String by project
    testImplementation("junit", "junit", junitVersion)
}
