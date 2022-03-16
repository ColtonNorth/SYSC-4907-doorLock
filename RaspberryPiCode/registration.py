import http.client
import urllib
from urllib import request
from urllib.request import urlopen
import requests
import json
import time
from time import sleep
import sqlite3

def get_new_username():
    URL = 'https://api.thingspeak.com/channels/1677753/fields/1.json?api_key=7JB5J9EMWYKXSLRC&results1'
    KEY = '7JB5J9EMWYKXSLRC'
    HEADER = '&results=1'
    NEW_URL = URL+KEY+HEADER
    #print(NEW_URL)

    get_data = requests.get(NEW_URL).json()

    temp = []
    for x in get_data['feeds']:
        print(x['field1'])
        temp.append(x['field1'])
    return temp[0]
  
  def get_new_password():
    URL = 'https://api.thingspeak.com/channels/1677753/fields/2.json?api_key=7JB5J9EMWYKXSLRC&results1'
    KEY = '7JB5J9EMWYKXSLRC'
    HEADER = '&results=1'
    NEW_URL = URL+KEY+HEADER
    #print(NEW_URL)

    get_data = requests.get(NEW_URL).json()

    temp = []
    for x in get_data['feeds']:
        print(x['field1'])
        temp.append(x['field1'])
    return temp[0]

if __name__ == '__main__':
    #connect to database file
    dbconnect = sqlite3.connect("SYSC4907.db");

    #If we want to access columns by name we need to set
    dbconnect.row_factory = sqlite3.Row;
    #now we create a cursor to work with db
    cursor = dbconnect.cursor();

    while True:
        #See if there are any new usernames and passwords and add to database
        mostRecentUsername = get_new_username()
        mostRecentPassword = get_new_password()
        cursor.execute('''SELECT username FROM data WHERE username=?''',(mostRecentUsername,))
        result = cursor.fetchone()
        if result:
            print('Username exists in database')
        else:
            print('New username registered, adding to database')
            cursor.execute('''Insert INTO data VALUES (?, ?)''',(mostRecentUsername, mostRecentPassword,))
            dbconnect.commit()

        print('Sleeping 3 seconds...')
        sleep(3)
