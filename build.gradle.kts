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
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

springBoot {
	buildInfo()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("io.micrometer:micrometer-registry-prometheus")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	implementation("org.springframework.boot:spring-boot-starter-security")
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
	}
	to {
		val sanitizedVersion = version.toString().replace('+', '_')
		image = "registration-service"
		tags = setOf("latest", "$sanitizedVersion")
	}
	container {
		ports = listOf("8080/tcp", "9000/tcp")
		creationTime = "USE_CURRENT_TIMESTAMP"
	}
}

task<Exec>("devVersion") {
	val sanitizedVersion = version.toString().replace('+', '_')
	commandLine("cmd","/c", "echo", "$sanitizedVersion",">", "dVersion")
}