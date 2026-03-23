package com.oceanlk.backend.config;

import com.oceanlk.backend.model.MediaItem;
import com.oceanlk.backend.repository.MediaItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataMigrationComponent {

    private final MediaItemRepository mediaItemRepository;

    @PostConstruct
    public void migrateMediaItems() {
        System.out.println("[DataMigration] @PostConstruct migrateMediaItems() starting...");
        System.out.println("[DataMigration] SKIPPING migration during local development - invalid MongoDB credentials");
        return;
        
        /*
        try {
            System.out.println("[DataMigration] Attempting to connect and query MongoDB for MediaItems...");
            long startTime = System.currentTimeMillis();
            
            List<MediaItem> items = mediaItemRepository.findAll();
            
            long queryTime = System.currentTimeMillis() - startTime;
            System.out.println("[DataMigration] MongoDB query successful! Retrieved " + items.size() + " items in " + queryTime + "ms");
            
            boolean changed = false;

            for (MediaItem item : items) {
                if (item.getGroup() == null) {
                    // Determine group based on category
                    String category = item.getCategory();
                    if ("LIFE_AT_OCH".equalsIgnoreCase(category)) {
                        item.setGroup("HR_PANEL");
                    } else if ("NEWS".equalsIgnoreCase(category) ||
                            "BLOG".equalsIgnoreCase(category) ||
                            "MEDIA".equalsIgnoreCase(category) ||
                            "PRESS_RELEASE".equalsIgnoreCase(category)) {
                        item.setGroup("MEDIA_PANEL");
                    } else {
                        // Default for mixed categories like GALLERY/EVENTS
                        // Most existing GALLERY items are likely from Media Panel
                        item.setGroup("MEDIA_PANEL");
                    }
                    mediaItemRepository.save(item);
                    changed = true;
                }
            }

            if (changed) {
                System.out.println("[DataMigration] Updated existing MediaItems with group field.");
            }
            System.out.println("[DataMigration] migrateMediaItems() completed successfully");
        } catch (Exception e) {
            System.err.println("[DataMigration] ERROR in migrateMediaItems(): " + e.getClass().getSimpleName());
            System.err.println("[DataMigration] Error message: " + e.getMessage());
            System.err.println("[DataMigration] Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "No cause"));
            e.printStackTrace();
            throw new RuntimeException("DataMigration failed - application cannot start", e);
        }
        */
    }
}
