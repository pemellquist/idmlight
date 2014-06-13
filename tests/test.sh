# GLOBAL VARS
TARGET="localhost"
TESTCOUNT=0
PASSCOUNT=0
FAILCOUNT=0

getit() {
((TESTCOUNT++))
echo '['$TESTCOUNT']' $NAME
STATUS=$(curl -X GET -k -s -H Accept:application/json -o result.json -w '%{http_code}' $URL)
if [ $STATUS -eq $PASSCODE ]; then
   ((PASSCOUNT++))
   cat result.json | python -mjson.tool
   echo "[PASS]"
else
   echo "[FAIL] Status=" $STATUS
   ((FAILCOUNT++))
fi
echo
}

deleteit() {
((TESTCOUNT++))
echo '['$TESTCOUNT']' $NAME
STATUS=$(curl -X DELETE -k -s -H Accept:application/json -o result.json -w '%{http_code}' $URL)
if [ $STATUS -eq $PASSCODE ]; then
   ((PASSCOUNT++))
   echo "[PASS]"
else
   echo "[FAIL] Status=" $STATUS
   ((FAILCOUNT++))
fi
echo
}

postit() {
((TESTCOUNT++))
echo '['$TESTCOUNT']' $NAME
STATUS=$(curl -X POST -k -s -H "Content-type:application/json" --data-binary "@"$POSTFILE -o result.json -w '%{http_code}' $URL)
if [ $STATUS -eq $PASSCODE ]; then
   ((PASSCOUNT++))
   cat result.json | python -mjson.tool
   echo "[PASS]"
else
   echo "[FAIL] Status=" $STATUS
   ((FAILCOUNT++))
fi
echo
}

putit() {
((TESTCOUNT++))
echo '['$TESTCOUNT']' $NAME
STATUS=$(curl -X PUT -k -s -H "Content-type:application/json" --data-binary "@"$PUTFILE -o result.json -w '%{http_code}' $URL)
if [ $STATUS -eq $PASSCODE ]; then
   ((PASSCOUNT++))
   cat result.json | python -mjson.tool
   echo "[PASS]"
else
   echo "[FAIL] Status=" $STATUS
   ((FAILCOUNT++))
fi
echo
}


#
# DOMAIN TESTS
# 

NAME="get all domains"
URL="https://$TARGET/v1/domains"
PASSCODE=200
getit

NAME="create a new domain"
URL="https://$TARGET/v1/domains"
POSTFILE=domain.json
PASSCODE=201
postit

NAME="get domain 1"
URL="https://$TARGET/v1/domains/1"
PASSCODE=200
getit

NAME="delete domain 1"
URL="https://$TARGET/v1/domains/1"
PASSCODE=204
deleteit

NAME="create a new domain"
URL="https://$TARGET/v1/domains"
POSTFILE=domain.json
PASSCODE=201
postit

NAME="get all domains"
URL="https://$TARGET/v1/domains"
PASSCODE=200
getit

NAME="update domain 2"
URL="https://$TARGET/v1/domains/2"
PUTFILE=domain.json
PASSCODE=200
putit

NAME="create a new domain"
URL="https://$TARGET/v1/domains"
POSTFILE=domain.json
PASSCODE=201
postit

NAME="get all domains"
URL="https://$TARGET/v1/domains"
PASSCODE=200
getit

#
# USER TESTS
#

NAME="get all users"
URL="https://$TARGET/v1/users"
PASSCODE=200
getit

NAME="create a new user"
URL="https://$TARGET/v1/users"
POSTFILE=user.json
PASSCODE=201
postit

NAME="get all users"
URL="https://$TARGET/v1/users"
PASSCODE=200
getit

NAME="get user 1"
URL="https://$TARGET/v1/users/1"
PASSCODE=200
getit

NAME="delete user 1"
URL="https://$TARGET/v1/users/1"
PASSCODE=204
deleteit

NAME="get all users"
URL="https://$TARGET/v1/users"
PASSCODE=200
getit

NAME="create a new user"
URL="https://$TARGET/v1/users"
POSTFILE=user.json
PASSCODE=201
postit

NAME="update a user"
URL="https://$TARGET/v1/users/2"
PUTFILE=user.json
PASSCODE=200
putit

NAME="create a new user"
URL="https://$TARGET/v1/users"
POSTFILE=user.json
PASSCODE=201
postit

NAME="get all users"
URL="https://$TARGET/v1/users"
PASSCODE=200
getit

# ROLE TESTS

NAME="get all roles"
URL="https://$TARGET/v1/roles"
PASSCODE=200
getit

NAME="create a new role"
URL="https://$TARGET/v1/roles"
POSTFILE=role.json
PASSCODE=201
postit

NAME="get all roles"
URL="https://$TARGET/v1/roles"
PASSCODE=200
getit

NAME="get role 1"
URL="https://$TARGET/v1/roles/1"
PASSCODE=200
getit

NAME="delete role 1"
URL="https://$TARGET/v1/roles/1"
PASSCODE=204
deleteit

NAME="create a new role"
URL="https://$TARGET/v1/roles"
POSTFILE=role.json
PASSCODE=201
postit

NAME="update role 2"
URL="https://$TARGET/v1/roles/2"
PUTFILE=role-put.json
PASSCODE=200
putit

NAME="create a new role"
URL="https://$TARGET/v1/roles"
POSTFILE=role.json
PASSCODE=201
postit

NAME="get all roles"
URL="https://$TARGET/v1/roles"
PASSCODE=200
getit

# Grant tests

NAME="grant a role"
URL="https://$TARGET/v1/domains/2/users/2/roles"
POSTFILE=grant.json
PASSCODE=201
postit

NAME="try to create a double grant"
URL="https://$TARGET/v1/domains/2/users/2/roles"
POSTFILE=grant.json
PASSCODE=403
postit

NAME="get all roles for domain and user"
URL="https://$TARGET/v1/domains/2/users/2/roles"
PASSCODE=200
getit

NAME="delete a grant"
URL="https://$TARGET/v1/domains/2/users/2/roles/2"
PASSCODE=204
deleteit

NAME="delete a grant"
URL="https://$TARGET/v1/domains/2/users/2/roles/2"
PASSCODE=404
deleteit

NAME="get all roles for domain and user"
URL="https://$TARGET/v1/domains/2/users/2/roles"
PASSCODE=200
getit


#
# RESULTS
#
echo "SUMMARY"
echo "======================================"
echo 'TESTS:'$TESTCOUNT 'PASS:'$PASSCOUNT 'FAIL:'$FAILCOUNT

