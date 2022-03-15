#!/usr/bin/env python3
import sqlite3
import os
#connect to database file
dbconnect = sqlite3.connect("SYSC4907.db");

#If we want to access columns by name we need to set
#row_factory to sqlite3.Row class
dbconnect.row_factory = sqlite3.Row;
#now we create a cursor to work with db
cursor = dbconnect.cursor();

#Values for experimenting with database
user = "admin"
passw = "password"
email = "example@gmail.com"

#value = "DELETE FROM vals"
#Creating a new table and inserting test values
cursor.execute('''CREATE TABLE IF NOT EXISTS data (username TEXT, password TEXT)''')
cursor.execute('''CREATE TABLE IF NOT EXISTS emails (email TEXT)''')
cursor.execute('''INSERT INTO data (username, password) VALUES(?, ?)''', (user, passw))
cursor.execute('''INSERT INTO emails (email) VALUES(?)''', (email))
#cursor.execute('''INSERT INTO emails VALUES('example@gmail.com');''')
#cursor.execute(value)

dbconnect.commit();

#execute simple select statement
cursor.execute('SELECT * FROM data');
#print data
print('username    password     email')
for row in cursor:
    print(row['username'],row['password'],row['email']);
print('\n')


#close the connection
dbconnect.close();
