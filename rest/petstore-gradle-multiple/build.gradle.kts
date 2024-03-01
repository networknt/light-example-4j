
plugins {
    base
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

allprojects {
    group = "com.networknt.example"
    version = "3.0.1"
    repositories {
        mavenLocal() // mavenLocal must be added first.
        jcenter()

    }
}

dependencies {
    // Make the root project archives configuration depend on every sub-project
    subprojects.forEach {
        archives(it)
    }
}
