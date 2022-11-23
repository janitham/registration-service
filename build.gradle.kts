import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.google.cloud.tools.jib") version "3.2.1"
	id("maven-publish")
	id("org.unbroken-dome.helm") version "1.7.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"

}

group = "vote.california"
version = "0.0.1-SNAPSHOT"
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
		image = "amazoncorretto:17.0.3-alpine3.15"
		auth {
			username = System.getenv("DOCKER_USER")
			password = System.getenv("DOCKER_PASSWORD")
		}
	}
	to {
		image = "janithasen/continuous-delivery-example"
		tags = setOf("latest", "${project.version}")
		auth {
			username = System.getenv("DOCKER_USER")
			password = System.getenv("DOCKER_PASSWORD")
		}
	}
	container {
		mainClass = "com.zlrx.blog.githubactionk8scd.GithubActionK8sCdApplicationKt"
		ports = listOf("8080/tcp", "9000/tcp")
		appRoot = "/app"
		workingDirectory = "/app"
		creationTime = "USE_CURRENT_TIMESTAMP"
		jvmFlags = listOf(
			"-XX:InitialRAMPercentage=40.0",
			"-XX:MaxRAMPercentage=75.0",
		)
	}
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