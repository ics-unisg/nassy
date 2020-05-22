# Installation of the tool

## Requirement

There are two requirements for the tool The first is at least a Java8 runtime environment, and a MySQL database. 

## Starting the program

Create a new database and import the *dcapSchema.sql* into the database (can be found in the dbSchema directory)
Adjust the *application.properties* in the */src/main/resources/* directory

After cloning the repository, go in the directory and type *./gradlew bootRun* (presss enter)
After the tool starts, the *email*, the *name* and the *created password* are printed in the console. It is recommended to change the password after that!

Via the address *...cheetah/swagger-ui.html* (and after authorization), the APIs for the user and administrators can be seen. Via *...cheetah/v2/api-docs?group=admin* and *...cheetah/v2/api-docs?group=user*, the JSON-description of the API can be requested.

The standard adress is *http://localhost:8989/cheetah/* (the adress can be changed in the *application.properties* in the */src/main/resources/* directory)

## Testing 

After installation, it is recommomendet to test the program. 
This can be done via the *Unit Tests* that can be found in */src/test/java*.

Furthermore, the program can be tested with the help of Python. In the 'TestForCheetah" dircectory there are several tests. To run the stack, execute *python3 stack.py* in the (or in an IDE). 
The full test (*fullTest.py") does not work out of the box but needs data that it could use (the path must be changed in line 45 in the file (be aware of adjust all parameters of the file)
To be sure all dependencies are satisfied, please run *dep.py"
