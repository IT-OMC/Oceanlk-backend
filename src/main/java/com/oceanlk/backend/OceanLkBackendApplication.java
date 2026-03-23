package com.oceanlk.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OceanLkBackendApplication {

	public static void main(String[] args) {
		System.out.println("[STARTUP] OceanLkBackendApplication.main() starting...");
		
		try {
			System.out.println("[STARTUP] Loading .env file...");
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();
			System.out.println("[STARTUP] .env file loaded successfully");
			
			dotenv.entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
				if (entry.getKey().contains("MONGODB") || entry.getKey().contains("JWT") || entry.getKey().contains("PORT")) {
					System.out.println("[STARTUP] Loaded env var: " + entry.getKey());
				}
			});
			
			System.out.println("[STARTUP] MONGODB_URI set to: " + System.getProperty("MONGODB_URI"));
			System.out.println("[STARTUP] SERVER_PORT: " + System.getProperty("SERVER_PORT", "8080"));
			System.out.println("[STARTUP] SPRING_PROFILES_ACTIVE: " + System.getProperty("SPRING_PROFILES_ACTIVE", "default"));
		} catch (Exception e) {
			System.err.println(
					"[STARTUP] Warning: .env file could not be loaded. Falling back to environment variables and defaults. "
						+ e.getMessage());
		}

		System.out.println("[STARTUP] Calling SpringApplication.run()...");
		SpringApplication.run(OceanLkBackendApplication.class, args);
		System.out.println("[STARTUP] SpringApplication.run() completed");
	}

}
