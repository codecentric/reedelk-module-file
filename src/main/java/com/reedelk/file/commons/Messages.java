package com.reedelk.file.commons;

public class Messages {

    private Messages() {
    }

    private static String formatMessage(String template, Object ...args) {
        return String.format(template, args);
    }

    interface FormattedMessage {
        String format(Object ...args);
    }

    public enum FileReadComponent implements FormattedMessage {

        FILE_NAME_ERROR("Could not evaluate file with with name=[%s]"),
        FILE_IS_DIRECTORY("Could not read file=[%s]: is a directory"),
        FILE_LOCK_ERROR("Could not acquire lock on file=[%s]: %s"),
        FILE_READ_ERROR("Could not read file=[%s]: %s");

        private String msg;

        FileReadComponent(String msg) {
            this.msg = msg;
        }

        @Override
        public String format(Object... args) {
            return formatMessage(msg, args);
        }
    }

    public enum ModuleFileReadComponent implements FormattedMessage {

        FILE_NOT_FOUND("Could not find file with name[%s], base path=[%s] in module with id=[%d]");

        private String msg;

        ModuleFileReadComponent(String msg) {
            this.msg = msg;
        }

        @Override
        public String format(Object... args) {
            return formatMessage(msg, args);
        }
    }

    public enum FileWriteComponent implements FormattedMessage {

        ERROR_FILE_NOT_FOUND("Could not find file=[%s]. Check that all the directories in the path exist already or enable the option 'Create directories' in the File Write component"),
        ERROR_FILE_WRITE_ALREADY_EXISTS("Could not write file=[%s]: the file already exists"),
        ERROR_FILE_WRITE_WITH_PATH("Could not write file with path=[%s]: %s"),
        ERROR_FILE_WRITE("Could not write file: %s");

        private String msg;

        FileWriteComponent(String msg) {
            this.msg = msg;
        }

        @Override
        public String format(Object... args) {
            return formatMessage(msg, args);
        }

    }

    public enum Misc implements FormattedMessage {

        FILE_NOT_FOUND("Could not find file=[%s]"),
        FILE_LOCK_MAX_RETRY_ERROR("Could not acquire lock on file=[%s]: %s"),
        MAX_ATTEMPTS_EXCEEDED("Max retry attempts (%d) exceeded");

        private String msg;

        Misc(String msg) {
            this.msg = msg;
        }

        @Override
        public String format(Object... args) {
            return formatMessage(msg, args);
        }

    }
}
