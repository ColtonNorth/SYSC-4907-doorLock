import http.client
import urllib
from urllib import request
from urllib.request import urlopen
import requests
import json
import time
from time import sleep
import sqlite3
import smtplib
import RPi.GPIO as GPIO

RELAY = 17
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(RELAY, GPIO.OUT)
GPIO.output(RELAY,GPIO.LOW)

def read_data_thingspeak_username():
    URL = 'https://api.thingspeak.com/channels/1629887/fields/1.json?api_key=3NI4ZQ8WKF430IA1&results1'
    KEY = '3NI4ZQ8WKF430IA1'
    HEADER = '&results=1'
    NEW_URL = URL+KEY+HEADER
    #print(NEW_URL)

    get_data = requests.get(NEW_URL).json()
    #print(get_data)

    temp = []
    for x in get_data['feeds']:
        print(x['field1'])
        temp.append(x['field1'])
    return temp[0]

def read_data_thingspeak_password():
    URL = 'https://api.thingspeak.com/channels/1629887/fields/2.json?api_key=3NI4ZQ8WKF430IA1&results1'
    KEY = '3NI4ZQ8WKF430IA1'
    HEADER = '&results=1'
    NEW_URL = URL+KEY+HEADER
    #print(NEW_URL)

    get_data = requests.get(NEW_URL).json()
    #print(get_data)

    temp = []
    for x in get_data['feeds']:
        print(x['field2'])
        temp.append(x['field2'])
    return temp[0]

def write_data_thingspeak():
        user = "default"
        passw = "default"
        email = "sample@gmail.com"
        key = "GKVWP488L3SWEB7M"
        params = urllib.parse.urlencode({'field1': user, 'field2': passw, 'field5': email, 'key':key })
        headers = {"Content-typZZe": "application/x-www-form-urlencoded","Accept": "text/plain"}
        conn = http.client.HTTPConnection("api.thingspeak.com:80")
        try:
            conn.request("POST", "/update", params, headers)
            response = conn.getresponse()
            print(response.status, response.reason)
            data = response.read()
            conn.close()
        except:
            print("connection closed")

def sign_in(value):
        key = "WZHU6SA35W68WB9K"
        params = urllib.parse.urlencode({'field1': value, 'key': key })
        headers = {"Content-typZZe": "application/x-www-form-urlencoded","Accept": "text/plain"}
        conn = http.client.HTTPConnection("api.thingspeak.com:80")
        try:
            conn.request("POST", "/update", params, headers)
            response = conn.getresponse()
            print(response.status, response.reason)
            data = response.read()
            conn.close()
        except:
            print("connection closed")

def read_data_thingspeak_status():
    URL = 'https://api.thingspeak.com/channels/1655931/fields/1.json?api_key=H7C4A5GLGL0IEH5N&results1'
    KEY = 'H7C4A5GLGL0IEH5N'
    HEADER = '&results=1'
    NEW_URL = URL+KEY+HEADER
    #print(NEW_URL)

    get_data = requests.get(NEW_URL).json()
    #print(get_data)

    temp = []
    for x in get_data['feeds']:
        print(x['field1'])
        temp.append(x['field1'])
    return int(temp[0])

if __name__ == '__main__':
    #connect to database file
    dbconnect = sqlite3.connect("SYSC4907.db");

    #If we want to access columns by name we need to set
    dbconnect.row_factory = sqlite3.Row;
    #now we create a cursor to work with db
    cursor = dbconnect.cursor();

    #Setting up email server
    #Create mail server
    server = smtplib.SMTP("smtp.gmail.com", 587)
    server.ehlo()
    server.starttls()
    server.login("SYSC4907.75@gmail.com", "x2LPt9MHsUM8PsuF")

    #default sign_in to 3
    sign_in(3)

    #variable to reset the status of the door lock after a number of iterations.
    count = 0
    loop = 0

    #Variable for unlocking/locking door
    lockDoor = 0
    while True:


        #Gathering data from thingspeak
        print('\n\n\nChecking for new users...')
        username = read_data_thingspeak_username()
        password = read_data_thingspeak_password()

        #If the username and password are in database, sign the user in.
        cursor.execute('''SELECT username FROM data WHERE username=?''',(username,))
        result = cursor.fetchone()
        if result:
            print('Username exists in database')
            cursor.execute('''SELECT password FROM data WHERE password=?''',(password,))
            p = cursor.fetchone()
            if p:
                #The account exists, sign in the user.
                print('Updating thingspeak channel')
                sign_in(2)
                #Now change the thingspeak values for username and password so we dont constantly log in.
                write_data_thingspeak()
                count = 1
        else:
            print('The account does not exist')

        #Here we will check the status of the door. If it is 1, the door will be unlocked and emails sent.
        status = read_data_thingspeak_status()
        if(status == 1):
            print('Door unlocked')
	    if(lockDoor == 0):
	    	GPIO.output(RELAY,GPIO.HIGH)
		lockDoor = 1
	    elif(lockDoor == 1):
		 GPIO.output(RELAY,GPIO.LOW)
		 lockDoor = 0
            sleep(3)
            sign_in(3)
            cursor.execute('SELECT * FROM emails');
            print('Sending Emails to customers...')
            for row in cursor:
                #print(row['email']);
                server.sendmail("SYSC4907.75@gmail.com", row['email'],"The door has been unlocked.")
                print('Emails sent')


        if count == 1:
            loop += 1
            if loop == 5:
                print('Resetting status to do nothing (3)')
                sign_in(3)
                count = 0
                loop = 0

        print('Sleeping 1 second...')
        sleep(1)
