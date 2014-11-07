final touchFile = new File(basedir, "target/postgres.log");
assert touchFile.isFile()
final csvFile = new File(basedir, "target/sql-output.csv");
assert csvFile.isFile()
final csv = csvFile.text
assert '''encoding,pg_encoding_to_char,datcollate,datdba
6,UTF8,sv_SE,10

0 rows affected
'''.equals(csv)
