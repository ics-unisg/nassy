import mysql.connector as mc
from mysql.connector import Error
import os
from help import setting

def deleteFromSystem(idArray):
    dbUser = input("Enter user database User: ")
    dbPassword = input("Enter user password: ")

    connection = mc.connect(host=setting.hostName,
                            user=dbUser,
                            passwd=dbPassword,
                            db=setting.schemaName)

    cur=connection.cursor()

    cur.execute('SET FOREIGN_KEY_CHECKS=0;')
    connection.commit()
    path=".."
    delDB = """delete from user_data where pk_user_data ={userDataId};"""
    selectPath="""SELECT path FROM user_data where pk_user_data ={userDataId}"""
    for e in idArray:
        getPath=selectPath.format(userDataId=e)

        cur.execute(getPath)
        p=cur.fetchone()[0]
        os.remove(path+p[1:])
        delDBtoEx = delDB.format(userDataId=e)

        cur.execute(delDBtoEx)
        connection.commit()
    cur.execute('SET FOREIGN_KEY_CHECKS=1;')
    connection.commit()
    connection.close



deleteFromSystem([21405])