import mysql.connector as mc
from mysql.connector import Error
import os
from help import setting

def delete():
    dbUser = input("Enter user database User: ")
    dbPassword = input("Enter user password: ")

    connection = mc.connect(host=setting.hostName,
                            user=dbUser,
                            passwd=dbPassword,
                            db=setting.schemaName)

    cur=connection.cursor()

    cur.execute('SELECT pk_user_data, path FROM user_data')
    fetchall = cur.fetchall()


    cur.execute('SET FOREIGN_KEY_CHECKS=0;')
    connection.commit()
    cur.execute("DELETE FROM `users` WHERE `email`='123@yahoo.com';")
    cur.execute("DELETE FROM `users` WHERE `email`='1234@yahoo.com';")
    cur.execute("DELETE FROM `studies` WHERE `name`='TestStudy';")
    cur.execute("delete from user_data where filename like \'p024a@TestStudy@p024a%\' and pk_user_data >0 ;")
    connection.commit()
    cur.execute('SET FOREIGN_KEY_CHECKS=1;')
    connection.commit()
    connection.close


delete()