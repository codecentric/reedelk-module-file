package com.reedelk.file.internal.commons;

import com.reedelk.runtime.api.commons.FormattedMessage;

public class Messages {

    private Messages() {
    }

    public enum FileRead implements FormattedMessage {

        FILE_NAME_ERROR("Could not evaluate file with with name=[%s]"),
        FILE_IS_DIRECTORY("Could not read file=[%s]: is a directory"),
        FILE_LOCK_ERROR("Could not acquire lock on file=[%s]: %s"),
        FILE_READ_ERROR("Could not read file=[%s]: %s");

        private String message;

        FileRead(String message) {
            this.message = message;
        }

        @Override
        public String template() {
            return message;
        }
    }

    public enum FileWrite implements FormattedMessage {

        ERROR_FILE_NOT_FOUND("Could not find file=[%s]. Check that all the directories in the path exist already or enable the option 'Create directories' in the File Write component"),
        ERROR_FILE_WRITE_ALREADY_EXISTS("Could not write file=[%s]: the file already exists"),
        ERROR_FILE_WRITE_WITH_PATH("Could not write file with path=[%s]: %s"),
        ERROR_FILE_WRITE("Could not write file: %s");

        private String message;

        FileWrite(String message) {
            this.message = message;
        }

        @Override
        public String template() {
            return message;
        }
    }

    public enum FileDelete implements FormattedMessage {

        ERROR_FILE_DELETE("The file could not be deleted, cause=[%s].");

        private String message;

        FileDelete(String message) {
            this.message = message;
        }

        @Override
        public String template() {
            return message;
        }
    }

    public enum Misc implements FormattedMessage {

        FILE_NOT_FOUND("Could not find file=[%s]"),
        FILE_LOCK_MAX_RETRY_ERROR("Could not acquire lock on file=[%s]: %s"),
        MAX_ATTEMPTS_EXCEEDED("Max retry attempts (%d) exceeded");

        private String message;

        Misc(String message) {
            this.message = message;
        }

        @Override
        public String template() {
            return message;
        }
    }
}
