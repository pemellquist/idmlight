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
   cat result.json | python -mjson.tool
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
URL="https://$TARGET/V1/tenants"
PASSCODE=200
getit

NAME="create a new tenant"
URL="https://$TARGET/V1/tenants"
POSTFILE=tenant.json
PASSCODE=201
postit

NAME="get tenant"
URL="https://$TARGET/V1/tenants/1"
PASSCODE=200
getit

NAME="delete tenant"
URL="https://$TARGET/V1/tenants/1"
PASSCODE=204
deleteit

NAME="create a new tenants"
URL="https://$TARGET/V1/tenants"
POSTFILE=tenant.json
PASSCODE=201
postit

NAME="get tenants"
URL="https://$TARGET/V1/tenants"
PASSCODE=200
getit

NAME="update a tenant"
URL="https://$TARGET/V1/tenants/2"
PUTFILE=tenant.json
PASSCODE=200
putit


#
# USER TESTS
#

NAME="get all users"
URL="https://$TARGET/V1/users"
PASSCODE=200
getit

NAME="create a new user"
URL="https://$TARGET/V1/users"
POSTFILE=user.json
PASSCODE=201
postit

NAME="get all users"
URL="https://$TARGET/V1/users"
PASSCODE=200
getit

NAME="get a user"
URL="https://$TARGET/V1/users/1"
PASSCODE=200
getit

NAME="delete user"
URL="https://$TARGET/V1/users/1"
PASSCODE=204
deleteit

NAME="get all users"
URL="https://$TARGET/V1/users"
PASSCODE=200
getit

NAME="create a new user"
URL="https://$TARGET/V1/users"
POSTFILE=user.json
PASSCODE=201
postit

NAME="update a user"
URL="https://$TARGET/V1/users/2"
PUTFILE=user.json
PASSCODE=200
putit


#
# RESULTS
#
echo "SUMMARY"
echo "======================================"
echo 'TESTS:'$TESTCOUNT 'PASS:'$PASSCOUNT 'FAIL:'$FAILCOUNT

