sudo service tokenidm stop 
echo "dropping all tables..."
sleep 3
sudo sqlite3 /opt/tokenidm/tokenidm.db < ../sql/tokenidm.sql
sudo service tokenidm start
