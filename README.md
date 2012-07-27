jloganalyzer
============

What for?
------------

This Java Webapplication parses logfiles for messages with a given loglevel (e.g. ERROR). The results are displayed via html and atom-feed. 
Currently log4j text files with ConversionPattern "%10d [%t] %-5p %c %x - %m%n" and log4j xml files are supported. But new ones can be implemented easyly.

This Webapplication is intended to be used for development stage where applications are not monitored via professional tools. 


The GUI
------------
### Graph of all results:
![Graph of all results](https://github.com/j0nathande/jloganalyzer/raw/master/doc/firefox-parsingresults-graphics.png)
### Atom-feed of last results:
![Atom-feed of last results](https://github.com/j0nathande/jloganalyzer/raw/master/doc/firefox-lastresults-atom.png)


Configuration for using it with your logfiles
------------

### Changes that have to be made
* Put config-files 'spring-logparser.xml' and 'spring-scheduler.xml' to an external config folder. The references to these files are at the bottom of file 'applicationContext.xml'. 
* Modify the map with id 'logfiles2parsers'. Each entry of this map consists of an id, the path to the file to parse and the reference to the parser.
* Modify the directory where the parsing results are stored. This path is configured in the file 'application.properties'.

### Changes that can be made
* Modify the scheduler in file 'spring-scheduler.xml' if the default doesn't suit you. The current runs are at 9:00 and 15:00 o'clock each day.
* Modify the logging of the jloganalyzer application itself. This is done via log4j in the file 'log4j.properties'.