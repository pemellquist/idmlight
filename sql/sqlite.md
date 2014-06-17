Some tips for using sqlite.

The openidm schema is defined in openidm.sql.

sqlite.sh will generate a default database file to be used in the openidm debian package. This includes creating all needed tables and initial values. Note, this will drop any existing tables.

Example how to look at sqlite db
--------------------------------
$sqlite3 tokenidm.db
sqlite> .tables
tenants  users  
sqlite> select * from tenants;
1|domain A|descr A|1
2|domain B|descr B|1
3|domain C|descr C|0
sqlite> select * from users;
1|user A|usera@gmail.com|changeme|descr user A|1
2|user B|userb@gmail.com|changeme|descr user B|1

sqlite>.schema users

