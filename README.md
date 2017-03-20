
The project is hosted on GitHub.com, you can download it directly from the website or install it from eclipse using this URL
https://github.com/WilliamStott/NLP.git

To install from eclipse:
1. Open eclipse,
2. File,
3. Import,
4. Git,
5. Projects from Git,
6. CloneURI,
7. Enter URL above in URI then press next,
8. Select Master branch and next,
9. Select next until completion

Once you have the project, you will need to install the language models which are not hosted on GitHub as the filesizes are too large.
So you will need to download them from this site.
http://stanfordnlp.github.io/CoreNLP/download.html
All the languages will be needed to be installed except from English(KBP)and Chinese.
Place the models into the project library
must add the models to the classpath in the java build section and add them as external JARS
classpath can be found by right clicking the project folder and pressing alt+enter



To avoid an memory error
You must increase the memory allocation used by Eclipse.
This is done by opening eclipse and clicking run -> run configurations -> Arguments tab -> in the VM Arguments box type "-Xms2048M -Xmx3075M" -> Apply









