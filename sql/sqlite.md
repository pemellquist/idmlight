Some tips for using sqlite.

The tokenidm schema is defined in tokenidm.sql . This schema can be used with other databases, but for this development environment sqlite is used.

sqlite.sh will generate a default database file to be used in the tokenidm debian package. This includes creating all needed tables and initial values. Note, this will drop any existing tables.

$sqlite3 tokenidm.db
sqlite> .tables
tenants  users  
sqlite> select * from tenants;
1|tenant A|descr A|1
2|tenant B|descr B|1
3|tenant C|descr C|0
sqlite> select * from users;
1|user A|usera@gmail.com|changeme|descr user A|1
2|user B|userb@gmail.com|changeme|descr user B|1
sqlite> 

