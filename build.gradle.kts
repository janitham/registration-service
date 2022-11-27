import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.google.cloud.tools.jib") version "3.2.1"
	id("maven-publish")
	id("org.unbroken-dome.helm") version "1.7.0"
	id("nebula.release") version "17.1.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "vote.california"
//version = "0.1.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

/*extra["snippetsDir"] = file("build/generated-snippets")
extra["testcontainersVersion"] = "1.17.6"*/

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
//	testImplementation("org.testcontainers:junit-jupiter")
//	testImplementation("org.testcontainers:mongodb")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib{
	from {
		image = "dvmarques/openjdk-17-jdk-alpine-with-timezone@sha256:cc26b502a9aa7015fc6927963a1ba4d453d76f54360df7ce9a3f32b75cb492c1"
		/*auth {
			username = System.getenv("DOCKER_USER")
			password = System.getenv("DOCKER_PASSWORD")
		}*/
	}
	to {
		val sanitizedVersion = version.toString().replace('+', '_')
		image = "registration-service"
		tags = setOf("latest", "$sanitizedVersion")
	}
	container {
		//mainClass = "vote.california.registrationservice.RegistrationServiceApplication"
		ports = listOf("8080/tcp", "9000/tcp")
		//appRoot = "/app"
		//workingDirectory = "/app"
		creationTime = "USE_CURRENT_TIMESTAMP"
		//jvmFlags = listOf(
		//	"-XX:InitialRAMPercentage=40.0",
		//	"-XX:MaxRAMPercentage=75.0",
		//)
	}
}

task<Exec>("devVersion") {
	commandLine("cmd","/c", "echo", "$version",">", "dVersion")
}

task<Exec>("updateDev") {
	commandLine("sed", "-i","/tag/c\\  tag: $version", "env/dev/charts/registration/values.yaml")
}

task<Exec>("subTreePull") {
    commandLine("git", "subtree", "pull", "--prefix", "env/dev/", "https://github.com/janitham/cmm707-gitops.git", "master", "--squash")
}

task<Exec>("subTreePush") {
	commandLine("git", "subtree", "push", "--prefix", "env/dev/", "https://github.com/janitham/cmm707-gitops.git", "master")
}

task<Exec>("gitAddAll") {
	commandLine("git", "add", "--all")
}

task<Exec>("gitCommit") {
	commandLine("git", "commit", "-m", "updating version")
}

task<Exec>("gitUser") {
    commandLine("git", "config", "user.name", "Action")
}