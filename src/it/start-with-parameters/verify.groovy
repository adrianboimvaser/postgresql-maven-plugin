// The normal logfile postgres.log should not be used, since the server is configured with logging_collector=true
File stdErrFile = new File(basedir, "target/postgres.log");
assert stdErrFile.isFile()
assert stdErrFile.text.length() == 0
// Check that the file name that we specified gets used and has the right content
File customLogFile = new File(basedir, "target/postgres-startstopcustom.log");
String fileContents = customLogFile.text
assert fileContents.length() > 0
assert fileContents =~ "database system is ready to accept connections"
assert fileContents =~ "database system is shut down"