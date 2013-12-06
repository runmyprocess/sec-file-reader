RunMyProcess SEC File Reader Adapter
====================================

The "File Reader Adapter" is used to retreve files from a local server. It requires the [sec Manager](https://github.com/runmyprocess/sec-manager) to be installed and running.  

##Install and Configure the Adapter

1. Make sure you have [java](http://www.oracle.com/technetwork/java/index.html) and [maven](http://maven.apache.org/) installed on your machine.
2. Download the sec-file-reader project and  run mvn clean install on the project's folder

run mvn clean install

	mvn clean install

3. copy the generated jar file (usually created on a generated "target" folder in the file-reader project's folder) to a folder of your choosing.
4. Create a "configFiles" folder in the jar file's path.
5. inside the "configFiles" folder you must create 2 config files: handler.config and the fileReader.config

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

* **protocol** is the name to identify our Adapter
* **protocolClass** is the class of the Adapter
* **handlerHost** is where the Adapter is running
* **connectionPort** is the port of the adapter where data will be received and returned
* **managerHost** is where the SEC is running 
* **managerPort** is the port where the SEC is listening for ping registrations
* **pingFrequency** is the frequency in which the manager will be ping (at least three times smaller than what's configured in the manager).
 

The **fileReader.config** file should look like this :
   
    #fileReader Configuration
    basePath=C:\\/Users\\/RMP\\/Desktop\\/;

Where : 

* **basePath** is the path from where the recieved path will look for the file.


##Running and Testing the Adapter

You can now run the Adapter by executing the generated jar in the chosen path:

    java -jar fileReaderAdapter.jar
    
If everything is configured correctly and the sec-Manager is running, you can now Post the manager to sent an smtp mail with the configured smtp provider.

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
