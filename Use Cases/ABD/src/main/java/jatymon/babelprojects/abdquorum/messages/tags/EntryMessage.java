package jatymon.babelprojects.abdquorum.messages.tags;

import jatymon.babelprojects.abdquorum.data.Database;

public interface EntryMessage {
    Database.Entry getEntry();
}
