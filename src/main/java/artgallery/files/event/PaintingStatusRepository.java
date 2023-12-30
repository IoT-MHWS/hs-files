package artgallery.files.event;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class PaintingStatusRepository {
  private final ArrayList<String> activeSessions = new ArrayList<>();
  private int idx = 0;

  public synchronized void add(String sessionId) {
    activeSessions.add(sessionId);
  }

  public synchronized void remove(String sessionId) {
    activeSessions.remove(sessionId);
  }

  // Round-robin implementation
  public synchronized String getNext() {
    if (activeSessions.isEmpty()) {
      return null;
    }
    return activeSessions.get(idx++ % activeSessions.size());
  }
}
