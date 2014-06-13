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
# TENANT TESTS
# 

NAME="get all tenants"
URL="https://$TARGET/v1/tenants"
PASSCODE=200
getit

NAME="create a new tenant"
URL="https://$TARGET/v1/tenants"
POSTFILE=tenant.json
PASSCODE=201
postit

NAME="get tenant 1"
URL="https://$TARGET/v1/tenants/1"
PASSCODE=200
getit

NAME="delete tenant 1"
URL="https://$TARGET/v1/tenants/1"
PASSCODE=204
deleteit

NAME="create a new tenant"
URL="https://$TARGET/v1/tenants"
POSTFILE=tenant.json
PASSCODE=201
postit

NAME="get all tenants"
URL="https://$TARGET/v1/tenants"
PASSCODE=200
getit

NAME="update tenant 2"
URL="https://$TARGET/v1/tenants/2"
PUTFILE=tenant.json
PASSCODE=200
putit

NAME="create a new tenant"
URL="https://$TARGET/v1/tenants"
POSTFILE=tenant.json
PASSCODE=201
postit

NAME="get all tenants"
URL="https://$TARGET/v1/tenants"
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

# TUR tests

NAME="grant a role"
URL="https://$TARGET/v1/tenants/2/users/2/roles"
POSTFILE=grant.json
PASSCODE=201
postit

URL="https://$TARGET/v1/tenants/2/users/2/roles"
POSTFILE=grant.json
PASSCODE=403
postit


NAME="get all roles for tenant and user"
URL="https://$TARGET/v1/tenants/2/users/2/roles"
PASSCODE=200
getit

NAME="delete a grant"
URL="https://$TARGET/v1/tenants/2/users/2/roles/2"
PASSCODE=204
deleteit

NAME="delete a grant"
URL="https://$TARGET/v1/tenants/2/users/2/roles/2"
PASSCODE=404
deleteit

NAME="get all roles for tenant and user"
URL="https://$TARGET/v1/tenants/2/users/2/roles"
PASSCODE=200
getit


#
# RESULTS
#
echo "SUMMARY"
echo "======================================"
echo 'TESTS:'$TESTCOUNT 'PASS:'$PASSCOUNT 'FAIL:'$FAILCOUNT

