package dev.aihub.manage;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

/** Local single-process JSON storage. A corrupt file stops startup rather than silently resetting data. */
final class StateFile<T> {
    private final ObjectMapper mapper;
    private final Path path;
    private final Class<T> type;

    StateFile(ObjectMapper mapper, String filename, Class<T> type) {
        this.mapper = mapper;
        this.path = Path.of(filename).toAbsolutePath();
        this.type = type;
    }
    Optional<T> read() {
        if (!Files.exists(path)) return Optional.empty();
        try { return Optional.of(mapper.readValue(path.toFile(), type)); }
        catch (IOException e) { throw new IllegalStateException("Cannot read data file " + path + "; restore a backup or repair it before starting", e); }
    }
    void write(T value) {
        try {
            Files.createDirectories(path.getParent());
            Path temporary = Files.createTempFile(path.getParent(), ".ai-hub-", ".tmp");
            try {
                mapper.writeValue(temporary.toFile(), value);
                try { Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING); }
                catch (AtomicMoveNotSupportedException e) { Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING); }
            } finally { Files.deleteIfExists(temporary); }
        } catch (IOException e) { throw new IllegalStateException("Cannot write data file " + path, e); }
    }
}
