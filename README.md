RunMyProcess SEC File Reader Adapter
====================================

The "File Reader Adapter" is used to retrieve files from a local server. It requires the [SEC Manager](https://github.com/runmyprocess/sec-manager) to be installed and running.  

##Install and Configure the Adapter

1. Make sure you have [Java](http://www.oracle.com/technetwork/java/index.html) and [Maven](http://maven.apache.org/) installed on your machine. You must also have the RMP's [JSON](https://github.com/runmyprocess/json/) and the [sec-sdk](https://github.com/runmyprocess/sec-sdk) libraries installed on your local mvn repo.
2. Download the sec-file-reader project and run mvn clean install in the project's folder.

Run mvn clean install :

	mvn clean install

3. Copy the generated jar file (usually created in a generated "target" folder in the file-reader project's folder) to a folder of your choice.
4. Create a "configFiles" folder in the jar file's path.
5. Inside the "configFiles" folder you must create 2 config files : handler.config and the fileReader.config

The **handler.config** file should look like this :
    
        #Generic Protocol Configuration
        protocol = fileReader
        protocolClass = com.runmyprocess.sec.FileReader
        handlerHost = 127.0.0.1
        connectionPort = 5832
        managerHost = 127.0.0.1
        managerPort = 4444
        pingFrequency = 300
    
Where : 

* **protocol** is the name to identify our Adapter.
* **protocolClass** is the class of the Adapter.
* **handlerHost** is where the Adapter is running.
* **connectionPort** is the port of the adapter where data will be received and returned.
* **managerHost** is where the SEC is running. 
* **managerPort** is the port where the SEC is listening for ping registrations.
* **pingFrequency** is the frequency in which the manager will be pinged (at least three times shorter than what's configured in the manager).
 

The **fileReader.config** file should look like this :
   
    #fileReader Configuration
    basePath=C:\\/Users\\/RMP\\/Desktop\\/;

Where : 

* **basePath** is the path from where the recieved path will look for the file.


##Running and Testing the Adapter

You can now run the Adapter by executing the generated jar in the chosen path :

    java -jar fileReaderAdapter.jar
    
If everything is configured correctly and the sec-Manager is running, you can now Post the manager to retrieve a file. 

The POST body should look like something like this:

	{
	"protocol":"fileReader",
	"data":{
			"path":"a.log"
		} 
	}
    
The expected return is a JSON object that should look like this :

	{
	"SECStatus":200,
	"file":{
		"file":"SEVMTE8h",
		"fileName":"a.log"
		}
	}

Where "**file**" contains the return information and "**file.file**" contains the file data in base64.
