sudo service openidm stop 
echo "dropping all tables..."
sleep 3
sudo sqlite3 /opt/openidm/openidm.db < ../sql/openidm.sql
sudo service openidm start
