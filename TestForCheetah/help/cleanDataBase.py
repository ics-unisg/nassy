import mysql.connector as mc
import os

from help import setting

connection = mc.connect (host =setting.hostName,
                         user = setting.dbUser,
                         passwd = setting.dbPassword,
                         db = setting.schemaName)

cur=connection.cursor()


cur.execute('SELECT pk_user_data, path FROM user_data')
fetchall = cur.fetchall()


cur.execute('SET FOREIGN_KEY_CHECKS=0;')
connection.commit()
path=".."
testString = """delete from user_data where pk_user_data ={userDataId};"""
for e in fetchall:
    if not os.path.exists(path+str(e[1])[1:]):
        print(e[0],str(e[1])[1:])
        newTestString = testString.format(userDataId=e[0])
        cur.execute(newTestString)
connection.commit()
cur.execute('SET FOREIGN_KEY_CHECKS=1;')
connection.commit()
connection.close